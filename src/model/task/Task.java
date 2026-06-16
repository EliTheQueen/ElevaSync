package model.task;

import java.util.UUID;

public class Task {

    private final UUID id;
    private final int destinationFloor;
    private final TaskPriority priority;
    private final long duration;

    public enum TaskPriority {
        HIGH,
        MEDIUM,
        LOW
    }

    public Task(int destinationFloor, TaskPriority priority, long duration) {
        this.id = UUID.randomUUID();
        this.destinationFloor = destinationFloor;
        this.priority = priority;
        this.duration = duration;
    }

    public UUID getId() { return id; }
    public int getDestinationFloor() { return destinationFloor; }
    public TaskPriority getPriority() { return priority; }
    public long getDuration() { return duration; }

}
