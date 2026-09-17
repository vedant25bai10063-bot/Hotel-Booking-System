# Problem Statement

## Project Title
Hotel Booking System

## Course / Platform
VITyarthi – Build Your Own Project

---

## Problem

Managing hotel bookings manually is slow and error-prone. Staff have to remember which rooms are available, keep track of customers, handle check-ins and check-outs, and manage payments — all at the same time. There is no simple system for small hotels that does not require a complicated setup like a web server or database.

---

## Solution

I built a console-based Hotel Booking System in Java that helps hotel staff do all of this easily from the terminal. The system handles:

- Viewing available rooms
- Searching rooms by type or budget
- Registering new customers
- Booking rooms with date selection
- Preventing double-bookings on the same dates
- Checking in and checking out guests
- Processing payments (Cash, Card, UPI, QR Code)
- Generating hotel reports
- Printing final receipts

All data is stored in CSV files so it is saved between sessions without needing a database.

---

## Features

1. 10 rooms across 3 types (Single, Double, Suite)
2. Automatic booking ID, customer ID, payment ID generation
3. 5% GST added automatically to the total amount
4. Booking status tracking: CONFIRMED → CHECKED_IN → CHECKED_OUT / CANCELLED
5. Room availability is updated in real time
6. Data is saved to CSV files immediately after every action
7. Hotel report generated using a separate thread (multithreading)
8. QR code payment opens in the default web browser

---

## Tools and Technologies

- Java (Standard Library)
- CSV files (plain text data storage)
- Terminal / Console interface

No database, no web server, no external libraries were used.

---

## Java Concepts Demonstrated

| Concept | Where Used |
|---------|-----------|
| Inheritance | Customer extends Person, Room subclasses |
| Abstraction | Abstract classes: Person, Room |
| Polymorphism | Room subclasses override getRoomCategory() |
| Method Overloading | searchRooms() with different parameters |
| Enums | RoomType, BookingStatus, PaymentMethod |
| Custom Exceptions | RoomNotAvailableException, BookingNotFoundException |
| File I/O | Reading and writing CSV files |
| ArrayList | Storing rooms, customers, bookings, payments |
| Multithreading | ReportThread generates the hotel report |
| LocalDate | Check-in/check-out date handling |

---

## Limitations

- This is a console app — no graphical interface
- Data is stored in plain CSV files, not a database
- No login or authentication system
- Only one user can use the system at a time

---

## Future Improvements

- Add a MySQL database for better data storage
- Build a simple JavaFX GUI
- Add input validation for phone and email
- Add a login system for admin access
