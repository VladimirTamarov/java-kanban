package model;

import com.google.gson.reflect.TypeToken;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class Task {

    protected final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    protected int id;
    protected String title;
    protected String description;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected Type type;
    public Task() {

    }

    public Task(String title, String description, Status status, String startTime, long durationInMinutes) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
        if (startTime != null) {
            this.startTime = LocalDateTime.parse(startTime, FORMATTER);
        } else this.startTime = null;
        this.duration = Duration.ofMinutes(durationInMinutes);
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public String toString() {
        if (getStartTime() != null) {
            return id + ","
                    + Type.TASK + ","
                    + title + ","
                    + status + ","
                    + description + ","
                    + startTime.format(FORMATTER) + ","
                    + duration.toMinutes();
        } else return id + ","
                + Type.TASK + ","
                + title + ","
                + status + ","
                + description;

    }

    public static class TaskListTypeToken extends TypeToken<List<Task>> {

    }
}
