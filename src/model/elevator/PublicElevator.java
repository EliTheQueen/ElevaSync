package model.elevator;

public class PublicElevator extends Elevator {

    public PublicElevator(int id, double maxWeight) {
        super(id, maxWeight);
    }

    @Override
    public ElevatorType getType() {
        return ElevatorType.PUBLIC;
    }

}
