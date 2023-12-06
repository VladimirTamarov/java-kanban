package model;

public class SubTask extends Task {

 protected int epicId;

    public SubTask(String title, String description, String status) {
        super(title, description, status);
    }

    public int getEpicId() {
        return epicId;
    }



    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "model.SubTask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
