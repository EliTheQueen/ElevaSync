package model.fairness;

import model.elevator.Elevator;
import model.passenger.Passenger;
import model.task.Task;

import java.util.Comparator;
import java.util.List;

public class CombinedFairnessStrategy implements FairnessStrategy {
    @Override
    public Passenger choose(List<Passenger> passengers, Elevator elevator) {
        return passengers.stream()
                .max(Comparator.comparingInt(this::score))
                .orElse(null);
    }

    private int score(Passenger passenger) {
        return taskScore(passenger) + roleScore(passenger) + passenger.getAge() / 2
                + (int) Math.min(30, passenger.getWaitingTime() / 1000);
    }

    private int taskScore(Passenger passenger) {
        Task.TaskPriority priority = passenger.getTask().getPriority();
        return switch (priority) {
            case HIGH -> 30;
            case MEDIUM -> 20;
            case LOW -> 10;
        };
    }

    private int roleScore(Passenger passenger) {
        return switch (passenger.getRole()) {
            case EDUCATIONAL_DEPUTY -> 25;
            case PROFESSOR -> 20;
            case TECHNICIAN -> 15;
            case PORTER -> 12;
            case STUDENT -> 10;
        };
    }
}
