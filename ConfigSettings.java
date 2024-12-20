package CLI;

import com.google.gson.Gson;

import java.io.*;
import java.util.InputMismatchException;

public class ConfigSettings {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    public ConfigSettings() {
    }

    public void setConfig(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    // Save to JSON file using Gson
    public void saveToFile(String configFileName) {
        try (Writer writer = new FileWriter(configFileName)) {
            Gson gson = new Gson();
            gson.toJson(this, writer);  // Serialize the ConfigSettings object into JSON and write to file
            System.out.println("\n\tConfiguration saved to " + configFileName + "\n");
        } catch (IOException e) {
            System.out.println("\n\tError saving configuration.\n");
        }
    }

    // Load from JSON file using Gson
    public static ConfigSettings loadFromFile(String configFileName) {
        try (Reader reader = new FileReader(configFileName)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, ConfigSettings.class);  // Deserialize the JSON file into ConfigSettings object
        } catch (IOException e) {
            System.out.println("\n\tError loading configuration.\n");
            return null;
        }
    }

    @Override
    public String toString() {
        return "\tConfigSettings\n{" +
                "\n\ttotalTickets=" + totalTickets +
                ",\n\tticketReleaseRate=" + ticketReleaseRate +
                ",\n\tcustomerRetrievalRate=" + customerRetrievalRate +
                ",\n\tmaxTicketCapacity=" + maxTicketCapacity +
                "\n}";
    }
}