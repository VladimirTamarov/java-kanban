package manager;


public class Node<Task> {
    private model.Task task;
    private Node<Task> next;
    private Node<Task> prev;

    public model.Task getTask() {
        return task;
    }

    public Node<Task> getNext() {
        return next;
    }

    public void setTask(model.Task task) {
        this.task = task;
    }

    public void setNext(Node<Task> next) {
        this.next = next;
    }

    public void setPrev(Node<Task> prev) {
        this.prev = prev;
    }

    public Node<Task> getPrev() {
        return prev;
    }

    @Override
    public String toString() {
        return "Node{" +
                "task=" + task +
                ", next=" + next +
                ", prev=" + prev +
                '}';
    }

    public Node(Node<Task> prev, model.Task task, Node<Task> next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }
}
