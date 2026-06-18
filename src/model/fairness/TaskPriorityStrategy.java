package model.fairness;

import model.elevator.Elevator;
import model.passenger.Passenger;
import model.task.Task;

import java.util.Comparator;
import java.util.List;

public class TaskPriorityStrategy implements FairnessStrategy{

    @Override
    public Passenger choose(List<Passenger> passengers, Elevator elevator) {
        return passengers.stream()
                .max(Comparator.comparingInt(this::priorityScore)
                        .thenComparingLong(Passenger::getWaitingTime))
                .orElse(null);
    }

    private int priorityScore(Passenger passenger) {
        Task.TaskPriority priority = passenger.getTask().getPriority();
        return switch (priority) {
            case HIGH -> 3;
            case MEDIUM -> 2;
            case LOW -> 1;
        };
    }
}
