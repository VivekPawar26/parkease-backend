package parkease_backend.service;

import parkease_backend.model.ParkingSlot;
import parkease_backend.model.Ticket;
import parkease_backend.model.Vehicle;
import parkease_backend.model.VehicleType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ParkingLot {

    private final List<ParkingSlot> slots;
    private double totalRevenue;

    public ParkingLot() {

        slots = new ArrayList<>();

        int id = 1;

        // 5 Bike slots
        for (int i = 0; i < 5; i++) {
            slots.add(new ParkingSlot(id++, VehicleType.BIKE));
        }

        // 5 Car slots
        for (int i = 0; i < 5; i++) {
            slots.add(new ParkingSlot(id++, VehicleType.CAR));
        }

        // 2 Bus slots
        for (int i = 0; i < 2; i++) {
            slots.add(new ParkingSlot(id++, VehicleType.BUS));
        }

        totalRevenue = 0.0;
    }

    // Find first available slot for vehicle type
    private Optional<ParkingSlot> findFreeSlot(VehicleType type) {

        return slots.stream()
                .filter(slot ->
                        slot.getType() == type &&
                        !slot.isOccupied())
                .findFirst();
    }

    // Park vehicle
    public ParkingSlot parkVehicle(Vehicle vehicle) {

        Optional<ParkingSlot> slotOpt =
                findFreeSlot(vehicle.getType());

        if (slotOpt.isEmpty()) {
            return null;
        }

        ParkingSlot slot = slotOpt.get();

        slot.parkVehicle(vehicle);

        return slot;
    }

    // Find vehicle by number plate
    private Optional<ParkingSlot> findSlotByPlate(String numberPlate) {

        return slots.stream()
                .filter(slot ->
                        slot.isOccupied()
                        && slot.getCurrentVehicle()
                        .getNumberPlate()
                        .equalsIgnoreCase(numberPlate))
                .findFirst();
    }

    // Exit vehicle and generate ticket
    public Ticket exitVehicle(String numberPlate) {

        Optional<ParkingSlot> slotOpt =
                findSlotByPlate(numberPlate);

        if (slotOpt.isEmpty()) {
            return null;
        }

        ParkingSlot slot = slotOpt.get();

        Vehicle vehicle = slot.getCurrentVehicle();

        Ticket ticket =
                new Ticket(vehicle, slot.getSlotId());

        double fee =
                ticket.calculateFee(LocalDateTime.now());

        totalRevenue += fee;

        slot.removeVehicle();

        return ticket;
    }

    // Get all parking slots
    public List<ParkingSlot> getSlots() {
        return slots;
    }

    // Available slots
    public long getAvailableCount(VehicleType type) {

        return slots.stream()
                .filter(slot ->
                        slot.getType() == type
                        && !slot.isOccupied())
                .count();
    }

    // Occupied slots
    public long getOccupiedCount(VehicleType type) {

        return slots.stream()
                .filter(slot ->
                        slot.getType() == type
                        && slot.isOccupied())
                .count();
    }

    // Total revenue
    public double getTotalRevenue() {
        return totalRevenue;
    }
}
