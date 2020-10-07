package applications;

import dataStructures.LinkedQueue;

class Machine {
    // data members
    private LinkedQueue<Job> jobQ; // queue of waiting jobs for this machine
    private int changeTime; // machine change-over time
    private int totalWait; // total delay at this machine
    private int numTasks; // number of tasks processed on this machine
    private Job activeJob; // job currently active on this machine

    // constructor
    Machine() {
        jobQ = new LinkedQueue<Job>();
    }

    public LinkedQueue<Job> getJobQ() {
        return jobQ;
    }

    public boolean jobsQueued() {
        return !jobQ.isEmpty();
    }

    public void setChangeTime(int changeTime) {
        this.changeTime = changeTime;
    }

    public int getTotalWait() {
        return totalWait;
    }

    public void setTotalWait(int timeNow) {
        this.totalWait = totalWait + timeNow - activeJob.getArrivalTime();
    }

    public int getNumTasks() {
        return numTasks;
    }

    public Job getActiveJob() {
        return activeJob;
    }

    public boolean isActive() {
        return activeJob != null;
    }

    public void updateActiveJob() {
        activeJob = jobQ.remove();
    }

	public void setInactive() {
        activeJob = null;
	}

	public int nextTask() {
		return activeJob.removeNextTask();
    }

    public int scheduleChangeOverTime() {
        setInactive();
        return changeTime;
    }

    public int workNextTask(int timeNow) {
        updateActiveJob();
        setTotalWait(timeNow);
        numTasks++;
        return nextTask();
    }
}
