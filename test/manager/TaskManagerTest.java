package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    @Test
    void shouldBeSameTaskWhenSameId() {
        Task task = new Task("Стройка", "Стройка дома", Status.NEW, "21.04.2024 15:00", 40);
        int id = manager.create(task);

        Assertions.assertEquals(manager.getTaskById(id), task, "Объекты Task не равны при равном ID");
    }

    @Test
    void shouldBeSameSubTaskWhenSameId() {
        SubTask subTask = new SubTask("Стройка", "Стройка дома", Status.NEW, "23.04.2024 10:00", 120);
        int id = manager.create(subTask);

        Assertions.assertEquals(manager.getSubTaskById(id), subTask, "Объекты SubTask не равны при равном ID");
    }

    @Test
    void shouldBeSameEpicWhenSameId() {
        Epic epic = new Epic("Стройка", "Стройка дома");
        int id = manager.create(epic);

        Assertions.assertEquals(manager.getEpicById(id), epic, "Объекты Epic не равны при равном ID");
    }


    @Test
    void createTask() {
        Task task = new Task("Стройка", "Стройка дома", Status.NEW, "23.04.2024 10:00", 120);
        int id = manager.create(task);

        Task savedTask = manager.getTaskById(id);

        Assertions.assertNotNull(savedTask, "Задача по ID не найдена");
        Assertions.assertEquals(task, savedTask, "Сохранённая задача не совпадает с вновь созданной");

        List<Task> allTasks = manager.getAllTasksList();

        Assertions.assertNotNull(allTasks, "Не возвращается полный список созданных задач");
        Assertions.assertEquals(1, allTasks.size(), "Неправильно отображается кол-во задач");
        Assertions.assertEquals(task, allTasks.get(0), "Неправильно возвращается задача из списка");


    }

    @Test
    void createSubTask() {
        SubTask task = new SubTask("Стройка", "Стройка дома", Status.NEW, "23.04.2024 10:00", 120);
        int id = manager.create(task);

        SubTask savedTask = manager.getSubTaskById(id);

        Assertions.assertNotNull(savedTask, "Подадача по ID не найдена");
        Assertions.assertEquals(task, savedTask, "Сохранённая подзадача не совпадает с вновь созданной");

        List<SubTask> allTasks = manager.getAllSubTasksList();

        Assertions.assertNotNull(allTasks, "Не возвращается полный список созданных подзадач");
        Assertions.assertEquals(1, allTasks.size(), "Неправильно отображается кол-во подзадач");
        Assertions.assertEquals(task, allTasks.get(0), "Неправильно возвращается подзадача из списка");
    }

    @Test
    void createEpic() {
        Epic epic = new Epic("Стройка", "Стройка дома");
        int id = manager.create(epic);

        Epic savedEpic = manager.getEpicById(id);

        Assertions.assertNotNull(savedEpic, "Эпик по ID не найден");
        Assertions.assertEquals(epic, savedEpic, "Сохранённый эпик не совпадает с вновь созданным");

        List<Epic> allEpics = manager.getAllEpicsList();

        Assertions.assertNotNull(allEpics, "Не возвращается полный список созданных эпиков");
        Assertions.assertEquals(1, allEpics.size(), "Неправильно отображается кол-во эпиков");
        Assertions.assertEquals(epic, allEpics.get(0), "Неправильно возвращается эпик из списка");
    }

    @Test
    void tasksClear() {
        Task task = new Task("Стройка", "Стройка дома", Status.NEW, "23.04.2024 10:00", 120);
        int id = manager.create(task);

        assertFalse(manager.getAllTasksList().isEmpty(), "Список задач пуст");

        manager.tasksClear();

        assertTrue(manager.getAllTasksList().isEmpty(), "Список задач не обнулился");

    }

    @Test
    void subTasksClear() {
        SubTask task = new SubTask("Стройка", "Стройка дома", Status.NEW, "23.04.2024 10:00", 120);
        int id = manager.create(task);

        assertFalse(manager.getAllSubTasksList().isEmpty(), "Список подзадач пуст");

        manager.subTasksClear();

        assertTrue(manager.getAllSubTasksList().isEmpty(), "Список подзадач не обнулился");
    }

    @Test
    void epicsClear() {
        Epic epic = new Epic("Стройка", "Стройка дома");
        int id = manager.create(epic);

        assertFalse(manager.getAllEpicsList().isEmpty(), "Список эпиков пуст");

        manager.epicsClear();

        assertTrue(manager.getAllEpicsList().isEmpty(), "Список эпиков не обнулился");
    }

    @Test
    void updateTask() {
        Task task = new Task("Стройка", "Стройка дома", Status.NEW, "23.04.2024 10:00", 120);
        Task task2 = new Task("Задача для обновления", "Описание", Status.NEW, "21.04.2024 15:00", 40);
        int id = manager.create(task);

        assertEquals(task, manager.getTaskById(id));

        task2.setId(id);

        manager.updateTask(task2);

        assertEquals(task2, manager.getTaskById(id), "Задача не обновилась");
    }

    @Test
    void updateSubTask() {
        SubTask task = new SubTask("Стройка", "Стройка дома", Status.NEW, "21.04.2024 15:00", 40);
        SubTask task2 = new SubTask("Задача для обновления", "Описание", Status.NEW, "23.04.2024 10:00", 120);
        int id = manager.create(task);

        assertEquals(task, manager.getSubTaskById(id));

        task2.setId(id);

        manager.updateSubTask(task2);

        assertEquals(task2, manager.getSubTaskById(id), "Подзадача не обновилась");
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("Стройка", "Стройка дома");
        Epic epic2 = new Epic("Эпик для обновления", "Описание");
        int id = manager.create(epic);

        assertEquals(epic, manager.getEpicById(id));

        epic2.setId(id);

        manager.updateEpic(epic2);

        assertEquals(epic2, manager.getEpicById(id), "Эпик не обновился");
    }

    @Test
    void removeTaskById() {
        Task task = new Task("Стройка", "Стройка дома", Status.NEW, "23.04.2024 10:00", 120);
        int id = manager.create(task);
        List<Task> taskList = manager.getAllTasksList();
        assertEquals(1, taskList.size());

        manager.removeTaskById(id);
        List<Task> actualTaskList = manager.getAllTasksList();

        assertEquals(0, actualTaskList.size(), "Задача не удалилась");
    }

    @Test
    void removeSubTaskById() {
        SubTask task = new SubTask("Стройка", "Стройка дома", Status.NEW, "23.04.2024 10:00", 120);
        int id = manager.create(task);
        Epic epic = new Epic("Обустройство поместья", "Обустроить загородное жилище");
        manager.create(epic);
        List<SubTask> subTaskList = manager.getAllSubTasksList();
        assertEquals(1, subTaskList.size());
        epic.getSubTasksIds().add(task.getId());

        manager.removeSubTaskById(id);
        List<SubTask> actualSubTaskList = manager.getAllSubTasksList();

        assertEquals(0, actualSubTaskList.size(), "Подзадача не удалилась");
    }

    @Test
    void removeEpicById() {
        Epic epic = new Epic("Стройка", "Стройка дома");
        int id = manager.create(epic);
        List<Epic> epicsList = manager.getAllEpicsList();
        assertEquals(1, epicsList.size());

        manager.removeEpicById(id);
        List<Epic> actualEpicsList = manager.getAllEpicsList();

        assertEquals(0, actualEpicsList.size(), "Эпик не удалился");
    }


    @Test
    void createTaskWithIdBySetterAndByMethod() {
        Task task = new Task("Стройка", "Стройка дома", Status.NEW, "23.04.2024 10:00", 120);
        manager.create(task);
        Task task2 = new Task("Стройка", "Стройка дома", Status.NEW, "20.04.2024 14:00", 120);
        manager.create(task2);
        task2.setId(5);

        assertEquals(2, manager.getAllTasksList().size());
    }

    @Test
    void removeSubTaskFromEpic() {
        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW, "20.04.2024 14:00", 120);
        manager.create(subTask);
        Epic epic = new Epic("Обустройство поместья", "Обустроить загородное жилище");
        epic.getSubTasksIds().add(subTask.getId());
        manager.create(epic);


        assertEquals(1, epic.getSubTasksIds().size());

        manager.removeSubTaskById(subTask.getId());

        assertEquals(0, epic.getSubTasksIds().size());
    }


    @Test
    void getStatusForEpic() {
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

        assertEquals(Status.NEW, epic.getStatus());

        subTask.setStatus(Status.DONE);
        manager.updateEpic(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());

        subTask2.setStatus(Status.DONE);
        subTask3.setStatus(Status.DONE);
        manager.updateEpic(epic);
        assertEquals(Status.DONE, epic.getStatus());

        subTask.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);
        subTask3.setStatus(Status.IN_PROGRESS);
        manager.updateEpic(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());


    }
}