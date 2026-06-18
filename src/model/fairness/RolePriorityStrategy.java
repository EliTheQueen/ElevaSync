package model.fairness;

import model.elevator.Elevator;
import model.passenger.Passenger;

import java.util.Comparator;
import java.util.List;

public class RolePriorityStrategy implements FairnessStrategy {
    @Override
    public Passenger choose(List<Passenger> passengers, Elevator elevator) {
        return passengers.stream()
                .max(Comparator.comparingInt(this::roleScore)
                        .thenComparingLong(Passenger::getWaitingTime))
                .orElse(null);
    }

    private int roleScore(Passenger passenger) {
        return switch (passenger.getRole()) {
            case EDUCATIONAL_DEPUTY -> 5;
            case PROFESSOR -> 4;
            case TECHNICIAN -> 3;
            case PORTER -> 2;
            case STUDENT -> 1;
        };
    }
}
