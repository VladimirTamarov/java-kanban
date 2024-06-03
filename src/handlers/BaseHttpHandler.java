package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static support.Library.getGson;

public class BaseHttpHandler {
    TaskManager taskManager;
    Gson gson;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = getGson();
    }

    protected void sendText(int code, HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public int getIdFromPath(String path){
        int id = Integer.parseInt(path.split("/")[2]);
        return id;
    }

    public boolean isRequestWithId(String path){
        return (path.contains("/tasks/") || path.contains("/subtasks/") || path.contains("/epics/"))
                && path.split("/").length == 3;
    }


}

