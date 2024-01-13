package com.hotel.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Money class.
 */
public class MoneyTest {

    @Test
    public void testCreateMoney_WithBigDecimal() {
        // Act
        Money money = new Money(BigDecimal.valueOf(100.0));

        // Assert
        assertEquals(BigDecimal.valueOf(100.0), money.getAmount());
    }

    @Test
    public void testCreateMoney_WithDouble() {
        // Act
        Money money = new Money(100.0);

        // Assert
        assertEquals(BigDecimal.valueOf(100.0), money.getAmount());
    }

    @Test
    public void testCreateMoney_ZeroAmount() {
        // Act
        Money money = new Money(0.0);

        // Assert
        assertEquals(BigDecimal.ZERO, money.getAmount());
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0, 50.0, 100.0, 1000.0})
    public void testCreateMoney_VariousAmounts(double amount) {
        // Act
        Money money = new Money(amount);

        // Assert
        assertEquals(BigDecimal.valueOf(amount), money.getAmount());
    }

    @Test
    public void testCreateMoney_NullAmount() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Money((BigDecimal) null));
    }

    @Test
    public void testCreateMoney_NegativeAmount() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Money(-100.0));
    }

    @Test
    public void testAdd_Success() {
        // Arrange
        Money money1 = new Money(100.0);
        Money money2 = new Money(50.0);

        // Act
        Money result = money1.add(money2);

        // Assert
        assertEquals(BigDecimal.valueOf(150.0), result.getAmount());
    }

    @Test
    public void testSubtract_Success() {
        // Arrange
        Money money1 = new Money(100.0);
        Money money2 = new Money(30.0);

        // Act
        Money result = money1.subtract(money2);

        // Assert
        assertEquals(BigDecimal.valueOf(70.0), result.getAmount());
    }

    @Test
    public void testSubtract_ResultInZero() {
        // Arrange
        Money money1 = new Money(100.0);
        Money money2 = new Money(100.0);

        // Act
        Money result = money1.subtract(money2);

        // Assert
        assertEquals(BigDecimal.ZERO, result.getAmount());
    }

    @Test
    public void testMultiply_Success() {
        // Arrange
        Money money = new Money(25.0);

        // Act
        Money result = money.multiply(4);

        // Assert
        assertEquals(BigDecimal.valueOf(100.0), result.getAmount());
    }

    @Test
    public void testEquals_SameAmount() {
        // Arrange
        Money money1 = new Money(100.0);
        Money money2 = new Money(100.0);

        // Act & Assert
        assertEquals(money1, money2);
    }

    @Test
    public void testHashCode_Consistency() {
        // Arrange
        Money money1 = new Money(100.0);
        Money money2 = new Money(100.0);

        // Act & Assert
        assertEquals(money1.hashCode(), money2.hashCode());
    }
}
