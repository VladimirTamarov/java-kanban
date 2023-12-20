package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private  final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private  final Map<Integer, SubTask> subTasks = new HashMap<>();
    private int nextId = 1;

    HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public int getNextId() {
        return nextId;
    }
@Override
    public int create(Task task){       // создание задач, присвоение им ID
        task.setId(getNextId());
        nextId++;
        tasks.put(task.getId(), task);
        return task.getId();
    }
@Override
    public int create(Epic epic){
        epic.setId(getNextId());
        nextId++;

        List<Integer> subTaskIds = epic.getSubTasksIds();   //получаем ID сабтасков
        for (Integer subTaskId : subTaskIds) {
            getSubTaskById(subTaskId).setEpicId(epic.getId());// присваеваем каждому сабтаску принадлежность к эпику (epicId)
        }

        Status status = getStatusForEpic(epic.getSubTasksIds());  // проверяем и присваеваем статус
        epic.setStatus(status);
        epics.put(epic.getId(),epic);
        return epic.getId();

    }
    @Override
    public int create(SubTask subTask){
        subTask.setId(getNextId());
        nextId++;
        subTasks.put(subTask.getId(),subTask);
        return subTask.getId();
    }

    @Override
    public Collection<Task> getAllTasksList(){                    //получение списков задач по типам

        return new ArrayList<>(tasks.values());
    }
    @Override
    public Collection<SubTask> getAllSubTasksList(){

        return new ArrayList<>(subTasks.values());
    }
    @Override
    public Collection<Epic> getAllEpicsList(){

        return new ArrayList<>(epics.values());
    }

    @Override
    public void tasksClear(){                 // удаление всех задач по типу
        tasks.clear();
    }
    @Override
    public void subTasksClear(){
        subTasks.clear();
    }
    @Override
    public void epicsClear(){
        epics.clear();
    }

    @Override
    public Task getTaskById(int id){// получение задачи по ID
           historyManager.addToHistory(tasks.get(id));
           return tasks.get(id);
    }
    @Override
    public SubTask getSubTaskById(int id){
        historyManager.addToHistory(subTasks.get(id));
           return subTasks.get(id);
    }
    @Override
    public Epic getEpicById(int id){
        historyManager.addToHistory(epics.get(id));
           return epics.get(id);
    }

    @Override
    public void updateTask(Task task){          //обновление задачи с верным ID
        tasks.put(task.getId(), task);
    }
    @Override
    public void updateSubTask(SubTask subTask){
        subTasks.put(subTask.getId(), subTask);
    }
    @Override
    public  void updateEpic(Epic epic){
        epics.put(epic.getId(), epic);
    }

    @Override
    public void removeTaskById(int id) {          // удаление задачи по ID
        tasks.remove(id);
    }
    @Override
    public void removeSubTaskById(int id){
        subTasks.remove(id);
    }
    @Override
    public void removeEpicById(int id){
        epics.remove(id);
    }
    @Override
    public List<SubTask> getEpicSubTasks(int epicId){            //возвращаем сабтаски конкретного эпика
        List<SubTask> subTasksByEpic = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == epicId){
                subTasksByEpic.add(subTask);
            }
        }
        return subTasksByEpic;

    }

    private Status getStatusForEpic(ArrayList<Integer> subTaskIds){// определяем статус эпика
        Status status;
        if (subTaskIds.size() == 0){
            return Status.NEW;
        }
        Set<Status> statusSet = new HashSet<>();// создаем сет уникальных статусов подзадач эпика
        for (Integer subTaskId : subTaskIds) {
            statusSet.add(getSubTaskById(subTaskId).getStatus()); //добавляем уникальные статусы подзадач в сет
        }

        if (statusSet.size()==1){
            if (statusSet.contains(Status.NEW)){
                status = Status.NEW;
            }
            else if(statusSet.contains(Status.DONE)){
                status = Status.DONE;
            }
            else status = Status.IN_PROGRESS;
        }
        else status = Status.IN_PROGRESS;

        return status;
    }







}
