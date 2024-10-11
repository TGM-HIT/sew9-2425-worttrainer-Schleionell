package org.example.wortrainer_reloaded;

import org.example.wortrainer_reloaded.model.Statistik;
import org.example.wortrainer_reloaded.model.WortBildPaar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.wortrainer_reloaded.service.WorttrainerService;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse für den Worttrainer.
 */
public class WorttrainerApplicationTests {

    private WorttrainerService worttrainerService;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        worttrainerService = new WorttrainerService();
        worttrainerService.addWortBildPaar(new WortBildPaar("Hund", "https://via.placeholder.com/150?text=Hund"));
        worttrainerService.addWortBildPaar(new WortBildPaar("Katze", "https://via.placeholder.com/150?text=Katze"));
        worttrainerService.addWortBildPaar(new WortBildPaar("Auto", "https://via.placeholder.com/150?text=Auto"));
    }

    @Test
    public void testWortBildPaarValid() throws MalformedURLException {
        WortBildPaar paar = new WortBildPaar("Baum", "https://via.placeholder.com/150?text=Baum");
        assertEquals("Baum", paar.getWort());
        assertEquals("https://via.placeholder.com/150?text=Baum", paar.getBildUrl().toString());
    }

    @Test
    public void testWortBildPaarInvalidWort() {
        assertThrows(IllegalArgumentException.class, () -> {
            new WortBildPaar(null, "https://via.placeholder.com/150?text=Baum");
        });
    }

    @Test
    public void testWortBildPaarInvalidUrl() {
        assertThrows(IllegalArgumentException.class, () -> {
            new WortBildPaar("Baum", null);
        });
    }

    @Test
    public void testWortBildPaarMalformedUrl() {
        assertThrows(MalformedURLException.class, () -> {
            new WortBildPaar("Baum", "invalid_url");
        });
    }

    @Test
    public void testStatistikInitialValues() {
        Statistik statistik = new Statistik();
        assertEquals(0, statistik.getGesamtVersuche());
        assertEquals(0, statistik.getRichtigeVersuche());
        assertEquals(0, statistik.getFalscheVersuche());
    }

    @Test
    public void testStatistikIncrement() {
        Statistik statistik = new Statistik();
        statistik.erhoeheGesamtVersuche();
        statistik.erhoeheRichtigeVersuche();
        statistik.erhoeheFalscheVersuche();
        assertEquals(1, statistik.getGesamtVersuche());
        assertEquals(1, statistik.getRichtigeVersuche());
        assertEquals(1, statistik.getFalscheVersuche());
    }

    @Test
    public void testWorttrainerServiceAddPaar() throws MalformedURLException {
        int initialSize = worttrainerService.getWortBildPaare().size();
        worttrainerService.addWortBildPaar(new WortBildPaar("Blume", "https://via.placeholder.com/150?text=Blume"));
        assertEquals(initialSize + 1, worttrainerService.getWortBildPaare().size());
    }

    @Test
    public void testWorttrainerServiceWaehlePaar() {
        worttrainerService.waehlePaar(0);
        assertNotNull(worttrainerService.getAktuellesPaar());
        assertEquals("Hund", worttrainerService.getAktuellesPaar().getWort());
    }

    @Test
    public void testWorttrainerServiceWaehlePaarInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            worttrainerService.waehlePaar(-1);
        });
    }

    @Test
    public void testWorttrainerServiceWaehleZufaelligesPaar() {
        worttrainerService.waehleZufaelligesPaar();
        assertNotNull(worttrainerService.getAktuellesPaar());
    }

    @Test
    public void testWorttrainerServiceRateWortRichtig() {
        worttrainerService.waehlePaar(0);
        boolean korrekt = worttrainerService.rateWort("Hund");
        assertTrue(korrekt);
        assertNull(worttrainerService.getAktuellesPaar());
        assertEquals(1, worttrainerService.getStatistik().getGesamtVersuche());
        assertEquals(1, worttrainerService.getStatistik().getRichtigeVersuche());
    }

    @Test
    public void testWorttrainerServiceRateWortFalsch() {
        worttrainerService.waehlePaar(0);
        boolean korrekt = worttrainerService.rateWort("Katze");
        assertFalse(korrekt);
        assertNotNull(worttrainerService.getAktuellesPaar());
        assertEquals(1, worttrainerService.getStatistik().getGesamtVersuche());
        assertEquals(1, worttrainerService.getStatistik().getFalscheVersuche());
    }

    @Test
    public void testWorttrainerServiceRateWortOhneAuswahl() {
        assertThrows(IllegalStateException.class, () -> {
            worttrainerService.rateWort("Hund");
        });
    }

    @Test
    public void testWorttrainerServiceRateWortNullEingabe() {
        worttrainerService.waehlePaar(0);
        assertThrows(IllegalArgumentException.class, () -> {
            worttrainerService.rateWort(null);
        });
    }

    @Test
    public void testPersistenzServiceSpeichernUndLaden() throws MalformedURLException {
        // Erstelle ein neues WorttrainerService-Objekt
        WorttrainerService originalService = new WorttrainerService();
        originalService.addWortBildPaar(new WortBildPaar("Baum", "https://via.placeholder.com/150?text=Baum"));
        originalService.addWortBildPaar(new WortBildPaar("Haus", "https://via.placeholder.com/150?text=Haus"));
        originalService.waehlePaar(1);
        originalService.rateWort("Haus"); // Richtige Antwort
        originalService.waehlePaar(0);
        originalService.rateWort("Hause"); // Falsche Antwort

        // Speichere den Zustand
        org.example.wortrainer_reloaded.persistence.PersistenzService persistenzService = new org.example.wortrainer_reloaded.persistence.JsonPersistenzService("test_worttrainer.json");
        persistenzService.speichern(originalService);

        // Lade den Zustand
        WorttrainerService geladenerService = persistenzService.laden();

        // Vergleiche die geladenen Daten mit den Originaldaten
        assertEquals(originalService.getWortBildPaare().size(), geladenerService.getWortBildPaare().size());
        assertEquals(originalService.getStatistik().getGesamtVersuche(), geladenerService.getStatistik().getGesamtVersuche());
        assertEquals(originalService.getStatistik().getRichtigeVersuche(), geladenerService.getStatistik().getRichtigeVersuche());
        assertEquals(originalService.getStatistik().getFalscheVersuche(), geladenerService.getStatistik().getFalscheVersuche());
    }
}
