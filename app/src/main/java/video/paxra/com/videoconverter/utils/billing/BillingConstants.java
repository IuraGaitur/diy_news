package video.paxra.com.videoconverter.utils.billing;

import com.android.billingclient.api.BillingClient;

import java.util.Arrays;
import java.util.List;

public final class BillingConstants {
    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
    public static final String SKU_PREMIUM = "premium";



    private static final String[] IN_APP_SKUS = {SKU_PREMIUM};

    private BillingConstants(){}

    /**
     * Returns the list of all SKUs for the billing type specified
     */
    public static final List<String> getSkuList() {
        return Arrays.asList(IN_APP_SKUS);
    }
}
