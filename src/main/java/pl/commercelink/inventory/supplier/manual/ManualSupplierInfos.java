package pl.commercelink.inventory.supplier.manual;

import pl.commercelink.inventory.supplier.api.ShippingCostPolicy;
import pl.commercelink.inventory.supplier.api.ShippingPolicy;
import pl.commercelink.inventory.supplier.api.ShippingTerms;
import pl.commercelink.inventory.supplier.api.SupplierInfo;
import pl.commercelink.inventory.supplier.api.SupplierType;

public final class ManualSupplierInfos {

    public static final String PREFIX = "manual:";
    public static final int ACCURACY_SCORE = 5;
    public static final int DEFAULT_LEAD_TIME_DAYS = 2;

    private ManualSupplierInfos() {
    }

    public static boolean isManual(String name) {
        return name != null && name.startsWith(PREFIX);
    }

    public static String identityFor(String label) {
        return PREFIX + label;
    }

    public static String label(String identity) {
        return isManual(identity) ? identity.substring(PREFIX.length()) : identity;
    }

    public static SupplierInfo forIdentity(String identity) {
        return new SupplierInfo(
                identity,
                SupplierType.Distributor,
                ACCURACY_SCORE,
                "PL",
                new ShippingPolicy(new ShippingTerms(DEFAULT_LEAD_TIME_DAYS, new ShippingCostPolicy.Free())));
    }
}
