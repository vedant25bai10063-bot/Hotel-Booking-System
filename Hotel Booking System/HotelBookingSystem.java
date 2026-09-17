import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;


// ENUMS


enum RoomType { 
    SINGLE,
    DOUBLE,
    SUITE
}

enum BookingStatus {
    CONFIRMED,
    CHECKED_IN,
    CHECKED_OUT,
    CANCELLED
}

enum PaymentMethod {
    CASH,
    CARD,
    UPI,
    QR
}


// CUSTOM EXCEPTIONS


class RoomNotAvailableException extends Exception {
    public RoomNotAvailableException(String message) {
        super(message);
    }
}

class BookingNotFoundException extends Exception {
    public BookingNotFoundException(String message) {
        super(message);
    }
}


// PERSON & CUSTOMER (INHERITANCE)


abstract class Person {
    protected String id;
    protected String name;
    protected String phone;
    protected String email;

    public Person(String id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public abstract void displayDetails();
}

class Customer extends Person {

    public Customer(String customerId, String name, String phone, String email) {
        super(customerId, name, phone, email);
    }

    public String getCustomerId() {
        return id;
    }

    @Override
    public void displayDetails() {
        System.out.printf("Customer ID: %-8s | Name: %-20s | Phone: %-12s | Email: %s\n",
                id, name, phone, email);
    }

    public String toCSV() {
        return id + "," + name + "," + phone + "," + email;
    }
}


// ROOM HIERARCHY (ABSTRACTION & POLYMORPHISM)


abstract class Room {
    protected int roomNumber;
    protected RoomType type;
    protected double pricePerNight;
    protected boolean isAvailable;

    public Room(int roomNumber, RoomType type, double pricePerNight, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.isAvailable = isAvailable;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public abstract String getRoomCategory();

    public void displayRoomInfo() {
        String status = isAvailable ? "Available" : "Occupied";
        System.out.printf("Room %-4d | Type: %-8s | Price: ₹%-7.2f | Status: %-10s | Category: %s\n",
                roomNumber, type, pricePerNight, status, getRoomCategory());
    }
}

class StandardRoom extends Room {
    public StandardRoom(int roomNumber, double pricePerNight) {
        super(roomNumber, RoomType.SINGLE, pricePerNight, true);
    }

    @Override
    public String getRoomCategory() {
        return "Standard Single Bed";
    }
}

class DeluxeRoom extends Room {
    public DeluxeRoom(int roomNumber, double pricePerNight) {
        super(roomNumber, RoomType.DOUBLE, pricePerNight, true);
    }

    @Override
    public String getRoomCategory() {
        return "Deluxe Double Bed";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom(int roomNumber, double pricePerNight) {
        super(roomNumber, RoomType.SUITE, pricePerNight, true);
    }

    @Override
    public String getRoomCategory() {
        return "Executive Luxury Suite";
    }
}


// BOOKING CLASS


class Booking {
    private String bookingId;
    private String customerId;
    private int roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BookingStatus status;
    private double totalAmount;

    public Booking(String bookingId, String customerId, int roomNumber, LocalDate checkInDate, LocalDate checkOutDate, BookingStatus status, double totalAmount) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public long getNumberOfNights() {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return nights <= 0 ? 1 : nights;
    }

    public String toCSV() {
        return bookingId + "," + customerId + "," + roomNumber + "," + checkInDate + "," + checkOutDate + "," + status + "," + String.format("%.2f", totalAmount);
    }

    public void displayBookingDetails() {
        System.out.println("----------------------------------------");
        System.out.println("Booking ID     : " + bookingId);
        System.out.println("Customer ID    : " + customerId);
        System.out.println("Room Number    : " + roomNumber);
        System.out.println("Check-In Date  : " + checkInDate);
        System.out.println("Check-Out Date : " + checkOutDate);
        System.out.println("Nights         : " + getNumberOfNights());
        System.out.println("Status         : " + status);
        System.out.printf("Total Amount   : ₹%.2f\n", totalAmount);
        System.out.println("----------------------------------------");
    }
}


// PAYMENT CLASS


class Payment {
    private String paymentId;
    private String bookingId;
    private double amount;
    private PaymentMethod paymentMethod;

    public Payment(String paymentId, String bookingId, double amount, PaymentMethod paymentMethod) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String toCSV() {
        return paymentId + "," + bookingId + "," + String.format("%.2f", amount) + "," + paymentMethod;
    }

    public void displayPaymentDetails() {
        System.out.printf("Payment ID: %-8s | Booking ID: %-8s | Amount: ₹%-8.2f | Method: %s\n",
                paymentId, bookingId, amount, paymentMethod);
    }
}


// MULTITHREADING FOR HOTEL REPORT


class ReportThread extends Thread {
    private ArrayList<Room> rooms;
    private ArrayList<Booking> bookings;
    private ArrayList<Payment> payments;

    public ReportThread(ArrayList<Room> rooms, ArrayList<Booking> bookings, ArrayList<Payment> payments) {
        this.rooms = rooms;
        this.bookings = bookings;
        this.payments = payments;
    }

    @Override
    public void run() {
        System.out.println("\nGenerating hotel report...");
        try {
            // Simulate brief calculation delay for multithreading output
            Thread.sleep(300);
        } catch (InterruptedException e) {
            System.out.println("Report generation interrupted.");
        }

        int totalRooms = rooms.size();
        int availableRooms = 0;
        int occupiedRooms = 0;

        for (Room r : rooms) {
            if (r.isAvailable()) {
                availableRooms++;
            } else {
                occupiedRooms++;
            }
        }

        int totalBookings = bookings.size();
        int cancelledBookings = 0;
        int completedBookings = 0;
        int activeBookings = 0;

        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.CANCELLED) {
                cancelledBookings++;
            } else if (b.getStatus() == BookingStatus.CHECKED_OUT) {
                completedBookings++;
            } else {
                activeBookings++;
            }
        }

        double totalRevenue = 0.0;
        for (Payment p : payments) {
            totalRevenue += p.getAmount();
        }

        System.out.println("========================================");
        System.out.println("             HOTEL REPORT               ");
        System.out.println("========================================");
        System.out.println("Total Rooms        : " + totalRooms);
        System.out.println("Available Rooms    : " + availableRooms);
        System.out.println("Occupied Rooms     : " + occupiedRooms);
        System.out.println("----------------------------------------");
        System.out.println("Total Bookings     : " + totalBookings);
        System.out.println("Active Bookings    : " + activeBookings);
        System.out.println("Completed Bookings : " + completedBookings);
        System.out.println("Cancelled Bookings : " + cancelledBookings);
        System.out.println("----------------------------------------");
        System.out.printf("Total Payments     : %d\n", payments.size());
        System.out.printf("Total Revenue      : ₹%.2f\n", totalRevenue);
        System.out.println("========================================\n");
    }
}


// MAIN HOTEL BOOKING SYSTEM


public class HotelBookingSystem {

    private static final String DATA_DIR = "data";
    private static final String CUSTOMERS_FILE = "data/customers.csv";
    private static final String BOOKINGS_FILE = "data/bookings.csv";
    private static final String PAYMENTS_FILE = "data/payments.csv";

    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Booking> bookings = new ArrayList<>();
    private ArrayList<Payment> payments = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public HotelBookingSystem() {
        initializeRooms();
        ensureDataDirectoryExists();
        loadCustomers();
        loadBookings();
        loadPayments();
        syncRoomAvailability();
    }

    // Pre-populate around 10 rooms
    private void initializeRooms() {
        rooms.add(new StandardRoom(101, 1500.00));
        rooms.add(new StandardRoom(102, 1500.00));
        rooms.add(new StandardRoom(103, 1500.00));
        rooms.add(new DeluxeRoom(201, 2500.00));
        rooms.add(new DeluxeRoom(202, 2500.00));
        rooms.add(new DeluxeRoom(203, 2500.00));
        rooms.add(new DeluxeRoom(204, 2500.00));
        rooms.add(new SuiteRoom(301, 4000.00));
        rooms.add(new SuiteRoom(302, 4000.00));
        rooms.add(new SuiteRoom(303, 4000.00));
    }

    private void ensureDataDirectoryExists() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // Sync room availability with active CHECKED_IN bookings
    private void syncRoomAvailability() {
        for (Room room : rooms) {
            room.setAvailable(true);
        }

        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.CHECKED_IN) {
                Room r = findRoomByNumber(b.getRoomNumber());
                if (r != null) {
                    r.setAvailable(false);
                }
            }
        }
    }

    // Load customers from CSV
    private void loadCustomers() {
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    customers.add(new Customer(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading customers.csv: " + e.getMessage());
        }
    }

    // Save all customers to CSV
    private void saveCustomers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMERS_FILE))) {
            writer.write("customerId,name,phone,email");
            writer.newLine();
            for (Customer c : customers) {
                writer.write(c.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving customers.csv: " + e.getMessage());
        }
    }

    // Load bookings from CSV
    private void loadBookings() {
        File file = new File(BOOKINGS_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String bId = parts[0].trim();
                    String cId = parts[1].trim();
                    int roomNum = Integer.parseInt(parts[2].trim());
                    LocalDate inDate = LocalDate.parse(parts[3].trim());
                    LocalDate outDate = LocalDate.parse(parts[4].trim());
                    BookingStatus status = BookingStatus.valueOf(parts[5].trim());
                    double total = Double.parseDouble(parts[6].trim());

                    bookings.add(new Booking(bId, cId, roomNum, inDate, outDate, status, total));
                }
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Error reading bookings.csv: " + e.getMessage());
        }
    }

    // Save all bookings to CSV
    private void saveBookings() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKINGS_FILE))) {
            writer.write("bookingId,customerId,roomNumber,checkInDate,checkOutDate,status,totalAmount");
            writer.newLine();
            for (Booking b : bookings) {
                writer.write(b.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings.csv: " + e.getMessage());
        }
    }

    // Load payments from CSV
    private void loadPayments() {
        File file = new File(PAYMENTS_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String pId = parts[0].trim();
                    String bId = parts[1].trim();
                    double amt = Double.parseDouble(parts[2].trim());
                    PaymentMethod method = PaymentMethod.valueOf(parts[3].trim());

                    payments.add(new Payment(pId, bId, amt, method));
                }
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Error reading payments.csv: " + e.getMessage());
        }
    }

    // Save all payments to CSV
    private void savePayments() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAYMENTS_FILE))) {
            writer.write("paymentId,bookingId,amount,paymentMethod");
            writer.newLine();
            for (Payment p : payments) {
                writer.write(p.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving payments.csv: " + e.getMessage());
        }
    }

    // Helper: Find room by number
    private Room findRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    // Helper: Find customer by ID
    private Customer findCustomerById(String customerId) {
        for (Customer c : customers) {
            if (c.getCustomerId().equalsIgnoreCase(customerId)) {
                return c;
            }
        }
        return null;
    }

    // Helper: Find booking by ID
    private Booking findBookingById(String bookingId) {
        for (Booking b : bookings) {
            if (b.getBookingId().equalsIgnoreCase(bookingId)) {
                return b;
            }
        }
        return null;
    }

    // Helper: Check if room has an overlapping CONFIRMED or CHECKED_IN booking
    private boolean isRoomBookedForDates(int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        for (Booking b : bookings) {
            if (b.getRoomNumber() == roomNumber &&
               (b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.CHECKED_IN)) {
                if (checkIn.isBefore(b.getCheckOutDate()) && checkOut.isAfter(b.getCheckInDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    // Auto-generate customer ID
    private String generateNextCustomerId() {
        int max = 1000;
        for (Customer c : customers) {
            if (c.getCustomerId().startsWith("C")) {
                try {
                    int num = Integer.parseInt(c.getCustomerId().substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return "C" + (max + 1);
    }

    // Auto-generate booking ID
    private String generateNextBookingId() {
        int max = 1000;
        for (Booking b : bookings) {
            if (b.getBookingId().startsWith("B")) {
                try {
                    int num = Integer.parseInt(b.getBookingId().substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return "B" + (max + 1);
    }

    // Auto-generate payment ID
    private String generateNextPaymentId() {
        int max = 1000;
        for (Payment p : payments) {
            if (p.getPaymentId().startsWith("P")) {
                try {
                    int num = Integer.parseInt(p.getPaymentId().substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return "P" + (max + 1);
    }

    // 1. View All Rooms
    public void viewAllRooms() {
        System.out.println("\n========================================");
        System.out.println("             ROOM LISTING               ");
        System.out.println("========================================");
        for (Room r : rooms) {
            r.displayRoomInfo();
        }
        System.out.println("========================================\n");
    }

    // 2. Search Room (Method Overloading)
    public void searchRooms(RoomType type) {
        System.out.println("\n--- Available Rooms of Type: " + type + " ---");
        boolean found = false;
        for (Room r : rooms) {
            if (r.getType() == type && r.isAvailable()) {
                r.displayRoomInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No available rooms found for type: " + type);
        }
    }

    public void searchRooms(double budget) {
        System.out.println("\n--- Available Rooms Within Budget: ₹" + budget + " ---");
        boolean found = false;
        for (Room r : rooms) {
            if (r.getPricePerNight() <= budget && r.isAvailable()) {
                r.displayRoomInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No available rooms found within budget of ₹" + budget);
        }
    }

    private void searchRoomMenu() {
        System.out.println("\n--- Search Room ---");
        System.out.println("1. Search by Room Type");
        System.out.println("2. Search by Maximum Budget");
        System.out.print("Enter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 1) {
                System.out.println("Select Type: 1. SINGLE  2. DOUBLE  3. SUITE");
                System.out.print("Enter choice: ");
                int typeChoice = Integer.parseInt(scanner.nextLine().trim());
                if (typeChoice == 1) searchRooms(RoomType.SINGLE);
                else if (typeChoice == 2) searchRooms(RoomType.DOUBLE);
                else if (typeChoice == 3) searchRooms(RoomType.SUITE);
                else System.out.println("Invalid room type choice.");
            } else if (choice == 2) {
                System.out.print("Enter maximum budget per night (₹): ");
                double budget = Double.parseDouble(scanner.nextLine().trim());
                searchRooms(budget);
            } else {
                System.out.println("Invalid search choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input number format.");
        }
    }

    // 3. Register Customer
    public Customer registerCustomer() {
        System.out.println("\n--- Customer Registration ---");
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            System.out.print("Name cannot be empty. Re-enter name: ");
            name = scanner.nextLine().trim();
        }

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Enter email address: ");
        String email = scanner.nextLine().trim();

        String cId = generateNextCustomerId();
        Customer newCustomer = new Customer(cId, name, phone, email);
        customers.add(newCustomer);
        saveCustomers();

        System.out.println("Customer registered successfully!");
        System.out.println("Assigned Customer ID: " + cId);
        return newCustomer;
    }

    // 4. Book Room
    public void bookRoom() {
        System.out.println("\n--- Book a Room ---");
        viewAllRooms();

        try {
            System.out.print("Enter room number to book: ");
            int roomNum = Integer.parseInt(scanner.nextLine().trim());

            Room room = findRoomByNumber(roomNum);
            if (room == null) {
                System.out.println("Error: Room " + roomNum + " does not exist.");
                return;
            }

            if (!room.isAvailable()) {
                throw new RoomNotAvailableException("Room " + roomNum + " is currently not available.");
            }

            System.out.print("Do you have a Customer ID? (y/n): ");
            String hasId = scanner.nextLine().trim();

            Customer customer = null;
            if (hasId.equalsIgnoreCase("y")) {
                System.out.print("Enter Customer ID: ");
                String cId = scanner.nextLine().trim();
                customer = findCustomerById(cId);
                if (customer == null) {
                    System.out.println("Customer ID not found. Let's register a new customer.");
                    customer = registerCustomer();
                }
            } else {
                customer = registerCustomer();
            }

            System.out.print("Enter check-in date (YYYY-MM-DD): ");
            LocalDate checkIn = LocalDate.parse(scanner.nextLine().trim());

            System.out.print("Enter check-out date (YYYY-MM-DD): ");
            LocalDate checkOut = LocalDate.parse(scanner.nextLine().trim());

            if (!checkOut.isAfter(checkIn)) {
                System.out.println("Error: Check-out date must be after check-in date.");
                return;
            }

            if (isRoomBookedForDates(roomNum, checkIn, checkOut)) {
                System.out.println("Room " + roomNum + " is already booked for the selected dates.");
                return;
            }

            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (nights <= 0) nights = 1;

            double roomCharges = nights * room.getPricePerNight();
            double gst = roomCharges * 0.05; // 5% GST
            double totalAmount = roomCharges + gst;

            String bId = generateNextBookingId();
            Booking booking = new Booking(bId, customer.getCustomerId(), room.getRoomNumber(), checkIn, checkOut, BookingStatus.CONFIRMED, totalAmount);
            bookings.add(booking);
            saveBookings();

            System.out.println("\n========================================");
            System.out.println("          BOOKING CONFIRMED!            ");
            System.out.println("========================================");
            System.out.println("Booking ID     : " + bId);
            System.out.println("Customer Name  : " + customer.getName());
            System.out.println("Room Number    : " + room.getRoomNumber());
            System.out.println("Room Type      : " + room.getType());
            System.out.println("Nights         : " + nights);
            System.out.printf("Room Charges   : ₹%.2f\n", roomCharges);
            System.out.printf("GST (5%%)       : ₹%.2f\n", gst);
            System.out.printf("Total Amount   : ₹%.2f\n", totalAmount);
            System.out.println("========================================\n");

        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid room number.");
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
        } catch (RoomNotAvailableException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }

    // 5. View Booking
    public void viewBooking() {
        System.out.println("\n--- View Booking ---");
        System.out.print("Enter Booking ID (or press Enter to view all): ");
        String bId = scanner.nextLine().trim();

        if (bId.isEmpty()) {
            if (bookings.isEmpty()) {
                System.out.println("No bookings found in system.");
                return;
            }
            System.out.println("\nListing all bookings (" + bookings.size() + "):");
            for (Booking b : bookings) {
                b.displayBookingDetails();
            }
        } else {
            try {
                Booking b = findBookingById(bId);
                if (b == null) {
                    throw new BookingNotFoundException("Booking ID " + bId + " not found.");
                }
                b.displayBookingDetails();

                Customer c = findCustomerById(b.getCustomerId());
                if (c != null) {
                    System.out.println("Customer Details:");
                    c.displayDetails();
                }
            } catch (BookingNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // 6. Check In
    public void checkIn() {
        System.out.println("\n--- Guest Check-In ---");
        System.out.print("Enter Booking ID: ");
        String bId = scanner.nextLine().trim();

        try {
            Booking b = findBookingById(bId);
            if (b == null) {
                throw new BookingNotFoundException("Booking ID " + bId + " not found.");
            }

            if (b.getStatus() == BookingStatus.CANCELLED) {
                System.out.println("Cannot check in: Booking has been cancelled.");
                return;
            }

            if (b.getStatus() == BookingStatus.CHECKED_IN) {
                System.out.println("Guest is already checked in for this booking.");
                return;
            }

            if (b.getStatus() == BookingStatus.CHECKED_OUT) {
                System.out.println("Booking is already completed and checked out.");
                return;
            }

            b.setStatus(BookingStatus.CHECKED_IN);
            saveBookings();

            Room room = findRoomByNumber(b.getRoomNumber());
            if (room != null) {
                room.setAvailable(false);
            }

            System.out.println("Check-In Successful for Booking ID " + bId + "!");
            System.out.println("Room " + b.getRoomNumber() + " is now marked as Occupied.");

        } catch (BookingNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 7. Check Out
    public void checkOut() {
        System.out.println("\n--- Guest Check-Out ---");
        System.out.print("Enter Booking ID: ");
        String bId = scanner.nextLine().trim();

        try {
            Booking b = findBookingById(bId);
            if (b == null) {
                throw new BookingNotFoundException("Booking ID " + bId + " not found.");
            }

            if (b.getStatus() == BookingStatus.CONFIRMED) {
                System.out.println("Customer must check in before checkout.");
                return;
            }

            if (b.getStatus() == BookingStatus.CANCELLED) {
                System.out.println("Cannot check out: Booking was cancelled.");
                return;
            }

            if (b.getStatus() == BookingStatus.CHECKED_OUT) {
                System.out.println("Booking has already been checked out.");
                return;
            }

            b.setStatus(BookingStatus.CHECKED_OUT);
            saveBookings();

            Room room = findRoomByNumber(b.getRoomNumber());
            if (room != null) {
                room.setAvailable(true);
            }

            long nights = b.getNumberOfNights();
            double roomRate = (room != null) ? room.getPricePerNight() : (b.getTotalAmount() / 1.05 / nights);
            double roomCharges = nights * roomRate;
            double gst = roomCharges * 0.05;

            System.out.println("\n========================================");
            System.out.println("              FINAL BILL                ");
            System.out.println("========================================");
            System.out.println("Booking ID     : " + b.getBookingId());
            System.out.println("Customer ID    : " + b.getCustomerId());
            System.out.println("Room Number    : " + b.getRoomNumber());
            System.out.println("Nights Stayed  : " + nights);
            System.out.printf("Room Charges   : ₹%.2f\n", roomCharges);
            System.out.printf("GST (5%%)       : ₹%.2f\n", gst);
            System.out.printf("Total Payable  : ₹%.2f\n", b.getTotalAmount());
            System.out.println("========================================");
            System.out.println("Check-Out Completed! Room " + b.getRoomNumber() + " is now Available again.\n");
            generatePaymentQRCode(b);

        } catch (BookingNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 8. Cancel Booking
    public void cancelBooking() {
        System.out.println("\n--- Cancel Booking ---");
        System.out.print("Enter Booking ID to cancel: ");
        String bId = scanner.nextLine().trim();

        try {
            Booking b = findBookingById(bId);
            if (b == null) {
                throw new BookingNotFoundException("Booking ID " + bId + " not found.");
            }

            if (b.getStatus() == BookingStatus.CANCELLED) {
                System.out.println("Booking " + bId + " is already cancelled.");
                return;
            }

            if (b.getStatus() == BookingStatus.CHECKED_OUT) {
                System.out.println("Cannot cancel a completed booking that has checked out.");
                return;
            }

            b.setStatus(BookingStatus.CANCELLED);
            saveBookings();

            Room room = findRoomByNumber(b.getRoomNumber());
            if (room != null) {
                room.setAvailable(true);
            }

            System.out.println("Booking " + bId + " cancelled successfully.");
            System.out.println("Room " + b.getRoomNumber() + " is now Available.");
            System.out.println("Note: Cancellation record is retained in CSV storage.");

        } catch (BookingNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Helper: Generate and display dynamic Payment QR Code
    public void generatePaymentQRCode(Booking booking) {
        String qrContent = "Hotel Booking Payment\nBooking ID: " + booking.getBookingId() +
                           "\nTotal Amount: ₹" + String.format("%.2f", booking.getTotalAmount());
        System.out.println("----------------------------------------");
        System.out.println("          PAYMENT QR CODE               ");
        System.out.println("----------------------------------------");
        System.out.println("Booking ID   : " + booking.getBookingId());
        System.out.printf("Total Amount : ₹%.2f\n", booking.getTotalAmount());

        try {
            String encodedData = URLEncoder.encode(qrContent, StandardCharsets.UTF_8.name());
            String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=" + encodedData;
            System.out.println("QR Link      : " + qrUrl);

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(qrUrl));
                System.out.println("Opening payment QR code in your default web browser...");
            } else {
                System.out.println("Copy and open the QR Link above in your web browser.");
            }
        } catch (Exception e) {
            System.out.println("Could not launch browser automatically: " + e.getMessage());
        }
        System.out.println("----------------------------------------\n");
    }

    // 9. Make Payment
    public void makePayment() {
        System.out.println("\n--- Make Payment ---");
        System.out.print("Enter Booking ID: ");
        String bId = scanner.nextLine().trim();

        try {
            Booking b = findBookingById(bId);
            if (b == null) {
                throw new BookingNotFoundException("Booking ID " + bId + " not found.");
            }

            System.out.printf("Booking Total Amount: ₹%.2f\n", b.getTotalAmount());
            System.out.println("Select Payment Method:");
            System.out.println("1. CASH");
            System.out.println("2. CARD");
            System.out.println("3. UPI");
            System.out.println("4. QR (Generate Payment QR Code)");
            System.out.print("Enter choice (1-4): ");

            int methodChoice = Integer.parseInt(scanner.nextLine().trim());
            PaymentMethod method;
            if (methodChoice == 1) {
                method = PaymentMethod.CASH;
            } else if (methodChoice == 2) {
                method = PaymentMethod.CARD;
            } else if (methodChoice == 3) {
                method = PaymentMethod.UPI;
            } else if (methodChoice == 4) {
                method = PaymentMethod.QR;
                generatePaymentQRCode(b);
                System.out.println("QR code generated. Please complete payment using the QR code.");
            } else {
                System.out.println("Invalid payment method selected. Defaulting to UPI.");
                method = PaymentMethod.UPI;
            }

            String pId = generateNextPaymentId();
            Payment payment = new Payment(pId, b.getBookingId(), b.getTotalAmount(), method);
            payments.add(payment);
            savePayments();

            System.out.println("\nPayment Successful!");
            payment.displayPaymentDetails();

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid numeric input.");
        } catch (BookingNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 10. Hotel Report (Uses Multithreading via ReportThread)
    public void showHotelReport() {
        ReportThread reportThread = new ReportThread(rooms, bookings, payments);
        reportThread.start();
        try {
            reportThread.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }
    }
    // 12. Print Final Receipt (Read-only)
    public void printFinalReceipt() {
        System.out.println("\n--- Print Final Receipt ---");
        System.out.print("Enter Booking ID: ");
        String bId = scanner.nextLine().trim();
        Booking b = findBookingById(bId);
        if (b == null) {
            System.out.println("Booking not found.");
            return;
        }
        System.out.println("========================================");
        System.out.println("              FINAL RECEIPT             ");
        System.out.println("========================================");
        System.out.println("Booking ID   : " + b.getBookingId());
        System.out.println("Customer ID  : " + b.getCustomerId());
        System.out.println("Room Number  : " + b.getRoomNumber());
        System.out.println("Check-In     : " + b.getCheckInDate());
        System.out.println("Check-Out    : " + b.getCheckOutDate());
        System.out.println("Nights       : " + b.getNumberOfNights());
        System.out.printf("Total Amount : ₹%.2f\n", b.getTotalAmount());
        // Find payment record
        Payment payment = null;
        for (Payment p : payments) {
            if (p.getBookingId().equalsIgnoreCase(b.getBookingId())) {
                payment = p;
                break;
            }
        }
        if (payment != null) {
            System.out.println("Payment ID   : " + payment.getPaymentId());
            System.out.println("Payment Method: " + payment.getPaymentMethod());
        } else {
            System.out.println("Payment Status: NOT PAID");
        }
        System.out.println("========================================\n");
    }

    // Main Menu Loop
    public void run() {
        boolean exit = false;

        while (!exit) {
            System.out.println("========================================");
            System.out.println("          HOTEL BOOKING SYSTEM          ");
            System.out.println("========================================");
            System.out.println("1. View All Rooms");
            System.out.println("2. Search Room");
            System.out.println("3. Register Customer");
            System.out.println("4. Book Room");
            System.out.println("5. View Booking");
            System.out.println("6. Check In");
            System.out.println("7. Check Out");
            System.out.println("8. Cancel Booking");
            System.out.println("9. Make Payment");
            System.out.println("10. Hotel Report");
            System.out.println("11. Print Final Receipt");
            System.out.println("12. Exit");
            System.out.println("========================================");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            try {
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        viewAllRooms();
                        break;
                    case 2:
                        searchRoomMenu();
                        break;
                    case 3:
                        registerCustomer();
                        break;
                    case 4:
                        bookRoom();
                        break;
                    case 5:
                        viewBooking();
                        break;
                    case 6:
                        checkIn();
                        break;
                    case 7:
                        checkOut();
                        break;
                    case 8:
                        cancelBooking();
                        break;
                    case 9:
                        makePayment();
                        break;
                    case 10:
                        showHotelReport();
                        break;
                    case 11:
                        printFinalReceipt();
                        break;
                    case 12:
                        exit = true;
                        System.out.println("\nThank you for using Hotel Booking System. Goodbye!");
                        break;
                    default:
                        System.out.println("\nInvalid choice! Please select an option from 1 to 12.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input! Please enter a valid number between 1 and 11.");
            }

            if (!exit) {
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    public static void main(String[] args) {
        HotelBookingSystem system = new HotelBookingSystem();
        system.run();
}
}
