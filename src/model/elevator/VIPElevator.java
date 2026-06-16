package model.elevator;

public class VIPElevator extends Elevator {

    public  VIPElevator(int id, double maxWeight) {
        super(id, maxWeight);
    }

    @Override
    public ElevatorType getType() {
        return ElevatorType.VIP;
    }
}
