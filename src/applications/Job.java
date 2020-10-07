package applications;

import dataStructures.LinkedQueue;

class Job {
    // data members
    private LinkedQueue<Task> taskQ; // this job's tasks
    private int length; // sum of scheduled task times
    private int arrivalTime; // arrival time at current queue
    private int id; // job identifier

    // constructor
    Job(int theId) {
        id = theId;
        taskQ = new LinkedQueue<Task>();
        // length and arrivalTime have default value 0
    }

    public void addTask(Task task) {
        getTaskQ().put(task);
    }

    /**
     * remove next task of job and return its time also update length
     */
    public int removeNextTask() {
        int theTime = getTaskQ().remove().getTime();
        length = getLength() + theTime;
        return theTime;
    }

    public LinkedQueue<Task> getTaskQ() {
        return taskQ;
    }

    public int getLength() {
        return length;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

}
