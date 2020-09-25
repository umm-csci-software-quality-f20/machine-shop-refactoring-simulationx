package applications;

public class JobSpecification {
    private int numTasks;
    private Task[] tasks;

    public void setNumTasks(int numTasks) {
        this.numTasks = numTasks;
    }

    public int getNumTasks() {
        return numTasks;
    }

    public void setSpecificationsForTasks(Task[] specificationsForTasks) {
        this.tasks = specificationsForTasks;
    }

    public Task[] getTasks() {
        return tasks;
    }
}
