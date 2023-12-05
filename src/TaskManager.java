import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int nextId = 1;


    public int getNextId() {
        return nextId;
    }

    public int create(Task task){       // создание задач, присвоение им ID
        task.setId(getNextId());
        nextId++;
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public int create(Epic epic){
        epic.setId(getNextId());
        nextId++;

        ArrayList<Integer> subTaskIds = epic.getSubTasksIds();   //получаем ID сабтасков
        for (Integer subTaskId : subTaskIds) {
            getSubTaskById(subTaskId).setEpicId(epic.getId());// присваеваем каждому сабтаску принадлежность к эпику (epicId)
        }

        String status = getStatusForEpic(epic.getSubTasksIds());  // проверяем и присваеваем статус
        epic.setStatus(status);
        epics.put(epic.getId(),epic);
        return epic.getId();

    }

    public int create(SubTask subTask){
        subTask.setId(getNextId());
        nextId++;
        subTasks.put(subTask.getId(),subTask);
        return subTask.getId();
    }


    public ArrayList<Task> getAllTasksList(){                    //получение списков задач по типам
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            allTasks.add(task);
        }
        return allTasks;
    }

    public ArrayList<SubTask> getAllSubTasksList(){
        ArrayList<SubTask> allSubTasks = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            allSubTasks.add(subTask);
        }
        return allSubTasks;
    }

    public ArrayList<Epic> getAllEpicsList(){
        ArrayList<Epic> allEpics = new ArrayList<>();
        for (Epic epic : epics.values()) {
            allEpics.add(epic);
        }
        return allEpics;
    }


    public void tasksClear(){                 // удаление всех задач по типу
        tasks.clear();
    }

    public void subTasksClear(){
        subTasks.clear();
    }

    public void epicsClear(){
        epics.clear();
    }


    public Task getTaskById(int id){           // получение задачи по ID
        return tasks.get(id);
    }

    public SubTask getSubTaskById(int id){
        return subTasks.get(id);
    }

    public Epic getEpicById(int id){
        return epics.get(id);
    }


    public void updateTask(Task task){          //обновление задачи с верным ID
        tasks.put(task.getId(), task);
    }

    public void updateSubTask(SubTask subTask){
        subTasks.put(subTask.getId(), subTask);
    }

    public  void updateEpic(Epic epic){
        epics.put(epic.getId(), epic);
    }


    public void removeTaskById(int id) {          // удаление задачи по ID
        tasks.remove(id);
    }

    public void removeSubTaskById(int id){
        subTasks.remove(id);
    }

    public void removeEpicById(int id){
        epics.remove(id);
    }

    public ArrayList<SubTask> getEpicSubTasks(int epicId){            //возвращаем сабтаски конкретного эпика
        ArrayList<SubTask> subTasksByEpic = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == epicId){
                subTasksByEpic.add(subTask);
            }
        }
        return subTasksByEpic;

    }

    private String getStatusForEpic(ArrayList<Integer> subTaskIds){// определяем статус эпика
        String status;
        ArrayList<String> statusList = new ArrayList<>();// создаем список статусов подзадач эпика
        for (Integer subTaskId : subTaskIds) {
            statusList.add(getSubTaskById(subTaskId).getStatus()); //добавляем все статусы подзадач в список
        }

        if ((subTaskIds.size()==0) || (Collections.frequency(statusList, "NEW") == statusList.size())){
            status = "NEW";
        }
        else if (Collections.frequency(statusList, "DONE") == statusList.size()) {
            status = "DONE";
        }
        else status = "IN_PROGRESS";

        return status;
    }




    }

