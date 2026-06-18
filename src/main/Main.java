package main;


import model.simulation.SimulationManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter floor count: ");
        int floorCount = scanner.nextInt();

        System.out.print("Enter elevator count: ");
        int elevatorCount = scanner.nextInt();

        SimulationManager manager = SimulationManager.getInstance();

        manager.initialize(floorCount, elevatorCount);
        manager.startSimulation();

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        manager.shutdownSimulation();
    }
}