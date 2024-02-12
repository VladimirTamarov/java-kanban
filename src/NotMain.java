import manager.FileBackedTaskManager;
import manager.Managers;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.nio.file.Path;
import java.nio.file.Paths;

public class NotMain {

    public static void main(String[] args) {
        Path path = Paths.get("test1.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(path, Managers.getDefaultHistory());

        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW);
        manager.create(task);
        Task task2 = new Task("Мытьё окон", "Помыть все окна", Status.NEW);
        manager.create(task2);

        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW);

        manager.create(subTask);

        SubTask subTask2 = new SubTask("Стройка дома", "Построить дом", Status.NEW);

        manager.create(subTask2);

        SubTask subTask3 = new SubTask("Благоустройство участка", "Проклдка дорожек, посадка растений", Status.NEW);

        manager.create(subTask3);

        Epic epic = new Epic("Обустройство поместья", "Обустроить загородное жилище");
        manager.create(epic);
        epic.getSubTasksIds().add(subTask.getId());
        epic.getSubTasksIds().add(subTask2.getId());
        epic.getSubTasksIds().add(subTask3.getId());

        Epic epic2 = new Epic("Эпик без подзадач", "У этого эпика нет никаких подзадач");
        manager.create(epic2);

        System.out.println(manager.getHistory());


        manager.getEpicById(6);
        manager.getTaskById(2);
        manager.getSubTaskById(3);
        manager.getEpicById(6);
        manager.getSubTaskById(3);

        System.out.println(manager.getHistory());

        manager.removeTaskById(2);
        System.out.println(manager.getHistory());

        manager.removeEpicById(6);
        System.out.println(manager.getHistory());


    }

}




