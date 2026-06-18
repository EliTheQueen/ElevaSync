package model.building;

import model.elevator.Elevator;
import model.fairness.FairnessStrategy;
import model.passenger.Passenger;

import java.util.*;

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
        Queue<Passenger> queue = elevatorQueues.get(elevatorId);
        if (queue == null) {
            throw new IllegalArgumentException("No queue exists for elevator " + elevatorId);
        }
        passenger.markQueueEnterTime();
        queue.add(passenger);
        System.out.println(passenger.getRole() + " joined elevator " + elevatorId + " queue on floor " + number);
        notifyAll();
    }

    public synchronized void removePassenger(int elevatorId, Passenger passenger) {
        Queue<Passenger> queue = elevatorQueues.get(elevatorId);
        if (queue != null) {
            queue.remove(passenger);
        }
    }

    public synchronized Passenger pollPassenger(int elevatorId, Elevator elevator, FairnessStrategy strategy) {
        Queue<Passenger> queue = elevatorQueues.get(elevatorId);
        if (queue == null || queue.isEmpty()) {
            return null;
        }

        List<Passenger> candidates = new ArrayList<>();
        for (Passenger passenger : queue) {
            if (elevator.canServe(passenger)) {
                candidates.add(passenger);
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }

        Passenger selected = strategy.choose(candidates, elevator);
        queue.remove(selected);
        return selected;
    }

    public int getNumber() {
        return number;
    }

    public synchronized boolean hasWaitingPassenger(int elevatorId) {
        Queue<Passenger> queue = elevatorQueues.get(elevatorId);
        return queue != null && !queue.isEmpty();
    }

}
