package applications;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimulationSpecificationGenerator extends Generator<SimulationSpecification> {
    // I initially thought of having larger numbers here, like 100 or 1,000, but
    // in playing with properties for `LinkedQueue` it became clear that if your
    // sizes were too big you never hit edge cases like an empty queue.
    public static final int MAX_MACHINES = 10;
    public static final int MAX_JOBS = 10;
    public static final int MAX_TASKS = 10;
    public static final int MAX_CHANGEOVER_TIME = 10;
    private static final int MAX_TASK_TIME = 10;

    public SimulationSpecificationGenerator() {
        super(SimulationSpecification.class);
    }

    @Override public SimulationSpecification generate(SourceOfRandomness r, GenerationStatus status) {
        SimulationSpecification result = new SimulationSpecification();
        // I add one to these random numbers so I don't end up with 0 machines
        // or 0 jobs.
        int numMachines = r.nextInt(MAX_MACHINES) + 1;
        int numJobs = r.nextInt(MAX_JOBS) + 1;

        result.setNumMachines(numMachines);
        result.setNumJobs(numJobs);

        // Ugh – the annoying problem of the indices starting at one again
        // so I have to make the array one too large and skip the first
        // slot.
        int[] changeOverTimes = new int[numMachines];
        for (int i=0; i<numMachines; ++i) {
            // Changeover times can be 0 so I don't need to add 1 here.
            changeOverTimes[i] = r.nextInt(MAX_CHANGEOVER_TIME);
        }
        result.setChangeOverTimes(changeOverTimes);

        JobSpecification[] jobSpecifications = new JobSpecification[numJobs];
        for (int i=0; i<numJobs; ++i) {
            jobSpecifications[i] = new JobSpecification();
        }
        result.setJobSpecification(jobSpecifications);
        for (int i=0; i<numJobs; ++i) {
            int numTasks = r.nextInt(MAX_TASKS) + 1;

            Task[] specificationsForTasks = new Task[numTasks];

            for (int j = 0; j < numTasks; ++j) {
                int theMachine = r.nextInt(numMachines) + 1;
                int theTaskTime = r.nextInt(MAX_TASK_TIME) + 1;
                specificationsForTasks[j] = new Task(theMachine-1, theTaskTime);
            }
            result.setSpecificationsForTasks(i, specificationsForTasks);
        }

        return result;
    }

    /**
     * We'll try each of the following ways of shrinking things:
     * * Removing a random machine (& all references to it in the task lists)
     * * Removing a random job & its task list
     * These two moves seem to get us down to one machine and one job
     * pretty consistently when there are fundamental errors in the code.
     *
     * @param r source of randomness
     * @param spec the simulation specification to shrink
     * @return a list of smaller specifications
     */
    @Override
    public List<SimulationSpecification> doShrink(
            SourceOfRandomness r, SimulationSpecification spec) {

        return Stream.of(
            removeRandomMachine(r, spec),
            removeRandomJob(r, spec)
        )
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    }

    private SimulationSpecification removeRandomMachine(
            SourceOfRandomness r, SimulationSpecification spec) {
        SimulationSpecification smallerSpec = new SimulationSpecification();
        int originalNumMachines = spec.getNumMachines();
        if (originalNumMachines == 1) {
            return null;
        }
        int numJobs = spec.getNumJobs();
        smallerSpec.setNumMachines(originalNumMachines - 1);
        smallerSpec.setNumJobs(numJobs);

        int machineToRemove = r.nextInt(originalNumMachines) + 1;

        int[] newChangeOvers = new int[originalNumMachines];
        for (int i=0, j=0; i<originalNumMachines; ++i) {
            if (i != machineToRemove) {
                newChangeOvers[j] = spec.getChangeOverTimes(i);
                ++j;
            }
        }
        smallerSpec.setChangeOverTimes(newChangeOvers);

        JobSpecification[] newJobSpecs = new JobSpecification[numJobs];
        for (int i=0; i<numJobs; ++i) {
            JobSpecification jobSpec = spec.getJobSpecifications(i);
            Task[] specsForTasks = jobSpec.getTasks();
            int numTasks = specsForTasks.length;
            int numTasksOnThisMachine = 0;
            for (int j=0; j<specsForTasks.length; ++j) {
                if (specsForTasks[j].getMachine() == machineToRemove) {
                    ++numTasksOnThisMachine;
                }
            }
            // If these are equal then *all* the tasks for this job are
            // on the machine we're going to remove, which would make this
            // task empty and break things, so we'll just not remove this
            // machine.
            if (numTasksOnThisMachine == numTasks) {
                return null;
            }

            final int newNumTasks = numTasks - numTasksOnThisMachine;
            Task[] newTasks = new Task[newNumTasks];
            for (int j=0, k=0; j<numTasks; ++j) {
                int machine = specsForTasks[j].getMachine();
                if (machine != machineToRemove) {
                    if (machine > machineToRemove) {
                        --machine;
                    }
                    newTasks[k] = new Task(machine, specsForTasks[j].getTime());
                    ++k;
                }
            }
            JobSpecification newJobSpec = new JobSpecification();
            newJobSpec.setTasks(newTasks);
            newJobSpecs[i] = newJobSpec;
        }
        smallerSpec.setJobSpecification(newJobSpecs);

        return smallerSpec;
    }

    private SimulationSpecification removeRandomJob(SourceOfRandomness r, SimulationSpecification spec) {
        SimulationSpecification smallerSpec = new SimulationSpecification();
        int originalNumJobs = spec.getNumJobs();
        if (originalNumJobs == 1) {
            return null;
        }
        int numMachines = spec.getNumMachines();
        smallerSpec.setNumMachines(numMachines);
        smallerSpec.setNumJobs(originalNumJobs-1);

        int[] changeOverTimes = new int[numMachines];
        for (int i=0; i<numMachines; ++i) {
            changeOverTimes[i] = spec.getChangeOverTimes(i);
        }
        smallerSpec.setChangeOverTimes(changeOverTimes);

        int jobToRemove = r.nextInt(originalNumJobs) + 1;

        JobSpecification[] newJobSpecs = new JobSpecification[originalNumJobs];
        for (int i=0, j=0; i<originalNumJobs; ++i) {
            if (i != jobToRemove) {
                newJobSpecs[j] = spec.getJobSpecifications(i);
                ++j;
            }
        }
        smallerSpec.setJobSpecification(newJobSpecs);

        return smallerSpec;
    }

    @Override public BigDecimal magnitude(Object value) {
        SimulationSpecification simulationSpecification = (SimulationSpecification) value;
        int size = simulationSpecification.getNumMachines();
        size += simulationSpecification.getNumJobs();
        for (int i=0; i<simulationSpecification.getNumJobs(); ++i) {
            size += simulationSpecification.getJobSpecifications(i).getTasks().length;
        }

        return BigDecimal.valueOf(size);
    }
}
