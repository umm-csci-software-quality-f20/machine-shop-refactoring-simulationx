package applications;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dataStructures.LinkedQueue;

public class MachineTest {
    Machine machine;

    @Before public void initialize() {
        machine = new Machine();
        LinkedQueue jobs = machine.getJobQ();
        for (int i = 0; i < 5; i++) {
            Job newJob = new Job(i);
            newJob.setArrivalTime(i+1);
            Task newTask = new Task(1,2);
            newJob.addTask(newTask);
            jobs.put(newJob);
        }
    }

    @Test
    public final void setsActiveJobToNull() {
        machine.setInactive();
        assertFalse(machine.isActive());
        assertNull(machine.getActiveJob());
    }

    @Test
    public final void returnsActiveJob() {
        int currentSize = machine.getJobQ().size();

        machine.updateActiveJob();
        assertEquals(0, machine.getActiveJob().getId());
        assertEquals(currentSize - 1, machine.getJobQ().size());

        machine.updateActiveJob();
        assertEquals(1, machine.getActiveJob().getId());
        assertEquals(currentSize - 2, machine.getJobQ().size());
    }

    @Test
    public final void scheduleChangesCorrectly() {
        int setTime = (int) (Math.random() * 10);
        machine.setChangeTime(setTime);
        int time = machine.scheduleChangeOverTime();
        assertFalse(machine.isActive());
        assertEquals(setTime, time);
    }

    @Test
    public final void worksNextTaskCorrectly() {
        int timeNow =  20;
        Job frontElement = (Job) machine.getJobQ().getFrontElement();
        int arrivalTime = frontElement.getArrivalTime();

        assertEquals(0, machine.getTotalWait());
        assertEquals(0, machine.getNumTasks());

        machine.workNextTask(timeNow);

        assertEquals(frontElement, machine.getActiveJob());
        assertEquals(timeNow - arrivalTime, machine.getTotalWait());
        assertEquals(1, machine.getNumTasks());
    }
}
