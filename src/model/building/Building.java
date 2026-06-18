package model.building;

import model.elevator.Elevator;

import java.util.ArrayList;
import java.util.List;

public class Building {

    private final List<Floor> floors;

    public Building(int floorCount) {
        floors = new ArrayList<>();
        for (int i = 0; i < floorCount; i++) {
            floors.add(new Floor(i, floorCount));
        }
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public Floor getFloor(int currentFloor) {
        return floors.get(currentFloor);
    }
}
