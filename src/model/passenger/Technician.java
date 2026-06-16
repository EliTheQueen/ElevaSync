package model.passenger;

import model.task.Task;

public class Technician extends Passenger{

    public Technician(int age, double weight, Task task ) {
        super(age, weight, task);
    }

    @Override
    public PassengerRole getRole() {
        return PassengerRole.TECHNICIAN;
    }
}
