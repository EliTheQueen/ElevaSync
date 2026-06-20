package model.elevator;

import model.building.Building;
import model.fairness.FairnessStrategy;
import model.passenger.Passenger;
import model.repair.RepairCenter;

public class PublicElevator extends Elevator {

    public PublicElevator(int id, double maxWeight, Building building, FairnessStrategy fairnessStrategy, RepairCenter repairCenter) {
        super(id, maxWeight, building, fairnessStrategy, repairCenter);
    }

    @Override
    public ElevatorType getType() {
        return ElevatorType.PUBLIC;
    }

    @Override
    public boolean canServe(Passenger passenger) {
        return passenger.getRole() != Passenger.PassengerRole.PORTER && passenger.getTotalWeight() <= maxWeight;
    }

}
