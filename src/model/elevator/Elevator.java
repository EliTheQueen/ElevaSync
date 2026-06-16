package model.elevator;

import model.passenger.Passenger;

public abstract class Elevator implements Runnable {

    public enum ElevatorType {
        PUBLIC,
        VIP,
        CARGO
    }

    protected final int id;

    protected int currentFloor;

    protected double maxWeight;

    protected boolean broken;

    protected boolean running = true;

    protected Passenger currentPassenger;

    protected ElevatorType type;

    public Elevator(
            int id,
            double maxWeight
    ) {
        this.id = id;
        this.maxWeight = maxWeight;

        this.currentFloor = 0;
        this.broken = false;
    }

    public abstract ElevatorType getType();

    @Override
    public void run() {
        System.out.println(getType() + " elevator " + id + " is running!");
        while (running) {
            moveOnFloor();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
        System.out.println(getType() + " elevator " + id + " is stopped!");
    }

    private void moveOnFloor() {
        currentFloor++;

        if (currentFloor > 5) {
            currentFloor = 0;
        }

        System.out.println(getType() + " elevator " + id + " is on floor " + currentFloor);
    }

    public void shutDown() {
        running = false;
    }
}
