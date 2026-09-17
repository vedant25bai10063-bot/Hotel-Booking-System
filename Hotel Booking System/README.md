# Hotel Booking System

A console-based Hotel Booking System built in Java. This project was made as part of the VITyarthi Build Your Own Project assignment.



## About the Project

This project lets hotel staff manage rooms, customers, bookings, check-ins, check-outs, payments, and receipts — all from the terminal/console. No database or web interface is used. All data is saved in CSV files.



## Project Structure

```
HotelBookingSystem/
├── HotelBookingSystem.java   ← All Java code is here
├── README.md
├── statement.md
└── data/
    ├── customers.csv         ← Customer records
    ├── bookings.csv          ← Booking records
    └── payments.csv          ← Payment records
```



## How to Run

**Step 1 – Open the command Prompt**
```
Press Windows + R, then type cmd and run
Direct to the folder where the file is saved using the cd command 
```

**Step 2 – Compile the code**
```
javac HotelBookingSystem.java
```

**Step 3 – Run the program**
```
java HotelBookingSystem
Then use...
```





## Main Menu Options

```
1.  View All Rooms
2.  Search Room
3.  Register Customer
4.  Book Room
5.  View Booking
6.  Check In
7.  Check Out
8.  Cancel Booking
9.  Make Payment
10. Hotel Report
11. Print Final Receipt
12. Exit
```



## Room Types and Prices

| Room Type | Price per Night |
|-----------|----------------|
| Single    | ₹1,500         |
| Double    | ₹2,500         |
| Suite     | ₹4,000         |

> All prices include 5% GST calculated at the time of booking.



## Payment Methods

- Cash
- Card
- UPI
- QR Code (opens QR in browser)



## Java Concepts Used

- Classes and Objects
- Inheritance (Person → Customer, Room → StandardRoom / DeluxeRoom / SuiteRoom)
- Abstraction (abstract classes: Person, Room)
- Polymorphism and Method Overriding
- Method Overloading (searchRooms by type and by budget)
- Encapsulation (private fields with getters/setters)
- Enums (RoomType, BookingStatus, PaymentMethod)
- Custom Exceptions (RoomNotAvailableException, BookingNotFoundException)
- ArrayList for storing data in memory
- File I/O (reading and writing CSV files)
- LocalDate and ChronoUnit for date calculations
- Multithreading (ReportThread extends Thread)



## Technologies Used

- Java (Standard Library only)
- CSV files for data storage
- No external libraries, frameworks, or database



## Author

Made by Vedant Tomar  
Reg No.- 25BAI10063

