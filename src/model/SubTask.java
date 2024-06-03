package model;

import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SubTask extends Task {

    protected int epicId;

    public SubTask(String title, String description, Status status, String startTime, long durationInMinutes) {
        super(title, description, status, startTime, durationInMinutes);
        this.type = Type.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }


    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return id + ","
                + Type.SUBTASK + ","
                + title + ","
                + status + ","
                + description + ","
                + epicId + ","
                + startTime.format(FORMATTER) + ","
                + duration.toMinutes();
    }

    public static class SubTaskListTypeToken extends TypeToken<List<SubTask>> {

    }
}
