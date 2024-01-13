package com.hotel.domain;

import com.hotel.util.Money;
import com.hotel.util.Name;
import com.hotel.util.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Hotel class.
 * Tests normal cases, boundary conditions, and invalid inputs.
 */
public class HotelTest {

    private Hotel hotel;
    private Room room;
    private Guest guest;

    @BeforeEach
    public void setUp() {
        hotel = new Hotel(new Name("Grand Hotel"));
        room = new Room(101, new RoomType("Deluxe", new Money(100.0)));
        guest = Guest.create(new Name("John Doe"), new Address("123 Main", "Boston", "02101"));
        hotel.addRoom(room);
    }

    // ==================== Normal Cases ====================

    @Test
    public void testCreateHotel_Success() {
        // Arrange & Act
        Hotel newHotel = new Hotel(new Name("Luxury Hotel"));

        // Assert
        assertEquals(new Name("Luxury Hotel"), newHotel.getName());
        assertEquals(0, newHotel.getAllRooms().size());
    }

    @Test
    public void testAddRoom_Success() {
        // Arrange
        Hotel newHotel = new Hotel(new Name("New Hotel"));
        Room newRoom = new Room(201, new RoomType("Standard", new Money(75.0)));

        // Act
        newHotel.addRoom(newRoom);

        // Assert
        assertEquals(1, newHotel.getAllRooms().size());
        assertEquals(newRoom, newHotel.getRoom(201));
    }

    @Test
    public void testGetRoom_Success() {
        // Act
        Room retrieved = hotel.getRoom(101);

        // Assert
        assertEquals(room, retrieved);
    }

    @Test
    public void testCreateReservation_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);

        // Act
        Reservation reservation = hotel.createReservation(guest, room, startDate, endDate);

        // Assert
        assertNotNull(reservation);
        assertEquals(guest, reservation.getGuest());
        assertEquals(room, reservation.getRoom());
    }

    @Test
    public void testCheckInGuest_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(3);
        Reservation reservation = hotel.createReservation(guest, room, startDate, endDate);

        // Act
        hotel.checkInGuest(reservation);

        // Assert
        assertEquals(Reservation.ReservationStatus.CHECKED_IN, reservation.getStatus());
        assertEquals(guest, room.getOccupant());
    }

    @Test
    public void testCheckOutGuest_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(3);
        Reservation reservation = hotel.createReservation(guest, room, startDate, endDate);
        hotel.checkInGuest(reservation);

        // Act
        hotel.checkOutGuest(reservation);

        // Assert
        assertEquals(Reservation.ReservationStatus.CHECKED_OUT, reservation.getStatus());
        assertNull(room.getOccupant());
    }

    @Test
    public void testCancelReservation_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);
        Reservation reservation = hotel.createReservation(guest, room, startDate, endDate);

        // Act
        hotel.cancelReservation(reservation);

        // Assert
        assertEquals(Reservation.ReservationStatus.CANCELLED, reservation.getStatus());
    }

    @Test
    public void testGetAvailableRooms_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);

        // Act
        List<Room> available = hotel.getAvailableRooms(
            new RoomType("Deluxe", new Money(100.0)), startDate, endDate);

        // Assert
        assertEquals(1, available.size());
        assertTrue(available.contains(room));
    }

    @Test
    public void testGetAvailableRooms_Booked() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);
        hotel.createReservation(guest, room, startDate, endDate);

        // Act
        List<Room> available = hotel.getAvailableRooms(
            new RoomType("Deluxe", new Money(100.0)), startDate, endDate);

        // Assert
        assertEquals(0, available.size());
    }

    // ==================== Boundary Cases ====================

    @Test
    public void testAddMultipleRooms() {
        // Arrange
        Hotel newHotel = new Hotel(new Name("Big Hotel"));
        for (int i = 1; i <= 10; i++) {
            newHotel.addRoom(new Room(i, new RoomType("Standard", new Money(75.0))));
        }

        // Act & Assert
        assertEquals(10, newHotel.getAllRooms().size());
    }

    // ==================== Invalid Inputs ====================

    @Test
    public void testCreateHotel_NullName() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Hotel(null));
    }

    @Test
    public void testAddRoom_NullRoom() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> hotel.addRoom(null));
    }

    @Test
    public void testAddRoom_DuplicateRoomNumber() {
        // Arrange
        Room duplicate = new Room(101, new RoomType("Standard", new Money(75.0)));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> hotel.addRoom(duplicate));
    }

    @Test
    public void testGetRoom_NonExistent() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> hotel.getRoom(999));
    }

    @Test
    public void testCreateReservation_RoomNotInHotel() {
        // Arrange
        Room otherRoom = new Room(202, new RoomType("Deluxe", new Money(100.0)));
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            hotel.createReservation(guest, otherRoom, startDate, endDate));
    }

    @Test
    public void testCreateReservation_NullParameters() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            hotel.createReservation(null, room, startDate, endDate));
    }

    @Test
    public void testCheckInGuest_NullReservation() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> hotel.checkInGuest(null));
    }

    @Test
    public void testCheckOutGuest_NullReservation() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> hotel.checkOutGuest(null));
    }

    @Test
    public void testCancelReservation_NullReservation() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> hotel.cancelReservation(null));
    }

    @Test
    public void testGetAvailableRooms_NullParameters() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            hotel.getAvailableRooms(null, LocalDate.now(), LocalDate.now().plusDays(1)));
    }

    // ==================== Guest Reservations ====================

    @Test
    public void testGetGuestReservations() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);
        hotel.createReservation(guest, room, startDate, endDate);

        // Act
        List<Reservation> reservations = hotel.getGuestReservations(guest);

        // Assert
        assertEquals(1, reservations.size());
    }

    @Test
    public void testGetActiveReservations() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);
        Reservation reservation = hotel.createReservation(guest, room, startDate, endDate);

        // Act
        List<Reservation> active = hotel.getActiveReservations();

        // Assert
        assertEquals(1, active.size());
        assertTrue(active.contains(reservation));
    }
}
