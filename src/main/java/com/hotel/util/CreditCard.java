package com.hotel.util;

import java.util.Objects;

/**
 * Value object for credit card details.
 * Immutable and defensively programmed.
 */
public class CreditCard {
    private final String cardNumber;
    private final String cardHolder;
    private final String expiryDate;
    private final String cvv;

    public CreditCard(String cardNumber, String cardHolder, String expiryDate, String cvv) {
        if (cardNumber == null || !isValidCardNumber(cardNumber)) {
            throw new IllegalArgumentException("Invalid card number");
        }
        if (cardHolder == null || cardHolder.trim().isEmpty()) {
            throw new IllegalArgumentException("Card holder name cannot be null or empty");
        }
        if (expiryDate == null || !isValidExpiryDate(expiryDate)) {
            throw new IllegalArgumentException("Invalid expiry date format (MM/YY)");
        }
        if (cvv == null || !isValidCVV(cvv)) {
            throw new IllegalArgumentException("Invalid CVV");
        }
        this.cardNumber = cardNumber.replaceAll("\\s", "");
        this.cardHolder = cardHolder.trim();
        this.expiryDate = expiryDate.trim();
        this.cvv = cvv.trim();
    }

    private static boolean isValidCardNumber(String cardNumber) {
        String cleaned = cardNumber.replaceAll("\\s", "");
        return cleaned.matches("\\d{13,19}");
    }

    private static boolean isValidExpiryDate(String expiryDate) {
        return expiryDate.matches("\\d{2}/\\d{2}");
    }

    private static boolean isValidCVV(String cvv) {
        return cvv.matches("\\d{3,4}");
    }

    public String getCardNumber() {
        return maskCardNumber(cardNumber);
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    private static String maskCardNumber(String cardNumber) {
        int length = cardNumber.length();
        return "*".repeat(length - 4) + cardNumber.substring(length - 4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditCard that = (CreditCard) o;
        return cardNumber.equals(that.cardNumber) &&
               cardHolder.equals(that.cardHolder) &&
               expiryDate.equals(that.expiryDate) &&
               cvv.equals(that.cvv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, cardHolder, expiryDate, cvv);
    }

    @Override
    public String toString() {
        return "CreditCard{" + "cardHolder='" + cardHolder + '\'' + ", masked=" + maskCardNumber(cardNumber) + '}';
    }
}
