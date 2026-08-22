package main;

import model.simulation.SimulationManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter floor count: ");
        int floorCount = scanner.nextInt();

        System.out.println("Enter elevator count: ");
        int elevatorCount = scanner.nextInt();

        if (floorCount < 2) {
            System.out.println("Floor count must be at least 2.");
            return;
        }

        if (elevatorCount < 3) {
            System.out.println("Elevator count must be at least 3.");
            return;
        }

        SimulationManager manager = SimulationManager.getInstance();

        manager.initialize(floorCount, elevatorCount);
        manager.startSimulation();

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        manager.shutDownSimulation();
    }
}