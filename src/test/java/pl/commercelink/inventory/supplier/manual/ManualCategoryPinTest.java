package pl.commercelink.inventory.supplier.manual;

import org.junit.jupiter.api.Test;
import pl.commercelink.inventory.supplier.api.ParsedRow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManualCategoryPinTest {

    private final ManualCsvRowParser parser =
            new ManualCsvRowParser(ManualSupplierInfos.identityFor("Hurtownia A"));

    private String categoryOf(String rawCategory) {
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Produkt", rawCategory, "10,00", "PLN", "1", "2"};
        return parser.parse(row).taxonomy().category().toString();
    }

    @Test
    void mapsKnownCategoryNameToCanonicalName() {
        assertEquals("CPU", categoryOf("CPU"));
        assertEquals("GPU", categoryOf("GPU"));
        assertEquals("Fan", categoryOf("Fan"));
        assertEquals("Mice", categoryOf("Mice"));
        assertEquals("Footrests", categoryOf("Footrests"));
    }

    @Test
    void mapsLowercaseInputToCanonicalName() {
        assertEquals("CPU", categoryOf("cpu"));
        assertEquals("ModdingPC", categoryOf("moddingpc"));
        assertEquals("KeyboardsAndMice", categoryOf("keyboardsandmice"));
        assertEquals("Footrests", categoryOf("footrests"));
    }

    @Test
    void mapsUppercaseInputToCanonicalName() {
        assertEquals("Mice", categoryOf("MICE"));
        assertEquals("Printers3D", categoryOf("PRINTERS3D"));
        assertEquals("Footrests", categoryOf("FOOTRESTS"));
    }

    @Test
    void mapsMixedCaseInputToCanonicalName() {
        assertEquals("GamingChairs", categoryOf("gAmInGcHaIrS"));
    }

    @Test
    void unknownCategoryFallsBackToOtherAndIsNotProcessable() {
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Produkt", "Zonk", "10,00", "PLN", "1", "2"};
        ParsedRow parsed = parser.parse(row);
        assertEquals("Other", parsed.taxonomy().category().toString());
        assertFalse(parsed.taxonomy().isProcessable());
    }

    @Test
    void parsesFullRow() {
        String[] row = {"5901234123457", "MFN-9", "BrandY", "Karta graficzna", "GPU", "1999,99", "PLN", "4", "5"};
        ParsedRow parsed = parser.parse(row);
        assertEquals("5901234123457", parsed.item().ean());
        assertEquals("MFN-9", parsed.item().mfn());
        assertEquals(1999.99, parsed.item().netPrice());
        assertEquals("PLN", parsed.item().currency());
        assertEquals(4, parsed.item().qty());
        assertEquals(5, parsed.item().leadTimeDays());
        assertEquals("manual:Hurtownia A", parsed.item().supplier());
        assertTrue(parsed.item().sellable());
        assertTrue(parsed.item().inStock());
        assertFalse(parsed.item().inDelivery());
        assertEquals("5901234123457", parsed.taxonomy().ean());
        assertEquals("MFN-9", parsed.taxonomy().mfn());
        assertEquals("BrandY", parsed.taxonomy().brand());
        assertEquals("Karta graficzna", parsed.taxonomy().name());
        assertEquals("GPU", parsed.taxonomy().category().toString());
        assertEquals(5, parsed.taxonomy().dataAccuracyScore());
        assertNull(parsed.taxonomy().netWeightInGrams());
        assertNull(parsed.taxonomy().grossWeightInGrams());
        assertTrue(parsed.taxonomy().isProcessable());
    }
}
