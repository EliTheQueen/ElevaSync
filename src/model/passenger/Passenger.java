package model.passenger;

import model.building.Building;
import model.building.Floor;
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
    protected Building building;

    public Passenger(int age, double weight, Task task, Building building) {
        this.age = age;
        this.weight = weight;
        this.task = task;
        this.building = building;
        this.currentFloor = 0;
        this.targetFloor = task.getDestinationFloor();
        this.id = UUID.randomUUID();
    }

    public abstract PassengerRole getRole();

    @Override
    public void run() {
        System.out.println(getRole() + " entered building.");

        waitForElevator();

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

    private void waitForElevator() {
        int elevatorId = 0;

        Floor floor = building.getFloor(currentFloor);

        floor.addPassenger(elevatorId, this);

        System.out.println(getRole() + " is waiting for elevator " + elevatorId + " on floor " + currentFloor);

        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
