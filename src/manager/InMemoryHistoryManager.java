package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    public Node<Task> head;
    public Node<Task>tail;
    private int size = 0;
    Map<Integer, Node<Task>> tasksMap = new HashMap<>();

    public Node<Task> linkLast(Task task){
       Node<Task> oldTail = tail;
       Node<Task> newNode = new Node<>(tail, task, null);
       tail = newNode;
       if (oldTail == null){
           head = newNode;
       }
       else {
           oldTail.setNext(newNode);
       }
       size++;
       return newNode;
    }

    @Override
    public void add(Task task){
        if (tasksMap.containsKey(task.getId())){
            removeNode(tasksMap.get(task.getId()));           // получаем Node из мапы по ID задачи и удаляем

        }
        tasksMap.put(task.getId(), linkLast(task));            // добавляем Node в мапу и складываем в конец списка
    }



    public List<Task> getTask(){
        List<Task> historyTaskList = new ArrayList<>();
        Node<Task> curTask = head;
        if (curTask == null){
            System.out.println("История просмотров пуста");
        }
        else {
            while (curTask != null){
                historyTaskList.add(curTask.getTask());
                curTask = curTask.getNext();
            }
        }
        return historyTaskList;
    }

    public void removeNode(Node<Task> node){
        if (node != null){
            Node<Task> next = node.getNext();
            Node<Task> prev = node.getPrev();
            node.setTask(null);

            if (head == node && tail == node){
                head = null;
                tail = null;
            }
            else if (head == node){
                head = next;
                head.setPrev(null);
            }
            else if (tail == node){
                tail = prev;
                tail.setNext(null);
            }
            else{
                prev.setNext(next);
                next.setPrev(prev);
            }
        }
    }
    @Override
    public void remove(int id) {
      if (tasksMap.containsKey(id)){
          removeNode(tasksMap.get(id));
      }
    }

    @Override
    public List<Task> getHistory() {
        return getTask();
    }
}
