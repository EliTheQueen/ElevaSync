package model.fairness;

import model.elevator.Elevator;
import model.passenger.Passenger;

import java.util.Comparator;
import java.util.List;

public class AgePriorityStrategy implements FairnessStrategy {
    @Override
    public Passenger choose(List<Passenger> passengers, Elevator elevator) {
        return passengers.stream()
                .max(Comparator.comparingInt(Passenger::getAge)
                        .thenComparingLong(Passenger::getWaitingTime))
                .orElse(null);
    }
}
