package pl.commercelink.inventory.supplier.manual;

public final class ManualSupplierNames {

    public static final String PREFIX = "manual:";

    private ManualSupplierNames() {
    }

    public static boolean isManual(String name) {
        return name != null && name.startsWith(PREFIX);
    }

    public static String identityFor(String label) {
        return PREFIX + label;
    }

    public static String label(String identity) {
        return isManual(identity) ? identity.substring(PREFIX.length()) : identity;
    }
}
