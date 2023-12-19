package manager;

import model.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private List<Task> history;

    public InMemoryHistoryManager(List<Task> history) {
        this.history = history;
    }

    @Override
    public List<Task> getHistory(){
        return history;
    }

    @Override
    public void addToHistory(Task task){
        if (history.size()<10){
            history.add(task);
        }
        else {
            history.remove(0);
            history.add(task);
        }
    }
}
