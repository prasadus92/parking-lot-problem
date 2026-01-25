# Parking Lot System

A production-grade, enterprise-level automated parking lot management system built with clean architecture principles.

## Overview

This system manages a multi-storey parking lot that can hold up to 'n' cars at any given time. Each slot is numbered starting at 1, with lower numbers being closer to the entry point. The system automatically allocates the nearest available slot to incoming vehicles and provides powerful querying capabilities.

## Features

- **Automated Slot Allocation**: Vehicles are automatically assigned to the nearest available slot
- **Efficient Queries**: O(1) lookups by registration number, O(k) lookups by color
- **Dual Input Modes**: Interactive command-line shell and file-based batch processing
- **Clean Architecture**: Domain-driven design with clear separation of concerns
- **Comprehensive Testing**: Unit tests, integration tests, and code coverage reporting
- **Type-Safe Commands**: Enum-based command pattern (no reflection)

## Architecture

```
+-------------------------------------------------------------+
|                    Application Layer                         |
|  +------------------+   +------------------+                 |
|  | ParkingLotApp    |   |   InputReader    |                 |
|  +--------+---------+   +--------+---------+                 |
|           |                      |                           |
|  +--------v----------------------v--------+                  |
|  |           Command Layer                |                  |
|  |  +----------+   +------------------+   |                  |
|  |  | Command  |   | CommandExecutor  |   |                  |
|  |  |  (Enum)  |   |                  |   |                  |
|  |  +----------+   +--------+---------+   |                  |
|  +------------------------- | ------------+                  |
|                             |                                |
|  +--------------------------v-------------+                  |
|  |           Service Layer                |                  |
|  |  +-------------------------------+     |                  |
|  |  |     ParkingLotService         |     |                  |
|  |  +---------------+---------------+     |                  |
|  +------------------|---------------------+                  |
|                     |                                        |
|  +------------------v---------------------+                  |
|  |           Domain Layer                 |                  |
|  |  +---------+ +------------+ +-------+  |                  |
|  |  | Vehicle | |ParkingSlot | |  Lot  |  |                  |
|  |  +---------+ +------------+ +-------+  |                  |
|  +----------------------------------------+                  |
|                                                              |
|  +----------------------------------------+                  |
|  |         Exception Layer                |                  |
|  |  Custom exceptions for each error      |                  |
|  +----------------------------------------+                  |
+-------------------------------------------------------------+
```

## Project Structure

```
parking-lot-problem/
├── src/
│   ├── main/java/com/gojek/parkinglot/
│   │   ├── app/                    # Application entry points
│   │   │   └── ParkingLotApplication.java
│   │   ├── command/                # Command pattern implementation
│   │   │   ├── Command.java        # Command enum
│   │   │   ├── CommandExecutor.java
│   │   │   ├── CommandParser.java
│   │   │   └── CommandResult.java
│   │   ├── domain/                 # Domain entities
│   │   │   ├── Vehicle.java
│   │   │   ├── ParkingSlot.java
│   │   │   └── ParkingLot.java
│   │   ├── exception/              # Custom exceptions
│   │   │   ├── ParkingLotException.java
│   │   │   ├── ParkingLotFullException.java
│   │   │   ├── ParkingLotNotCreatedException.java
│   │   │   ├── SlotNotFoundException.java
│   │   │   ├── VehicleNotFoundException.java
│   │   │   └── InvalidCommandException.java
│   │   ├── io/                     # I/O utilities
│   │   │   ├── InputReader.java
│   │   │   └── OutputWriter.java
│   │   └── service/                # Business logic
│   │       └── ParkingLotService.java
│   └── test/java/com/gojek/parkinglot/
│       ├── domain/                 # Domain tests
│       ├── command/                # Command tests
│       ├── service/                # Service tests
│       └── integration/            # Integration tests
├── pom.xml
└── README.md
```

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Build Instructions

```bash
# Clean and build with tests
mvn clean install

# Build without tests
mvn clean install -DskipTests

# Run tests only
mvn test

# Generate code coverage report
mvn test jacoco:report
# Report available at: target/site/jacoco/index.html
```

## Running the Application

### Interactive Mode

```bash
java -jar target/parking-lot-2.0.0.jar
```

### File Input Mode

```bash
java -jar target/parking-lot-2.0.0.jar input_file.txt
```

## Available Commands

| Command | Description | Example |
|---------|-------------|---------|
| `create_parking_lot <n>` | Create a parking lot with n slots | `create_parking_lot 6` |
| `park <reg_no> <color>` | Park a vehicle | `park KA-01-HH-1234 White` |
| `leave <slot>` | Remove vehicle from slot | `leave 4` |
| `status` | Show all parked vehicles | `status` |
| `registration_numbers_for_cars_with_colour <color>` | Find registrations by color | `registration_numbers_for_cars_with_colour White` |
| `slot_numbers_for_cars_with_colour <color>` | Find slots by color | `slot_numbers_for_cars_with_colour White` |
| `slot_number_for_registration_number <reg_no>` | Find slot by registration | `slot_number_for_registration_number KA-01-HH-1234` |
| `help` | Show available commands | `help` |
| `exit` | Exit the application | `exit` |

## Example Session

### Input File

```
create_parking_lot 6
park KA-01-HH-1234 White
park KA-01-HH-9999 White
park KA-01-BB-0001 Black
park KA-01-HH-7777 Red
park KA-01-HH-2701 Blue
park KA-01-HH-3141 Black
leave 4
status
park KA-01-P-333 White
park DL-12-AA-9999 White
registration_numbers_for_cars_with_colour White
slot_numbers_for_cars_with_colour White
slot_number_for_registration_number KA-01-HH-3141
slot_number_for_registration_number MH-04-AY-1111
```

### Output

```
Created a parking lot with 6 slots
Allocated slot number: 1
Allocated slot number: 2
Allocated slot number: 3
Allocated slot number: 4
Allocated slot number: 5
Allocated slot number: 6
Slot number 4 is free
Slot No.    Registration No    Colour
1           KA-01-HH-1234      White
2           KA-01-HH-9999      White
3           KA-01-BB-0001      Black
5           KA-01-HH-2701      Blue
6           KA-01-HH-3141      Black
Allocated slot number: 4
Sorry, parking lot is full
KA-01-HH-1234, KA-01-HH-9999, KA-01-P-333
1, 2, 4
6
Not found
```

## Design Decisions

### Why Min-Heap for Slot Allocation?
We use a `PriorityQueue` (min-heap) for available slots to ensure O(log n) slot allocation. This guarantees the nearest slot is always allocated first, even after vehicles leave.

### Why Enum-Based Command Pattern?
Using enums instead of reflection for command handling provides:
- Compile-time type safety
- Better IDE support (autocomplete, refactoring)
- Clearer code that's easier to debug
- Exhaustive switch statement checking

### Why Immutable Vehicle?
The `Vehicle` class is immutable to ensure:
- Thread safety without synchronization
- Protection against accidental modification
- Clear value object semantics

### Exception Hierarchy
Custom exceptions provide:
- Meaningful error messages
- Easy differentiation between error types
- Support for programmatic error handling

## Performance Characteristics

| Operation | Time Complexity |
|-----------|-----------------|
| Park | O(log n) |
| Leave | O(log n) |
| Find by Registration | O(1) |
| Find by Color | O(k) where k = matching vehicles |
| Status | O(n) |

## Testing

The project includes comprehensive tests:

- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test complete workflows end-to-end
- **Edge Case Coverage**: Invalid inputs, boundary conditions

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ParkingLotDomainTest

# Run with coverage
mvn test jacoco:report
```

## Code Quality

- **Clean Code**: Follows SOLID principles
- **Documentation**: Comprehensive JavaDoc on all public APIs
- **Type Safety**: Strong typing throughout
- **Error Handling**: Custom exceptions with meaningful messages
- **Testability**: Dependency injection for easy testing

## License

This project is available under the MIT License.

## Version History

- **2.0.0**: Complete enterprise-level refactoring
  - Clean architecture implementation
  - Enum-based command pattern
  - Custom exception hierarchy
  - Comprehensive test suite
  - Modern Maven configuration

- **1.0.0**: Initial implementation
