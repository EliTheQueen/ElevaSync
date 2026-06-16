package model.passenger;

import model.task.Task;

public class Professor extends Passenger {

    public Professor(int age, double weight, Task task) {
        super(age, weight, task);
    }

    @Override
    public PassengerRole getRole() {
        return PassengerRole.PROFESSOR;
    }
}
