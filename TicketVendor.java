package CLI;

public class TicketVendor implements Runnable {
    private final int vendorId;
    private final int releaseInterval;
    private final TicketPool sharedTicketPool;

    public TicketVendor(int vendorId, int releaseInterval, TicketPool sharedTicketPool) {
        this.vendorId = vendorId;
        this.releaseInterval = releaseInterval;
        this.sharedTicketPool = sharedTicketPool;
    }

    @Override
    public void run() {
        while (true) {
            if (sharedTicketPool.getTotalTickets() - sharedTicketPool.getTicketsAdded() <= 0) {
                break; // Stop processing if all tickets have been added
            }

            try {
                Thread.sleep((long) (releaseInterval * 1000));

                int ticketsToRelease = (int) (Math.random() * 6) + 1;
                // Ensure you do not add more tickets than remaining
                if (sharedTicketPool.getTotalTickets() - sharedTicketPool.getTicketsAdded() > 0) {
                    sharedTicketPool.addTickets(ticketsToRelease, vendorId);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "TicketVendor{" +
                "\n\tvendorId=" + vendorId +
                ",\n\treleaseInterval=" + releaseInterval +
                "\n}";
    }
}