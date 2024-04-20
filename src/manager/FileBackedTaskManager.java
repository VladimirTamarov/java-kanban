package manager;

import model.*;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    public static void main(String[] args) {
        Path path = Paths.get("test1.csv");
        /*FileBackedTaskManager manager = loadFromFile(path); // для сценария с восстановлением из файла
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getEpicById(6));
        System.out.println(manager.getHistory());
        System.out.println(manager.getTaskById(2));
        System.out.println(manager.getHistory());*/

         //Для сценария с записью в файл:
        FileBackedTaskManager manager = new FileBackedTaskManager(path);
        Task task = new Task("Стройка дома", "Закуп материалов", Status.NEW);
        manager.create(task);
        Task task2 = new Task("Мытьё окон", "Помыть все окна", Status.NEW);
        manager.create(task2);

        SubTask subTask = new SubTask("Стройка бани", "Закуп печки", Status.NEW);

        manager.create(subTask);

        SubTask subTask2 = new SubTask("Стройка дома", "Построить дом", Status.NEW);

        manager.create(subTask2);

        SubTask subTask3 = new SubTask("Благоустройство участка", "Проклдка дорожек, посадка растений", Status.NEW);

        manager.create(subTask3);
        Epic epic = new Epic("Обустройство поместья", "Обустроить загородное жилище");
        manager.create(epic);
        epic.getSubTasksIds().add(subTask.getId());
        epic.getSubTasksIds().add(subTask2.getId());
        epic.getSubTasksIds().add(subTask3.getId());
        subTask.setEpicId(6);
        subTask2.setEpicId(6);
        subTask3.setEpicId(6);

        Epic epic2 = new Epic("Эпик без подзадач", "У этого эпика нет никаких подзадач");
        manager.create(epic2);




    }

    private Path path;

    final int TITLE_SIZE = 1;    // кол-во строк, которое занимает стандартный заголовок файла данных

    public FileBackedTaskManager(Path path) {
        this.path = path;


    }






    public static FileBackedTaskManager loadFromFile(Path path) {
        FileBackedTaskManager manager = new FileBackedTaskManager(path);

        String content;
        try {
            content = Files.readString(path);
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка чтения данных из файла");
        }

        String[] lines = content.split("\n");

        if (lines.length == manager.TITLE_SIZE){
            return manager;
        }
        for (int i = manager.TITLE_SIZE; i < lines.length; i++) {
            if (lines[i].isBlank()) {
                break;
            }
            Task task = fromString(lines[i]);
            if (task.getType() == Type.EPIC) {
                manager.epics.put(task.getId(), (Epic) task);

            } else if (task.getType() == Type.SUBTASK) {
                SubTask subTask = (SubTask) task;
                Epic epic = manager.epics.getOrDefault(subTask.getEpicId(), null);

                if (epic != null) {
                    epic.getSubTasksIds().add(subTask.getId());
                    //epic.setStatus(manager.getStatusForEpic(epic.getSubTasksIds()));
                    manager.subTasks.put(task.getId(), subTask);


                }


            } else {
                manager.tasks.put(task.getId(), task);
            }
        }
        List<Integer> history = historyFromString(lines[lines.length - 1]);
        for (Integer id : history) {
            manager.addToHistory(id);

        }
        return manager;


    }

    private static Task fromString(String value) {
        String[] data = value.split(",");
        if (data.length == 6) {
            SubTask subTask = new SubTask(data[2], data[4], Status.valueOf(data[3]));
            subTask.setEpicId(Integer.parseInt(data[5]));
            subTask.setId(Integer.parseInt(data[0]));
            return subTask;
        } else if (Type.EPIC.equals(Type.valueOf(data[1]))) {
            Epic epic = new Epic(data[2], data[4]);
            epic.setStatus(Status.valueOf(data[3]));
            epic.setId(Integer.parseInt(data[0]));
            return epic;
        } else {
            Task task = new Task(data[2], data[4], Status.valueOf(data[3]));
            task.setId(Integer.parseInt(data[0]));
            return task;
        }
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        List<Task> list = manager.getHistory();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).getId());
            if (i+1 != list.size()){
                sb.append(",");
            }
        }
        return sb.toString();
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> historyIDs = new ArrayList<>();
        String[] values = value.split(",");
        if (values.length == 0){
            return null;
        }
        for (String s : values) {
            historyIDs.add(Integer.parseInt(s));
        }
        return historyIDs;

    }


    @Override
    public void tasksClear() {
        super.tasksClear();
        save();
    }

    @Override
    public void subTasksClear() {
        super.subTasksClear();
        save();
    }

    @Override
    public void epicsClear() {
        super.epicsClear();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;

    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public int create(Task task) {
        super.create(task);
        save();
        return task.getId();
    }

    @Override
    public int create(Epic epic) {
        super.create(epic);
        save();
        return epic.getId();

    }

    @Override
    public int create(SubTask subTask) {
        super.create(subTask);
        save();
        return subTask.getId();

    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(path.toFile())) {
            fileWriter.write("id,type,name,status,description,epic\n");

            if (!getAllEpicsList().isEmpty()) {
                ArrayList<Epic> epics = getAllEpicsList();
                for (Epic epic : epics) {
                    fileWriter.write(epic.toString() + "\n");
                }
            }
            if (!getAllTasksList().isEmpty()) {
                ArrayList<Task> tasks = getAllTasksList();
                for (Task task : tasks) {
                    fileWriter.write(task.toString() + "\n");
                }
            }
            if (!getAllSubTasksList().isEmpty()) {
                ArrayList<SubTask> subTasks = getAllSubTasksList();
                for (SubTask subTask : subTasks) {
                    fileWriter.write(subTask.toString() + "\n");
                }
            }
            fileWriter.write("\n");

            fileWriter.write(historyToString(historyManager));


        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных в файл!");
        }

    }


}
