package com.hotel.domain;

import java.util.Objects;

/**
 * Represents a room in the hotel.
 * Can be occupied or available.
 */
public class Room {
    private final int number;
    private final RoomType roomType;
    private Guest occupant;

    public Room(int number, RoomType roomType) {
        if (number <= 0) {
            throw new IllegalArgumentException("Room number must be positive");
        }
        if (roomType == null) {
            throw new IllegalArgumentException("Room type cannot be null");
        }
        this.number = number;
        this.roomType = roomType;
        this.occupant = null;
    }

    public int getNumber() {
        return number;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public Guest getOccupant() {
        return occupant;
    }

    public boolean isOccupied() {
        return occupant != null;
    }

    public boolean isAvailable() {
        return occupant == null;
    }

    /**
     * Sets the occupant of the room.
     * @param guest the guest to set as occupant, or null to free the room
     */
    protected void setOccupant(Guest guest) {
        this.occupant = guest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return number == room.number &&
               roomType.equals(room.roomType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, roomType);
    }

    @Override
    public String toString() {
        return "Room{" + "number=" + number +
               ", type=" + roomType.getKind() +
               ", available=" + isAvailable() + '}';
    }
}
