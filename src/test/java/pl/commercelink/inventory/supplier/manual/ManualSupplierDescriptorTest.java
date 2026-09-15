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
    void forIdentityKeepsBothStoredShapesAsIs() {
        // given / when / then
        for (String identity : new String[]{"manual:Asus", "manual-k7f3a9c2"}) {
            ManualSupplierDescriptor fromIdentity = ManualSupplierDescriptor.forIdentity(identity);
            assertEquals(identity, fromIdentity.name());
            assertEquals(identity, fromIdentity.supplierInfo().name());
        }
    }

    @Test
    void forIdentityStampsParsedRowsWithTheIdentity() {
        // given
        ManualSupplierDescriptor tokened = ManualSupplierDescriptor.forIdentity("manual-k7f3a9c2");

        // when
        FeedFormat.Csv csv = assertInstanceOf(FeedFormat.Csv.class, tokened.feedFormat());
        var row = csv.parser().tryParse(new String[]{
                "4711111111111", "MFN-1", "Brand", "Name", "Cat", "10.0", "PLN", "3", "2"}).orElseThrow();

        // then
        assertEquals("manual-k7f3a9c2", row.item().supplier());
    }

    @Test
    void providerDoesNotDownloadAnything() throws Exception {
        // when
        Optional<?> downloaded = descriptor.create(Map.of()).download();

        // then
        assertTrue(downloaded.isEmpty());
    }
}
