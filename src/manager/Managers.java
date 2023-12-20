package manager;

import java.util.ArrayList;
import java.util.LinkedList;

public class Managers {

    public static TaskManager getDefault(){
          return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager(new LinkedList<>());
    }
}
