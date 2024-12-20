# LiveEventTicketing Management System

## Overview
The LiveEventTicketing Management System is a Java-based application designed to manage the sale and distribution of tickets for live events. The system allows for configuration, saving, and loading of settings, and simulates the process of ticket vendors adding tickets to a pool and customers purchasing them.

## Features
- Configure system settings such as total tickets, ticket release rate, customer retrieval rate, and maximum ticket capacity.
- Save and load configuration settings using JSON files.
- Simulate ticket vendors adding tickets to a pool.
- Simulate customers purchasing tickets from the pool.
- Thread-safe operations using Java's `ReentrantLock` and `Condition`.

## Project Structure
- `ConfigSettings.java`: Manages configuration settings and handles saving/loading to/from JSON files.
- `TicketingApp.java`: Main application class that provides a CLI for configuring and starting the system.
- `TicketPool.java`: Manages the pool of tickets, ensuring thread-safe operations for adding and removing tickets.
- `TicketVendor.java`: Represents a ticket vendor that adds tickets to the pool at specified intervals.
- `TicketBuyer.java`: Represents a customer that purchases tickets from the pool at specified intervals.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- IntelliJ IDEA or any other Java IDE

### Running the Application
1. Clone the repository:
    ```sh
    git clone https://github.com/real-nizith/LiveEventTicketing.git
    cd LiveEventTicketing
    ```

2. Open the project in your IDE.

3. Run the `TicketingApp` class to start the application.

### Usage
1. Configure the system by entering the required settings.
2. Save the configuration if needed.
3. Load a previously saved configuration if available.
4. Start the program to simulate ticket vendors and customers.

## Example
```sh
--------------------------------------------------------------
                 Welcome to the LiveEventTicketing management 
--------------------------------------------------------------

        ✦    1. Configure System
        ✦    2. Save Configuration
        ✦    3. Load Configuration
        ✦    4. Start Program
        ✦    5. Exit

        Select an option (1-5): 1

    Enter total tickets: 100
    Enter maximum ticket capacity: 50
    Enter ticket release rate (in seconds): 2
    Enter customer retrieval rate (in seconds): 3

    System configured successfully!