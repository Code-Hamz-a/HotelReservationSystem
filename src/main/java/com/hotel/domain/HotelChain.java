package com.hotel.domain;

import com.hotel.util.CreditCard;
import com.hotel.util.Identity;
import java.time.LocalDate;
import java.util.*;

/**
 * Manages hotel chain operations.
 * Handles reservation management across multiple hotels.
 */
public class HotelChain {
    private final String name;
    private final Map<String, Hotel> hotels;
    private final Map<Identity, ReservationManager> managers;

    public HotelChain(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Chain name cannot be null or empty");
        }
        this.name = name.trim();
        this.hotels = new HashMap<>();
        this.managers = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    /**
     * Registers a hotel with the chain.
     */
    public void addHotel(Hotel hotel) {
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel cannot be null");
        }
        if (hotels.containsKey(hotel.getName().getValue())) {
            throw new IllegalArgumentException("Hotel already exists in chain");
        }
        hotels.put(hotel.getName().getValue(), hotel);
    }

    /**
     * Gets a hotel by name.
     */
    public Hotel getHotel(String hotelName) {
        Hotel hotel = hotels.get(hotelName);
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel not found: " + hotelName);
        }
        return hotel;
    }

    /**
     * Gets all hotels in the chain.
     */
    public Collection<Hotel> getAllHotels() {
        return new ArrayList<>(hotels.values());
    }

    /**
     * Registers a reservation manager.
     */
    public void registerManager(ReservationManager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Manager cannot be null");
        }
        managers.put(manager.getId(), manager);
    }

    /**
     * Makes a reservation across the chain.
     */
    public Reservation makeReservation(Hotel hotel, Guest guest, Room room,
                                      LocalDate startDate, LocalDate endDate,
                                      ReservationManager manager, CreditCard creditCard) {
        if (hotel == null || guest == null || room == null || startDate == null ||
            endDate == null || manager == null || creditCard == null) {
            throw new IllegalArgumentException("All parameters must be non-null");
        }
        if (!canMakeReservation(room, startDate, endDate)) {
            throw new IllegalStateException("Cannot make reservation for requested dates");
        }

        Reservation reservation = hotel.createReservation(guest, room, startDate, endDate);
        manager.recordReservation(creditCard, reservation);
        return reservation;
    }

    /**
     * Cancels a reservation across the chain.
     */
    public void cancelReservation(Hotel hotel, Reservation reservation, ReservationManager manager) {
        if (hotel == null || reservation == null || manager == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (!canCancelReservation(reservation)) {
            throw new IllegalStateException("Cannot cancel reservation");
        }

        hotel.cancelReservation(reservation);
        manager.cancelReservation(reservation);
    }

    /**
     * Checks in a guest across the chain.
     */
    public void checkInGuest(Hotel hotel, Reservation reservation) {
        if (hotel == null || reservation == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (!canCheckInGuest(reservation)) {
            throw new IllegalStateException("Cannot check in guest");
        }

        hotel.checkInGuest(reservation);
    }

    /**
     * Checks out a guest across the chain.
     */
    public void checkOutGuest(Hotel hotel, Reservation reservation) {
        if (hotel == null || reservation == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (!canCheckOutGuest(reservation)) {
            throw new IllegalStateException("Cannot check out guest");
        }

        hotel.checkOutGuest(reservation);
    }

    public boolean canMakeReservation(Room room, LocalDate startDate, LocalDate endDate) {
        return room != null && startDate != null && endDate != null &&
               endDate.isAfter(startDate);
    }

    public boolean canCancelReservation(Reservation reservation) {
        return reservation != null &&
               reservation.getStatus() != Reservation.ReservationStatus.CHECKED_OUT &&
               reservation.getStatus() != Reservation.ReservationStatus.CANCELLED;
    }

    public boolean canCheckInGuest(Reservation reservation) {
        return reservation != null &&
               reservation.getStatus() == Reservation.ReservationStatus.CONFIRMED &&
               !LocalDate.now().isBefore(reservation.getStartDate());
    }

    public boolean canCheckOutGuest(Reservation reservation) {
        return reservation != null &&
               reservation.getStatus() == Reservation.ReservationStatus.CHECKED_IN;
    }

    @Override
    public String toString() {
        return "HotelChain{" +
               "name='" + name + '\'' +
               ", hotels=" + hotels.size() +
               '}';
    }
}
