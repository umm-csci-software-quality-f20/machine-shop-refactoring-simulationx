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
        assertFalse(machine.isActive());
        assertNull(machine.getActiveJob());
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
