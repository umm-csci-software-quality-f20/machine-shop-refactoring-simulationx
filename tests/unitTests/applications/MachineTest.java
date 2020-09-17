package applications;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MachineTest {
    Machine machine;

    @Before public void initialize() {
        machine = new Machine();
    }
    
    @Test
    public final void setsActiveJobToNull(){
        machine.setInactive();
        assertTrue(machine.isInactive());
        assertNull(machine.getActiveJob());
    }
    
    @Test
    public final void testIncNumTasks(){
        machine.setNumTasks(1);
        assertTrue(machine.getNumTasks()==1);
        machine.incNumTasks();
        assertTrue(machine.getNumTasks()==2);
    }

    @Test
    public final void testSetNumTasks(){
        for (int i = 0; i < 100; i++) {
            int r = (int) Math.random()*100;
            machine.setNumTasks(r);
            assertTrue(machine.getNumTasks()==r);
        }
    }

    @Test
    public final void returnsActiveJob(){
        Job job = new Job(12);
        machine.getJobQ().put(job);
        assertTrue(machine.getJobQ().size() == 1);
        machine.updateActiveJob();
        assertTrue(machine.getActiveJob() == job);
        assertTrue(machine.getJobQ().size() == 0);
    }
}