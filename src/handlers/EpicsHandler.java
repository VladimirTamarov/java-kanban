package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.NotFoundException;
import manager.TaskManager;
import model.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    public EpicsHandler(TaskManager taskManager) {
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

        if (path.equals("/epics")) {
            response = gson.toJson(taskManager.getAllEpicsList());
            code = 200;
            System.out.println("Идёт обработка запроса /epics");

        } else if (path.contains("/epics/") && path.split("/").length == 3) {

            try {
                int id = Integer.parseInt(path.split("/")[2]);
                System.out.println("Идёт обработка запроса /epics/" + id);

                response = gson.toJson(taskManager.getEpicById(id));
                code = 200;
            } catch (NotFoundException | NumberFormatException e) {
                response = "Эпик не найден";
                code = 404;
            }
        } else if (path.contains("/epics/") && path.split("/").length == 4
                && (path.split("/")[3].equals("subtasks")))

            try {
                int id = Integer.parseInt(path.split("/")[2]);
                System.out.println("Идёт обработка запроса /epics/" + id + "/subtasks");

                response = gson.toJson(taskManager.getEpicSubTasks(id));
                code = 200;
            } catch (NotFoundException | NumberFormatException e) {
                response = "Эпик не найден";
                code = 404;

            }
        else {
            response = "Запрос не верный";
            code = 404;
        }

        sendText(code, exchange, response);
    }

    private void postHandler(HttpExchange exchange, String requestBody) throws IOException {
        String response;
        int code;
        Epic epicFromJson = gson.fromJson(requestBody, Epic.class);
        if (taskManager.getEpics().containsKey(epicFromJson.getId())) {
            code = 201;
            taskManager.updateEpic(epicFromJson);
            response = "Обновлён эпик, id " + epicFromJson.getId();
        } else {
            code = 201;
            taskManager.create(epicFromJson);
            response = "Эпик успешно создан. Id=" + epicFromJson.getId();
        }
        sendText(code, exchange, response);
    }

    private void deleteHandler(HttpExchange exchange, String path) throws IOException {
        int code;
        String response;

        if (path.contains("/epics/") && path.split("/").length == 3) {

            try {
                int id = Integer.parseInt(path.split("/")[2]);
                System.out.println("Идёт обработка запроса /epics/" + id);

                taskManager.removeEpicById(id);
                code = 200;
                response = "Эпик удалён. id=" + id;
            } catch (NotFoundException | NumberFormatException e) {
                response = "Эпик не найден";
                code = 404;
            }
        } else {
            response = "Запрос неверный";
            code = 404;
        }

        sendText(code, exchange, response);
    }
}

