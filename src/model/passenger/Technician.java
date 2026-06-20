package model.passenger;

import model.building.Building;
import model.elevator.Elevator;
import model.repair.RepairCenter;
import model.task.Task;

public class Technician extends Passenger {

    private static final long REPAIR_TIME = 2000;

    private final RepairCenter repairCenter;
    private volatile boolean running = true;

    public Technician(int age, double weight, Task task, Building building, RepairCenter repairCenter, RepairCenter repairCenter1) {
        super(age, weight, task, building);
        this.repairCenter = repairCenter1;
    }

    @Override
    public PassengerRole getRole() {
        return PassengerRole.TECHNICIAN;
    }

    @Override
    public void run() {
        System.out.println("Technician is ready for repair request");
        while (running) {
            try {
                Elevator elevator = repairCenter.takeBrokenElevator();

                if (elevator == null) {
                    break;
                }

                System.out.println("Technician is going to repair elevator" + elevator.getId());
                Thread.sleep(REPAIR_TIME);

                elevator.repair();
            } catch (InterruptedException e) {
                running = false;
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Technician is going to sleep");
    }

    public void shutdown() {
        running = false;
    }
}
