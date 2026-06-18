package model.building;

import model.elevator.Elevator;
import model.passenger.Passenger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Floor {
    private int number;
    private final Map<Integer, Queue<Passenger>> elevatorQueues = new HashMap<>();

    public  Floor(int number, int elevatorCount) {
        this.number = number;
        for (int i = 0; i < elevatorCount; i++) {
            elevatorQueues.put(i, new LinkedList<>());
        }
    }

    public synchronized void addPassenger(int elevatorId, Passenger passenger) {
        elevatorQueues.get(elevatorId).add(passenger);
        notifyAll();
    }

    public synchronized Passenger pollPassenger(int elevatorId) {
        return elevatorQueues.get(elevatorId).poll();
    }

    public int getNumber() {
        return number;
    }

    public synchronized Passenger selectPassenger(int elevatorId, FairnessStrategy strategy, Elevator elevator) {
        Queue<Passenger> queue = elevatorQueues.get(elevatorId);
        // اینجا با strategy یکی را انتخاب کن
    }
}
