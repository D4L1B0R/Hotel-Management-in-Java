package UnitTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import entiteti.Osoblje;
import entiteti.Rezervacija;
import entiteti.Soba;
import entiteti.Sobarica;
import funkcionalnosti.RecepcionerSobe;
import hotel.HotelListePodataka;
import podaci.Izvestaji;
import podaci.OdlasciDolasci;
import podaci.PrihodiRashodi;
import podaci.SlobodneSobe;
import podaci.TipSobe;
import prikaz.IspisZapis;

class RecepcionerSobeTest {

	RecepcionerSobe recepcionerSobe = new RecepcionerSobe();
	IspisZapis main = new IspisZapis();
	// INICIJALIZACIJA POTREBNIH PODATAKA
	void f() {
		for (Sobarica sobarica : Izvestaji.getInstance().getOcisceneSobe().keySet()) {
			if (!Izvestaji.getInstance().getOcisceneSobe().get(sobarica).containsKey(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
				Izvestaji.getInstance().getOcisceneSobe().get(sobarica).put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), 0);
			}
		}
		Integer[] niz = new Integer[3];
		niz[0] = 0;
		niz[1] = 0;
		niz[2] = 0;
		if (!Izvestaji.getInstance().getObradaRezervacija().containsKey(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
			Izvestaji.getInstance().getObradaRezervacija().put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), niz);
		}
		if (!PrihodiRashodi.getInstance().getDnevniPrihodi().containsKey(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
			PrihodiRashodi.getInstance().getDnevniPrihodi().put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), 0.0);
		}
		Double plate = 0.0;
		if (HotelListePodataka.getInstance().getListaZaposlenih() != null) {
			for (Osoblje osoblje : HotelListePodataka.getInstance().getListaZaposlenih()) {
				plate += osoblje.getPlata();
			}
		}
		if (!PrihodiRashodi.getInstance().getDnevniRashodi().containsKey(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
            PrihodiRashodi.getInstance().getDnevniRashodi().put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), plate);
        }
		if (PrihodiRashodi.getInstance().getPrihodiPoTipuSobe() != null) {
			for (TipSobe tip : PrihodiRashodi.getInstance().getPrihodiPoTipuSobe().keySet()) {
				if (!PrihodiRashodi.getInstance().getPrihodiPoTipuSobe().get(tip).containsKey(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
					PrihodiRashodi.getInstance().getPrihodiPoTipuSobe().get(tip).put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), 0.0);
				}
			}
		}
		ArrayList<Rezervacija> rezervacijeDolasci = new ArrayList<>();
		if (OdlasciDolasci.getInstance().getDnevniDolasci() != null && OdlasciDolasci.getInstance().getDnevniDolasci().containsKey(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
			for (Rezervacija rez : OdlasciDolasci.getInstance().getDnevniDolasci().get(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")) )) {
				rezervacijeDolasci.add(rez);
			}
		}
		OdlasciDolasci.getInstance().getDnevniDolasci().put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), rezervacijeDolasci);
		ArrayList<Rezervacija> rezervacijeOdlasci = new ArrayList<>();
		if (OdlasciDolasci.getInstance().getDnevniOdlasci() != null && OdlasciDolasci.getInstance().getDnevniOdlasci().containsKey(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) ) {
			for (Rezervacija rez : OdlasciDolasci.getInstance().getDnevniOdlasci().get(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")) )) {
				rezervacijeOdlasci.add(rez);
			}
		}
		OdlasciDolasci.getInstance().getDnevniOdlasci().put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), rezervacijeOdlasci);
		int zauzetost = 0;
		if (OdlasciDolasci.getInstance().getDnevniOdlasci() != null && OdlasciDolasci.getInstance().getDnevnaZauzetost().containsKey(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) ) {
			zauzetost = OdlasciDolasci.getInstance().getDnevnaZauzetost().get(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
				
		}
		OdlasciDolasci.getInstance().getDnevnaZauzetost().put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), zauzetost);
	}
	@Test
	void testPopunjavanjeSobe() {
		main.zapisPodataka();
		f();
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == 101) {
				assertEquals(soba.getStatus().toString(), "SLOBODNA");
				break;
			}
		}
		for (String datum :	OdlasciDolasci.getInstance().getDnevniDolasci().keySet()) {
			if (datum.equals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
				assertEquals(OdlasciDolasci.getInstance().getDnevniDolasci().get(datum).size(), 0);
				break;
			}
		}
		boolean nadjen = false;
		for (Soba slobodneSobe : SlobodneSobe.getInstance().getSlobodneSobe().keySet()) {
			if (slobodneSobe.getBrojSobe() == 101) {
				for (String datum : SlobodneSobe.getInstance().getSlobodneSobe().get(slobodneSobe)) {
                    if (datum.equals("08.06.2024.,15.06.2024.")) {
                    	nadjen = true;
                    	break;
                    }
				}
			}
		}
		assertFalse(nadjen);
		for (String datum : OdlasciDolasci.getInstance().getDnevnaZauzetost().keySet()) {
			if (datum.equals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
				assertEquals(OdlasciDolasci.getInstance().getDnevnaZauzetost().get(datum), 0);
				break;
			}
		}
		boolean nadjen3 = false;
		for (Soba soba : Izvestaji.getInstance().getNocenjeSoba().keySet()) {
			if (soba.getBrojSobe() == 101) {
				for (String datum : Izvestaji.getInstance().getNocenjeSoba().get(soba)) {
					if (datum.equals("08.06.2024.,15.06.2024.")) {
						nadjen3 = true;
						break;
					}
				}
			}
		}
		assertFalse(nadjen3);
		recepcionerSobe.popunjavanjeSobe(2, 101);
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == 101) {
				assertEquals(soba.getStatus().toString(), "ZAUZETA");
				break;
			}
		}
		for (String datum :	OdlasciDolasci.getInstance().getDnevniDolasci().keySet()) {
			if (datum.equals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
				assertEquals(OdlasciDolasci.getInstance().getDnevniDolasci().get(datum).size(), 1);
				assertEquals(OdlasciDolasci.getInstance().getDnevniDolasci().get(datum).get(0).getId(), 2);
				break;
			}
		}
		for (Soba slobodneSobe : SlobodneSobe.getInstance().getSlobodneSobe().keySet()) {
			if (slobodneSobe.getBrojSobe() == 101) {
				for (String datum : SlobodneSobe.getInstance().getSlobodneSobe().get(slobodneSobe)) {
                    if (datum.equals("08.06.2024.,15.06.2024.")) {
                    	nadjen = true;
                    	break;
                    }
				}
			}
		}
		assertTrue(nadjen);
		for (String datum : OdlasciDolasci.getInstance().getDnevnaZauzetost().keySet()) {
			if (datum.equals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
				assertEquals(OdlasciDolasci.getInstance().getDnevnaZauzetost().get(datum), 1);
				break;
			}
		}
		for (Soba soba : Izvestaji.getInstance().getNocenjeSoba().keySet()) {
			if (soba.getBrojSobe() == 101) {
				for (String datum : Izvestaji.getInstance().getNocenjeSoba().get(soba)) {
					if (datum.equals("08.06.2024.,15.06.2024.")) {
						nadjen3 = true;
						break;
					}
				}
			}
		}
		assertTrue(nadjen3);
	}
	@Test
	void testOslobadjanjeSobe() {
		recepcionerSobe.popunjavanjeSobe(2, 101);
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == 101) {
				assertEquals(soba.getStatus().toString(), "ZAUZETA");
				break;
			}
		}
		for (String datum : OdlasciDolasci.getInstance().getDnevniDolasci().keySet()) {
			if (datum.equals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
				assertEquals(OdlasciDolasci.getInstance().getDnevniDolasci().get(datum).size(), 1);
				assertEquals(OdlasciDolasci.getInstance().getDnevniDolasci().get(datum).get(0).getId(), 2);
				break;
			}
		}
		boolean nadjen = false;
		for (Soba slobodneSobe : SlobodneSobe.getInstance().getSlobodneSobe().keySet()) {
			if (slobodneSobe.getBrojSobe() == 101) {
				for (String datum : SlobodneSobe.getInstance().getSlobodneSobe().get(slobodneSobe)) {
                    if (datum.equals("08.06.2024.,15.06.2024.")) {
                    	nadjen = true;
                    	break;
                    }
				}
			}
		}
		for (String datum : OdlasciDolasci.getInstance().getDnevnaZauzetost().keySet()) {
			if (datum.equals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
				assertEquals(OdlasciDolasci.getInstance().getDnevnaZauzetost().get(datum), 1);
				break;
			}
		}
		recepcionerSobe.oslobadjanjeSobe(2, 101);
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == 101) {
				assertEquals(soba.getStatus().toString(), "SPREMANJE");
				break;
			}
		}
		for (String datum :	OdlasciDolasci.getInstance().getDnevniDolasci().keySet()) {
			if (datum.equals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
				assertEquals(OdlasciDolasci.getInstance().getDnevniDolasci().get(datum).size(), 0);
				break;
			}
		}
		nadjen = false;
		for (Soba slobodneSobe : SlobodneSobe.getInstance().getSlobodneSobe().keySet()) {
			if (slobodneSobe.getBrojSobe() == 101) {
				for (String datum : SlobodneSobe.getInstance().getSlobodneSobe().get(slobodneSobe)) {
                    if (datum.equals("08.06.2024.,15.06.2024.")) {
                    	nadjen = true;
                    	break;
                    }
				}
			}
		}
		assertFalse(nadjen);
		for (String datum : OdlasciDolasci.getInstance().getDnevnaZauzetost().keySet()) {
			if (datum.equals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
				assertEquals(OdlasciDolasci.getInstance().getDnevnaZauzetost().get(datum), 0);
				break;
			}
		}
		boolean nadjen3 = false;
		for (Soba soba : Izvestaji.getInstance().getNocenjeSoba().keySet()) {
			if (soba.getBrojSobe() == 101) {
				for (String datum : Izvestaji.getInstance().getNocenjeSoba().get(soba)) {
					if (datum.equals("08.06.2024.,15.06.2024.")) {
						nadjen3 = true;
						break;
					}
				}
			}
		}
		assertFalse(nadjen3);
	}
}