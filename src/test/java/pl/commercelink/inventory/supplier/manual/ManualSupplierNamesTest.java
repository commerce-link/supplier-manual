package pl.commercelink.inventory.supplier.manual;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManualSupplierNamesTest {

    @Test
    void identityForPrependsPrefix() {
        // given
        String label = "Hurtownia Kowalski";

        // when
        String identity = ManualSupplierNames.identityFor(label);

        // then
        assertEquals("manual:Hurtownia Kowalski", identity);
    }

    @Test
    void isManualRecognizesPrefixedNames() {
        // when / then
        assertTrue(ManualSupplierNames.isManual("manual:Hurtownia Kowalski"));
        assertFalse(ManualSupplierNames.isManual("Acme"));
        assertFalse(ManualSupplierNames.isManual(null));
    }

    @Test
    void labelStripsPrefix() {
        // when / then
        assertEquals("Hurtownia Kowalski", ManualSupplierNames.label("manual:Hurtownia Kowalski"));
        assertEquals("Acme", ManualSupplierNames.label("Acme"));
    }
}
