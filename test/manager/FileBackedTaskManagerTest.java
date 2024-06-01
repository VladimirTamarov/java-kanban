package manager;

import exception.ManagerLoadException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    Path path;


    @BeforeEach
    public void beforeEach() {
        try {
            path = File.createTempFile("tempFile", ".csv").toPath();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        manager = new FileBackedTaskManager(path);

    }

    @Test
    void saveAndLoadEmptyFile() {
        manager.save();
        FileBackedTaskManager managerFromFile = FileBackedTaskManager.loadFromFile(path);


        assertEquals(Collections.EMPTY_MAP, managerFromFile.tasks);
        assertEquals(Collections.EMPTY_MAP, managerFromFile.subTasks);
        assertEquals(Collections.EMPTY_MAP, managerFromFile.epics);

    }


    @Test
    void shouldCorrectSaveAndLoadData() {
        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW, "21.04.2024 15:00", 40);
        manager.create(task);
        Task task2 = new Task("Мытьё окон", "Помыть все окна", Status.NEW, "23.04.2024 10:00", 120);
        manager.create(task2);
        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW, "20.04.2024 14:00", 120);
        manager.create(subTask);
        SubTask subTask2 = new SubTask("Стройка дома", "Построить дом", Status.NEW, "01.05.2024 14:00", 30);
        manager.create(subTask2);
        SubTask subTask3 = new SubTask("Благоустройство участка", "Прокладка дорожек и посадка растений",
                Status.DONE, "28.04.2024 14:00", 120);
        manager.create(subTask3);
        Epic epic = new Epic("Обустройство поместья", "Обустроить загородное жилище");

        epic.getSubTasksIds().add(subTask.getId());
        epic.getSubTasksIds().add(subTask2.getId());
        epic.getSubTasksIds().add(subTask3.getId());
        manager.create(epic);

        manager.getTaskById(1);
        manager.getSubTaskById(4);

        FileBackedTaskManager managerFromFile = FileBackedTaskManager.loadFromFile(path);

        assertEquals(1, managerFromFile.getAllEpicsList().size(), "Эпики из файла восстановаились некорректно");
        assertEquals(2, managerFromFile.getAllTasksList().size(), "Таски из файла восстановаились некорректно");
        assertEquals(3, managerFromFile.getAllSubTasksList().size(), "Подзадачи из файла восстановаились некорректно");

        assertEquals(2, managerFromFile.getHistory().size(), "История просмотров восстановилась некорректно");

    }

    @Test
    void testLoadException() {
        path = Path.of("src1//tests//data.test.exe");
        assertThrows(ManagerLoadException.class, () -> {
                    FileBackedTaskManager managerFromFile = FileBackedTaskManager.loadFromFile(path);
                },
                "Некорректный файл для загрузки данных должен приводить к исключению");


    }
}






