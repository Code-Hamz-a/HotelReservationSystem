package com.hotel.domain;

import com.hotel.util.CreditCard;
import com.hotel.util.Identity;
import java.util.*;

/**
 * Manages reservations and payment information.
 */
public class ReservationManager {
    private final Identity id;
    private final Map<Identity, CreditCard> creditCards;
    private final List<Reservation> managedReservations;

    public ReservationManager() {
        this.id = new Identity();
        this.creditCards = new HashMap<>();
        this.managedReservations = new ArrayList<>();
    }

    public Identity getId() {
        return id;
    }

    /**
     * Records credit card details for a reservation.
     */
    public void recordCreditCardDetails(CreditCard creditCard) {
        if (creditCard == null) {
            throw new IllegalArgumentException("Credit card cannot be null");
        }
        creditCards.put(id, creditCard);
    }

    /**
     * Records a reservation.
     */
    public void recordReservation(CreditCard creditCard, Reservation reservation) {
        if (creditCard == null || reservation == null) {
            throw new IllegalArgumentException("Credit card and reservation cannot be null");
        }
        recordCreditCardDetails(creditCard);
        managedReservations.add(reservation);
    }

    /**
     * Cancels a reservation.
     */
    public void cancelReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        if (!managedReservations.contains(reservation)) {
            throw new IllegalArgumentException("Reservation not managed by this manager");
        }
        managedReservations.remove(reservation);
    }

    /**
     * Gets all managed reservations.
     */
    public List<Reservation> getManagedReservations() {
        return new ArrayList<>(managedReservations);
    }

    /**
     * Gets the number of managed reservations.
     */
    public int getReservationCount() {
        return managedReservations.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationManager that = (ReservationManager) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ReservationManager{" +
               "id=" + id +
               ", managedReservations=" + managedReservations.size() +
               '}';
    }
}
