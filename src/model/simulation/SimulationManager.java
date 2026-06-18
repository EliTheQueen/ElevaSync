package model.simulation;

import model.building.Building;
import model.elevator.Elevator;
import model.passenger.Passenger;

import java.util.ArrayList;
import java.util.List;

public class SimulationManager {

    private static SimulationManager instance;

    private Building building;

    private final List<Elevator> elevators = new ArrayList<>();
    private final List<Passenger> passengers = new ArrayList<>();

    private final List<Thread> elevatorThreads = new ArrayList<>();
    private final List<Thread> passengerThreads = new ArrayList<>();

    private boolean running;

    private SimulationManager() {}

    public static synchronized SimulationManager getInstance() {
        if (instance == null) {
            instance = new SimulationManager();
        }
        return instance;
    }

    public void initialize(int floorCount, int elevatorCount) {

        building = new Building(floorCount, elevatorCount);
        createElevator(elevatorCount);
        createPassengers(floorCount);

        running = true;
    }

    public void startSimulation() {
        startElevators();
        startPassengers();
    }

    public void shutdownSimulation() {
        for (Elevator elevator : elevators) {
            elevator.shutDown();
        }

        running = false;

        waitForThreadsToFinish();

        printReport();
    }

    private void createElevator(int floorCount) {

    }

    private void createPassengers(int floorCount) {

    }

    private void startElevators() {
        for (Elevator elevator : elevators) {
            Thread thread = new Thread(elevator);
            elevatorThreads.add(thread);
            thread.start();
        }
    }

    private void startPassengers() {
        for (Passenger passenger : passengers) {
            Thread thread = new Thread(passenger);
            passengerThreads.add(thread);
            thread.start();
        }
    }

    private void waitForThreadsToFinish() {
        try {
            for (Thread thread : passengerThreads) {
                thread.join();
            }
            for (Thread thread : elevatorThreads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void printReport() {
        System.out.println("Simulation finished.");
    }
}
