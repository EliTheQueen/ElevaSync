package model.elevator;

import model.building.Building;
import model.fairness.FairnessStrategy;
import model.passenger.Passenger;

public class CargoElevator extends Elevator {

    public CargoElevator(int id, double maxWeight, Building building, FairnessStrategy fairnessStrategy, RepairCenter repairCenter) {
        super(id, maxWeight, building, fairnessStrategy, repairCenter);
    }

    @Override
    public ElevatorType getType() {
        return ElevatorType.CARGO;
    }

    @Override
    public boolean canServe(Passenger passenger) {
        return passenger.getRole() == Passenger.PassengerRole.PORTER && passenger.getTotalWeight() <= maxWeight;
    }
}
