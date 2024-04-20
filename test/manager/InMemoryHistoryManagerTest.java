package manager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private static HistoryManager manager;
    private Task task;

    @BeforeEach
    void initInMemoryTaskManager() {
        manager = Managers.getDefaultHistory();
        task = new Task("Стройка", "Стройка дома", Status.NEW);
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

        final  List<Task> history = manager.getHistory();

        assertEquals(0, history.size(),"История не удалилась");
    }

    @Test
    void addDuplicatedTask(){
        manager.add(task);
        manager.add(task);
        final List<Task> history = manager.getHistory();

        assertNotNull(history, "История пустая");
        assertEquals(1, history.size(), "Ошибка при добавлении одинаковой задачи в историю");
    }



}