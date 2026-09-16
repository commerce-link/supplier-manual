package pl.commercelink.inventory.supplier.manual;

import pl.commercelink.inventory.supplier.api.FeedFormat;
import pl.commercelink.inventory.supplier.api.SupplierInfo;
import pl.commercelink.inventory.supplier.api.SupplierProvider;
import pl.commercelink.inventory.supplier.api.SupplierProviderDescriptor;

import java.util.Map;
import java.util.Optional;

public class ManualSupplierDescriptor implements SupplierProviderDescriptor {

    private final String identity;

    public ManualSupplierDescriptor(String label) {
        this(label, false);
    }

    private ManualSupplierDescriptor(String value, boolean raw) {
        this.identity = raw ? value : ManualSupplierInfos.identityFor(value);
    }

    /**
     * Builds the descriptor from a stored connection identity taken as-is, whatever its shape
     * (legacy {@code manual:Label} or tokened {@code manual-k7f3a9c2}). The label constructor
     * above can only mint the legacy shape, so callers holding an identity must use this one.
     */
    public static ManualSupplierDescriptor forIdentity(String identity) {
        return new ManualSupplierDescriptor(identity, true);
    }

    @Override
    public SupplierInfo supplierInfo() {
        return ManualSupplierInfos.forIdentity(identity);
    }

    @Override
    public FeedFormat feedFormat() {
        return new FeedFormat.Csv(new ManualCsvRowParser(identity), ';');
    }

    @Override
    public SupplierProvider create(Map<String, String> configuration) {
        return Optional::empty;
    }
}
