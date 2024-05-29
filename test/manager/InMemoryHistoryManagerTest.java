package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {
    private static HistoryManager manager;
    private Task task;
    private SubTask subTask;
    private Epic epic;

    @BeforeEach
    void initInMemoryTaskManager() {
        manager = Managers.getDefaultHistory();
        task = new Task("Стройка", "Стройка дома", Status.NEW, "01.05.2024 14:00", 30);
        task.setId(1);
        subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW, "20.04.2024 14:00", 120);
        subTask.setId(2);
        epic = new Epic("Обустройство поместья", "Обустроить загородное жилище");
        subTask.setId(3);
    }

    @Test
    void add() {
        manager.add(task);
        final List<Task> history = manager.getHistory();

        assertNotNull(history, "История пустая");
        assertEquals(1, history.size(), "Задача не добавлена в историю");
    }

    @Test
    void remove() {
        manager.add(task);
        manager.remove(task.getId());

        final List<Task> history = manager.getHistory();

        assertEquals(0, history.size(), "История не удалилась");
    }

    @Test
    void removeFromHead() {

        manager.add(task);
        manager.add(subTask);
        manager.add(epic);
        manager.remove(task.getId());

        final List<Task> history = manager.getHistory();
        assertEquals(2, history.size());
    }

    @Test
    void removeFromMid() {
        manager.add(task);
        manager.add(subTask);
        manager.add(epic);
        manager.remove(subTask.getId());

        final List<Task> history = manager.getHistory();
        assertEquals(2, history.size());
    }

    @Test
    void removeFromTail() {
        manager.add(task);
        manager.add(subTask);
        manager.add(epic);
        manager.remove(epic.getId());

        final List<Task> history = manager.getHistory();
        assertEquals(2, history.size());
    }

    @Test
    void addDuplicatedTask() {
        manager.add(task);
        manager.add(task);
        final List<Task> history = manager.getHistory();

        assertNotNull(history, "История пустая");
        assertEquals(1, history.size(), "Ошибка при добавлении одинаковой задачи в историю");
    }

    @Test
    void emptyHistory() {
        final List<Task> history = manager.getHistory();
        assertEquals(0, history.size());
    }


}