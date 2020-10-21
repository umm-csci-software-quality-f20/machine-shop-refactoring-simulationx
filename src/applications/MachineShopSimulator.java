/** machine shop simulation */

package applications;

public class MachineShopSimulator {

    public static final String NUMBER_OF_MACHINES_MUST_BE_AT_LEAST_1 = "number of machines must be >= 1";
    public static final String NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1 = "number of machines and jobs must be >= 1";
    public static final String CHANGE_OVER_TIME_MUST_BE_AT_LEAST_0 = "change-over time must be >= 0";
    public static final String EACH_JOB_MUST_HAVE_AT_LEAST_1_TASK = "each job must have >= 1 task";
    public static final String BAD_MACHINE_NUMBER_OR_TASK_TIME = "bad machine number or task time";

    // data members of MachineShopSimulator
    private int timeNow; // current time
    private int numMachines; // number of machines
    private int numJobs; // number of jobs
    private EventList eList; // pointer to event list
    private Machine[] machine; // array of machines
    private int largeTime; // all machines finish before this

    /** entry point for machine shop simulator */
    public static void main(String[] args) {
        /*
         * It's vital that we (re)set this to 0 because if the simulator is called
         * multiple times (as happens in the acceptance tests), because timeNow
         * is static it ends up carrying over from the last time it was run. I'm
         * not convinced this is the best place for this to happen, though.
         */
        final SpecificationReader specificationReader = new SpecificationReader();
        SimulationSpecification specification = specificationReader.readSpecification();
        MachineShopSimulator simulator = new MachineShopSimulator();
        SimulationResults simulationResults = simulator.runSimulation(specification);
        simulationResults.print();
    }

    // methods

    public  SimulationResults runSimulation(SimulationSpecification specification) {
        largeTime = Integer.MAX_VALUE;
        timeNow = 0;
        startShop(specification); // initial machine loading
        SimulationResults simulationResults = new SimulationResults(numJobs);
        simulate(simulationResults); // run all jobs through shop
        outputStatistics(simulationResults);
        return simulationResults;
    }

    /** load first jobs onto each machine
     * @param specification
     * */
    void startShop(SimulationSpecification specification) {
        // Move this to startShop when ready
        numMachines = specification.getNumMachines();
        numJobs = specification.getNumJobs();
        createEventAndMachineQueues(specification);

        // Move this to startShop when ready
        setMachineChangeOverTimes(specification);

        // Move this to startShop when ready
        setUpJobs(specification);

        for (int p = 0; p < numMachines; p++) {
            changeState(p);
        }
    }

    private void createEventAndMachineQueues(SimulationSpecification specification) {
        // create event and machine queues
        eList = new EventList(specification.getNumMachines(), largeTime);
        machine = new Machine[specification.getNumMachines()];
        for (int i = 0; i < specification.getNumMachines(); i++)
            machine[i] = new Machine();
    }

    private void setMachineChangeOverTimes(SimulationSpecification specification) {
        for (int i = 0; i < specification.getNumMachines(); ++i) {
            machine[i].setChangeTime(specification.getChangeOverTimes(i+1));
        }
    }

    private void setUpJobs(SimulationSpecification specification) {
        // input the jobs
        Job theJob;
        for (int i = 0; i < specification.getNumJobs(); i++) {
            Task[] tasks = specification.getJobSpecifications(i).getTasks();
            int firstMachine = 0; // machine for first task

            // create the job
            theJob = new Job(i);

            for (int j = 0; j < tasks.length; j++) {
                Task task = tasks[j];
                if (j == 0)
                    firstMachine = task.getMachine(); // job's first machine
                theJob.addTask(task);
            } // task queue
            machine[firstMachine].getJobQ().put(theJob);
        }
    }

    /**
     * change the state of theMachine
     *
     * @return last job run on this machine
     */
    Job changeState(int theMachine) {// Task on theMachine has finished, schedule next one.
        Job activeJob = null;
        int finishTime = largeTime;

        if (machine[theMachine].isActive()) {// Machine is working on a job
            activeJob = machine[theMachine].getActiveJob();
            finishTime= timeNow + machine[theMachine].scheduleChangeOverTime();
        } else if (machine[theMachine].jobsQueued()) {
            finishTime = timeNow + machine[theMachine].workNextTask(timeNow);
        }
        eList.setFinishTime(theMachine, finishTime);

        return activeJob;
    }


    /** process all jobs to completion
     * @param simulationResults
     * */
    void simulate(SimulationResults simulationResults) {
        while (numJobs > 0) {// at least one job left
            int nextToFinish = eList.nextEventMachine();
            timeNow = eList.nextEventTime(nextToFinish);
            // change job on machine nextToFinish
            Job theJob = changeState(nextToFinish);
            // move theJob to its next machine
            // decrement numJobs if theJob has finished
            if (theJob != null && !moveToNextMachine(theJob, simulationResults))
                numJobs--;
        }
    }

    /**
     * move theJob to machine for its next task
     * @return false iff no next task
     */
    boolean moveToNextMachine(Job theJob, SimulationResults simulationResults) {
        if (theJob.getTaskQ().isEmpty()) {// no next task
            simulationResults.setJobCompletionData(theJob.getId(), timeNow, timeNow - theJob.getLength());
            return false;
        } else {// theJob has a next task
                // get machine for next task
            int p = theJob.getTaskQ().getFrontElement().getMachine();
            // put on machine p's wait queue
            machine[p].getJobQ().put(theJob);
            theJob.setArrivalTime(timeNow);
            // if p idle, schedule immediately
            if (eList.nextEventTime(p) == largeTime) {// machine is idle
                changeState(p);
            }
            return true;
        }
    }

    /** output wait times at machines
     * @param simulationResults
     * */
    void outputStatistics(SimulationResults simulationResults) {
        simulationResults.setFinishTime(timeNow);
        simulationResults.setNumMachines(numMachines);
        setNumTasksPerMachine(simulationResults);
        setTotalWaitTimePerMachine(simulationResults);
    }

    private void setNumTasksPerMachine(SimulationResults simulationResults) {
        int[] numTasksPerMachine = new int[numMachines];
        for (int i=0; i<numMachines; ++i) {
            numTasksPerMachine[i] = machine[i].getNumTasks();
        }
        simulationResults.setNumTasksPerMachine(numTasksPerMachine);
    }

    private void setTotalWaitTimePerMachine(SimulationResults simulationResults) {
        int[] totalWaitTimePerMachine = new int[numMachines];
        for (int i=0; i<numMachines; ++i) {
            totalWaitTimePerMachine[i] = machine[i].getTotalWait();
        }
        simulationResults.setTotalWaitTimePerMachine(totalWaitTimePerMachine);
    }
}
