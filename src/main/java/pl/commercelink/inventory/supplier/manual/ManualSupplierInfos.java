package pl.commercelink.inventory.supplier.manual;

import pl.commercelink.inventory.supplier.api.ShippingCostPolicy;
import pl.commercelink.inventory.supplier.api.ShippingPolicy;
import pl.commercelink.inventory.supplier.api.ShippingTerms;
import pl.commercelink.inventory.supplier.api.SupplierInfo;
import pl.commercelink.inventory.supplier.api.SupplierType;

public final class ManualSupplierInfos {

    public static final int ACCURACY_SCORE = 5;
    public static final int DEFAULT_LEAD_TIME_DAYS = 2;

    private ManualSupplierInfos() {
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
