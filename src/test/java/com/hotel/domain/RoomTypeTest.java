package com.hotel.domain;

import com.hotel.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RoomType class.
 * Tests normal cases, boundary conditions, and invalid inputs.
 */
public class RoomTypeTest {

    private Money cost;
    private RoomType roomType;

    @BeforeEach
    public void setUp() {
        cost = new Money(100.0);
        roomType = new RoomType("Deluxe", cost);
    }

    // ==================== Normal Cases ====================

    @Test
    public void testCreateRoomType_Success() {
        // Arrange & Act
        RoomType deluxe = new RoomType("Deluxe", new Money(100.0));

        // Assert
        assertEquals("Deluxe", deluxe.getKind());
        assertEquals(new Money(100.0), deluxe.getCost());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Single", "Double", "Suite", "Presidential"})
    public void testCreateRoomType_VariousKinds(String kind) {
        // Arrange & Act
        RoomType room = new RoomType(kind, new Money(150.0));

        // Assert
        assertEquals(kind, room.getKind());
        assertNotNull(room.getCost());
    }

    @ParameterizedTest
    @CsvSource({
        "Single, 50.0",
        "Double, 100.0",
        "Suite, 200.0"
    })
    public void testCreateRoomType_VariousCosts(String kind, String cost) {
        // Arrange & Act
        RoomType room = new RoomType(kind, new Money(Double.parseDouble(cost)));

        // Assert
        assertEquals(kind, room.getKind());
        assertEquals(new Money(Double.parseDouble(cost)), room.getCost());
    }

    // ==================== Boundary Cases ====================

    @Test
    public void testCreateRoomType_MinimumCost() {
        // Arrange & Act
        RoomType room = new RoomType("Economy", new Money(0.0));

        // Assert
        assertEquals(new Money(0.0), room.getCost());
    }

    @Test
    public void testCreateRoomType_LargeCost() {
        // Arrange & Act
        RoomType room = new RoomType("Luxury", new Money(10000.0));

        // Assert
        assertEquals(new Money(10000.0), room.getCost());
    }

    @Test
    public void testCreateRoomType_SingleCharacterKind() {
        // Arrange & Act
        RoomType room = new RoomType("A", new Money(100.0));

        // Assert
        assertEquals("A", room.getKind());
    }

    @Test
    public void testCreateRoomType_KindWithSpaces() {
        // Arrange & Act
        RoomType room = new RoomType("   Deluxe   ", new Money(100.0));

        // Assert
        assertEquals("Deluxe", room.getKind());
    }

    // ==================== Invalid Inputs ====================

    @Test
    public void testCreateRoomType_NullKind() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new RoomType(null, cost));
    }

    @Test
    public void testCreateRoomType_EmptyKind() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new RoomType("", cost));
    }

    @Test
    public void testCreateRoomType_BlankKind() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new RoomType("   ", cost));
    }

    @Test
    public void testCreateRoomType_NullCost() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new RoomType("Deluxe", null));
    }

    // ==================== Equality and Hash ====================

    @Test
    public void testEquals_SameValues() {
        // Arrange
        RoomType room1 = new RoomType("Deluxe", new Money(100.0));
        RoomType room2 = new RoomType("Deluxe", new Money(100.0));

        // Act & Assert
        assertEquals(room1, room2);
    }

    @Test
    public void testEquals_DifferentKind() {
        // Arrange
        RoomType room1 = new RoomType("Deluxe", new Money(100.0));
        RoomType room2 = new RoomType("Standard", new Money(100.0));

        // Act & Assert
        assertNotEquals(room1, room2);
    }

    @Test
    public void testEquals_DifferentCost() {
        // Arrange
        RoomType room1 = new RoomType("Deluxe", new Money(100.0));
        RoomType room2 = new RoomType("Deluxe", new Money(150.0));

        // Act & Assert
        assertNotEquals(room1, room2);
    }

    @Test
    public void testHashCode_Consistency() {
        // Arrange
        RoomType room1 = new RoomType("Deluxe", new Money(100.0));
        RoomType room2 = new RoomType("Deluxe", new Money(100.0));

        // Act & Assert
        assertEquals(room1.hashCode(), room2.hashCode());
    }

    @Test
    public void testToString() {
        // Act
        String result = roomType.toString();

        // Assert
        assertTrue(result.contains("Deluxe"));
        assertTrue(result.contains("100"));
    }
}
