import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
       TaskManager taskManager = new TaskManager();

       Task task1 = new Task("Оплатить ЖКХ", "Отсканировать QR код с квитанции и перевести деньги",
               "NEW");
       Task task2 = new Task("Починить телевизор", "Вызвать мастера по ремонту ТВ на дом", "IN_PROGRESS");
       taskManager.create(task1);
       taskManager.create(task2);


       SubTask subTask1 = new SubTask("Стены", "Возведение стен и внутренних переговродок", "NEW");
       SubTask subTask2 = new SubTask("Крыша", "Монтаж кровли", "NEW");
       taskManager.create(subTask1);
       taskManager.create(subTask2);

       Epic epic1 = new Epic("Строительство дома", "Возвести качественное капитальное строение");
       epic1.getSubTasksIds().add(subTask1.getId());
       epic1.getSubTasksIds().add(subTask2.getId());
       taskManager.create(epic1);

       SubTask subTask3 = new SubTask("Дизайн", "Разработать дизайн-проект интерьера", "DONE");
       taskManager.create(subTask3);

       Epic epic2 = new Epic("Интерьер", "Решить вопрос с интерьером");
       epic2.getSubTasksIds().add(subTask3.getId());
       taskManager.create(epic2);


        System.out.println(taskManager.getAllEpicsList());




        }

        }




