package CLI;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TicketingApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static ConfigSettings configSettings;
    private static boolean isConfigured = false;
    private static boolean isSaved = false;
    private static final String CONFIG_FILE = "ConfigSettings.json";

    public static void main(String[] args) {
        System.out.println("\n\n");
        System.out.println("-".repeat(70));
        System.out.println(" \t\t\tWelcome to the LiveEventTicketing management ");
        System.out.println("-".repeat(70));
        System.out.println("\n\n");

        while (true) {
            System.out.println("\t\t✦\t1. Configure System");
            System.out.println("\t\t✦\t2. Save Configuration");
            System.out.println("\t\t✦\t3. Load Configuration");
            System.out.println("\t\t✦\t4. Start Program");
            System.out.println("\t\t✦\t5. Exit\n\n");
            System.out.print("\t\tSelect an option (1-5): ");

            try {
                int selection = scanner.nextInt();
                switch (selection) {
                    case 1 -> configureSystem();
                    case 2 -> saveConfiguration();
                    case 3 -> loadConfiguration();
                    case 4 -> {
                        startProgram();
                        return; // Exit after starting the program
                    }
                    case 5 -> {
                        System.out.println("\n\t\t\t Program terminated.! \n");
                        return;
                    }
                    default -> System.out.println("\n\t\tInvalid selection. Try again.\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("\n\t\tInvalid input. Please enter a positive number.\n");
                scanner.nextLine();
            }
        }
    }

    private static void configureSystem() {
        int totalTickets = promptForPositiveInt("\n\tEnter total tickets: ");
        int maxTicketCapacity = promptForPositiveInt("\tEnter maximum ticket capacity: ");
        int ticketReleaseRate = promptForPositiveInt("\tEnter ticket release rate (in seconds): ");
        int customerRetrievalRate = promptForPositiveInt("\tEnter customer retrieval rate (in seconds): ");

        configSettings = new ConfigSettings();
        configSettings.setConfig(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);

        isConfigured = true;
        isSaved = false;
        System.out.println("\n\tSystem configured successfully!\n");
    }

    private static void saveConfiguration() {
        if (!isConfigured) {
            System.out.println("\n\tError: Please configure the system first before saving.\n");
            return;
        }

        configSettings.saveToFile(CONFIG_FILE);
        isSaved = true;
    }

    private static void loadConfiguration() {
        if (!isSaved) {
            System.out.println("\n\tError: No saved configuration to load. Please save the configuration first.\n");
            return;
        }

        configSettings = ConfigSettings.loadFromFile(CONFIG_FILE);
        if (configSettings != null) {
            System.out.println("\n\tConfiguration loaded successfully:");
            System.out.println(configSettings);
            isConfigured = true;
        }
    }

    private static void startProgram() {
        if (!isConfigured || configSettings.getMaxTicketCapacity() <= 0 || configSettings.getTotalTickets() <= 0 || configSettings.getTicketReleaseRate() <= 0 || configSettings.getCustomerRetrievalRate() <= 0) {
            System.out.println("\n\tError: Please configure the system correctly before starting.\n");
            return;
        }

        TicketPool ticketPool = new TicketPool(configSettings.getMaxTicketCapacity(), configSettings.getTotalTickets());

        // Start Vendor Threads
        Thread[] vendorThreads = new Thread[10];
        for (int i = 1; i <= 10; i++) {
            vendorThreads[i - 1] = new Thread(new TicketVendor(i, configSettings.getTicketReleaseRate(), ticketPool), "Vendor-" + i);
            vendorThreads[i - 1].start();
        }

        // Start Customer Threads
        Thread[] customerThreads = new Thread[10];
        for (int i = 1; i <= 10; i++) {
            customerThreads[i - 1] = new Thread(new TicketBuyer(i, configSettings.getCustomerRetrievalRate(), ticketPool), "Customer-" + i);
            customerThreads[i - 1].start();
        }

        // Let the threads execute and automatically terminate when all tickets are sold
    }

    private static int promptForPositiveInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                int input = scanner.nextInt();
                if (input <= 0) {
                    System.out.println("\n\tError: Please enter a positive number.");
                } else {
                    return input;
                }
            } catch (InputMismatchException e) {
                System.out.println("\n\tInvalid input. Please enter a positive number.");
                scanner.nextLine();
            }
        }
    }
}