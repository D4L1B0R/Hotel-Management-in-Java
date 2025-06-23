package UnitTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import entiteti.Rezervacija;
import entiteti.Soba;
import funkcionalnosti.MenadzerRezervacije;
import hotel.HotelListePodataka;
import podaci.Cenovnik;
import podaci.StatusRezervacije;
import podaci.TipSobe;
import prikaz.IspisZapis;

class MenadzerRezervacijeTest {
	
	MenadzerRezervacije menadzerRezervacije = new MenadzerRezervacije();
	IspisZapis main = new IspisZapis();
	
	@Test
	void testPotragaRezervacije() {
		main.zapisPodataka();
		assertNotNull(menadzerRezervacije.potragaRezervacije(1));
	}
	@Test
	void testPromenaStatusa() {
		main.zapisPodataka();
		menadzerRezervacije.promenaStatusa(1, StatusRezervacije.OTKAZANA);
		assertEquals(StatusRezervacije.OTKAZANA, menadzerRezervacije.potragaRezervacije(1).getStatus());
	}
	@Test
	void testPromenaDatumaPocetkaRezervacije() {
		main.zapisPodataka();
		menadzerRezervacije.promenaDatumaPocetkaRezervacije(1, "01.06.2021.");
		assertEquals("01.06.2021.", menadzerRezervacije.potragaRezervacije(1).getDatumPocetkaRezervacije());
	}
	@Test
	void testPromenaDatumaKrajaRezervacije() {
		main.zapisPodataka();
		menadzerRezervacije.promenaDatumaKrajaRezervacije(1, "05.06.2021.");
		assertEquals("05.06.2021.", menadzerRezervacije.potragaRezervacije(1).getDatumKrajaRezervacije());
	}
	@Test
	void testPromenaSobe() {
		main.zapisPodataka();
		for (TipSobe  tipSobe : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
            if (tipSobe.getNazivTipaSobe().equals("jednokrevetna (1)")) {
                Soba novaSoba = new Soba(202, tipSobe);
                menadzerRezervacije.promenaSobe(1, novaSoba);
                assertEquals(novaSoba, menadzerRezervacije.potragaRezervacije(1).getSoba());
            }
        }
	}
	@Test
	void testPromenaBrojaOsoba() {
        main.zapisPodataka();
        assertEquals(3, menadzerRezervacije.potragaRezervacije(1).getBrojOsoba());
        menadzerRezervacije.promenaBrojaOsoba(1, 2);
        assertEquals(2, menadzerRezervacije.potragaRezervacije(1).getBrojOsoba());
	}
	@Test
	void testPromenaCene() {
        main.zapisPodataka();
        assertEquals(27000, menadzerRezervacije.potragaRezervacije(1).getCena());
        menadzerRezervacije.promenaCene(1, 100);
        assertEquals(100, menadzerRezervacije.potragaRezervacije(1).getCena());
	}
	@Test
	void testPromenaGosta() {
        main.zapisPodataka();
        assertEquals("milicaM", menadzerRezervacije.potragaRezervacije(1).getGost().getKorisnickoIme());
	}
	@Test
	void testNadjiStatus() {
		main.zapisPodataka();
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacija.getId() == 1) {
        		assertEquals(rezervacija.getStatus(), menadzerRezervacije.nadjiStatus(1));
			}
		}
	}
	@Test
	void testNadjiDatumPocetkaRezervacije() {
		main.zapisPodataka();
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacija.getId() == 1) {
				assertEquals(rezervacija.getDatumPocetkaRezervacije(), menadzerRezervacije.nadjiDatumPocetkaRezervacije(1));
			}
		}
	}
	@Test
	void testNadjiDatumKrajaRezervacije() {
		main.zapisPodataka();
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacija.getId() == 1) {
				assertEquals(rezervacija.getDatumKrajaRezervacije(), menadzerRezervacije.nadjiDatumKrajaRezervacije(1, "05.06.2021."));
			}
		}
	}
	@Test
	void testNadjiSobu() {
		main.zapisPodataka();
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacija.getId() == 1) {
				assertEquals(rezervacija.getSoba(), menadzerRezervacije.nadjiSobu(1));
			}
		}
	}
	@Test
	void testNadjiGosta() {
		main.zapisPodataka();
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacija.getId() == 1) {
				assertEquals(rezervacija.getGost(), menadzerRezervacije.nadjiGosta(1));
			}
		}
	}
	@Test
	void testNadjiBrojOsoba() {
		main.zapisPodataka();
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacija.getId() == 1) {
				assertEquals(rezervacija.getBrojOsoba(), menadzerRezervacije.nadjiBrojOsoba(1));
			}
		}
	}
	@Test
	void testNadjiCenu() {
		main.zapisPodataka();
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacija.getId() == 1) {
				assertEquals(rezervacija.getCena(), menadzerRezervacije.nadjiCenu(1));
			}
		}
	}
	@Test
	void testPronadjiRezervacijuPoKorisniku() {
		main.zapisPodataka();
		assertNotNull(menadzerRezervacije.pronadjiRezervacijuPoKorisniku("Milica", "Milić"));
	}
}