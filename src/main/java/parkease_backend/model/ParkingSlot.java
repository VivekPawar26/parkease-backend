package parkease_backend.model;

public class ParkingSlot {

    private int slotId;
    private VehicleType type;
    private boolean occupied;
    private Vehicle currentVehicle;

    public ParkingSlot(int slotId, VehicleType type) {
        this.slotId = slotId;
        this.type = type;
        this.occupied = false;
        this.currentVehicle = null;
    }

    public int getSlotId() {
        return slotId;
    }

    public VehicleType getType() {
        return type;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Vehicle getCurrentVehicle() {
        return currentVehicle;
    }

    public void parkVehicle(Vehicle vehicle) {
        this.currentVehicle = vehicle;
        this.occupied = true;
    }

    public void removeVehicle() {
        this.currentVehicle = null;
        this.occupied = false;
    }

    @Override
    public String toString() {
        String status = occupied
                ? "OCCUPIED by " + currentVehicle
                : "FREE";

        return "Slot #" + slotId + " [" + type + "] - " + status;
    }
}