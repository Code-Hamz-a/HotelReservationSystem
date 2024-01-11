package com.hotel.util;

import java.util.Objects;
import java.util.UUID;

/**
 * Value object for identity.
 * Auto-generates unique IDs.
 */
public class Identity {
    private final String id;

    public Identity() {
        this.id = UUID.randomUUID().toString();
    }

    public Identity(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        this.id = id.trim();
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identity identity = (Identity) o;
        return id.equals(identity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
