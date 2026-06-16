package model.building;

public class Floor {
    private int number;
    private final Map<Integer, PassengerQueue> elevatorQueues;

    public  Floor(int number) {
        this.number = number;
    }

    public int getNumber() { return number; }
}
