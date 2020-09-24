package applications;

public class JobSpecification {
    private int numTasks;
    private int[] specificationsForTasks;
    private Task[] tasks;

    public void setNumTasks(int numTasks) {
        this.numTasks = numTasks;
    }

    public int getNumTasks() {
        return numTasks;
    }

    public void setSpecificationsForTasks(int[] specificationsForTasks) {
        this.specificationsForTasks = specificationsForTasks;
    }

    public void setSpecificationsForTasks(Task[] specificationsForTasks) {
        this.tasks = specificationsForTasks;
    }

    public int[] getSpecificationsForTasks() {
        return specificationsForTasks;
    }

    public Task[] getTasks() {
        return tasks;
    }
}
