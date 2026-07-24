package pl.commercelink.inventory.supplier.manual;

import org.junit.jupiter.api.Test;
import pl.commercelink.inventory.supplier.api.ParsedRow;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManualCsvRowParserTest {

    private final ManualCsvRowParser parser =
            new ManualCsvRowParser(ManualSupplierInfos.identityFor("Hurtownia A"));

    @Test
    void parsesFullRowAndPassesEnteredCategoryAsRawCategory() {
        // given
        String[] row = {"5901234123457", "MFN-9", "BrandY", "Karta graficzna", "GPU", "1999,99", "PLN", "4", "5"};

        // when
        ParsedRow parsed = parser.parse(row);

        // then
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
        assertEquals("BrandY", parsed.product().brand());
        assertEquals("Karta graficzna", parsed.product().name());
        assertEquals("GPU", parsed.product().rawCategory());
        assertEquals(5, parsed.product().dataAccuracyScore());
        assertNull(parsed.product().netWeightInGrams());
        assertNull(parsed.product().grossWeightInGrams());
    }

    @Test
    void arbitraryCategoryTextIsPassedVerbatimAsRawCategory() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "mysz gamingowa RGB", "10,00", "PLN", "1", "2"};

        // when
        ParsedRow parsed = parser.parse(row);

        // then
        assertEquals("mysz gamingowa RGB", parsed.product().rawCategory());
    }

    @Test
    void enteredCategoryIsPassedVerbatimIncludingServicesMarker() {
        // given
        String[] lower = {"5901234123457", "MFN-1", "BrandX", "Montaż PC", "services", "50,00", "PLN", "1", "2"};
        String[] upper = {"5901234123457", "MFN-2", "BrandX", "Serwis laptopa", "SERVICES", "80,00", "PLN", "1", "2"};

        // when
        ParsedRow parsedLower = parser.parse(lower);
        ParsedRow parsedUpper = parser.parse(upper);

        // then
        assertEquals("services", parsedLower.product().rawCategory());
        assertEquals("SERVICES", parsedUpper.product().rawCategory());
    }

    @Test
    void blankCategoryYieldsNullRawCategory() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "", "10,00", "PLN", "1", "2"};

        // when
        ParsedRow parsed = parser.parse(row);

        // then
        assertNull(parsed.product().rawCategory());
    }

    @Test
    void parsesCommaDecimalPrice() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "Mice", "12,50", "PLN", "7", "3"};

        // when
        ParsedRow parsed = parser.parse(row);

        // then
        assertEquals(12.50, parsed.item().netPrice());
        assertEquals(7, parsed.item().qty());
        assertEquals(3, parsed.item().leadTimeDays());
    }

    @Test
    void zeroQuantityIsNotInStock() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "Mice", "10,00", "PLN", "0", "2"};

        // when
        ParsedRow parsed = parser.parse(row);

        // then
        assertFalse(parsed.item().inStock());
    }

    @Test
    void nonNumericPriceThrowsInParseButTryParseSkipsIt() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "Mice", "abc", "PLN", "1", "2"};

        // when / then
        assertThrows(NumberFormatException.class, () -> parser.parse(row));
        assertTrue(parser.tryParse(row).isEmpty());
    }

    @Test
    void nonNumericQuantityThrowsInParseButTryParseSkipsIt() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "Mice", "10,00", "PLN", "x", "2"};

        // when / then
        assertThrows(NumberFormatException.class, () -> parser.parse(row));
        assertTrue(parser.tryParse(row).isEmpty());
    }

    @Test
    void shortRowUsesDefaultsForMissingQuantityAndLeadTime() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "Mice", "10,00"};

        // when
        ParsedRow parsed = parser.parse(row);

        // then
        assertEquals(0, parsed.item().qty());
        assertEquals(2, parsed.item().leadTimeDays());
    }

    @Test
    void rowMissingBothIdentifiersIsSkipped() {
        // given
        String[] row = {"", "", "BrandX", "Mysz", "Mice", "10,00", "PLN", "1", "2"};

        // when
        Optional<ParsedRow> parsed = parser.tryParse(row);

        // then
        assertTrue(parsed.isEmpty());
    }

    @Test
    void rowMissingNameOrPriceIsSkipped() {
        // given
        String[] noName = {"5901234123457", "MFN-1", "BrandX", "", "Mice", "10,00", "PLN", "1", "2"};
        String[] noPrice = {"5901234123457", "MFN-1", "BrandX", "Mysz", "Mice", "", "PLN", "1", "2"};

        // when / then
        assertTrue(parser.tryParse(noName).isEmpty());
        assertTrue(parser.tryParse(noPrice).isEmpty());
    }
}
