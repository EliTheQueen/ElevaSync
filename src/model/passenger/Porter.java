package model.passenger;

import model.building.Building;
import model.task.Task;

public class Porter extends Passenger {

    private final double cargoWeight;

    public Porter(int age, double weight, Task task, Building building, double cargoWeight) {
        super(age, weight, task, building);
        this.cargoWeight = cargoWeight;
    }

    @Override
    public PassengerRole getRole() {
        return PassengerRole.PORTER;
    }

    @Override
    public double getTotalWeight() {
        return weight + cargoWeight;
    }

    public double getCargoWeight() {
        return cargoWeight;
    }
}
