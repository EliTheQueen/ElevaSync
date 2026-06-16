package main;

import model.elevator.*;
import model.passenger.*;
import model.task.*;

public class Main {
    public static void main(String[] args) {

        Elevator elevator =
                new PublicElevator(1, 500);

        Thread elevatorThread =
                new Thread(elevator);

        elevatorThread.start();

        for (int i = 0; i < 3; i++) {

            Task task =
                    new Task(
                            2 + i,
                            Task.TaskPriority.HIGH,
                            2000
                    );

            Passenger passenger =
                    new Student(
                            18 + i,
                            60 + i,
                            task
                    );

            Thread passengerThread =
                    new Thread(passenger);

            passengerThread.start();
        }

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        elevator.shutDown();
    }
}
