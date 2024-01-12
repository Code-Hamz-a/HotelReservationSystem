package com.hotel.domain;

import com.hotel.util.Identity;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a reservation in the hotel system.
 * Immutable once created.
 */
public class Reservation {
    private final Identity id;
    private final Guest guest;
    private final Room room;
    private final LocalDate reservationDate;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private ReservationStatus status;

    public enum ReservationStatus {
        CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED
    }

    private Reservation(Identity id, Guest guest, Room room, LocalDate reservationDate,
                       LocalDate startDate, LocalDate endDate, ReservationStatus status) {
        this.id = id;
        this.guest = guest;
        this.room = room;
        this.reservationDate = reservationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public static Reservation create(Guest guest, Room room, LocalDate startDate, LocalDate endDate) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest cannot be null");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
        if (endDate.isBefore(startDate) || endDate.equals(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        return new Reservation(new Identity(), guest, room, LocalDate.now(),
                             startDate, endDate, ReservationStatus.CONFIRMED);
    }

    public Identity getId() {
        return id;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void checkIn() {
        if (status != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Can only check in confirmed reservations");
        }
        if (LocalDate.now().isBefore(startDate)) {
            throw new IllegalStateException("Check-in date has not arrived");
        }
        this.status = ReservationStatus.CHECKED_IN;
    }

    public void checkOut() {
        if (status != ReservationStatus.CHECKED_IN) {
            throw new IllegalStateException("Can only check out guests that are checked in");
        }
        this.status = ReservationStatus.CHECKED_OUT;
    }

    public void cancel() {
        if (status == ReservationStatus.CHECKED_OUT || status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel completed or already cancelled reservations");
        }
        this.status = ReservationStatus.CANCELLED;
    }

    public int getNumberOfNights() {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reservation{" +
               "id=" + id +
               ", guest=" + guest.getName() +
               ", room=" + room.getNumber() +
               ", startDate=" + startDate +
               ", endDate=" + endDate +
               ", status=" + status +
               '}';
    }
}
