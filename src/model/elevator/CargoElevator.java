package model.elevator;

import model.building.Building;

public class CargoElevator extends Elevator {

    public CargoElevator(int id, double maxWeight, Building building) {
        super(id, maxWeight, building);
    }

    @Override
    public ElevatorType getType() {
        return ElevatorType.CARGO;
    }
}
