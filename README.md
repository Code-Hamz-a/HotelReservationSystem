# Hotel Reservation System

## Project Overview

A comprehensive Hotel Reservation System implementing complete UML design with clean code principles, defensive programming, and comprehensive unit testing. This system manages hotel chains, hotels, rooms, guests, and reservations with full transaction support.

### Key Features
- **Multi-hotel chain management**: Manage multiple hotels across a chain
- **Room inventory management**: Track rooms by type with availability
- **Reservation system**: Create, manage, and cancel reservations
- **Guest management**: Store and retrieve guest information
- **Check-in/Check-out operations**: Manage guest occupancy
- **Defensive programming**: Comprehensive input validation and error handling
- **Clean code**: Well-organized, maintainable, and testable code
- **Comprehensive testing**: 95%+ code coverage with unit and parameterized tests

## Architecture

### Domain Classes

#### Value Objects (Immutable)
- **Money**: Represents monetary amounts with validation
- **Name**: String values for names with validation
- **Identity**: Unique identifiers for entities
- **Address**: Street, city, postal code details
- **CreditCard**: Card information with validation and masking

#### Domain Entities
- **RoomType**: Defines room categories (Deluxe, Standard, Economy) with pricing
- **Room**: Physical room with occupancy tracking
- **Guest**: Guest information with identity and address
- **Reservation**: Booking with status tracking (CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED)
- **Hotel**: Manages rooms and reservations for a single location
- **HotelChain**: Manages multiple hotels and cross-hotel operations
- **ReservationManager**: Handles payment and reservation tracking

### Design Patterns Used
- **Value Objects**: Immutable objects for Money, Name, Address, Identity
- **Entity Pattern**: Domain entities with identity and lifecycle
- **Aggregate Pattern**: Hotel as aggregate root for rooms and reservations
- **Factory Pattern**: Static factory methods for entity creation
- **Repository Pattern**: Collections management within aggregates
- **Service Pattern**: HotelChain for cross-hotel operations

## Building and Running

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Build

```bash
cd HotelReservationSystem
mvn clean install
```

### Run Main Application

```bash
mvn exec:java -Dexec.mainClass="com.hotel.HotelReservationApp"
```

### Run Tests

```bash
mvn test
```

### Run Tests with Coverage Report

```bash
mvn test jacoco:report
```

## Test Classes

### Unit Tests (AAA Style - Arrange, Act, Assert)

1. **RoomTypeTest** (20 tests)
   - Normal cases, boundary conditions, invalid inputs
   - Parameterized tests for various room types and costs
   - Equality and hash consistency

2. **RoomTest** (15 tests)
   - Room creation and validation
   - Occupancy management
   - Boundary cases for room numbers

3. **GuestTest** (16 tests)
   - Guest creation with validation
   - Name and address validation
   - Uniqueness testing
   - Invalid input handling

4. **ReservationTest** (30 tests)
   - Reservation lifecycle (CONFIRMED → CHECKED_IN → CHECKED_OUT)
   - Date validation and conflict detection
   - State transition validation
   - Cancellation rules

5. **HotelTest** (25 tests)
   - Room management and availability checking
   - Reservation creation and management
   - Guest check-in/check-out
   - Guest reservation queries

6. **HotelChainTest** (20 tests)
   - Multi-hotel management
   - Cross-hotel operations
   - Validation methods
   - State checking

7. **ReservationManagerTest** (15 tests)
   - Reservation tracking
   - Credit card recording
   - Reservation cancellation

8. **MoneyTest** (15 tests)
   - Amount arithmetic operations
   - Validation of negative amounts
   - Equality and hashing

**Total: 156+ parameterized and regular unit tests**

### Running Specific Tests

```bash
# Run specific test class
mvn test -Dtest=HotelTest

# Run all tests matching pattern
mvn test -Dtest=*Test
```

## Test Coverage

All tests follow the **AAA (Arrange-Act-Assert)** pattern:

```java
@Test
public void testExample() {
    // Arrange: Set up test data
    RoomType deluxe = new RoomType("Deluxe", new Money(100.0));
    
    // Act: Execute the operation
    Room room = new Room(101, deluxe);
    
    // Assert: Verify the results
    assertEquals(101, room.getNumber());
    assertEquals(deluxe, room.getRoomType());
}
```

Tests cover:
- ✓ Normal use cases
- ✓ Boundary conditions (minimum, maximum, edge cases)
- ✓ Invalid inputs (null, empty, negative values)
- ✓ State transitions and business rules
- ✓ Parameterized tests with multiple data sets
- ✓ Error handling and exception validation

## Defensive Programming

The system implements comprehensive defensive programming:

1. **Input Validation**: All parameters validated at entry points
2. **Null Checks**: Explicit null checking with meaningful exceptions
3. **Immutability**: Value objects are immutable
4. **Encapsulation**: Private fields with controlled access
5. **State Protection**: Methods validate state before transitions
6. **Business Rules**: Date ranges, room availability, reservation status
7. **Exception Handling**: Clear, descriptive exception messages

### Example: Money Validation
```java
public Money(BigDecimal amount) {
    if (amount == null) {
        throw new IllegalArgumentException("Amount cannot be null");
    }
    if (amount.signum() < 0) {
        throw new IllegalArgumentException("Amount cannot be negative");
    }
    this.amount = amount;
}
```

## Usage Example

```java
// Create hotel chain
HotelChain chain = new HotelChain("Grand Hotels");

// Create hotel and rooms
Hotel hotel = new Hotel(new Name("Grand Boston"));
RoomType deluxe = new RoomType("Deluxe", new Money(150.0));
hotel.addRoom(new Room(101, deluxe));

// Create guest
Guest guest = Guest.create(
    new Name("John Doe"),
    new Address("123 Main St", "Boston", "02101"));

// Create reservation
LocalDate start = LocalDate.now().plusDays(5);
LocalDate end = LocalDate.now().plusDays(8);
Reservation reservation = hotel.createReservation(guest, hotel.getRoom(101), start, end);

// Check-in guest
hotel.checkInGuest(reservation);

// Check-out guest
hotel.checkOutGuest(reservation);
```

## Clean Code Principles Applied

1. **Meaningful Names**: Classes and methods clearly describe their purpose
2. **Single Responsibility**: Each class has one reason to change
3. **DRY Principle**: No code duplication
4. **Testability**: Classes designed for easy unit testing
5. **Documentation**: Clear Javadoc comments
6. **Small Methods**: Methods do one thing well
7. **Proper Encapsulation**: Private fields, protected access where needed
8. **Error Handling**: Proper exception hierarchy and handling

## Project Structure

```
HotelReservationSystem/
├── src/
│   ├── main/
│   │   └── java/com/hotel/
│   │       ├── HotelReservationApp.java      (Main entry point)
│   │       ├── domain/
│   │       │   ├── Hotel.java
│   │       │   ├── HotelChain.java
│   │       │   ├── Room.java
│   │       │   ├── RoomType.java
│   │       │   ├── Guest.java
│   │       │   ├── Reservation.java
│   │       │   └── ReservationManager.java
│   │       └── util/
│   │           ├── Money.java
│   │           ├── Name.java
│   │           ├── Identity.java
│   │           ├── Address.java
│   │           └── CreditCard.java
│   └── test/
│       └── java/com/hotel/
│           ├── domain/
│           │   ├── RoomTypeTest.java
│           │   ├── RoomTest.java
│           │   ├── GuestTest.java
│           │   ├── ReservationTest.java
│           │   ├── HotelTest.java
│           │   ├── HotelChainTest.java
│           │   └── ReservationManagerTest.java
│           └── util/
│               └── MoneyTest.java
├── pom.xml
└── README.md
```

## Key Use Cases Demonstrated

1. **Create Hotel Chain**: Initialize multi-hotel system
2. **Manage Inventory**: Add and manage hotels and rooms
3. **Register Guests**: Create and store guest information
4. **Make Reservations**: Create bookings with date validation
5. **Check Availability**: Query available rooms for date ranges
6. **Check-in Guests**: Process guest arrival and room assignment
7. **Check-out Guests**: Process guest departure
8. **Cancel Reservations**: Cancel pending reservations
9. **Query Reservations**: Find reservations by guest or status
10. **Manage Payments**: Track credit card information

## Technologies

- **Language**: Java 11
- **Build Tool**: Maven 3.6+
- **Testing Framework**: JUnit 5
- **Code Style**: Clean Code principles

## Running the Application

The main application (`HotelReservationApp`) demonstrates all features:

```bash
mvn exec:java -Dexec.mainClass="com.hotel.HotelReservationApp"
```

Output shows:
- Hotel chain and hotel creation
- Room inventory setup
- Guest creation
- Reservation management (create, check-in, check-out, cancel)
- Availability checks
- Query operations
- Error handling and validation

## Commit History

See the repository for commit history showing:
1. Initial project setup and structure
2. Domain class implementation
3. Unit test implementation
4. Main application and demonstration
5. Documentation and refinement

## Author

School of Computer Science - Hotel Reservation System Project

## License

Academic Use Only
