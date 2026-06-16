package model.passenger;

import model.task.Task;

public class Porter extends Passenger {

    public Porter(int age, double weight, Task task) {
        super(age, weight, task);
    }

    @Override
    public PassengerRole getRole() {
        return PassengerRole.PORTER;
    }
}
