package model.fairness;

import model.elevator.Elevator;
import model.passenger.Passenger;

import java.util.List;

public interface FairnessStrategy {
    Passenger choose(List<Passenger> passengers, Elevator elevator);
}
