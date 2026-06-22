package model.simulation;

import model.building.Building;
import model.elevator.CargoElevator;
import model.elevator.Elevator;
import model.elevator.PublicElevator;
import model.elevator.VIPElevator;
import model.fairness.CombinedFairnessStrategy;
import model.fairness.FairnessStrategy;
import model.passenger.*;
import model.repair.RepairCenter;
import model.task.Task;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class SimulationManager {
    //??
    private static SimulationManager instance;

    private Building building;
    private RepairCenter repairCenter;
    private FairnessStrategy fairnessStrategy;

    private final List<Elevator> elevators = new ArrayList<>();
    private final List<Passenger> passengers = new ArrayList<>();
    private final List<Technician> technicians = new ArrayList<>();

    private final List<Thread> elevatorThreads = new ArrayList<>();
    private final List<Thread> passengerThreads = new ArrayList<>();
    private final List<Thread> technicianThreads = new ArrayList<>();

    private final AtomicLong totalElevatorTravelTime = new AtomicLong(0);
    private final List<UUID> completedTaskIds = Collections.synchronizedList(new ArrayList<>());

    private final Object waitingLock = new Object();
    private final double maxTotalElevatorWeight = 2000;
    private double currentTotalElevatorWeight = 0;

    private final Random random = new Random();

    private SimulationManager() {}

    public static synchronized SimulationManager getInstance() {
        if (instance == null) {
            instance = new SimulationManager();
        }
        return instance;
    }

    public void initialize(int floorCount, int elevatorCount) {

        building = new Building(floorCount, elevatorCount);
        repairCenter = new RepairCenter();
        fairnessStrategy = new CombinedFairnessStrategy();

        createElevator(elevatorCount);
        building.setElevators(elevators);

        createPassengers(20, floorCount);
        createTechnician(2);
    }

    private void createElevator(int elevatorCount) {
        for (int i = 0; i < elevatorCount; i++) {
            Elevator elevator;
            if (i % 3 == 0) {
                elevator = new PublicElevator(i, 500, building, fairnessStrategy, repairCenter);
            } else if (i % 3 == 1) {
                elevator = new VIPElevator(i, 450, building, fairnessStrategy, repairCenter);
            }  else {
                elevator = new CargoElevator(i, 900, building, fairnessStrategy, repairCenter);
            }

            elevators.add(elevator);
            System.out.println("Elevator " + i + " created");
        }
    }

    private void createPassengers(int passengerCount, int floorCount) {
        for (int i = 0; i < passengerCount; i++) {
            Task task = createRandomTask(floorCount);

            int age = random.nextInt(50) + 18;
            double weight = random.nextInt(45) + 50;

            Passenger passenger = createRandomPassenger(age, weight, task);
            passengers.add(passenger);
            System.out.println("Passenger " + i + " with role " + passenger.getRole() + " created");
        }
    }

    private Passenger createRandomPassenger(int age, double weight, Task task) {
        int type = random.nextInt(4);
        Passenger passenger;
        switch (type) {
            case 0 -> passenger = new Student(age, weight, task, building);
            case 1 -> passenger = new Professor(age, weight, task, building);
            case 2 -> passenger = new EducationalDeputy(age, weight, task, building);
            default -> passenger = new Porter(age, weight, task, building, random.nextInt(80) + 20);
        }
        return passenger;
    }

    private Task createRandomTask(int floorCount) {
        int destinationFloor = random.nextInt(floorCount - 1) + 1;
        long duration = random.nextInt(3000) + 1000;

        Task.TaskPriority[] priorities = Task.TaskPriority.values();
        Task.TaskPriority priority = priorities[random.nextInt(priorities.length)];

        return new Task(destinationFloor, priority, duration);
    }

    private void createTechnician(int technicianCount) {
        for (int i = 0; i < technicianCount; i++) {
            Task task = new Task(0, Task.TaskPriority.HIGH, 1000);
            Technician technician = new Technician(35 + i, 75, task, building, repairCenter);
            technicians.add(technician);
            System.out.println("Technician " + i + " created");
        }
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

    private void startTechnicians() {
        for (Technician technician : technicians) {
            Thread thread = new Thread(technician);
            technicianThreads.add(thread);
            thread.start();
        }
    }

    public void addTravelTime(long time) {
        totalElevatorTravelTime.addAndGet(time);
    }

    public void addCompletedTask(UUID taskId) {
        completedTaskIds.add(taskId);
    }

    public void enterElevatorWeight(double weight) throws InterruptedException {
        synchronized (waitingLock) {
            while (currentTotalElevatorWeight + weight > maxTotalElevatorWeight) {
                waitingLock.wait();
            }

            currentTotalElevatorWeight += weight;
        }
    }

    public void leaveElevatorWeight(double weight) {
        synchronized (waitingLock) {
            currentTotalElevatorWeight -= weight;

            if (currentTotalElevatorWeight < 0) {
                currentTotalElevatorWeight = 0;
            }

            waitingLock.notifyAll();
        }
    }

    public void startSimulation() {
        startElevators();
        startPassengers();
        startTechnicians();
    }

    public void shutDownSimulation() {
        for (Elevator elevator : elevators) {
            elevator.shutDown();
        }
        for (Technician technician : technicians) {
            technician.shutdown();
        }

        repairCenter.shutdown();

        interruptAllThreads();
        waitForThreadsToFinish();
        
        printReport();
    }

    private void interruptAllThreads() {
        for (Thread thread : elevatorThreads) {
            thread.interrupt();
        }
        for (Thread thread : passengerThreads) {
            thread.interrupt();
        }
        for (Thread thread : technicianThreads) {
            thread.interrupt();
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
            for (Thread thread : technicianThreads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void printReport() {
        System.out.println();
        System.out.println("========== Final Report ===========");
        System.out.println("Total Elevator Travel Time: " + totalElevatorTravelTime.get() + " ms");
        System.out.println("Completed task ids:");
        synchronized (completedTaskIds) {
            for (UUID taskId : completedTaskIds) {
                System.out.println(taskId);
            }
        }
        System.out.println("===================================");
    }
}
