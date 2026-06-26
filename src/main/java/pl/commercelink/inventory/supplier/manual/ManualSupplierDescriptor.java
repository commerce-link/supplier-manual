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
        this.identity = ManualSupplierNames.identityFor(label);
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
