package com.hotel.domain;

import com.hotel.util.Address;
import com.hotel.util.Name;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Guest class.
 * Tests normal cases, boundary conditions, and invalid inputs.
 */
public class GuestTest {

    private Name name;
    private Address address;

    @org.junit.jupiter.api.BeforeEach
    public void setUp() {
        name = new Name("John Doe");
        address = new Address("123 Main St", "Boston", "02101");
    }

    // ==================== Normal Cases ====================

    @Test
    public void testCreateGuest_Success() {
        // Arrange & Act
        Guest guest = Guest.create(name, address);

        // Assert
        assertNotNull(guest.getId());
        assertEquals(name, guest.getName());
        assertEquals(address, guest.getAddress());
    }

    @Test
    public void testCreateGuest_WithCustomId() {
        // Arrange & Act
        Guest guest = Guest.create("GUEST-001", name, address);

        // Assert
        assertNotNull(guest.getId());
        assertEquals(name, guest.getName());
        assertEquals(address, guest.getAddress());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Alice", "Bob Smith", "Maria Garcia", "John O'Brien"})
    public void testCreateGuest_VariousNames(String guestName) {
        // Arrange & Act
        Guest guest = Guest.create(new Name(guestName), address);

        // Assert
        assertEquals(new Name(guestName), guest.getName());
    }

    // ==================== Boundary Cases ====================

    @Test
    public void testCreateGuest_SingleCharacterName() {
        // Arrange & Act
        Guest guest = Guest.create(new Name("A"), address);

        // Assert
        assertEquals("A", guest.getName().getValue());
    }

    @Test
    public void testCreateGuest_LongName() {
        // Arrange
        String longName = "A".repeat(100);

        // Act
        Guest guest = Guest.create(new Name(longName), address);

        // Assert
        assertEquals(longName, guest.getName().getValue());
    }

    @Test
    public void testCreateGuest_DifferentAddresses() {
        // Arrange
        Address address1 = new Address("123 Main", "NYC", "10001");
        Address address2 = new Address("456 Oak", "Boston", "02101");

        // Act
        Guest guest1 = Guest.create(name, address1);
        Guest guest2 = Guest.create(name, address2);

        // Assert
        assertNotEquals(guest1.getId(), guest2.getId());
        assertNotEquals(guest1, guest2);
    }

    // ==================== Invalid Inputs ====================

    @Test
    public void testCreateGuest_NullName() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> Guest.create(null, address));
    }

    @Test
    public void testCreateGuest_NullAddress() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> Guest.create(name, null));
    }

    @Test
    public void testCreateGuest_WithCustomId_NullId() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> Guest.create(null, name, address));
    }

    @Test
    public void testCreateGuest_WithCustomId_EmptyId() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> Guest.create("", name, address));
    }

    @Test
    public void testCreateGuest_WithCustomId_NullName() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> Guest.create("GUEST-001", null, address));
    }

    @Test
    public void testCreateGuest_WithCustomId_NullAddress() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> Guest.create("GUEST-001", name, null));
    }

    // ==================== Uniqueness Tests ====================

    @Test
    public void testGuestUniqueness_DifferentIds() {
        // Arrange & Act
        Guest guest1 = Guest.create(name, address);
        Guest guest2 = Guest.create(name, address);

        // Assert
        assertNotEquals(guest1.getId(), guest2.getId());
    }

    @Test
    public void testGuestEquality_SameId() {
        // Arrange
        Guest guest1 = Guest.create("GUEST-001", name, address);
        Guest guest2 = Guest.create("GUEST-001", name, address);

        // Act & Assert
        assertEquals(guest1, guest2);
    }

    @Test
    public void testHashCode_Consistency() {
        // Arrange
        Guest guest1 = Guest.create("GUEST-001", name, address);
        Guest guest2 = Guest.create("GUEST-001", name, address);

        // Act & Assert
        assertEquals(guest1.hashCode(), guest2.hashCode());
    }

    @Test
    public void testToString() {
        // Arrange & Act
        Guest guest = Guest.create(name, address);
        String result = guest.toString();

        // Assert
        assertTrue(result.contains("Guest"));
        assertTrue(result.contains("John Doe"));
    }
}
