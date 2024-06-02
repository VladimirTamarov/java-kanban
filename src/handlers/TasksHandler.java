package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.NotFoundException;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        int code;
        String response;
        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        switch (method) {
            case "GET":
                getHandler(exchange, path);
                break;
            case "POST":
                postHandler(exchange, requestBody);
                break;
            case "DELETE":
                deleteHandler(exchange, path);
                break;


        }
    }

    private void getHandler(HttpExchange exchange, String path) throws IOException {
        int code;
        String response;

        if (path.equals("/tasks")) {
            response = gson.toJson(taskManager.getAllTasksList());
            code = 200;
            System.out.println("Идёт обработка запроса /tasks");

        } else if (isRequestWithId(path)) {

            try {
                int id = getIdFromPath(path);
                System.out.println("Идёт обработка запроса /tasks/" + id);

                response = gson.toJson(taskManager.getTaskById(id));
                code = 200;
            } catch (NotFoundException | NumberFormatException e) {
                response = "Задача не найдена";
                code = 404;
            }
        } else {
            response = "Запрос не верный";
            code = 404;
        }

        sendText(code, exchange, response);
    }

    private void postHandler(HttpExchange exchange, String requestBody) throws IOException {
        String response;
        int code;
        Task taskFromJson = gson.fromJson(requestBody, Task.class);
        if (taskManager.getTasks().containsKey(taskFromJson.getId())) {
            code = 201;
            taskManager.updateTask(taskFromJson);
            response = "Обновлена задача, id " + taskFromJson.getId();
        } else if (taskManager.isOverlapInTime(taskFromJson)) {
            code = 406;
            response = "Обнаружено пересечение задачи во времени!";
        } else {
            code = 201;
            taskManager.create(taskFromJson);
            response = "Задача успешно создана. Id=" + taskFromJson.getId();
        }
        sendText(code, exchange, response);
    }

    private void deleteHandler(HttpExchange exchange, String path) throws IOException {
        int code;
        String response;

        if (isRequestWithId(path)) {

            try {
                int id = getIdFromPath(path);
                System.out.println("Идёт обработка запроса /tasks/" + id);

                taskManager.removeTaskById(id);
                code = 200;
                response = "Задача удалена. id=" + id;
            } catch (NotFoundException | NumberFormatException e) {
                response = "Задача не найдена";
                code = 404;
            }
        } else {
            response = "Запрос не верный";
            code = 404;
        }

        sendText(code, exchange, response);
    }
}
