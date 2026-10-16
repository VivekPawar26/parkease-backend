package parkease_backend.model;

import java.time.LocalDateTime;

public class Vehicle {

    private String numberPlate;
    private VehicleType type;
    private LocalDateTime entryTime;

    public Vehicle(String numberPlate, VehicleType type, LocalDateTime entryTime) {
        this.numberPlate = numberPlate;
        this.type = type;
        this.entryTime = entryTime;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public VehicleType getType() {
        return type;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    @Override
    public String toString() {
        return numberPlate + " (" + type + ")";
    }
}
