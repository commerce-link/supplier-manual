package pl.commercelink.inventory.supplier.manual;

import pl.commercelink.inventory.supplier.api.CsvRowParser;
import pl.commercelink.inventory.supplier.api.InventoryItem;
import pl.commercelink.inventory.supplier.api.ParsedRow;
import pl.commercelink.inventory.supplier.api.Taxonomy;
import pl.commercelink.taxonomy.ProductCategory;

import java.util.Arrays;

public class ManualCsvRowParser implements CsvRowParser {

    private static final int EAN = 0;
    private static final int MFN = 1;
    private static final int BRAND = 2;
    private static final int NAME = 3;
    private static final int CATEGORY = 4;
    private static final int NET_PRICE = 5;
    private static final int CURRENCY = 6;
    private static final int QTY = 7;
    private static final int LEAD_TIME = 8;

    private final String supplierIdentity;

    public ManualCsvRowParser(String supplierIdentity) {
        this.supplierIdentity = supplierIdentity;
    }

    @Override
    public ParsedRow parse(String[] row) {
        String ean = at(row, EAN);
        String mfn = at(row, MFN);
        String name = at(row, NAME);
        String priceText = at(row, NET_PRICE);
        if ((ean.isEmpty() && mfn.isEmpty()) || name.isEmpty() || priceText.isEmpty()) {
            return null;
        }

        double netPrice = Double.parseDouble(priceText.replace(',', '.'));
        String currency = at(row, CURRENCY);
        int qty = parseInt(at(row, QTY), 0);
        int leadTimeDays = parseInt(at(row, LEAD_TIME), ManualSupplierInfos.DEFAULT_LEAD_TIME_DAYS);
        ProductCategory category = category(at(row, CATEGORY));

        InventoryItem item = new InventoryItem(
                ean, mfn, netPrice, currency, qty, leadTimeDays,
                supplierIdentity, true, qty > 0, false);
        Taxonomy taxonomy = new Taxonomy(
                ean, mfn, at(row, BRAND), name, category,
                ManualSupplierInfos.ACCURACY_SCORE, null, null);
        return new ParsedRow(item, taxonomy);
    }

    private static String at(String[] row, int index) {
        return index < row.length && row[index] != null ? row[index].trim() : "";
    }

    private static int parseInt(String value, int fallback) {
        if (value.isEmpty()) {
            return fallback;
        }
        return Integer.parseInt(value);
    }

    private static ProductCategory category(String raw) {
        return Arrays.stream(ProductCategory.values())
                .filter(c -> c.eq(raw))
                .findFirst()
                .orElse(ProductCategory.Other);
    }
}
