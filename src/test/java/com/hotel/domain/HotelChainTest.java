package com.hotel.domain;

import com.hotel.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HotelChain class.
 * Tests normal cases, boundary conditions, and invalid inputs.
 */
public class HotelChainTest {

    private HotelChain chain;
    private Hotel hotel;
    private Guest guest;
    private Room room;
    private CreditCard creditCard;
    private ReservationManager manager;

    @BeforeEach
    public void setUp() {
        chain = new HotelChain("Grand Chains Inc");
        hotel = new Hotel(new Name("Grand Hotel Boston"));
        room = new Room(101, new RoomType("Deluxe", new Money(100.0)));
        hotel.addRoom(room);
        chain.addHotel(hotel);
        
        guest = Guest.create(new Name("John Doe"), new Address("123 Main", "Boston", "02101"));
        creditCard = new CreditCard("4111111111111111", "John Doe", "12/25", "123");
        manager = new ReservationManager();
    }

    // ==================== Normal Cases ====================

    @Test
    public void testCreateHotelChain_Success() {
        // Act
        HotelChain newChain = new HotelChain("Hotel Chain Co");

        // Assert
        assertEquals("Hotel Chain Co", newChain.getName());
    }

    @Test
    public void testAddHotel_Success() {
        // Arrange
        Hotel newHotel = new Hotel(new Name("Grand Hotel NYC"));
        HotelChain newChain = new HotelChain("New Chain");

        // Act
        newChain.addHotel(newHotel);

        // Assert
        assertEquals(1, newChain.getAllHotels().size());
    }

    @Test
    public void testGetHotel_Success() {
        // Act
        Hotel retrieved = chain.getHotel("Grand Hotel Boston");

        // Assert
        assertEquals(hotel, retrieved);
    }

    @Test
    public void testMakeReservation_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);

        // Act
        Reservation reservation = chain.makeReservation(
            hotel, guest, room, startDate, endDate, manager, creditCard);

        // Assert
        assertNotNull(reservation);
        assertEquals(guest, reservation.getGuest());
    }

    @Test
    public void testCancelReservation_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);
        Reservation reservation = chain.makeReservation(
            hotel, guest, room, startDate, endDate, manager, creditCard);

        // Act
        chain.cancelReservation(hotel, reservation, manager);

        // Assert
        assertEquals(Reservation.ReservationStatus.CANCELLED, reservation.getStatus());
    }

    @Test
    public void testCheckInGuest_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(3);
        Reservation reservation = chain.makeReservation(
            hotel, guest, room, startDate, endDate, manager, creditCard);

        // Act
        chain.checkInGuest(hotel, reservation);

        // Assert
        assertEquals(Reservation.ReservationStatus.CHECKED_IN, reservation.getStatus());
    }

    @Test
    public void testCheckOutGuest_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(3);
        Reservation reservation = chain.makeReservation(
            hotel, guest, room, startDate, endDate, manager, creditCard);
        chain.checkInGuest(hotel, reservation);

        // Act
        chain.checkOutGuest(hotel, reservation);

        // Assert
        assertEquals(Reservation.ReservationStatus.CHECKED_OUT, reservation.getStatus());
    }

    // ==================== Invalid Inputs ====================

    @Test
    public void testCreateHotelChain_NullName() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new HotelChain(null));
    }

    @Test
    public void testCreateHotelChain_EmptyName() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new HotelChain(""));
    }

    @Test
    public void testAddHotel_NullHotel() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> chain.addHotel(null));
    }

    @Test
    public void testAddHotel_Duplicate() {
        // Arrange
        Hotel duplicate = new Hotel(new Name("Grand Hotel Boston"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> chain.addHotel(duplicate));
    }

    @Test
    public void testGetHotel_NotFound() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> chain.getHotel("Non-existent"));
    }

    @Test
    public void testMakeReservation_NullParameters() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            chain.makeReservation(null, guest, room, startDate, endDate, manager, creditCard));
    }

    @Test
    public void testCancelReservation_NullParameters() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            chain.cancelReservation(null, null, manager));
    }

    // ==================== Validation Methods ====================

    @Test
    public void testCanMakeReservation_ValidParams() {
        // Act & Assert
        assertTrue(chain.canMakeReservation(room, 
            LocalDate.now().plusDays(1), 
            LocalDate.now().plusDays(3)));
    }

    @Test
    public void testCanMakeReservation_InvalidDates() {
        // Act & Assert
        assertFalse(chain.canMakeReservation(room, 
            LocalDate.now().plusDays(3), 
            LocalDate.now().plusDays(1)));
    }

    @Test
    public void testCanCancelReservation_Confirmed() {
        // Arrange
        Reservation reservation = Reservation.create(
            guest, room, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));

        // Act & Assert
        assertTrue(chain.canCancelReservation(reservation));
    }

    @Test
    public void testCanCancelReservation_CheckedOut() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(3);
        Reservation reservation = Reservation.create(guest, room, startDate, endDate);
        reservation.checkIn();
        reservation.checkOut();

        // Act & Assert
        assertFalse(chain.canCancelReservation(reservation));
    }

    @Test
    public void testCanCheckInGuest_Confirmed() {
        // Arrange
        Reservation reservation = Reservation.create(
            guest, room, LocalDate.now(), LocalDate.now().plusDays(3));

        // Act & Assert
        assertTrue(chain.canCheckInGuest(reservation));
    }

    @Test
    public void testCanCheckOutGuest_CheckedIn() {
        // Arrange
        Reservation reservation = Reservation.create(
            guest, room, LocalDate.now(), LocalDate.now().plusDays(3));
        reservation.checkIn();

        // Act & Assert
        assertTrue(chain.canCheckOutGuest(reservation));
    }

    @Test
    public void testCanCheckOutGuest_Confirmed() {
        // Arrange
        Reservation reservation = Reservation.create(
            guest, room, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));

        // Act & Assert
        assertFalse(chain.canCheckOutGuest(reservation));
    }
}
