package parkease_backend.controller;

import org.springframework.web.bind.annotation.*;

import parkease_backend.model.ParkingSlot;
import parkease_backend.model.Ticket;
import parkease_backend.model.Vehicle;
import parkease_backend.model.VehicleType;
import parkease_backend.service.ParkingLot;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parking")
@CrossOrigin(origins = "*")
public class ParkingController {

    private final ParkingLot parkingLot;

    public ParkingController(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    // Test backend
    @GetMapping("/test")
    public String test() {
        return "Smart Parking Backend is running!";
    }

    // Get all parking slots
    @GetMapping("/slots")
    public List<ParkingSlot> getAllSlots() {
        return parkingLot.getSlots();
    }

    // Get parking summary
@GetMapping("/status")
public Map<String, Object> getStatus() {

    Map<String, Object> status = new HashMap<>();

    long totalSlots = 0;
    long availableSlots = 0;
    long occupiedSlots = 0;

    for (VehicleType type : VehicleType.values()) {

        long available = parkingLot.getAvailableCount(type);
        long occupied = parkingLot.getOccupiedCount(type);
        long total = available + occupied;

        Map<String, Long> typeStatus = new HashMap<>();
        typeStatus.put("total", total);
        typeStatus.put("available", available);
        typeStatus.put("occupied", occupied);

        status.put(type.toString().toLowerCase(), typeStatus);

        totalSlots += total;
        availableSlots += available;
        occupiedSlots += occupied;
    }

    status.put("totalSlots", totalSlots);
    status.put("availableSlots", availableSlots);
    status.put("occupiedSlots", occupiedSlots);
    status.put("totalRevenue", parkingLot.getTotalRevenue());

    return status;
}
    // Park vehicle
    @PostMapping("/park")
    public Map<String, Object> parkVehicle(
            @RequestBody ParkRequest request) {

        Vehicle vehicle = new Vehicle(
                request.numberPlate,
                VehicleType.valueOf(
                        request.type.toUpperCase()
                ),
                LocalDateTime.now()
        );

        ParkingSlot slot =
                parkingLot.parkVehicle(vehicle);

        Map<String, Object> response = new HashMap<>();

        if (slot == null) {

            response.put("success", false);
            response.put(
                    "message",
                    "No free " + request.type + " slot available."
            );

        } else {

            response.put("success", true);
            response.put(
                    "message",
                    "Vehicle parked successfully."
            );
            response.put(
                    "slotId",
                    slot.getSlotId()
            );
            response.put(
                    "vehicle",
                    vehicle
            );
        }

        return response;
    }

    // Exit vehicle
    @PostMapping("/exit")
    public Map<String, Object> exitVehicle(
            @RequestBody ExitRequest request) {

        Ticket ticket =
                parkingLot.exitVehicle(request.numberPlate);

        Map<String, Object> response =
                new HashMap<>();

        if (ticket == null) {

            response.put("success", false);
            response.put(
                    "message",
                    "Vehicle not found."
            );

        } else {

    response.put("success", true);
    response.put("message", "Vehicle exited successfully.");
    response.put("vehicleNumber", ticket.getVehicle().getNumberPlate());
    response.put("vehicleType", ticket.getVehicle().getType().toString());
    response.put("slotId", ticket.getSlotId());
    response.put("entryTime", ticket.getEntryTime().toString());
    response.put("exitTime", ticket.getExitTime().toString());
    response.put("fee", ticket.getFee());
    response.put("receipt", ticket.getReceipt());
}

        return response;
    }

    // Get total revenue
    @GetMapping("/revenue")
    public Map<String, Object> getRevenue() {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "totalRevenue",
                parkingLot.getTotalRevenue()
        );

        return response;
    }

    // Request for parking vehicle
    public static class ParkRequest {

        public String numberPlate;
        public String type;
    }

    // Request for exiting vehicle
    public static class ExitRequest {

        public String numberPlate;
    }
}
