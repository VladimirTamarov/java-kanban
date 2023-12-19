package manager;

import java.util.ArrayList;

public class Managers {

    public static TaskManager getDefault(){
          return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager(new ArrayList<>());
    }
}
