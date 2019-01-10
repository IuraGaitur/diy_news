package video.paxra.com.videoconverter.utils.billing;

public interface BillingProvider {
    BillingManager getBillingManager();
    boolean isPremiumPurchased();
}