package model.building;

import model.passenger.Passenger;

import java.util.ArrayList;
import java.util.List;

public class PassengerQueue {

    private final List<Passenger> pasengers = new ArrayList<>();

    public synchronized void  addPassenger(Passenger passenger) {
        pasengers.add(passenger);
    }

//    public synchronized void pickupPassenger(FairnessStrategy strategy) {
//    }

    public synchronized void notifyElevatorArrived() {
        notifyAll();
    }
}
