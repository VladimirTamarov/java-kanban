package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksIds = new ArrayList<>();

    public Epic(String title, String description) {
        this.title = title;
        this.description = description;
        this.type = Type.EPIC;
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }


    @Override
    public String toString() {
        return    id +","
                + Type.EPIC + ","
                + title + ","
                + status + ","
                + description;
    }
}
