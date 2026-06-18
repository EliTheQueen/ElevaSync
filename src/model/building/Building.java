package model.building;

import model.elevator.Elevator;
import model.passenger.Passenger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Building {

    private final List<Floor> floors;
    private final List<Elevator> elevators = new ArrayList<>();

    public Building(int floorCount, int elevatorCount) {
        floors = new ArrayList<>();
        for (int i = 0; i < floorCount; i++) {
            floors.add(new Floor(i, floorCount));
        }
    }

    public Floor getFloor(int floorNumber) {
        return floors.get(floorNumber);
    }

    public List<Floor> getFloors() {
        return Collections.unmodifiableList(floors);
    }

    public int getFloorCount() {
        return floors.size();
    }

    public synchronized void setElevators(List<Elevator> elevators) {
        this.elevators.clear();
        this.elevators.addAll(elevators);
    }

    public synchronized List<Elevator> getElevators() {
        return new ArrayList<>(elevators);
    }

    public synchronized int findBestElevatorIdFor(Passenger passenger, int forbiddenElevatorNumber) {
        for (Elevator elevator : elevators) {
            if (elevator.getId() != forbiddenElevatorNumber && elevator.canServe(passenger) && !elevator.isBroken()) {
                return elevator.getId();
            }
        }
        return -1;
    }
}
