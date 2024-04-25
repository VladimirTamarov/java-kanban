package manager;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TaskManager {

    int getNextId();

    int create(Task task);

    int create(Epic epic);


    int create(SubTask subTask);


    List<Task> getAllTasksList();

    List<SubTask> getAllSubTasksList();

    List<Epic> getAllEpicsList();


    void tasksClear();

    void subTasksClear();

    void epicsClear();


    Task getTaskById(int id);

    SubTask getSubTaskById(int id);

    Epic getEpicById(int id);


    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void removeTaskById(int id);

    void removeSubTaskById(int id);

    void removeEpicById(int id);

    Collection<SubTask> getEpicSubTasks(int epicId);

    List<Task> getHistory();

    Set<Task> getPrioritizedTask();

    boolean isOverlapInTime(Task task);


}

