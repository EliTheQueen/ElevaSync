package model.elevator;

public class CargoElevator extends Elevator {

    public CargoElevator(int id, double maxWeight) {
        super(id, maxWeight);
    }

    @Override
    public ElevatorType getType() {
        return ElevatorType.CARGO;
    }
}
