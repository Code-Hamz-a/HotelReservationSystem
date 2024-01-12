package com.hotel.domain;

import com.hotel.util.Name;
import java.time.LocalDate;
import java.util.*;

/**
 * Represents a hotel with multiple rooms and reservations.
 */
public class Hotel {
    private final Name name;
    private final Map<Integer, Room> rooms;
    private final List<Reservation> reservations;

    public Hotel(Name name) {
        if (name == null) {
            throw new IllegalArgumentException("Hotel name cannot be null");
        }
        this.name = name;
        this.rooms = new HashMap<>();
        this.reservations = new ArrayList<>();
    }

    public Name getName() {
        return name;
    }

    /**
     * Adds a room to the hotel.
     */
    public void addRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        if (rooms.containsKey(room.getNumber())) {
            throw new IllegalArgumentException("Room number " + room.getNumber() + " already exists");
        }
        rooms.put(room.getNumber(), room);
    }

    /**
     * Gets a room by number.
     */
    public Room getRoom(int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room not found: " + roomNumber);
        }
        return room;
    }

    /**
     * Gets all rooms.
     */
    public Collection<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    /**
     * Finds available rooms of a specific type for given dates.
     */
    public List<Room> getAvailableRooms(RoomType roomType, LocalDate startDate, LocalDate endDate) {
        if (roomType == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException("Room type and dates cannot be null");
        }
        if (endDate.isBefore(startDate) || endDate.equals(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        List<Room> available = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.getRoomType().equals(roomType) && isRoomAvailable(room, startDate, endDate)) {
                available.add(room);
            }
        }
        return available;
    }

    /**
     * Checks if a room is available for the given date range.
     */
    private boolean isRoomAvailable(Room room, LocalDate startDate, LocalDate endDate) {
        return reservations.stream()
            .filter(r -> r.getRoom().equals(room))
            .filter(r -> r.getStatus() != Reservation.ReservationStatus.CANCELLED)
            .noneMatch(r -> hasDateConflict(r, startDate, endDate));
    }

    /**
     * Checks for date conflict between existing and new reservation.
     */
    private boolean hasDateConflict(Reservation existing, LocalDate startDate, LocalDate endDate) {
        LocalDate existStart = existing.getStartDate();
        LocalDate existEnd = existing.getEndDate();
        return !(endDate.isBefore(existStart) || startDate.isAfter(existEnd));
    }

    /**
     * Creates a reservation for a guest.
     */
    public Reservation createReservation(Guest guest, Room room, LocalDate startDate, LocalDate endDate) {
        if (guest == null || room == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (!rooms.containsValue(room)) {
            throw new IllegalArgumentException("Room does not belong to this hotel");
        }
        if (!isRoomAvailable(room, startDate, endDate)) {
            throw new IllegalStateException("Room is not available for the requested dates");
        }

        Reservation reservation = Reservation.create(guest, room, startDate, endDate);
        reservations.add(reservation);
        return reservation;
    }

    /**
     * Cancels a reservation.
     */
    public void cancelReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        if (!reservations.contains(reservation)) {
            throw new IllegalArgumentException("Reservation does not belong to this hotel");
        }
        reservation.cancel();
    }

    /**
     * Checks in a guest.
     */
    public void checkInGuest(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        if (!reservations.contains(reservation)) {
            throw new IllegalArgumentException("Reservation does not belong to this hotel");
        }
        if (LocalDate.now().isBefore(reservation.getStartDate())) {
            throw new IllegalStateException("Check-in date has not arrived");
        }
        reservation.checkIn();
        reservation.getRoom().setOccupant(reservation.getGuest());
    }

    /**
     * Checks out a guest.
     */
    public void checkOutGuest(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        if (!reservations.contains(reservation)) {
            throw new IllegalArgumentException("Reservation does not belong to this hotel");
        }
        reservation.checkOut();
        reservation.getRoom().setOccupant(null);
    }

    /**
     * Gets all reservations for a specific guest.
     */
    public List<Reservation> getGuestReservations(Guest guest) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest cannot be null");
        }
        return reservations.stream()
            .filter(r -> r.getGuest().equals(guest))
            .toList();
    }

    /**
     * Gets all active reservations.
     */
    public List<Reservation> getActiveReservations() {
        return reservations.stream()
            .filter(r -> r.getStatus() != Reservation.ReservationStatus.CANCELLED &&
                        r.getStatus() != Reservation.ReservationStatus.CHECKED_OUT)
            .toList();
    }

    @Override
    public String toString() {
        return "Hotel{" +
               "name=" + name +
               ", rooms=" + rooms.size() +
               ", reservations=" + reservations.size() +
               '}';
    }
}
