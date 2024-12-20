package CLI;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class TicketPool {
    private int tickets;
    private final int maxCapacity;
    private final int totalTickets;
    private int ticketsAdded;
    private int ticketsSold;
    private boolean isSoldOut = false;

    // New ReentrantLock and Condition objects
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public TicketPool(int maxCapacity, int totalTickets) {
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
        this.ticketsAdded = 0;
        this.ticketsSold = 0;
        this.tickets = 0;
    }

    public void addTickets(int ticketsToAdd, int vendorId) {
        lock.lock();
        try {
            if (ticketsAdded >= totalTickets) {
                return;
            }

            while (tickets + ticketsToAdd > maxCapacity) {
                try {
                    if (ticketsAdded >= totalTickets) {
                        return;
                    }
                    System.out.println("\n\tInsufficient space for " + ticketsToAdd + " tickets. Vendor-" + vendorId + " waiting.");
                    notFull.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            if (ticketsAdded + ticketsToAdd > totalTickets) {
                ticketsToAdd = totalTickets - ticketsAdded;
            }

            tickets += ticketsToAdd;
            ticketsAdded += ticketsToAdd;

            System.out.println("\n\t" + ticketsToAdd + " tickets added to the pool by Vendor-" + vendorId +
                    ", Total tickets remaining: " + (totalTickets - ticketsAdded));
            System.out.println("\tRemaining tickets in pool: " + tickets);

            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void removeTickets(int ticketsToRemove, int buyerId) {
        lock.lock();
        try {
            if (isSoldOut) {
                return;
            }

            while (tickets < ticketsToRemove) {
                if (ticketsSold >= totalTickets) {
                    markAsSoldOut();
                    return;
                }
                try {
                    System.out.println("\n\tNot enough tickets for Customer-" + buyerId + ". Waiting.");
                    notEmpty.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            tickets -= ticketsToRemove;
            ticketsSold += ticketsToRemove;

            System.out.println("\n\t" + ticketsToRemove + " tickets purchased by Customer-" + buyerId + ", Remaining tickets in pool: " + tickets);
            System.out.println("\n\tTotal tickets sold: " + ticketsSold + "/" + totalTickets);

            if (ticketsSold >= totalTickets) {
                markAsSoldOut();
            }

            notFull.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private void markAsSoldOut() {
        lock.lock();
        try {
            if (!isSoldOut) {
                System.out.println("\n\tAll tickets have been sold. The system is now closed.");
                isSoldOut = true;
            }
        } finally {
            lock.unlock();
        }
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }

    public int getTicketsAdded() {
        return ticketsAdded;
    }
}