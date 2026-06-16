package model.passenger;

import model.task.Task;

public class Student extends Passenger {

    public Student(int age, double weight, Task task) {
        super(age, weight, task);
    }

    @Override
    public PassengerRole getRole() {
        return PassengerRole.STUDENT;
    }
}
