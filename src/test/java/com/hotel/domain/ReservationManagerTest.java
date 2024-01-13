package com.hotel.domain;

import com.hotel.util.CreditCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReservationManager class.
 * Tests normal cases, boundary conditions, and invalid inputs.
 */
public class ReservationManagerTest {

    private ReservationManager manager;
    private CreditCard creditCard;
    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        manager = new ReservationManager();
        creditCard = new CreditCard("4111111111111111", "John Doe", "12/25", "123");
        
        Guest guest = Guest.create(
            new com.hotel.util.Name("John Doe"),
            new com.hotel.util.Address("123 Main", "Boston", "02101"));
        Room room = new Room(101, new RoomType("Deluxe", new com.hotel.util.Money(100.0)));
        reservation = Reservation.create(
            guest, room, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
    }

    // ==================== Normal Cases ====================

    @Test
    public void testCreateManager_Success() {
        // Act
        ReservationManager newManager = new ReservationManager();

        // Assert
        assertNotNull(newManager.getId());
        assertEquals(0, newManager.getReservationCount());
    }

    @Test
    public void testRecordCreditCardDetails_Success() {
        // Act
        manager.recordCreditCardDetails(creditCard);

        // Assert
        // Credit card recorded successfully (no exception)
    }

    @Test
    public void testRecordReservation_Success() {
        // Act
        manager.recordReservation(creditCard, reservation);

        // Assert
        assertEquals(1, manager.getReservationCount());
        assertTrue(manager.getManagedReservations().contains(reservation));
    }

    @Test
    public void testCancelReservation_Success() {
        // Arrange
        manager.recordReservation(creditCard, reservation);

        // Act
        manager.cancelReservation(reservation);

        // Assert
        assertEquals(0, manager.getReservationCount());
    }

    @Test
    public void testGetManagedReservations() {
        // Arrange
        manager.recordReservation(creditCard, reservation);

        // Act
        List<Reservation> reservations = manager.getManagedReservations();

        // Assert
        assertEquals(1, reservations.size());
        assertTrue(reservations.contains(reservation));
    }

    @Test
    public void testGetReservationCount_Multiple() {
        // Arrange
        manager.recordReservation(creditCard, reservation);
        
        Guest guest2 = Guest.create(
            new com.hotel.util.Name("Jane Doe"),
            new com.hotel.util.Address("456 Oak", "Boston", "02102"));
        Room room2 = new Room(102, new RoomType("Standard", new com.hotel.util.Money(75.0)));
        Reservation reservation2 = Reservation.create(
            guest2, room2, LocalDate.now().plusDays(5), LocalDate.now().plusDays(7));
        manager.recordReservation(creditCard, reservation2);

        // Act & Assert
        assertEquals(2, manager.getReservationCount());
    }

    // ==================== Invalid Inputs ====================

    @Test
    public void testRecordCreditCardDetails_NullCard() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            manager.recordCreditCardDetails(null));
    }

    @Test
    public void testRecordReservation_NullCard() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            manager.recordReservation(null, reservation));
    }

    @Test
    public void testRecordReservation_NullReservation() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            manager.recordReservation(creditCard, null));
    }

    @Test
    public void testCancelReservation_NullReservation() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            manager.cancelReservation(null));
    }

    @Test
    public void testCancelReservation_NotManaged() {
        // Arrange
        Guest guest = Guest.create(
            new com.hotel.util.Name("Unknown"),
            new com.hotel.util.Address("000 Unknown", "Unknown", "00000"));
        Room room = new Room(999, new RoomType("Unknown", new com.hotel.util.Money(1.0)));
        Reservation unmanaged = Reservation.create(
            guest, room, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            manager.cancelReservation(unmanaged));
    }

    // ==================== Uniqueness ====================

    @Test
    public void testManagerUniqueness() {
        // Arrange & Act
        ReservationManager manager1 = new ReservationManager();
        ReservationManager manager2 = new ReservationManager();

        // Assert
        assertNotEquals(manager1.getId(), manager2.getId());
    }

    @Test
    public void testEquals_SameId() {
        // Arrange
        ReservationManager manager1 = new ReservationManager();
        ReservationManager manager2 = manager1;

        // Act & Assert
        assertEquals(manager1, manager2);
    }

    @Test
    public void testHashCode_Consistency() {
        // Act & Assert
        assertEquals(manager.hashCode(), manager.hashCode());
    }

    @Test
    public void testToString() {
        // Act
        String result = manager.toString();

        // Assert
        assertTrue(result.contains("ReservationManager"));
    }
}
