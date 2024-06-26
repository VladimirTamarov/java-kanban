package manager;

import exception.NotFoundException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {


    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    public HistoryManager historyManager;
    private int nextId = 1;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public int getNextId() {
        return nextId;
    }

    @Override
    public int create(Task task) {       // создание задач, присвоение им ID

        task.setId(getNextId());
        nextId++;
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        isOverlapInTime(task);

        return task.getId();
    }

    @Override
    public int create(Epic epic) {
        epic.setId(getNextId());
        nextId++;

        List<Integer> subTaskIds = epic.getSubTasksIds();   //получаем ID сабтасков
        for (Integer subTaskId : subTaskIds) {
            subTasks.get(subTaskId).setEpicId(epic.getId());// присваеваем каждому сабтаску принадлежность к эпику (epicId)
        }

        Status status = getStatusForEpic(epic.getSubTasksIds());  // проверяем и присваеваем статус
        epic.setStatus(status);
        setTimeAndDurationForEpic(epic);
        epics.put(epic.getId(), epic);
        if (epic.getStartTime() != null) {
            prioritizedTasks.add(epic);
        }
        return epic.getId();

    }

    @Override
    public Set<Task> getPrioritizedTask() {
        return prioritizedTasks;
    }

    @Override
    public int create(SubTask subTask) {
        subTask.setId(getNextId());
        nextId++;
        subTasks.put(subTask.getId(), subTask);
        if (subTask.getStartTime() != null) {
            prioritizedTasks.add(subTask);
        }
        isOverlapInTime(subTask);
        return subTask.getId();
    }

    @Override
    public ArrayList<Task> getAllTasksList() {                    //получение списков задач по типам

        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasksList() {

        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpicsList() {

        return new ArrayList<>(epics.values());
    }

    @Override
    public void tasksClear() {                 // удаление всех задач по типу
        tasks.clear();
    }

    @Override
    public void subTasksClear() {
        subTasks.clear();
    }

    @Override
    public void epicsClear() {
        epics.clear();
    }

    @Override
    public Task getTaskById(int id) {// получение задачи по ID
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            throw new NotFoundException("Задача не найдена");
        }
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        } else throw new NotFoundException("Подзадача не найдена");
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else throw new NotFoundException("Эпик не найден");
    }

    public void addToHistory(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
        } else if (subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
        } else if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
        }
    }

    public void setTimeAndDurationForEpic(Epic epic) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        Duration duration = Duration.ofMinutes(0);

        List<SubTask> epicsSubTasksList = epic.getSubTasksIds().stream()
                .map(subTasks::get)
                .collect(Collectors.toList());

        for (SubTask subTask : epicsSubTasksList) {
            if (startTime == null || startTime.isAfter(subTask.getStartTime())) {
                startTime = subTask.getStartTime();
            }
            if (endTime == null || endTime.isBefore(subTask.getEndTime())) {
                endTime = subTask.getEndTime();
            }
            duration = duration.plus(subTask.getDuration());
        }


        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(duration);

    }

    @Override
    public void updateTask(Task task) {    //обновление задачи с верным ID
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            if (task.getStartTime() != null) {
                prioritizedTasks.remove(tasks.get(task.getId()));
                prioritizedTasks.add(task);
            }
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            Optional<Epic> mayBeEpic = Optional.ofNullable(epics.get(getEpicIdBySubtaskId(subTask.getId())));// получаем эпик, к которому принадлежит данная подзадача
            /*if (mayBeEpic.isPresent()) {
                Epic epic = mayBeEpic.get();
                Status status = getStatusForEpic(epic.getSubTasksIds());  // обновляем статус эпика
                epic.setStatus(status);
                setTimeAndDurationForEpic(epic);
                if (subTask.getStartTime() != null) {
                    prioritizedTasks.remove(subTasks.get(subTask.getId()));
                    prioritizedTasks.add(subTask);
                }
            }*/
            mayBeEpic.ifPresent(epic -> {
                epic.setStatus(getStatusForEpic(epic.getSubTasksIds()));
                setTimeAndDurationForEpic(epic);
                if (subTask.getStartTime() != null) {
                    prioritizedTasks.remove(subTasks.get(subTask.getId()));
                    prioritizedTasks.add(subTask);
                }
            });
            subTasks.put(subTask.getId(), subTask);
        }

    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Status status = getStatusForEpic(epic.getSubTasksIds());
            epic.setStatus(status);
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void removeTaskById(int id) {// удаление задачи по ID
        if (tasks.containsKey(id)) {
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
            historyManager.remove(id);
        } else throw new NotFoundException("Задача для удаления не найдена");
    }

    @Override
    public void removeSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            SubTask subTask = subTasks.get(id);
            Optional<Epic> mayBeEpic = Optional.ofNullable(epics.get(getEpicIdBySubtaskId(id)));// получаем эпик, к которому принадлежит данная подзадача
        /*if (mayBeEpic.isPresent()) {
            Epic epic = mayBeEpic.get();
            epic.getSubTasksIds().remove((Integer) subTask.getId());// удаляем удаляемую подзадачу из эпика
            Status status = getStatusForEpic(epic.getSubTasksIds());  // обновляем статус эпика
            epic.setStatus(status);
            setTimeAndDurationForEpic(epic);
        }*/
            mayBeEpic.ifPresent(epic -> {
                epic.getSubTasksIds().remove((Integer) subTask.getId());
                epic.setStatus(getStatusForEpic(epic.getSubTasksIds()));
                setTimeAndDurationForEpic(epic);
            });
            prioritizedTasks.remove(subTasks.get(id));
            subTasks.remove(id);
            historyManager.remove(id);
        } else {
            throw new NotFoundException("Подзадача не найдена");
        }
    }

    @Override
    public boolean isOverlapInTime(Task task) {
        if (prioritizedTasks.size() == 0) {
            return false;
        }
        /* for (Task curTask : prioritizedTasks) {
            if (Objects.equals(task.getId(), curTask.getId())) {
                continue;
            }
            if (task.getStartTime() == null || curTask.getStartTime() == null) {
                break;
            }

            if (task.getEndTime().isBefore(curTask.getStartTime())
                    || task.getEndTime().equals(curTask.getStartTime())
                    || task.getStartTime().isAfter(curTask.getEndTime())
                    || task.getStartTime().equals(curTask.getEndTime())) {
                return false;
            } else {
                System.out.println("Вновь созданная задача имеет пересечение во времени с уже существующей!");
                break;
            }

        }*/

        return prioritizedTasks.stream().
                filter(prTask -> !Objects.equals(prTask.getId(), task.getId()))
                .filter(prTask -> prTask.getStartTime() != null)
                .noneMatch(prTask ->
                        prTask.getEndTime().isBefore(task.getStartTime())
                                || prTask.getEndTime().equals(task.getStartTime())
                                || prTask.getStartTime().isAfter(task.getEndTime())
                                || prTask.getStartTime().equals(task.getEndTime()));


    }

    @Override
    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            List<Integer> subtasksIds = getEpicById(id).getSubTasksIds();
            for (Integer subtasksId : subtasksIds) {
                removeSubTaskById(subtasksId);
            }
            prioritizedTasks.remove(epics.get(id));
            epics.remove(id);
            historyManager.remove(id);
        } else throw new NotFoundException("Эпик для удаления не найден");
    }

    @Override
    public List<SubTask> getEpicSubTasks(int epicId) {            //возвращаем сабтаски конкретного эпика
        /*List<SubTask> subTasksByEpic = new ArrayList<>();      // реализация через For each
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == epicId) {
                subTasksByEpic.add(subTask);
            }
        }
        return subTasksByEpic;*/
        return subTasks.values()        // реализация через stream
                .stream()
                .filter(subTask -> subTask.getEpicId() == epicId)
                .collect(Collectors.toList());
    }

    protected Status getStatusForEpic(ArrayList<Integer> subTaskIds) {// определяем статус эпика
        Status status;
        if (subTaskIds.size() == 0) {
            return Status.NEW;
        }
        Set<Status> statusSet = new HashSet<>();// создаем сет уникальных статусов подзадач эпика
        for (Integer subTaskId : subTaskIds) {
            statusSet.add(subTasks.get(subTaskId).getStatus()); //добавляем уникальные статусы подзадач в сет
        }

        if (statusSet.size() == 1) {
            if (statusSet.contains(Status.NEW)) {
                status = Status.NEW;
            } else if (statusSet.contains(Status.DONE)) {
                status = Status.DONE;
            } else status = Status.IN_PROGRESS;
        } else status = Status.IN_PROGRESS;

        return status;
    }

    @Override
    public List<Task> getHistory() {                //получаем историю
        return historyManager.getHistory();
    }

    public int getEpicIdBySubtaskId(int id) {
        return subTasks.get(id).getEpicId();
    }


}


