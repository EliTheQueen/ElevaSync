package model.elevator;

import model.building.Building;
import model.fairness.FairnessStrategy;
import model.passenger.Passenger;
import model.repair.RepairCenter;
import model.simulation.SimulationManager;

import java.util.Random;

public abstract class Elevator implements Runnable {

    public enum ElevatorType {
        PUBLIC,
        VIP,
        CARGO
    }

    private static final long TRAVEL_TIME_PER_FLOOR = 700;
    private static final int BREAKDOWN_PERCENT = 3;

    protected final int id;
    protected final double maxWeight;
    protected final Building building;
    protected final FairnessStrategy fairnessStrategy;
    protected final RepairCenter repairCenter;

    protected int currentFloor;
    protected volatile boolean broken;
    protected volatile boolean running = true;
    protected Passenger currentPassenger;

    private boolean goingUp = true;
    private final Random random = new Random();

    public Elevator(int id, double maxWeight, Building building, FairnessStrategy fairnessStrategy, RepairCenter repairCenter) {
        this.id = id;
        this.maxWeight = maxWeight;
        this.building = building;
        this.fairnessStrategy = fairnessStrategy;
        this.repairCenter = repairCenter;
        this.currentFloor = 0;
        this.broken = false;
    }

    public abstract ElevatorType getType();

    public abstract boolean canServe(Passenger passenger);

    @Override
    public void run() {
        System.out.println(getType() + " elevator " + id + " is running!");
        while (running) {

            try {
                if (broken) {
                    Thread.sleep(300);
                    continue;
                }

                checkBreakdown();

                if (broken) {
                    continue;
                }

                Passenger passenger = building.getFloor(currentFloor).pollPassenger(id, this, fairnessStrategy);
                if (passenger != null) {
                    servePassenger(passenger);
                } else {
                    moveOneFloor();
                }
            } catch (InterruptedException e) {
                running = false;
                Thread.currentThread().interrupt();
            }

        }
        System.out.println(getType() + " elevator " + id + " stopped!");
    }

    private void servePassenger(Passenger passenger) throws InterruptedException {
        double passengerWeight = passenger.getTotalWeight();
        SimulationManager.getInstance().enterElevatorWeight(passengerWeight);

        try {
            currentPassenger = passenger;
            passenger.markRiding(id);
            System.out.println(passenger.getRole() + " entered " + getType() + " elevator " + id + " on floor " + currentFloor);

            moveTo(passenger.getTargetFloor());

            if (broken || currentFloor != passenger.getTargetFloor()) {
                return;
            }

            System.out.println(passenger.getRole() + " left elevator " + id + " on floor " + currentFloor);

            passenger.notifyArrived(currentFloor);

        } finally {
            currentPassenger = null;
            SimulationManager.getInstance().leaveElevatorWeight(passengerWeight);
        }
    }

    private void moveTo(int targetFloor) throws InterruptedException {
        while (!broken && currentFloor != targetFloor) {
            if (currentFloor < targetFloor) {
                currentFloor++;
            } else {
                currentFloor--;
            }
            SimulationManager.getInstance().addTravelTime(TRAVEL_TIME_PER_FLOOR);
            System.out.println(getType() + " elevator " + id + " is on floor " + currentFloor);
            Thread.sleep(TRAVEL_TIME_PER_FLOOR);
            checkBreakdown();
        }
    }

    private void moveOneFloor() throws InterruptedException {
        int lastFloor = building.getFloorCount() - 1;
        if (lastFloor <= 0) {
            Thread.sleep(300);
            return;
        }

        if (currentFloor >= lastFloor) {
            goingUp = false;
        } else if (currentFloor <= 0) {
            goingUp = true;
        }

        if (goingUp) {
            currentFloor++;
        } else {
            currentFloor--;
        }

        SimulationManager.getInstance().addTravelTime(TRAVEL_TIME_PER_FLOOR);
        System.out.println(getType() + " elevator " + id + " is on floor " + currentFloor);
        Thread.sleep(TRAVEL_TIME_PER_FLOOR);
    }

    private void checkBreakdown() {
        if (!broken && random.nextInt(100) < BREAKDOWN_PERCENT) {
            broken = true;
            System.out.println(getType() + " elevator " + id + " broke on floor " + currentFloor);

            if (currentPassenger != null) {
                Passenger passenger = currentPassenger;
                currentPassenger = null;
                passenger.notifyElevatorBroken(currentFloor, id);
            }

            repairCenter.reportBrokenElevator(this);
        }
    }

    public synchronized void repair() {
        broken = false;
        System.out.println(getType() + " elevator " + id + " was repaired on floor " + currentFloor);
    }

    public void shutDown() {
        running = false;
    }

    public int getId() {
        return id;
    }

    public boolean isBroken() {
        return broken;
    }

    public double getMaxWeight() {
        return maxWeight;
    }
}
