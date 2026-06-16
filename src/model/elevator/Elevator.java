package model.elevator;

public abstract class Elevator {

    public enum ElevatorType {
        PUBLIC,
        VIP,
        CARGO
    }

    protected int id;

    protected int currentFloor;

    protected double maxWeight;

    protected boolean broken;

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
}
