package model.repair;

import model.elevator.Elevator;

import java.util.LinkedList;
import java.util.Queue;

public class RepairCenter {

    private final Queue<Elevator> brokenElevators = new LinkedList<>();
    private boolean running = true;

    public synchronized void reportBrokenElevator(Elevator elevator) {
        brokenElevators.add(elevator);
        System.out.println("Repair center received broken elevator " + elevator.getId());
        notifyAll();
    }

    public synchronized Elevator takeBrokenElevator() throws InterruptedException {
        while (running && brokenElevators.isEmpty()) {
            wait();
        }
        if (!running) {
            return null;
        }

        return brokenElevators.poll();
    }

    public synchronized void shutdown() {
        running = false;
        notifyAll();
    }
}
