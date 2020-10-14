package applications;

import exceptions.MyInputException;
import utilities.MyInputStream;

public class SpecificationReader {

    private final SimulationSpecification specification;
    private final MyInputStream keyboard;

    public SpecificationReader() {
        specification = new SimulationSpecification();
        keyboard = new MyInputStream();
    }

    /** input machine shop data */
    public SimulationSpecification readSpecification() {
        readNumberMachinesAndJobs();
        readChangeOverTimes();
        readJobSpecifications();
        return specification;
    }

    private void readNumberMachinesAndJobs() {
        System.out.println("Enter number of machines and jobs");
        int numMachines = keyboard.readInteger();
        int numJobs = keyboard.readInteger();
        if (numMachines < 1 || numJobs < 1) {
            throw new MyInputException(MachineShopSimulator.NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1);
        } else {
            specification.setNumMachines(numMachines);
            specification.setNumJobs(numJobs);
        }
    }

    private void readChangeOverTimes() {
        // input the change-over times
        int[] changeOverTimes = new int[specification.getNumMachines()+1];

        System.out.println("Enter change-over times for machines");
        for (int j = 1; j <= specification.getNumMachines(); j++) {
            int ct = keyboard.readInteger();
            if (ct < 0)
                throw new MyInputException(MachineShopSimulator.CHANGE_OVER_TIME_MUST_BE_AT_LEAST_0);
            changeOverTimes[j] = ct;
        }

        specification.setChangeOverTimes(changeOverTimes);
    }

    private void readJobSpecifications() {
        // input the jobs
        JobSpecification[] jobSpecifications = new JobSpecification[specification.getNumJobs()+1];
        for (int i=1; i <= specification.getNumJobs(); i++) {
            jobSpecifications[i] = new JobSpecification();
        }
        specification.setJobSpecification(jobSpecifications);
        for (int i = 1; i <= specification.getNumJobs(); i++) {
            System.out.println("Enter number of tasks for job " + i);
            int tasks = keyboard.readInteger(); // number of tasks
            if (tasks < 1)
                throw new MyInputException(MachineShopSimulator.EACH_JOB_MUST_HAVE_AT_LEAST_1_TASK);

            Task[] taskArray = new Task[tasks];

            System.out.println("Enter the tasks (machine, time)"
                    + " in process order");
            for (int j = 0; j < tasks; j++) { // get tasks for job i
                int theMachine = keyboard.readInteger();
                int theTaskTime = keyboard.readInteger();
                if (theMachine < 1 || theMachine > specification.getNumMachines()
                        || theTaskTime < 1)
                    throw new MyInputException(MachineShopSimulator.BAD_MACHINE_NUMBER_OR_TASK_TIME);
                taskArray[j] = new Task(theMachine, theTaskTime);
            }
            specification.setSpecificationsForTasks(i, taskArray);
        }
    }
}
