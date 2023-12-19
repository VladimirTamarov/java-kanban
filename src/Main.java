

import manager.*;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;




public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager  = Managers.getDefault();

        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW);
        inMemoryTaskManager.create(task);

        SubTask task2 = new SubTask("Стройка бани", "Закуп печки", Status.NEW);

        inMemoryTaskManager.create(task2);

        Epic epic = new Epic("Гашение ипотеки", "Отдать деньги в банк");

        inMemoryTaskManager.create(epic);
        inMemoryTaskManager.getTaskById(1);

        inMemoryTaskManager.getSubTaskById(2);
        inMemoryTaskManager.getEpicById(3);









        }

        }




