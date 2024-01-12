package com.hotel.domain;

import com.hotel.util.Address;
import com.hotel.util.Identity;
import com.hotel.util.Name;
import java.util.Objects;

/**
 * Represents a guest in the hotel system.
 */
public class Guest {
    private final Identity id;
    private final Name name;
    private final Address address;

    private Guest(Identity id, Name name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public static Guest create(Name name, Address address) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        return new Guest(new Identity(), name, address);
    }

    public static Guest create(String id, Name name, Address address) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        return new Guest(new Identity(id), name, address);
    }

    public Identity getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return id.equals(guest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Guest{" +
               "id=" + id +
               ", name=" + name +
               ", address=" + address +
               '}';
    }
}
