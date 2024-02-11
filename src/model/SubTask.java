package model;

public class SubTask extends Task {

    protected int epicId;

    public SubTask(String title, String description, Status status) {
        super(title, description, status);
        this.type=Type.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }


    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return  id +","
                + Type.SUBTASK + ","
                + title + ","
                + status + ","
                + description + ","
                + epicId;
    }
}
