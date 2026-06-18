package model.elevator;

import model.building.Building;
import model.fairness.FairnessStrategy;
import model.passenger.Passenger;

public class VIPElevator extends Elevator {

    public  VIPElevator(int id, double maxWeight, Building building, FairnessStrategy fairnessStrategy, RepairCenter repairCenter) {
        super(id, maxWeight, building, fairnessStrategy, repairCenter);
    }

    @Override
    public ElevatorType getType() {
        return ElevatorType.VIP;
    }

    @Override
    public boolean canServe(Passenger passenger) {
        return (passenger.getRole() == Passenger.PassengerRole.PROFESSOR
                || passenger.getRole() == Passenger.PassengerRole.EDUCATIONAL_DEPUTY)
                && passenger.getTotalWeight() <= maxWeight;
    }
}
