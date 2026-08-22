package model.passenger;

import model.building.Building;
import model.building.Floor;
import model.simulation.SimulationManager;
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

    private static final long MAX_WAIT_TIME = 2500;

    protected final int age;
    protected final double weight;
    protected final UUID id;
    protected int currentFloor;
    protected int targetFloor;
    protected Task task;
    protected int assignedElevatorId = -1;
    protected PassengerState state;
    protected Building building;
    private boolean arrived;
    private boolean elevatorBroken;
    private long queueEnterTime;

    public Passenger(int age, double weight, Task task, Building building) {
        this.age = age;
        this.weight = weight;
        this.task = task;
        this.building = building;
        this.currentFloor = 0;
        this.targetFloor = task.getDestinationFloor();
        this.id = UUID.randomUUID();
        this.state = PassengerState.WAITING;
    }

    public abstract PassengerRole getRole();

    @Override
    public void run() {
        try {
            System.out.println(getRole() + " entered building.");

            requestElevator(task.getDestinationFloor());
            waitUntilArrived();

            doTask();

            requestElevator(0);
            waitUntilArrived();

            state = PassengerState.FINISHED;
            System.out.println(getRole() + " left building.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void requestElevator(int destinationFloor) throws InterruptedException {
        targetFloor = destinationFloor;
        arrived = false;
        elevatorBroken = false;
        state = PassengerState.WAITING;

        int forbiddenElevatorId = -1;
        while (!arrived) {
            int elevatorId = building.findBestElevatorIdFor(this, forbiddenElevatorId);
            if (elevatorId == -1) {
                forbiddenElevatorId = -1;
                Thread.sleep(300);
                continue;
            }

            elevatorBroken = false;
            assignedElevatorId = elevatorId;

            building.getFloor(currentFloor).addPassenger(elevatorId, this);

            boolean shouldTryAnotherQueue = waitInQueueOrRide(elevatorId);

            if (!shouldTryAnotherQueue) {
                return;
            }

            boolean removedFromQueue = building.getFloor(currentFloor).removePassenger(elevatorId, this);

            if (!removedFromQueue) {
                waitForArrivalOrBreakdown();

                if (arrived) {
                    return;
                }
            } else {
                System.out.println(getRole() + " cancelled waiting for elevator " + elevatorId + " on floor " + currentFloor);
            }

            forbiddenElevatorId = elevatorId;
        }
    }

    private synchronized boolean waitInQueueOrRide(int elevatorId) throws InterruptedException {

        long maxWaitTime = Math.max(MAX_WAIT_TIME, 2L * (building.getFloorCount() - 1) * 700L + 1000L);

        long start = System.currentTimeMillis();
        while (!arrived && !elevatorBroken && state == PassengerState.WAITING) {
            long elapsed = System.currentTimeMillis() - start;
            long remaining = maxWaitTime - elapsed;

            if (remaining <= 0) {
                int alternativeElevatorId = building.findBestElevatorIdFor(this, elevatorId);

                if (alternativeElevatorId == -1) {
                    start = System.currentTimeMillis();
                    continue;
                }

                return true;
            }

            wait(remaining);
        }

        while (!arrived && !elevatorBroken) {
            wait();
        }

        if (elevatorBroken) {
            return true;
        }
        return false;
    }

    private synchronized void waitUntilArrived() throws InterruptedException {
        while (!arrived) {
            wait();
        }
    }

    private synchronized void waitForArrivalOrBreakdown() throws InterruptedException {
        while (!arrived && !elevatorBroken) {
            wait();
        }
    }

    private void doTask() throws InterruptedException {
        state = PassengerState.WORKING;
        System.out.println(getRole() + " is doing task " + task.getId() + " on floor " + currentFloor);
        Thread.sleep(task.getDuration());
        System.out.println(getRole() + " finished task " + task.getId());
        SimulationManager.getInstance().addCompletedTask(task.getId());
    }

    public synchronized void markRiding(int elevatorId) {
        assignedElevatorId = elevatorId;
        state = PassengerState.RIDING;
        notifyAll();
    }

    public synchronized void notifyArrived(int floor) {
        currentFloor = floor;
        arrived = true;
        elevatorBroken = false;
        notifyAll();
    }

    public synchronized void notifyElevatorBroken(int floor, int brokenElevatorId) {
        currentFloor = floor;
        assignedElevatorId = -1;
        state = PassengerState.WAITING;
        elevatorBroken = true;
        System.out.println(getRole() + " got out of broken elevator " + brokenElevatorId + " on floor " + floor);
        notifyAll();
    }

    public void markQueueEnterTime() {
        queueEnterTime = System.currentTimeMillis();
    }

    public long getWaitingTime() {
        return Math.max(0, System.currentTimeMillis() - queueEnterTime);
    }

    public double getTotalWeight() {
        return weight;
    }

    public int getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public UUID getId() {
        return id;
    }

    public Task getTask() {
        return task;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }
}
