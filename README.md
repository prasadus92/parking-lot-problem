#Problem Statement (Parking Lot)

I own a multi­storey parking lot that can hold up to 'n' cars at any given point in time. Each slot is given a number starting at 1 increasing with increasing distance from the entry point in steps of one. I want to create an automated ticketing system that allows my customers to use my parking lot without human intervention. 

When a car enters my parking lot, I want to have a ticket issued to the driver. The ticket issuing process includes us documenting the registration number (number plate) and the colour of the car and allocating an available parking slot to the car before actually handing over a ticket to the driver (we assume that our customers are nice enough to always park in the slots allocated to them). The customer should be allocated a parking slot which is nearest to the entry. At the exit the customer returns the ticket which then marks the slot they were using as being available.

Due to government regulation, the system should provide me with the ability to find out:

a) Registration numbers of all cars of a particular colour.
b) Slot number in which a car with a given registration number is parked.
c) Slot numbers of all slots where a car of a particular colour is parked.

We interact with the system via a simple set of commands which produce a specific output. Please take a look at the example below, which includes all the commands you need to support - they're self explanatory. The system should allow input in two ways. Just to clarify, the same codebase should support both modes of input - we don't want two distinct submissions.

1) It should provide us with an interactive command prompt based shell where commands can be typed in
2) It should accept a filename as a parameter at the command prompt and read the commands from that file


#Output Format

###Example: File Input

To run the program:

$ my_program file_inputs.txt > output.txt

Input (in file):

```sh
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

Output (to console, newline after every output):

```sh
Created a parking lot with 6 slots

Allocated slot number: 1

Allocated slot number: 2

Allocated slot number: 3

Allocated slot number: 4

Allocated slot number: 5

Allocated slot number: 6

Slot number 4 is free

Slot No. Registration No Colour

1 KA-01-HH-1234 White

2 KA-01-HH-9999 White

3 KA-01-BB-0001 Black

5 KA-01-HH-2701 Blue

6 KA-01-HH-3141 Black

Allocated slot number: 4

Sorry, parking lot is full

KA-01-HH-1234, KA-01-HH-9999, KA-01-P-333

1, 2, 4

6

Not found
```

###Example: Interactive

To run the program and launch the shell:

$ my_program

Assuming a parking lot with 6 slots, the following commands should be run in sequence by

typing them in at a prompt and should produce output as described below the command:

Input:

```sh
create_parking_lot 6
```

Output:

```sh
Created a parking lot with 6 slots
```

Input:

```sh
park KA-01-HH-1234 White
```

Output:

```sh
Allocated slot number: 1
```

Input:

```sh
park KA-01-HH-9999 White
```

Output:

```sh
Allocated slot number: 2
```

Input:

```sh
park KA-01-BB-0001 Black
```

Output:

```sh
Allocated slot number: 3
```

Input:

```sh
park KA-01-HH-7777 Red
```

Output:

```sh
Allocated slot number: 4
```

Input:

```sh
park KA-01-HH-2701 Blue
```

Output:

```sh
Allocated slot number: 5
```

Input:

```sh
park KA-01-HH-3141 Black
```

Output:

```sh
Allocated slot number: 6
```

Input:

```sh
leave 4
```

Output:

```sh
Slot number 4 is free
```

Input:

```sh
status
```

Output:

```sh
Slot No. Registration No Color

1 KA-01-HH-1234 White

2 KA-01-HH-9999 White

3 KA-01-BB-0001 Black

5 KA-01-HH-2701 Blue

6 KA-01-HH-3141 Black
```

Input:

```sh
park KA­01­P­333 White
```

Output:

```sh
Allocated slot number: 4
```

Input:

```sh
park DL­12­AA­9999 White
```

Output:

```sh
Sorry, parking lot is full
```

Input:

```sh
registration_numbers_for_cars_with_colour White
```

Output:

```sh
KA-01-HH-1234, KA-01-HH-9999, KA-01-P-333
```

Input:

```sh
slot_numbers_for_cars_with_colour White
```

Output:

```sh
1, 2, 4
```

Input:

```sh
slot_number_for_registration_number KA-01-HH-3141
```

Output:

```sh
6
```

Input:

```sh
slot_number_for_registration_number MH-04-AY-1111
```

Output:

```sh
Not found
```


#Project Requirements

1. Latest version of JDK.
2. Latest version of maven.


#Build Instructions

Run the following command -
```sh
mvn clean install
```


#Running the project

From the project directory, run this command - 

```java
   $ java -jar target/gojek-1.0.jar (Interactive command-line mode)
   $ java -jar target/gojek-1.0.jar <inputfile> (File input)
```
   

