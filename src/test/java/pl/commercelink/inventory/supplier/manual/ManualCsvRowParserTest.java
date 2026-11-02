package pl.commercelink.inventory.supplier.manual;

import org.junit.jupiter.api.Test;
import pl.commercelink.inventory.supplier.api.FeedItem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManualCsvRowParserTest {

    private final ManualCsvRowParser parser = new ManualCsvRowParser();

    @Test
    void parsesFullRowAndPassesEnteredCategory() {
        // given
        String[] row = {"5901234123457", "MFN-9", "BrandY", "Karta graficzna", "GPU", "1999,99", "PLN", "4", "5"};

        // when
        FeedItem item = parser.parse(row);

        // then
        assertEquals("5901234123457", item.getEan());
        assertEquals("MFN-9", item.getMfn());
        assertEquals("BrandY", item.getBrand());
        assertEquals("Karta graficzna", item.getName());
        assertEquals("GPU", item.getCategory());
        assertEquals(1999.99, item.getNetPrice());
        assertEquals("PLN", item.getCurrency());
        assertEquals(4, item.getQty());
        assertEquals(5, item.getLeadTimeDays());
        assertTrue(item.isSellable());
        assertTrue(item.isInStock());
        assertFalse(item.isInDelivery());
        assertNull(item.getSku());
        assertNull(item.getNetWeightInGrams());
        assertNull(item.getGrossWeightInGrams());
    }

    @Test
    void arbitraryCategoryTextIsPassedVerbatim() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "mysz gamingowa RGB", "10,00", "PLN", "1", "2"};

        // when
        FeedItem item = parser.parse(row);

        // then
        assertEquals("mysz gamingowa RGB", item.getCategory());
    }

    @Test
    void enteredCategoryIsPassedVerbatimIncludingServicesMarker() {
        // given
        String[] lower = {"5901234123457", "MFN-1", "BrandX", "Montaż PC", "services", "50,00", "PLN", "1", "2"};
        String[] upper = {"5901234123457", "MFN-2", "BrandX", "Serwis laptopa", "SERVICES", "80,00", "PLN", "1", "2"};

        // when
        FeedItem parsedLower = parser.parse(lower);
        FeedItem parsedUpper = parser.parse(upper);

        // then
        assertEquals("services", parsedLower.getCategory());
        assertEquals("SERVICES", parsedUpper.getCategory());
    }

    @Test
    void blankCategoryYieldsNullCategory() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "", "10,00", "PLN", "1", "2"};

        // when
        FeedItem item = parser.parse(row);

        // then
        assertNull(item.getCategory());
    }

    @Test
    void parsesCommaDecimalPrice() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "Mice", "12,50", "PLN", "7", "3"};

        // when
        FeedItem item = parser.parse(row);

        // then
        assertEquals(12.50, item.getNetPrice());
        assertEquals(7, item.getQty());
        assertEquals(3, item.getLeadTimeDays());
    }

    @Test
    void zeroQuantityIsNotInStock() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "Mice", "10,00", "PLN", "0", "2"};

        // when
        FeedItem item = parser.parse(row);

        // then
        assertFalse(item.isInStock());
    }

    @Test
    void nonNumericPriceThrowsInParse() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "Mice", "abc", "PLN", "1", "2"};

        // when / then
        assertThrows(NumberFormatException.class, () -> parser.parse(row));
    }

    @Test
    void nonNumericQuantityThrowsInParse() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "Mice", "10,00", "PLN", "x", "2"};

        // when / then
        assertThrows(NumberFormatException.class, () -> parser.parse(row));
    }

    @Test
    void shortRowUsesDefaultsForMissingQuantityAndLeadTime() {
        // given
        String[] row = {"5901234123457", "MFN-1", "BrandX", "Mysz", "Mice", "10,00"};

        // when
        FeedItem item = parser.parse(row);

        // then
        assertEquals(0, item.getQty());
        assertEquals(2, item.getLeadTimeDays());
    }

    @Test
    void rowMissingBothIdentifiersIsSkipped() {
        // given
        String[] row = {"", "", "BrandX", "Mysz", "Mice", "10,00", "PLN", "1", "2"};

        // when / then
        assertNull(parser.parse(row));
    }

    @Test
    void rowMissingNameOrPriceIsSkipped() {
        // given
        String[] noName = {"5901234123457", "MFN-1", "BrandX", "", "Mice", "10,00", "PLN", "1", "2"};
        String[] noPrice = {"5901234123457", "MFN-1", "BrandX", "Mysz", "Mice", "", "PLN", "1", "2"};

        // when / then
        assertNull(parser.parse(noName));
        assertNull(parser.parse(noPrice));
    }
}
