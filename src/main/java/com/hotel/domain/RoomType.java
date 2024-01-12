package com.hotel.domain;

import com.hotel.util.Money;
import java.util.Objects;

/**
 * Represents the type of room available in the hotel.
 * Immutable value object.
 */
public class RoomType {
    private final String kind;
    private final Money cost;

    public RoomType(String kind, Money cost) {
        if (kind == null || kind.trim().isEmpty()) {
            throw new IllegalArgumentException("Room kind cannot be null or empty");
        }
        if (cost == null) {
            throw new IllegalArgumentException("Cost cannot be null");
        }
        this.kind = kind.trim();
        this.cost = cost;
    }

    public String getKind() {
        return kind;
    }

    public Money getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomType roomType = (RoomType) o;
        return kind.equals(roomType.kind) &&
               cost.equals(roomType.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, cost);
    }

    @Override
    public String toString() {
        return kind + " - " + cost;
    }
}
