package parkease_backend.model;

public enum VehicleType {
    BIKE,
    CAR,
    BUS;

    public double getHourlyRate() {
        switch (this) {
            case BIKE:
                return 10.0;
            case CAR:
                return 20.0;
            case BUS:
                return 40.0;
            default:
                return 15.0;
        }
    }
}