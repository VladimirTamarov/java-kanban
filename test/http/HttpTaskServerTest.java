package http;

import com.google.gson.*;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    HttpTaskServer server;
    TaskManager taskManager;
    Gson gson = Managers.getGson();

    HttpTaskServerTest() throws IOException {
    }


    @BeforeEach
    void setUp() throws IOException {
        server = new HttpTaskServer();
        server.startServer();
    }

    @AfterEach
    void stop() {
        server.stopServer();
    }

    @Test
    public void addNewTaskTest() throws IOException, InterruptedException {
        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW, "20.03.2027 14:00",
                40);
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = HttpTaskServer.taskManager.getAllTasksList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Стройка дома", tasksFromManager.get(0).getTitle(), "Некорретный заголовок задачи");

    }

    @Test
    public void updateTaskTest() throws IOException, InterruptedException {
        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW, "20.03.2027 14:00",
                40);
        int id = HttpTaskServer.taskManager.create(task);
        Task task2 = new Task("Возведение ограждения", "Закуп материалов", Status.NEW,
                "20.03.2023 14:00",
                40);
        task2.setId(id);
        String taskJson = gson.toJson(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = HttpTaskServer.taskManager.getAllTasksList();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Возведение ограждения", tasksFromManager.get(0).getTitle(),
                "Некорретный заголовок задачи после апдейта");
    }

    @Test
    public void createTaskWithSameTime() throws IOException, InterruptedException {
        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW, "20.03.2024 14:00",
                40);
        int id = HttpTaskServer.taskManager.create(task);
        Task task2 = new Task("Возведение ограждения", "Закуп материалов", Status.NEW,
                "20.03.2024 14:00",
                40);
        String taskJson = gson.toJson(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode(), "Ошибка создания задачи с пересечнием во времени!");
        List<Task> tasksFromManager = HttpTaskServer.taskManager.getAllTasksList();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void getAllTasksTest() throws IOException, InterruptedException {
        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW, "20.03.2024 14:00",
                40);
        int id = HttpTaskServer.taskManager.create(task);
        Task task2 = new Task("Возведение ограждения", "Закуп материалов", Status.NEW,
                "20.03.2024 14:00",
                40);
        int id2 = HttpTaskServer.taskManager.create(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray());

        List<Task> tasks = gson.fromJson(jsonElement, new Task.TaskListTypeToken().getType());
        assertEquals("Стройка дома", tasks.get(0).getTitle(),
                "Задача из списка десериализовалась неверно");

    }

    @Test
    public void notFoundTaskId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/111");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void incorrectTaskId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/incorrectID");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void deleteTaskTest() throws IOException, InterruptedException {
        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW, "20.03.2024 14:00",
                40);
        int id = HttpTaskServer.taskManager.create(task);
        List<Task> tasksFromManager = HttpTaskServer.taskManager.getAllTasksList();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        tasksFromManager = HttpTaskServer.taskManager.getAllTasksList();
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");


    }

    @Test
    public void incorrectPath() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks2/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }


    @Test
    public void deleteTaskWithIncorrectId() throws IOException, InterruptedException {
        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW, "20.03.2024 14:00",
                40);
        int id = HttpTaskServer.taskManager.create(task);
        List<Task> tasksFromManager = HttpTaskServer.taskManager.getAllTasksList();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/123");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

        tasksFromManager = HttpTaskServer.taskManager.getAllTasksList();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void getTaskByIdTest() throws IOException, InterruptedException {
        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW, "20.03.2024 14:00",
                40);
        int idTask = HttpTaskServer.taskManager.create(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + idTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject());

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        int taskId = jsonObject.get("id").getAsInt();
        String title = jsonObject.get("title").getAsString();
        String description = jsonObject.get("description").getAsString();

        assertEquals(task.getId(), taskId);
        assertEquals(task.getTitle(), title);
        assertEquals(task.getDescription(), description);

    }

    @Test
    public void getSubTaskByIdTest() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW,
                "20.04.2024 14:00", 120);
        int id = HttpTaskServer.taskManager.create(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject());

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        int taskId = jsonObject.get("id").getAsInt();
        String title = jsonObject.get("title").getAsString();
        String description = jsonObject.get("description").getAsString();

        assertEquals(subTask.getId(), taskId);
        assertEquals(subTask.getTitle(), title);
        assertEquals(subTask.getDescription(), description);

    }

    @Test
    public void addNewSubTaskTest() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW,
                "20.04.2024 14:00", 120);
        String taskJson = gson.toJson(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<SubTask> tasksFromManager = HttpTaskServer.taskManager.getAllSubTasksList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Стройка бани", tasksFromManager.get(0).getTitle(), "Некорретный заголовок задачи");

    }

    @Test
    public void updateSubTaskTest() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW,
                "20.04.2024 14:00", 120);
        int id = HttpTaskServer.taskManager.create(subTask);
        SubTask subTask2 = new SubTask("Стройка дома", "Построить дом", Status.NEW,
                "28.04.2024 14:00", 120);
        subTask2.setId(id);
        String taskJson = gson.toJson(subTask2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<SubTask> tasksFromManager = HttpTaskServer.taskManager.getAllSubTasksList();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Стройка дома", tasksFromManager.get(0).getTitle(),
                "Некорретный заголовок задачи после апдейта");
    }

    @Test
    public void incorrectSubTaskId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/incorrectID");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void notFoundSubTaskId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/111");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void deleteSubTaskTest() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW,
                "20.04.2024 14:00", 120);
        int id = HttpTaskServer.taskManager.create(subTask);
        List<SubTask> tasksFromManager = HttpTaskServer.taskManager.getAllSubTasksList();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        tasksFromManager = HttpTaskServer.taskManager.getAllSubTasksList();
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void deleteSubTaskWithIncorrectId() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW,
                "20.04.2024 14:00", 120);
        int id = HttpTaskServer.taskManager.create(subTask);
        List<SubTask> tasksFromManager = HttpTaskServer.taskManager.getAllSubTasksList();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1234");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

        tasksFromManager = HttpTaskServer.taskManager.getAllSubTasksList();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void getAllSubTasksTest() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW,
                "20.04.2024 14:00", 120);
        int id = HttpTaskServer.taskManager.create(subTask);
        SubTask subTask2 = new SubTask("Стройка дома", "Построить дом", Status.NEW,
                "28.04.2024 14:00", 120);
        int id2 = HttpTaskServer.taskManager.create(subTask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray());

        List<SubTask> tasks = gson.fromJson(jsonElement, new SubTask.SubTaskListTypeToken().getType());
        assertEquals("Стройка бани", tasks.get(0).getTitle(),
                "Задача из списка десериализовалась неверно");

    }

    @Test
    public void createSubTaskWithSameTime() throws IOException, InterruptedException {
        SubTask subtask = new SubTask("Стройка дома", "Закуп материалов", Status.NEW, "20.03.2024 14:00",
                40);
        int id = HttpTaskServer.taskManager.create(subtask);
        SubTask subtask2 = new SubTask("Возведение ограждения", "Закуп материалов", Status.NEW,
                "20.03.2024 14:00",
                40);
        String taskJson = gson.toJson(subtask2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode(), "Ошибка создания подзадачи с пересечнием во времени!");
        List<SubTask> tasksFromManager = HttpTaskServer.taskManager.getAllSubTasksList();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество подзадач");
    }

    @Test
    public void addNewEpicTest() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW,
                "20.04.2024 14:00", 120);
        HttpTaskServer.taskManager.create(subTask);
        SubTask subTask2 = new SubTask("Стройка дома", "Построить дом", Status.NEW,
                "28.04.2024 14:00", 120);
        HttpTaskServer.taskManager.create(subTask2);
        SubTask subTask3 = new SubTask("Благоустройство участка", "Проклдка дорожек, посадка растений",
                Status.NEW, "01.05.2024 14:00", 30);
        HttpTaskServer.taskManager.create(subTask3);
        Epic epic = new Epic("Обустройство поместья", "Обустроить загородное жилище");
        epic.getSubTasksIds().add(subTask.getId());
        epic.getSubTasksIds().add(subTask2.getId());
        epic.getSubTasksIds().add(subTask3.getId());
        HttpTaskServer.taskManager.create(epic);

        String taskJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Epic> tasksFromManager = HttpTaskServer.taskManager.getAllEpicsList();

        assertNotNull(tasksFromManager, "Эпики не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество Эпиков");
        assertEquals("Обустройство поместья", tasksFromManager.get(0).getTitle(), "Некорретный заголовок эпика");


    }

    @Test
    public void getAllEpicsTest() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW,
                "20.04.2024 14:00", 120);
        HttpTaskServer.taskManager.create(subTask);
        SubTask subTask2 = new SubTask("Стройка дома", "Построить дом", Status.NEW,
                "28.04.2024 14:00", 120);
        HttpTaskServer.taskManager.create(subTask2);
        SubTask subTask3 = new SubTask("Благоустройство участка", "Проклдка дорожек, посадка растений",
                Status.NEW, "01.05.2024 14:00", 30);
        HttpTaskServer.taskManager.create(subTask3);
        Epic epic = new Epic("Обустройство поместья", "Обустроить загородное жилище");
        epic.getSubTasksIds().add(subTask.getId());
        epic.getSubTasksIds().add(subTask2.getId());
        epic.getSubTasksIds().add(subTask3.getId());
        HttpTaskServer.taskManager.create(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray());

        List<Epic> tasks = gson.fromJson(jsonElement, new Epic.EpicListTypeToken().getType());
        assertEquals("Обустройство поместья", tasks.get(0).getTitle(),
                "Задача из списка десериализовалась неверно");

    }

    @Test
    public void getEpicByIdTest() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW,
                "20.04.2024 14:00", 120);
        HttpTaskServer.taskManager.create(subTask);
        SubTask subTask2 = new SubTask("Стройка дома", "Построить дом", Status.NEW,
                "28.04.2024 14:00", 120);
        HttpTaskServer.taskManager.create(subTask2);
        SubTask subTask3 = new SubTask("Благоустройство участка", "Проклдка дорожек, посадка растений",
                Status.NEW, "01.05.2024 14:00", 30);
        HttpTaskServer.taskManager.create(subTask3);
        Epic epic = new Epic("Обустройство поместья", "Обустроить загородное жилище");
        epic.getSubTasksIds().add(subTask.getId());
        epic.getSubTasksIds().add(subTask2.getId());
        epic.getSubTasksIds().add(subTask3.getId());
        int id = HttpTaskServer.taskManager.create(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject());

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        int taskId = jsonObject.get("id").getAsInt();
        String title = jsonObject.get("title").getAsString();
        String description = jsonObject.get("description").getAsString();

        assertEquals(epic.getId(), taskId);
        assertEquals(epic.getTitle(), title);
        assertEquals(epic.getDescription(), description);

    }

    @Test
    public void deleteEpicTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Обустройство поместья", "Обустроить загородное жилище");
        int id = HttpTaskServer.taskManager.create(epic);

        List<Epic> tasksFromManager = HttpTaskServer.taskManager.getAllEpicsList();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        tasksFromManager = HttpTaskServer.taskManager.getAllEpicsList();
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void deleteEpicWithIncorrectId() throws IOException, InterruptedException {
        Epic epic = new Epic("Обустройство поместья", "Обустроить загородное жилище");
        int id = HttpTaskServer.taskManager.create(epic);

        List<Epic> tasksFromManager = HttpTaskServer.taskManager.getAllEpicsList();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/786");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

        tasksFromManager = HttpTaskServer.taskManager.getAllEpicsList();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void getAllSubtasksFromEpic() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW,
                "20.04.2024 14:00", 120);
        HttpTaskServer.taskManager.create(subTask);
        SubTask subTask2 = new SubTask("Стройка дома", "Построить дом", Status.NEW,
                "28.04.2024 14:00", 120);
        HttpTaskServer.taskManager.create(subTask2);
        SubTask subTask3 = new SubTask("Благоустройство участка", "Проклдка дорожек, посадка растений",
                Status.NEW, "01.05.2024 14:00", 30);
        HttpTaskServer.taskManager.create(subTask3);
        Epic epic = new Epic("Обустройство поместья", "Обустроить загородное жилище");
        epic.getSubTasksIds().add(subTask.getId());
        epic.getSubTasksIds().add(subTask2.getId());
        epic.getSubTasksIds().add(subTask3.getId());
        int id = HttpTaskServer.taskManager.create(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/"+id+"/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray());

        List<SubTask> tasks = gson.fromJson(jsonElement, new SubTask.SubTaskListTypeToken().getType());
        assertEquals(3, tasks.size());


    }

    @Test
    public void getEpicByIncorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/12");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

    }

    @Test
    public void getHistoryTest() throws IOException, InterruptedException {
        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW, "20.03.2024 14:00",
                40);
        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW,
                "20.04.2024 14:00", 120);
        HttpTaskServer.taskManager.create(task);
        HttpTaskServer.taskManager.create(subTask);
        HttpTaskServer.taskManager.getTaskById(task.getId());
        HttpTaskServer.taskManager.getSubTaskById(subTask.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray());

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertEquals(2, jsonArray.size());

    }

    @Test
    public void getPrioritizedTest() throws IOException, InterruptedException {
        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW, "20.03.2024 14:00",
                40);
        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW,
                "20.04.2024 14:00", 120);
        HttpTaskServer.taskManager.create(task);
        HttpTaskServer.taskManager.create(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray());

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertEquals(2, jsonArray.size());
    }







}