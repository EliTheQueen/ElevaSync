package model.elevator;

import model.building.Building;

public class VIPElevator extends Elevator {

    public  VIPElevator(int id, double maxWeight, Building building) {
        super(id, maxWeight, building);
    }

    @Override
    public ElevatorType getType() {
        return ElevatorType.VIP;
    }
}
