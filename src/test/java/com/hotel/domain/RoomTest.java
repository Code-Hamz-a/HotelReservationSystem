package com.hotel.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.hotel.util.Money;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Room class.
 * Tests normal cases, boundary conditions, and invalid inputs.
 */
public class RoomTest {

    private RoomType roomType;
    private Room room;

    @BeforeEach
    public void setUp() {
        roomType = new RoomType("Deluxe", new Money(100.0));
        room = new Room(101, roomType);
    }

    // ==================== Normal Cases ====================

    @Test
    public void testCreateRoom_Success() {
        // Arrange & Act
        Room newRoom = new Room(102, roomType);

        // Assert
        assertEquals(102, newRoom.getNumber());
        assertEquals(roomType, newRoom.getRoomType());
        assertNull(newRoom.getOccupant());
        assertTrue(newRoom.isAvailable());
        assertFalse(newRoom.isOccupied());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100, 999})
    public void testCreateRoom_ValidRoomNumbers(int number) {
        // Arrange & Act
        Room newRoom = new Room(number, roomType);

        // Assert
        assertEquals(number, newRoom.getNumber());
    }

    // ==================== Boundary Cases ====================

    @Test
    public void testCreateRoom_MinimumValidNumber() {
        // Arrange & Act
        Room newRoom = new Room(1, roomType);

        // Assert
        assertEquals(1, newRoom.getNumber());
    }

    @Test
    public void testCreateRoom_LargeRoomNumber() {
        // Arrange & Act
        Room newRoom = new Room(9999, roomType);

        // Assert
        assertEquals(9999, newRoom.getNumber());
    }

    // ==================== Invalid Inputs ====================

    @Test
    public void testCreateRoom_NegativeNumber() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Room(-1, roomType));
    }

    @Test
    public void testCreateRoom_ZeroNumber() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Room(0, roomType));
    }

    @Test
    public void testCreateRoom_NullRoomType() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Room(101, null));
    }

    // ==================== Occupancy Tests ====================

    @Test
    public void testSetOccupant_Success() {
        // Arrange
        Guest guest = Guest.create(new com.hotel.util.Name("John Doe"),
                                  new com.hotel.util.Address("123 Main St", "Boston", "02101"));

        // Act
        room.setOccupant(guest);

        // Assert
        assertEquals(guest, room.getOccupant());
        assertTrue(room.isOccupied());
        assertFalse(room.isAvailable());
    }

    @Test
    public void testSetOccupant_FreesRoom() {
        // Arrange
        Guest guest = Guest.create(new com.hotel.util.Name("Jane Doe"),
                                  new com.hotel.util.Address("456 Oak Ave", "Boston", "02102"));
        room.setOccupant(guest);

        // Act
        room.setOccupant(null);

        // Assert
        assertNull(room.getOccupant());
        assertTrue(room.isAvailable());
        assertFalse(room.isOccupied());
    }

    @Test
    public void testInitiallyAvailable() {
        // Act & Assert
        assertTrue(room.isAvailable());
        assertFalse(room.isOccupied());
        assertNull(room.getOccupant());
    }

    // ==================== Equality and Hash ====================

    @Test
    public void testEquals_SameRoomNumber() {
        // Arrange
        Room room1 = new Room(101, roomType);
        Room room2 = new Room(101, roomType);

        // Act & Assert
        assertEquals(room1, room2);
    }

    @Test
    public void testEquals_DifferentRoomNumber() {
        // Arrange
        Room room1 = new Room(101, roomType);
        Room room2 = new Room(102, roomType);

        // Act & Assert
        assertNotEquals(room1, room2);
    }

    @Test
    public void testHashCode_Consistency() {
        // Arrange
        Room room1 = new Room(101, roomType);
        Room room2 = new Room(101, roomType);

        // Act & Assert
        assertEquals(room1.hashCode(), room2.hashCode());
    }

    @Test
    public void testToString() {
        // Act
        String result = room.toString();

        // Assert
        assertTrue(result.contains("101"));
        assertTrue(result.contains("Deluxe"));
    }
}
