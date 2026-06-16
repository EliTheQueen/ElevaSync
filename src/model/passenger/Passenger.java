package model.passenger;

import model.task.Task;

public abstract class Passenger {

    public enum PassengerRole {

        STUDENT,
        PROFESSOR,
        EDUCATIONAL_DEPUTY,
        PORTER,
        TECHNICIAN

    }

    public enum PassengerState {

        WAITING,
        RIDING,
        WORKING,
        FINISHED

    }

    protected int age;
    protected double weight;

    protected int currentFloor;

    protected Task task;

    protected PassengerState state;

    public Passenger(
            int age,
            double weight,
            Task task
    ) {
        this.age = age;
        this.weight = weight;
        this.task = task;

        this.currentFloor = 0;
        this.state = PassengerState.WAITING;
    }

    public abstract PassengerRole getRole();
}
