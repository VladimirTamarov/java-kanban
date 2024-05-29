package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {


    private LocalDateTime endTime;
    private final ArrayList<Integer> subTasksIds = new ArrayList<>();

    public Epic(String title, String description) {
        this.title = title;
        this.description = description;
        this.type = Type.EPIC;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }


    @Override
    public String toString() {
        if (getStartTime() != null) {
            return id + ","
                    + Type.EPIC + ","
                    + title + ","
                    + status + ","
                    + description + ","
                    + startTime.format(FORMATTER) + ","
                    + duration.toMinutes();
        } else return id + ","
                + Type.EPIC + ","
                + title + ","
                + status + ","
                + description;
    }
}
