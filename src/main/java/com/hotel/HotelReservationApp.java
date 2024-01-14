package com.hotel;

import com.hotel.domain.*;
import com.hotel.util.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Main application entry point demonstrating the hotel reservation system.
 * Demonstrates all key use cases and interactions between objects.
 */
public class HotelReservationApp {

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("HOTEL RESERVATION SYSTEM - DEMONSTRATION");
        System.out.println("=".repeat(70));

        // ==================== Initialize Hotel Chain ====================
        System.out.println("\n[1] INITIALIZING HOTEL CHAIN");
        System.out.println("-".repeat(70));
        
        HotelChain chain = new HotelChain("Grand International Hotels");
        System.out.println("✓ Created hotel chain: " + chain.getName());

        // ==================== Create Hotels and Rooms ====================
        System.out.println("\n[2] CREATING HOTELS AND ROOMS");
        System.out.println("-".repeat(70));
        
        Hotel bostonHotel = new Hotel(new Name("Grand Hotel Boston"));
        Hotel nycHotel = new Hotel(new Name("Grand Hotel NYC"));
        
        chain.addHotel(bostonHotel);
        chain.addHotel(nycHotel);
        System.out.println("✓ Added hotels to chain");

        // Create room types
        RoomType deluxe = new RoomType("Deluxe", new Money(150.0));
        RoomType standard = new RoomType("Standard", new Money(100.0));
        RoomType economy = new RoomType("Economy", new Money(75.0));
        System.out.println("✓ Created room types: " + deluxe + ", " + standard + ", " + economy);

        // Add rooms to Boston hotel
        for (int i = 101; i <= 105; i++) {
            bostonHotel.addRoom(new Room(i, deluxe));
        }
        for (int i = 201; i <= 205; i++) {
            bostonHotel.addRoom(new Room(i, standard));
        }
        System.out.println("✓ Added 10 rooms to Boston hotel");

        // Add rooms to NYC hotel
        for (int i = 301; i <= 303; i++) {
            nycHotel.addRoom(new Room(i, deluxe));
        }
        System.out.println("✓ Added 3 rooms to NYC hotel");

        // ==================== Create Guests ====================
        System.out.println("\n[3] CREATING GUESTS");
        System.out.println("-".repeat(70));
        
        Guest guest1 = Guest.create(
            new Name("John Doe"),
            new Address("123 Main Street", "New York", "10001"));
        System.out.println("✓ Created guest: " + guest1.getName());

        Guest guest2 = Guest.create(
            new Name("Jane Smith"),
            new Address("456 Oak Avenue", "Boston", "02101"));
        System.out.println("✓ Created guest: " + guest2.getName());

        Guest guest3 = Guest.create(
            new Name("Bob Johnson"),
            new Address("789 Elm Road", "Chicago", "60601"));
        System.out.println("✓ Created guest: " + guest3.getName());

        // ==================== Create Reservation Managers ====================
        System.out.println("\n[4] CREATING RESERVATION MANAGERS");
        System.out.println("-".repeat(70));
        
        ReservationManager manager1 = new ReservationManager();
        ReservationManager manager2 = new ReservationManager();
        System.out.println("✓ Created 2 reservation managers");

        chain.registerManager(manager1);
        chain.registerManager(manager2);
        System.out.println("✓ Registered managers with hotel chain");

        // ==================== Create Credit Cards ====================
        System.out.println("\n[5] CREATING CREDIT CARDS");
        System.out.println("-".repeat(70));
        
        CreditCard card1 = new CreditCard("4111111111111111", "John Doe", "12/25", "123");
        CreditCard card2 = new CreditCard("4111111111111112", "Jane Smith", "06/24", "456");
        CreditCard card3 = new CreditCard("4111111111111113", "Bob Johnson", "09/26", "789");
        System.out.println("✓ Created 3 credit cards");

        // ==================== MAKE RESERVATIONS ====================
        System.out.println("\n[6] MAKING RESERVATIONS");
        System.out.println("-".repeat(70));
        
        LocalDate start1 = LocalDate.now().plusDays(5);
        LocalDate end1 = LocalDate.now().plusDays(8);
        Reservation res1 = chain.makeReservation(
            bostonHotel,
            guest1,
            bostonHotel.getRoom(101),
            start1,
            end1,
            manager1,
            card1);
        System.out.println("✓ Reservation 1: " + guest1.getName() + 
                         " - Room 101 (Deluxe) - " + res1.getNumberOfNights() + " nights");
        System.out.println("  Status: " + res1.getStatus());

        LocalDate start2 = LocalDate.now().plusDays(10);
        LocalDate end2 = LocalDate.now().plusDays(15);
        Reservation res2 = chain.makeReservation(
            bostonHotel,
            guest2,
            bostonHotel.getRoom(201),
            start2,
            end2,
            manager2,
            card2);
        System.out.println("✓ Reservation 2: " + guest2.getName() + 
                         " - Room 201 (Standard) - " + res2.getNumberOfNights() + " nights");
        System.out.println("  Status: " + res2.getStatus());

        LocalDate start3 = LocalDate.now().plusDays(3);
        LocalDate end3 = LocalDate.now().plusDays(7);
        Reservation res3 = chain.makeReservation(
            nycHotel,
            guest3,
            nycHotel.getRoom(301),
            start3,
            end3,
            manager1,
            card3);
        System.out.println("✓ Reservation 3: " + guest3.getName() + 
                         " - NYC Hotel Room 301 (Deluxe) - " + res3.getNumberOfNights() + " nights");
        System.out.println("  Status: " + res3.getStatus());

        // ==================== CHECK AVAILABLE ROOMS ====================
        System.out.println("\n[7] CHECKING AVAILABLE ROOMS");
        System.out.println("-".repeat(70));
        
        LocalDate checkStart = LocalDate.now().plusDays(5);
        LocalDate checkEnd = LocalDate.now().plusDays(8);
        List<Room> availableDeluxe = bostonHotel.getAvailableRooms(deluxe, checkStart, checkEnd);
        System.out.println("✓ Available Deluxe rooms in Boston (" + checkStart + " to " + checkEnd + "): " + 
                         (availableDeluxe.size()));
        System.out.println("  (Booked: Room 101)");

        availableDeluxe.forEach(r -> System.out.println("  - Room " + r.getNumber() + " available"));

        // ==================== CHECK IN GUESTS ====================
        System.out.println("\n[8] CHECKING IN GUESTS");
        System.out.println("-".repeat(70));
        
        // Simulate check-in on start date by adjusting reservation dates
        LocalDate checkInStart = LocalDate.now();
        LocalDate checkInEnd = LocalDate.now().plusDays(3);
        Reservation checkInRes = chain.makeReservation(
            bostonHotel,
            guest1,
            bostonHotel.getRoom(102),
            checkInStart,
            checkInEnd,
            manager1,
            card1);
        System.out.println("✓ Created reservation for check-in: Room 102");

        chain.checkInGuest(bostonHotel, checkInRes);
        System.out.println("✓ Checked in: " + guest1.getName() + " to Room 102");
        System.out.println("  Reservation Status: " + checkInRes.getStatus());
        System.out.println("  Room Occupant: " + bostonHotel.getRoom(102).getOccupant().getName());

        // ==================== CHECK OUT GUEST ====================
        System.out.println("\n[9] CHECKING OUT GUEST");
        System.out.println("-".repeat(70));
        
        chain.checkOutGuest(bostonHotel, checkInRes);
        System.out.println("✓ Checked out: " + guest1.getName() + " from Room 102");
        System.out.println("  Reservation Status: " + checkInRes.getStatus());
        System.out.println("  Room Occupant: " + (bostonHotel.getRoom(102).getOccupant() != null ? 
                         "Occupied" : "Available"));

        // ==================== CANCEL RESERVATION ====================
        System.out.println("\n[10] CANCELING RESERVATION");
        System.out.println("-".repeat(70));
        
        System.out.println("✓ Before cancellation - Reservation 2 Status: " + res2.getStatus());
        chain.cancelReservation(bostonHotel, res2, manager2);
        System.out.println("✓ After cancellation - Reservation 2 Status: " + res2.getStatus());
        System.out.println("  Guest: " + res2.getGuest().getName());

        // ==================== QUERY GUEST RESERVATIONS ====================
        System.out.println("\n[11] QUERYING GUEST RESERVATIONS");
        System.out.println("-".repeat(70));
        
        List<Reservation> guest1Reservations = bostonHotel.getGuestReservations(guest1);
        System.out.println("✓ Reservations for " + guest1.getName() + ": " + guest1Reservations.size());

        // ==================== QUERY ACTIVE RESERVATIONS ====================
        System.out.println("\n[12] QUERYING ACTIVE RESERVATIONS");
        System.out.println("-".repeat(70));
        
        List<Reservation> activeReservations = bostonHotel.getActiveReservations();
        System.out.println("✓ Active reservations in Boston hotel: " + activeReservations.size());
        activeReservations.forEach(r -> 
            System.out.println("  - " + r.getGuest().getName() + " (Room " + r.getRoom().getNumber() + ")"));

        // ==================== DISPLAY MANAGER RESERVATIONS ====================
        System.out.println("\n[13] RESERVATION MANAGER SUMMARY");
        System.out.println("-".repeat(70));
        
        System.out.println("✓ Manager 1 - Managed Reservations: " + manager1.getReservationCount());
        manager1.getManagedReservations().forEach(r ->
            System.out.println("  - " + r.getGuest().getName() + " (Status: " + r.getStatus() + ")"));

        System.out.println("✓ Manager 2 - Managed Reservations: " + manager2.getReservationCount());
        manager2.getManagedReservations().forEach(r ->
            System.out.println("  - " + r.getGuest().getName() + " (Status: " + r.getStatus() + ")"));

        // ==================== DEMONSTRATE DEFENSIVE PROGRAMMING ====================
        System.out.println("\n[14] DEFENSIVE PROGRAMMING DEMONSTRATION");
        System.out.println("-".repeat(70));
        
        try {
            Guest invalidGuest = Guest.create(new Name(""), new Address("", "", ""));
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Caught exception: " + e.getMessage());
        }

        try {
            Hotel invalidHotel = new Hotel(null);
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Caught exception: " + e.getMessage());
        }

        try {
            CreditCard invalidCard = new CreditCard("1234", "John", "13/25", "123");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Caught exception: " + e.getMessage());
        }

        try {
            Reservation invalidRes = Reservation.create(
                guest1, bostonHotel.getRoom(101),
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1));
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Caught exception: " + e.getMessage());
        }

        // ==================== SUMMARY ====================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("DEMONSTRATION COMPLETE");
        System.out.println("=".repeat(70));
        System.out.println("\nSummary:");
        System.out.println("  Total Hotels: " + chain.getAllHotels().size());
        System.out.println("  Total Boston Hotel Rooms: " + bostonHotel.getAllRooms().size());
        System.out.println("  Total Active Reservations: " + activeReservations.size());
        System.out.println("\nAll UML relationships and use cases have been successfully demonstrated.");
        System.out.println("=".repeat(70));
    }
}
