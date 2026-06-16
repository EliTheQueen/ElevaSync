package model.building;

import java.util.ArrayList;
import java.util.List;

public class Building {

    private final List<Floor> floors;

    public Building(int floorCount) {
        floors = new ArrayList<>();
        for (int i = 0; i < floorCount; i++) {
            floors.add(new Floor(i));
        }
    }

    public List<Floor> getFloors() {
        return floors;
    }
}
