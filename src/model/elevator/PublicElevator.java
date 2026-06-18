package model.elevator;

import model.building.Building;

public class PublicElevator extends Elevator {

    public PublicElevator(int id, double maxWeight, Building building) {
        super(id, maxWeight, building);
    }

    @Override
    public ElevatorType getType() {
        return ElevatorType.PUBLIC;
    }

}
