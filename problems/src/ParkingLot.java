import java.util.*;
class ParkingLot {
    enum Status {
        EMPTY, OCCUPIED, DELETED
    }
    static class Spot {
        String license;
        long entryTime;
        Status status;
        Spot() {
            status = Status.EMPTY;
        }
    }
    private Spot[] table;
    private int capacity = 500;
    private int size = 0;
    private int totalProbes = 0;
    private int operations = 0;
    private Map<Integer, Integer> hourCount = new HashMap<>();
    public ParkingLot() {
        table = new Spot[capacity];
        for (int i = 0; i < capacity; i++) table[i] = new Spot();
    }
    private int hash(String plate) {
        int h = 0;
        for (char c : plate.toCharArray()) h = (31 * h + c) % capacity;
        return h;
    }
    public int parkVehicle(String plate) {
        int index = hash(plate);
        int probes = 0;
        while (table[index].status == Status.OCCUPIED) {
            index = (index + 1) % capacity;
            probes++;
        }
        table[index].license = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = Status.OCCUPIED;
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        hourCount.put(hour, hourCount.getOrDefault(hour, 0) + 1);
        size++;
        totalProbes += probes;
        operations++;
        System.out.println("Assigned spot #" + index + " (" + probes + " probes)"); return index;
    }
    public void exitVehicle(String plate) {
        int index = hash(plate);
        while (table[index].status != Status.EMPTY) {
            if (table[index].status == Status.OCCUPIED && plate.equals(table[index].license)) { long duration = System.currentTimeMillis() - table[index].entryTime; double hours = duration / (1000.0 * 60 * 60);
                double fee = hours * 5.5;
                table[index].status = Status.DELETED;
                size--;
                System.out.printf("Spot #%d freed, Duration: %.2f hours, Fee: $%.2f\n", index, hours, fee);
                return;
            }
            index = (index + 1) % capacity;
        }
        System.out.println("Vehicle not found");
    }
    public int findNearestAvailable() {
        for (int i = 0; i < capacity; i++) {
            if (table[i].status != Status.OCCUPIED) return i;
        }
        return -1;
    }
    public void getStatistics() {
        double occupancy = (size * 100.0) / capacity;
        double avgProbes = operations == 0 ? 0 : (double) totalProbes / operations;
        int peakHour = -1, max = 0;
        for (int h : hourCount.keySet()) {
            if (hourCount.get(h) > max) {
                max = hourCount.get(h);
                peakHour = h;
            }
        }
        System.out.printf("Occupancy: %.2f%%\n", occupancy);
        System.out.printf("Avg Probes: %.2f\n", avgProbes);
        if (peakHour != -1) System.out.println("Peak Hour: " + peakHour + "-" + (peakHour + 1)); }
    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot();
        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");
        lot.exitVehicle("ABC-1234");
        lot.getStatistics();
    }
}
