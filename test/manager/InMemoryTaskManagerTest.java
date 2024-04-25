package manager;

import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @BeforeEach
    void initInMemoryTaskManager() {
        manager = new InMemoryTaskManager();

    }

    @Test
    void isOverlapInTime() {
        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW, "20.03.2024 14:00",
                40);
        manager.create(task);

        // проверяем граничные значения по пересечению-непересечению времени задач
        SubTask testSubTask = new SubTask("Стройка дома", "Построить дом", Status.NEW,
                "20.03.2024 14:00", 120);
        SubTask testSubTask2 = new SubTask("Стройка дома", "Построить дом", Status.NEW,
                "20.03.2024 13:59", 2);
        SubTask testSubTask3 = new SubTask("Стройка дома", "Построить дом", Status.NEW,
                "20.03.2024 14:30", 10);
        assertTrue(manager.isOverlapInTime(testSubTask));
        assertTrue(manager.isOverlapInTime(testSubTask2));
        assertTrue(manager.isOverlapInTime(testSubTask3));

        SubTask testSubTask4 = new SubTask("Стройка дома", "Построить дом", Status.NEW,
                "20.03.2024 13:30", 30);
        SubTask testSubTask5 = new SubTask("Стройка дома", "Построить дом", Status.NEW,
                "20.03.2023 14:30", 10);
        SubTask testSubTask6 = new SubTask("Стройка дома", "Построить дом", Status.NEW,
                "20.03.2023 14:40", 10);
        assertFalse(manager.isOverlapInTime(testSubTask4));
        assertFalse(manager.isOverlapInTime(testSubTask5));
        assertFalse(manager.isOverlapInTime(testSubTask6));
    }
}










