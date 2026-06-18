package model.passenger;

import model.building.Building;
import model.task.Task;

public class Professor extends Passenger {

    public Professor(int age, double weight, Task task, Building building) {
        super(age, weight, task, building);
    }

    @Override
    public PassengerRole getRole() {
        return PassengerRole.PROFESSOR;
    }
}
