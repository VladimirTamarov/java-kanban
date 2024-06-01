package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class PrioritizedTaskHandler extends BaseHttpHandler implements HttpHandler {
    public PrioritizedTaskHandler(TaskManager taskManager) {
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

        if (method.equals("GET") && path.equals("/prioritized")) {
            code = 200;
            response = gson.toJson(taskManager.getPrioritizedTask());
        } else {
            code = 404;
            response = "Неверный запрос";
        }
        sendText(code, exchange, response);
    }
}

