package com.hotel.domain;

import com.hotel.util.Address;
import com.hotel.util.Money;
import com.hotel.util.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Reservation class.
 * Tests normal cases, boundary conditions, and invalid inputs.
 */
public class ReservationTest {

    private Guest guest;
    private Room room;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    public void setUp() {
        guest = Guest.create(new Name("John Doe"), new Address("123 Main", "Boston", "02101"));
        room = new Room(101, new RoomType("Deluxe", new Money(100.0)));
        startDate = LocalDate.now().plusDays(1);
        endDate = LocalDate.now().plusDays(5);
    }

    // ==================== Normal Cases ====================

    @Test
    public void testCreateReservation_Success() {
        // Arrange & Act
        Reservation reservation = Reservation.create(guest, room, startDate, endDate);

        // Assert
        assertNotNull(reservation.getId());
        assertEquals(guest, reservation.getGuest());
        assertEquals(room, reservation.getRoom());
        assertEquals(startDate, reservation.getStartDate());
        assertEquals(endDate, reservation.getEndDate());
        assertEquals(Reservation.ReservationStatus.CONFIRMED, reservation.getStatus());
    }

    @Test
    public void testGetNumberOfNights() {
        // Arrange & Act
        Reservation reservation = Reservation.create(guest, room, startDate, endDate);

        // Assert
        assertEquals(4, reservation.getNumberOfNights());
    }

    @Test
    public void testCheckIn_Success() {
        // Arrange
        Reservation reservation = Reservation.create(guest, room, startDate, endDate);

        // Act
        reservation.checkIn();

        // Assert
        assertEquals(Reservation.ReservationStatus.CHECKED_IN, reservation.getStatus());
    }

    @Test
    public void testCheckOut_Success() {
        // Arrange
        Reservation reservation = Reservation.create(guest, room, startDate, endDate);
        reservation.checkIn();

        // Act
        reservation.checkOut();

        // Assert
        assertEquals(Reservation.ReservationStatus.CHECKED_OUT, reservation.getStatus());
    }

    @Test
    public void testCancel_Success() {
        // Arrange
        Reservation reservation = Reservation.create(guest, room, startDate, endDate);

        // Act
        reservation.cancel();

        // Assert
        assertEquals(Reservation.ReservationStatus.CANCELLED, reservation.getStatus());
    }

    // ==================== Boundary Cases ====================

    @Test
    public void testCreateReservation_MinimumStay_OneNight() {
        // Arrange
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(2);

        // Act
        Reservation reservation = Reservation.create(guest, room, start, end);

        // Assert
        assertEquals(1, reservation.getNumberOfNights());
    }

    @Test
    public void testCreateReservation_LongStay() {
        // Arrange
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(365);

        // Act
        Reservation reservation = Reservation.create(guest, room, start, end);

        // Assert
        assertEquals(364, reservation.getNumberOfNights());
    }

    @Test
    public void testCreateReservation_TomorrowCheckIn() {
        // Arrange
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(2);

        // Act
        Reservation reservation = Reservation.create(guest, room, start, end);

        // Assert
        assertTrue(reservation.getStartDate().isAfter(LocalDate.now()));
    }

    // ==================== Invalid Inputs ====================

    @Test
    public void testCreateReservation_NullGuest() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> Reservation.create(null, room, startDate, endDate));
    }

    @Test
    public void testCreateReservation_NullRoom() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> Reservation.create(guest, null, startDate, endDate));
    }

    @Test
    public void testCreateReservation_NullStartDate() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> Reservation.create(guest, room, null, endDate));
    }

    @Test
    public void testCreateReservation_NullEndDate() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> Reservation.create(guest, room, startDate, null));
    }

    @Test
    public void testCreateReservation_PastStartDate() {
        // Arrange
        LocalDate pastDate = LocalDate.now().minusDays(1);
        LocalDate futureDate = LocalDate.now().plusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> Reservation.create(guest, room, pastDate, futureDate));
    }

    @Test
    public void testCreateReservation_EndDateBeforeStartDate() {
        // Arrange
        LocalDate start = LocalDate.now().plusDays(5);
        LocalDate end = LocalDate.now().plusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> Reservation.create(guest, room, start, end));
    }

    @Test
    public void testCreateReservation_SameDates() {
        // Arrange
        LocalDate date = LocalDate.now().plusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> Reservation.create(guest, room, date, date));
    }

    // ==================== State Transition Tests ====================

    @Test
    public void testCheckIn_NotConfirmed() {
        // Arrange
        Reservation reservation = Reservation.create(guest, room, startDate, endDate);
        reservation.checkIn();

        // Act & Assert
        assertThrows(IllegalStateException.class, reservation::checkIn);
    }

    @Test
    public void testCheckOut_NotCheckedIn() {
        // Arrange
        Reservation reservation = Reservation.create(guest, room, startDate, endDate);

        // Act & Assert
        assertThrows(IllegalStateException.class, reservation::checkOut);
    }

    @Test
    public void testCancel_CheckedOut() {
        // Arrange
        Reservation reservation = Reservation.create(guest, room, startDate, endDate);
        reservation.checkIn();
        reservation.checkOut();

        // Act & Assert
        assertThrows(IllegalStateException.class, reservation::cancel);
    }

    @Test
    public void testCancel_AlreadyCancelled() {
        // Arrange
        Reservation reservation = Reservation.create(guest, room, startDate, endDate);
        reservation.cancel();

        // Act & Assert
        assertThrows(IllegalStateException.class, reservation::cancel);
    }

    // ==================== Equality and Hash ====================

    @Test
    public void testEquals_SameId() {
        // Arrange
        Reservation res1 = Reservation.create(guest, room, startDate, endDate);
        Reservation res2 = res1;

        // Act & Assert
        assertEquals(res1, res2);
    }

    @Test
    public void testHashCode_Consistency() {
        // Arrange
        Reservation res1 = Reservation.create(guest, room, startDate, endDate);

        // Act & Assert
        assertEquals(res1.hashCode(), res1.hashCode());
    }

    @Test
    public void testToString() {
        // Arrange & Act
        Reservation reservation = Reservation.create(guest, room, startDate, endDate);
        String result = reservation.toString();

        // Assert
        assertTrue(result.contains("Reservation"));
        assertTrue(result.contains("CONFIRMED"));
    }
}
