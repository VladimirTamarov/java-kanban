package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.NotFoundException;
import manager.TaskManager;
import model.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    public SubtasksHandler(TaskManager taskManager) {
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

        if (path.equals("/subtasks")) {
            response = gson.toJson(taskManager.getAllSubTasksList());
            code = 200;
            System.out.println("Идёт обработка запроса /subtasks");

        } else if (path.contains("/subtasks/") && path.split("/").length == 3) {

            try {
                int id = Integer.parseInt(path.split("/")[2]);
                System.out.println("Идёт обработка запроса /subtasks/" + id);

                response = gson.toJson(taskManager.getSubTaskById(id));
                code = 200;
            } catch (NotFoundException | NumberFormatException e) {
                response = "Подадача не найдена";
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
        SubTask subTaskFromJson = gson.fromJson(requestBody, SubTask.class);
        if (taskManager.getSubTasks().containsKey(subTaskFromJson.getId())) {
            code = 201;
            taskManager.updateSubTask(subTaskFromJson);
            response = "Обновлена подзадача, id " + subTaskFromJson.getId();
        } else if (taskManager.isOverlapInTime(subTaskFromJson)) {
            code = 406;
            response = "Обнаружено пересечение задачи во времени!";
        } else {
            code = 201;
            taskManager.create(subTaskFromJson);
            response = "Подзадача успешно создана. Id=" + subTaskFromJson.getId();
        }
        sendText(code, exchange, response);
    }

    private void deleteHandler(HttpExchange exchange, String path) throws IOException {
        int code;
        String response;

        if (path.contains("/subtasks/") && path.split("/").length == 3) {

            try {
                int id = Integer.parseInt(path.split("/")[2]);
                System.out.println("Идёт обработка запроса /subtasks/" + id);

                taskManager.removeSubTaskById(id);
                code = 200;
                response = "Подадача удалена. id=" + id;
            } catch (NotFoundException | NumberFormatException e) {
                response = "Подзадача не найдена";
                code = 404;
            }
        } else {
            response = "Запрос не верный";
            code = 404;
        }

        sendText(code, exchange, response);
    }

}

