package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private static TaskManager manager;

    @BeforeEach
    void initInMemoryTaskManager() {
        manager = Managers.getDefault();

    }

    @Test
    void shouldBeSameTaskWhenSameId() {
        Task task = new Task("Стройка", "Стройка дома", Status.NEW);
        int id = manager.create(task);

        Assertions.assertEquals(manager.getTaskById(id), task, "Объекты Task не равны при равном ID");
    }

    @Test
    void shouldBeSameSubTaskWhenSameId() {
        SubTask subTask = new SubTask("Стройка", "Стройка дома", Status.NEW);
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
        Task task = new Task("Стройка", "Стройка дома", Status.NEW);
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
        SubTask task = new SubTask("Стройка", "Стройка дома", Status.NEW);
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
        Task task = new Task("Стройка", "Стройка дома", Status.NEW);
        int id = manager.create(task);

        assertFalse(manager.getAllTasksList().isEmpty(), "Список задач пуст");

        manager.tasksClear();

        assertTrue(manager.getAllTasksList().isEmpty(), "Список задач не обнулился");

    }

    @Test
    void subTasksClear() {
        SubTask task = new SubTask("Стройка", "Стройка дома", Status.NEW);
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
        Task task = new Task("Стройка", "Стройка дома", Status.NEW);
        Task task2 = new Task("Задача для обновления", "Описание", Status.NEW);
        int id = manager.create(task);

        assertEquals(task, manager.getTaskById(id));

        task2.setId(id);

        manager.updateTask(task2);

        assertEquals(task2, manager.getTaskById(id), "Задача не обновилась");
    }

    @Test
    void updateSubTask() {
        SubTask task = new SubTask("Стройка", "Стройка дома", Status.NEW);
        SubTask task2 = new SubTask("Задача для обновления", "Описание", Status.NEW);
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
        Task task = new Task("Стройка", "Стройка дома", Status.NEW);
        int id = manager.create(task);
        List<Task> taskList = manager.getAllTasksList();
        assertEquals(1, taskList.size());

        manager.removeTaskById(id);
        List<Task> actualTaskList = manager.getAllTasksList();

        assertEquals(0, actualTaskList.size(), "Задача не удалилась");
    }

    @Test
    void removeSubTaskById() {
        SubTask task = new SubTask("Стройка", "Стройка дома", Status.NEW);
        int id = manager.create(task);
        List<SubTask> subTaskList = manager.getAllSubTasksList();
        assertEquals(1, subTaskList.size());

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
    void createTaskWithIdBySetterAndByMethod(){
        Task task = new Task("Стройка", "Стройка дома", Status.NEW);
        manager.create(task);
        Task task2 = new Task("Стройка", "Стройка дома", Status.NEW);
        manager.create(task2);
        task2.setId(5);

        assertEquals(2, manager.getAllTasksList().size());
    }






    }










