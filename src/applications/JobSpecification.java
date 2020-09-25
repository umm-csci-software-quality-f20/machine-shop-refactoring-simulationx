package applications;

public class JobSpecification {
    private Task[] tasks;

    public void setSpecificationsForTasks(Task[] specificationsForTasks) {
        this.tasks = specificationsForTasks;
    }

    public Task[] getTasks() {
        return tasks;
    }
}
