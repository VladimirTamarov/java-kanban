package http;


import com.sun.net.httpserver.HttpServer;
import handlers.*;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    protected static TaskManager taskManager;
    private final HttpServer httpServer;

    public HttpTaskServer() throws IOException {

        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        taskManager = Managers.getDefault();
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicsHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedTaskHandler(taskManager));

    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW, "20.03.2024 14:00",
                40);
        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW,
                "20.04.2024 14:00", 120);

        taskManager.create(subTask);

        SubTask subTask2 = new SubTask("Стройка дома", "Построить дом", Status.NEW,
                "28.04.2024 14:00", 120);

        taskManager.create(subTask2);

        SubTask subTask3 = new SubTask("Благоустройство участка",
                "Проклдка дорожек, посадка растений", Status.NEW, "01.05.2024 14:00", 30);

        taskManager.create(subTask3);

        Epic epic = new Epic("Обустройство поместья", "Обустроить загородное жилище");

        epic.getSubTasksIds().add(subTask.getId());
        epic.getSubTasksIds().add(subTask2.getId());
        epic.getSubTasksIds().add(subTask3.getId());
        taskManager.create(epic);

        taskManager.create(task);
        taskManager.getEpicById(4);
        taskManager.getTaskById(5);
        server.startServer();


    }


    public void startServer() {
        httpServer.start();
        System.out.println("Сервер запущен, порт: " + PORT);

    }

    public void stopServer() {
        httpServer.stop(0);
        System.out.println("Сервер остановлен.");
    }
}
