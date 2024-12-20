package CLI;

public class TicketBuyer implements Runnable {
    private final int buyerId;
    private final int retrievalInterval;
    private final TicketPool sharedTicketPool;

    public TicketBuyer(int buyerId, int retrievalInterval, TicketPool sharedTicketPool) {
        this.buyerId = buyerId;
        this.retrievalInterval = retrievalInterval;
        this.sharedTicketPool = sharedTicketPool;
    }

    @Override
    public void run() {
        while (true) {
            if (sharedTicketPool.getTicketsSold() >= sharedTicketPool.getTotalTickets()) {
                break; // Exit if all tickets are sold
            }
            try {
                Thread.sleep((long) (retrievalInterval * 1000));

                if (sharedTicketPool.getTicketsSold() >= sharedTicketPool.getTotalTickets()) {
                    break;
                }

                int ticketsToRetrieve = (int) (Math.random() * 5) + 1;
                sharedTicketPool.removeTickets(ticketsToRetrieve, buyerId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "TicketBuyer{" +
                "\n\tbuyerId=" + buyerId +
                ",\n\tretrievalInterval=" + retrievalInterval +
                "\n}";
    }
}