

import manager.*;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;


public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault(Managers.getDefaultHistory());

        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW);
        inMemoryTaskManager.create(task);
        Task task2 = new Task("Мытьё окон", "Помыть все окна", Status.NEW);
        inMemoryTaskManager.create(task2);

        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW);

        inMemoryTaskManager.create(subTask);

        SubTask subTask2 = new SubTask("Стройка дома", "Построить дом", Status.NEW);

        inMemoryTaskManager.create(subTask2);

        SubTask subTask3 = new SubTask("Благоустройство участка", "Проклдка дорожек, посадка растений", Status.NEW);

        inMemoryTaskManager.create(subTask3);

        Epic epic = new Epic("Обустройство поместья", "Обустроить загородное жилище");
        inMemoryTaskManager.create(epic);
        epic.getSubTasksIds().add(subTask.getId());
        epic.getSubTasksIds().add(subTask2.getId());
        epic.getSubTasksIds().add(subTask3.getId());

        Epic epic2 = new Epic("Эпик без подзадач", "У этого эпика нет никаких подзадач");
        inMemoryTaskManager.create(epic2);

        System.out.println(inMemoryTaskManager.getHistory());


        inMemoryTaskManager.getEpicById(6);
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getSubTaskById(3);
        inMemoryTaskManager.getEpicById(6);
        inMemoryTaskManager.getSubTaskById(3);

        System.out.println(inMemoryTaskManager.getHistory());

        inMemoryTaskManager.removeTaskById(2);
        System.out.println(inMemoryTaskManager.getHistory());

        inMemoryTaskManager.removeEpicById(6);
        System.out.println(inMemoryTaskManager.getHistory());


    }

}




