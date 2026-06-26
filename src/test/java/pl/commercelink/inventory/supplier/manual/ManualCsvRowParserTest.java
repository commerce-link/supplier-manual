package pl.commercelink.inventory.supplier.manual;

import org.junit.jupiter.api.Test;
import pl.commercelink.inventory.supplier.api.ParsedRow;
import pl.commercelink.taxonomy.ProductCategory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManualCsvRowParserTest {

    private final ManualCsvRowParser parser =
            new ManualCsvRowParser(ManualSupplierNames.identityFor("Hurtownia A"));

    @Test
    void parsesCanonicalRowWithCommaDecimal() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "Mice", "12,50", "PLN", "7", "3"};

        // when
        ParsedRow parsed = parser.parse(row);

        // then
        assertEquals("5901234123457", parsed.item().ean());
        assertEquals(12.50, parsed.item().netPrice());
        assertEquals(7, parsed.item().qty());
        assertEquals(3, parsed.item().leadTimeDays());
        assertEquals("manual:Hurtownia A", parsed.item().supplier());
        assertTrue(parsed.item().sellable());
        assertTrue(parsed.item().inStock());
        assertEquals(ProductCategory.Mice, parsed.taxonomy().category());
        assertEquals("BrandX", parsed.taxonomy().brand());
    }

    @Test
    void unknownCategoryFallsBackToOther() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "Nonexistent", "10,00", "PLN", "1", ""};

        // when
        ParsedRow parsed = parser.parse(row);

        // then
        assertEquals(ProductCategory.Other, parsed.taxonomy().category());
        assertEquals(2, parsed.item().leadTimeDays());
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
