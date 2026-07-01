package pl.commercelink.inventory.supplier.manual;

import org.junit.jupiter.api.Test;
import pl.commercelink.inventory.supplier.api.FeedFormat;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManualSupplierDescriptorTest {

    private final ManualSupplierDescriptor descriptor = new ManualSupplierDescriptor("Hurtownia A");

    @Test
    void nameAndInfoUseTheManualIdentity() {
        // when / then
        assertEquals("manual:Hurtownia A", descriptor.name());
        assertEquals("manual:Hurtownia A", descriptor.supplierInfo().name());
    }

    @Test
    void feedFormatIsCanonicalCsv() {
        // when
        FeedFormat format = descriptor.feedFormat();

        // then
        FeedFormat.Csv csv = assertInstanceOf(FeedFormat.Csv.class, format);
        assertEquals(';', csv.separator());
        assertInstanceOf(ManualCsvRowParser.class, csv.parser());
    }

    @Test
    void providerDoesNotDownloadAnything() throws Exception {
        // when
        Optional<?> downloaded = descriptor.create(Map.of()).download();

        // then
        assertTrue(downloaded.isEmpty());
    }
}
