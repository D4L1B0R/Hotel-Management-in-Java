package UnitTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import entiteti.Osoblje;
import funkcionalnosti.MenadzerCenovnik;
import hotel.HotelListePodataka;
import podaci.Cenovnik;
import podaci.DodatneUsluge;
import prikaz.IspisZapis;

class MenadzerCenovnikTest {
	
	private MenadzerCenovnik menadzerCenovnik = new MenadzerCenovnik();
	IspisZapis main = new IspisZapis();
	@Test
	void testDobijDodatnuUslugu() {
		main.zapisPodataka();
		assertEquals("bazen", menadzerCenovnik.dobijDodatnuUslugu("bazen").getDodatneUsluge());
    }
	@Test
	void testDobijDatum() {
		main.zapisPodataka();
		assertEquals("01.01.2024.,31.12.2024.", menadzerCenovnik.dobijDatum("bazen", "01.01.2024.", "31.12.2024."));
	}
	@Test
	void testPodesiCenuDodatnihUsluga() {
		main.zapisPodataka();
		Osoblje admin = null;
		for (Osoblje osoba : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (osoba.getKorisnickoIme().equals("dalenik")) {
				admin = osoba;
				break;
			}
		}
		menadzerCenovnik.podesiCenuDodatnihUsluga(admin, "bazen", "01.01.2024.", "31.12.2024.", 1500);
		double cena = 0;
		for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
			if (usluga.getDodatneUsluge().equals("bazen")) {
				for (String datumi : Cenovnik.getInstance().getDodatneUsluge().get(usluga).keySet()) {
					if (datumi.equals("01.01.2024.,31.12.2024.")) {
						cena = Cenovnik.getInstance().getDodatneUsluge().get(usluga).get(datumi);
						break;
					}
				}
			}
		}
		assertEquals(1500.0, cena);
	}
	@Test
	void testInicijalizujCenuDodatnihUsluga() {
		main.zapisPodataka();
		Osoblje admin = null;
		for (Osoblje osoba : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (osoba.getKorisnickoIme().equals("dalenik")) {
				admin = osoba;
				break;
			}
		}
		menadzerCenovnik.inicijalizujCenuDodatnihUsluga(admin, "bazen", "01.01.2025.", "31.10.2025.", 1500);
		for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
			if (usluga.getDodatneUsluge().equals("bazen")) {
				for (String datumi : Cenovnik.getInstance().getDodatneUsluge().get(usluga).keySet()) {
					if (datumi.equals("01.01.2024.,31.12.2024.")) {
						assertEquals("bazen", usluga.getDodatneUsluge());
						assertEquals("01.01.2025.,31.10.2025.", datumi);
						assertEquals(1500.0, Cenovnik.getInstance().getDodatneUsluge().get(usluga).get(datumi));
						break;
					}
				}
			}
		}
	}
	@Test
	void testPodesiDodatnuUslugu() {
		main.zapisPodataka();
		for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
			if (usluga.getDodatneUsluge().equals("bazen")) {
				menadzerCenovnik.podesiDodatnuUslugu("bazen", "sauna");
				assertEquals("sauna", usluga.getDodatneUsluge());
				break;
			}
		}
	}
	@Test
	void testPodesiDatumDodatneUsluge() {
		main.zapisPodataka();
		for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
			if (usluga.getDodatneUsluge().equals("bazen")) {
				for (String datumi : Cenovnik.getInstance().getDodatneUsluge().get(usluga).keySet()) {
					if (datumi.equals("01.01.2024.,31.12.2024.")) {
						assertEquals("01.01.2024.,31.12.2024.", datumi);
						menadzerCenovnik.podesiDatumDodatneUsluge("bazen", "01.01.2024.", "31.12.2024.", "01.04.2024.", "31.10.2025.");
						break;
					}
				}
			}
		}
		for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
			if (usluga.getDodatneUsluge().equals("bazen")) {
				for (String datumi : Cenovnik.getInstance().getDodatneUsluge().get(usluga).keySet()) {
					if (datumi.equals("01.04.2024.,31.10.2025.")) {
						assertEquals("01.04.2024.,31.10.2025.", datumi);
						break;
					}
				}
			}
		}
	}
	@Test
	void testDodajDodatnuUslugu() {
		main.zapisPodataka();
		Osoblje admin = null;
		for (Osoblje osoba : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (osoba.getKorisnickoIme().equals("dalenik")) {
				admin = osoba;
				break;
			}
		}
		menadzerCenovnik.dodajDodatnuUslugu(admin, "sauna", "01.05.2024.", "31.06.2024.", 500);
		for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
			if (usluga.getDodatneUsluge().equals("sauna")) {
				for (String datumi : Cenovnik.getInstance().getDodatneUsluge().get(usluga).keySet()) {
					if (datumi.equals("01.01.2024.,31.12.2024.")) {
						assertEquals("sauna", usluga.getDodatneUsluge());
						assertEquals("01.01.2024.,31.12.2024.", datumi);
						assertEquals(500.0, Cenovnik.getInstance().getDodatneUsluge().get(usluga).get(datumi));
						break;
					}
				}
			}
		}
	}
	@Test
	void testInicijalizujDodatneUsluge() {
		main.zapisPodataka();
		Osoblje admin = null;
		for (Osoblje osoba : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (osoba.getKorisnickoIme().equals("dalenik")) {
				admin = osoba;
				break;
			}
		}
		menadzerCenovnik.inicijalizujDodatnuUslugu(admin, "sauna");
		for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
			if (usluga.getDodatneUsluge().equals("sauna")) {
				assertEquals("sauna", usluga.getDodatneUsluge());
				break;
			}
		}
	}
	@Test
	void testNadjiCenuDodatneUsluge() {
		main.zapisPodataka();
		assertEquals(2100.0, menadzerCenovnik.nadjiCenuDodatneUsluge("bazen", "01.01.2024.", "31.12.2024."));
	}
	@Test
	void testIzbrisiDodatnuUslugu() {
		main.zapisPodataka();
		// PRE BRISANJA
		boolean obrisan = false;
		for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
			if (usluga.getDodatneUsluge().equals("bazen")) {
				obrisan = true;
				break;
			}
		}
		assertEquals(obrisan, true);
		Osoblje admin = null;
		for (Osoblje osoba : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (osoba.getKorisnickoIme().equals("dalenik")) {
				admin = osoba;
				break;
			}
		}
		menadzerCenovnik.izbrisiDodatnuUslugu(admin, "bazen");
		// POSLE BRISANJA
		obrisan = false;
		for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
			if (usluga.getDodatneUsluge().equals("bazen")) {
				obrisan = true;
				break;
			}
		}
		assertEquals(obrisan, false);
	}
	@Test
	void testIzbrisiDatumDodatneUsluge() {
		main.zapisPodataka();
		// PRE BRISANJA
		boolean obrisan = false;
		for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
			if (usluga.getDodatneUsluge().equals("bazen")) {
				for (String datumi : Cenovnik.getInstance().getDodatneUsluge().get(usluga).keySet()) {
					if (datumi.equals("01.01.2024.,31.12.2024.")) {
						obrisan = true;
						break;
					}
				}
			}
		}
		assertEquals(obrisan, true);
		menadzerCenovnik.izbrisiDatumDodatneUsluge("bazen", "01.01.2024.", "31.12.2024.");
		// POSLE BRISANJA
		obrisan = false;
		for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
			if (usluga.getDodatneUsluge().equals("bazen")) {
				for (String datumi : Cenovnik.getInstance().getDodatneUsluge().get(usluga).keySet()) {
					if (datumi.equals("01.01.2024.,31.12.2024.")) {
						obrisan = true;
						break;
					}
				}
			}
		}
		assertEquals(obrisan, false);
	}
	// PROIZVOLJNO ODABRANI DATUMI, VREDNOSTI SU POZAJMLJENE IZ IZVESTAJA KOJI ČUVA TE PODATKE U TXT FORMATU
	@Test
	void testVidiPrihode() {
		main.zapisPodataka();
		assertEquals(188750.0, menadzerCenovnik.vidiPrihode("08.06.2024.", "08.06.2024.").get(0));
	}
	@Test
	void testVidiRashode() {
		main.zapisPodataka();
		assertEquals(852000.0, menadzerCenovnik.vidiRashode("07.06.2024.", "09.06.2024.").get(0) + menadzerCenovnik.vidiRashode("07.06.2024.", "09.06.2024.").get(1) + menadzerCenovnik.vidiRashode("07.06.2024.", "09.06.2024.").get(2));
	}
}
