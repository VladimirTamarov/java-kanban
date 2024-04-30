import manager.FileBackedTaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.nio.file.Path;
import java.nio.file.Paths;

public class NotMain {

    public static void main(String[] args) {
        Path path = Paths.get("test1.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(path);

        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW, "20.03.2024 14:00", 40);
        manager.create(task);
        Task task2 = new Task("Мытьё окон", "Помыть все окна", Status.NEW, "21.03.2024 14:39", 120);
        manager.create(task2);

        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW, "20.04.2024 14:00", 120);

        manager.create(subTask);

        SubTask subTask2 = new SubTask("Стройка дома", "Построить дом", Status.NEW, "28.04.2024 14:00", 120);

        manager.create(subTask2);

        SubTask subTask3 = new SubTask("Благоустройство участка", "Проклдка дорожек, посадка растений", Status.NEW, "01.05.2024 14:00", 30);

        manager.create(subTask3);

        Epic epic = new Epic("Обустройство поместья", "Обустроить загородное жилище");

        epic.getSubTasksIds().add(subTask.getId());
        epic.getSubTasksIds().add(subTask2.getId());
        epic.getSubTasksIds().add(subTask3.getId());
        manager.create(epic);
        Epic epic2 = new Epic("Эпик без подзадач", "У этого эпика нет никаких подзадач");
        manager.create(epic2);

        System.out.println(manager.getHistory());

        manager.getEpicById(6);
        manager.getTaskById(2);
        manager.getSubTaskById(3);
        manager.getEpicById(6);
        manager.getSubTaskById(3);

        System.out.println("Отсортированные задачи:");
        System.out.println(manager.getPrioritizedTask());
        System.out.println();
        System.out.println(subTask3.getEndTime());
        System.out.println(epic);
        System.out.println(manager.getEpicSubTasks(6));

        subTask.setStatus(Status.DONE);
        manager.updateSubTask(subTask);
        System.out.println(subTask);
        manager.removeSubTaskById(3);
        System.out.println(manager.getAllSubTasksList());
    }

}




