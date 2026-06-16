package model.passenger;

import model.task.Task;

import java.util.UUID;

public abstract class Passenger implements Runnable{

    public enum PassengerRole {

        STUDENT,
        PROFESSOR,
        EDUCATIONAL_DEPUTY,
        PORTER,
        TECHNICIAN

    }

    public enum PassengerState {

        WAITING,
        RIDING,
        WORKING,
        FINISHED

    }

    protected final int age;
    protected final double weight;
    protected final UUID id;
    protected int currentFloor;
    protected int targetFloor;
    protected Task task;
    protected UUID assignedElevatorId;
    protected PassengerState state;

    public Passenger(
            int age,
            double weight,
            Task task
    ) {
        this.age = age;
        this.weight = weight;
        this.task = task;

        this.currentFloor = 0;
        this.state = PassengerState.WAITING;
        this.id = UUID.randomUUID();
    }

    public abstract PassengerRole getRole();

    @Override
    public void run() {
        System.out.println(getRole() + " entered building.");

        requestElevator();

        doTask();

        requestReturnElevator();

        System.out.println(getRole() + " left building.");
    }

    private void requestReturnElevator() {
        System.out.println(getRole() + " is returning to ground floor.");
    }

    private void doTask() {
        try {
            state = PassengerState.WORKING;
            System.out.println(getRole() + " is doing task " + task.getId());

            Thread.sleep(task.getDuration());
            state = PassengerState.FINISHED;
            System.out.println(getRole() + " is finished task " + task.getId());

        }  catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void requestElevator() {
        System.out.println(getRole() + " is waiting for elevator to floor " + task.getDestinationFloor());
    }
}
