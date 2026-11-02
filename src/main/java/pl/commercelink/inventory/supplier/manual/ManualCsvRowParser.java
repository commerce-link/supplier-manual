package pl.commercelink.inventory.supplier.manual;

import pl.commercelink.inventory.supplier.api.CsvRowParser;
import pl.commercelink.inventory.supplier.api.FeedItem;

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

    @Override
    public FeedItem parse(String[] row) {
        String ean = at(row, EAN);
        String mfn = at(row, MFN);
        String name = at(row, NAME);
        String priceText = at(row, NET_PRICE);
        if ((ean.isEmpty() && mfn.isEmpty()) || name.isEmpty() || priceText.isEmpty()) {
            return null;
        }

        double netPrice = Double.parseDouble(priceText.replace(',', '.'));
        int qty = parseInt(at(row, QTY), 0);
        int leadTimeDays = parseInt(at(row, LEAD_TIME), ManualSupplierInfos.DEFAULT_LEAD_TIME_DAYS);
        String entered = at(row, CATEGORY);
        String category = entered.isEmpty() ? null : entered;

        return new ManualFeedItem(ean, mfn, at(row, BRAND), name, category, netPrice, at(row, CURRENCY), qty, leadTimeDays);
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

    private record ManualFeedItem(String ean, String mfn, String brand, String name, String category,
                                  double netPrice, String currency, int qty, int leadTimeDays) implements FeedItem {

        @Override
        public String getEan() {
            return ean;
        }

        @Override
        public String getMfn() {
            return mfn;
        }

        @Override
        public String getBrand() {
            return brand;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getCategory() {
            return category;
        }

        @Override
        public double getNetPrice() {
            return netPrice;
        }

        @Override
        public String getCurrency() {
            return currency;
        }

        @Override
        public int getQty() {
            return qty;
        }

        @Override
        public int getLeadTimeDays() {
            return leadTimeDays;
        }

        @Override
        public boolean isInStock() {
            return qty > 0;
        }
    }
}
