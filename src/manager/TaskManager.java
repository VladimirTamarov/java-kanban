package manager;
import model.Epic;
import model.SubTask;
import model.Task;
import java.util.ArrayList;
import java.util.Collection;

public interface TaskManager {

    int getNextId();

    int create(Task task);

    int create(Epic epic);



    int create(SubTask subTask);


    Collection<Task> getAllTasksList();

    Collection<SubTask> getAllSubTasksList();

    Collection<Epic> getAllEpicsList();


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


}

