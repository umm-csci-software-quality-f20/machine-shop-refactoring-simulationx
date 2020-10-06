package applications;

// top-level nested classes
class Task {
    // data members
    private int machine;
    private int time;

    // constructor
    Task(int theMachine, int theTime) {
        machine = theMachine;
        time = theTime;
    }

    public int getMachine() {
        return machine;
    }

    public int getTime() {
        return time;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{ Machine Number: ").append(machine).append(" | Duration: ").append(time).append(" }");
        return builder.toString();
    }
}
