package model.passenger;

import model.task.Task;

public class EducationalDeputy extends Passenger {

    public EducationalDeputy(int age, double weight, Task task) {
        super(age, weight, task);
    }

    @Override
    public PassengerRole getRole() {
        return PassengerRole.EDUCATIONAL_DEPUTY;
    }
}
