package pl.commercelink.inventory.supplier.manual;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManualCategoryMappingsTest {

    private static final List<String> EXPECTED = List.of(
            "CPU",
            "Cooler",
            "GPU",
            "Motherboard",
            "PSU",
            "Storage",
            "Memory",
            "Case",
            "Fan",
            "ModdingPC",
            "Other",
            "Services",
            "Laptops",
            "Desktops",
            "Workstations",
            "Servers",
            "AllInOnePCs",
            "GraphicsTablets",
            "Software",
            "Smartphones",
            "StationaryPhones",
            "Tablets",
            "SmartphoneCases",
            "ScreenProtectors",
            "Chargers",
            "Powerbanks",
            "MobileHeadphones",
            "Printers",
            "LaserPrinters",
            "InkPrinters",
            "PhotoPrinters",
            "LargeFormatPrinters",
            "LabelPrinters",
            "Printers3D",
            "Scanners",
            "MultifunctionPrinters",
            "Displays",
            "Keyboards",
            "Mice",
            "KeyboardsAndMice",
            "Headphones",
            "Microphones",
            "Webcams",
            "Speakers",
            "MousePads",
            "GamingChairs",
            "OfficeChairs",
            "GamingDesks",
            "OfficeDesks",
            "StandingDesks",
            "MonitorMounts",
            "Footrests");

    @Test
    void categoriesContainExactlyTheCanonicalNamesInDeclarationOrder() {
        assertEquals(52, ManualCsvRowParser.CATEGORIES.size());
        assertEquals(EXPECTED, ManualCsvRowParser.CATEGORIES);
    }
}
