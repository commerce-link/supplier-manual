package pl.commercelink.inventory.supplier.manual;

import org.junit.jupiter.api.Test;
import pl.commercelink.inventory.supplier.api.SupplierInfo;
import pl.commercelink.inventory.supplier.api.SupplierType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManualSupplierInfosTest {

    @Test
    void buildsLocalDistributorInfoKeyedByIdentity() {
        // given
        String identity = ManualSupplierNames.identityFor("Hurtownia A");

        // when
        SupplierInfo info = ManualSupplierInfos.forIdentity(identity);

        // then
        assertEquals(identity, info.name());
        assertEquals(SupplierType.Distributor, info.type());
        assertEquals("PL", info.origin());
        assertTrue(info.isLocalFor("PL"));
        assertEquals(2, info.shippingTermsFor("PL").arrivalDays());
    }
}
