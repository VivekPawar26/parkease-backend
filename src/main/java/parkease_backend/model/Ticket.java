package parkease_backend.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ticket {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Vehicle vehicle;
    private int slotId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double fee;

    public Ticket(Vehicle vehicle, int slotId) {
        this.vehicle = vehicle;
        this.slotId = slotId;
        this.entryTime = vehicle.getEntryTime();
    }

    public double calculateFee(LocalDateTime exitTime) {
        this.exitTime = exitTime;

        Duration duration = Duration.between(entryTime, exitTime);
        long minutes = duration.toMinutes();

        // Minimum billing: 1 hour
        long hours = (long) Math.ceil(minutes / 60.0);

        if (hours == 0) {
            hours = 1;
        }

        this.fee = hours * vehicle.getType().getHourlyRate();

        return this.fee;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getSlotId() {
        return slotId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public double getFee() {
        return fee;
    }

    public String getReceipt() {

        StringBuilder sb = new StringBuilder();

        sb.append("----- PARKING RECEIPT -----\n");
        sb.append("Vehicle No : ")
                .append(vehicle.getNumberPlate())
                .append("\n");

        sb.append("Type       : ")
                .append(vehicle.getType())
                .append("\n");

        sb.append("Slot       : ")
                .append(slotId)
                .append("\n");

        sb.append("Entry Time : ")
                .append(entryTime.format(FORMATTER))
                .append("\n");

        sb.append("Exit Time  : ")
                .append(exitTime.format(FORMATTER))
                .append("\n");

        sb.append("Fee        : Rs. ")
                .append(fee)
                .append("\n");

        sb.append("----------------------------");

        return sb.toString();
    }
}