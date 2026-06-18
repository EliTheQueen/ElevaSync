package model.passenger;

import model.building.Building;
import model.task.Task;

public class Student extends Passenger {

    public Student(int age, double weight, Task task, Building building) {
        super(age, weight, task, building);
    }

    @Override
    public PassengerRole getRole() {
        return PassengerRole.STUDENT;
    }
}
