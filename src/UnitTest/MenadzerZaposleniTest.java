package UnitTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import entiteti.Osoblje;
import entiteti.Sobarica;
import funkcionalnosti.MenadzerZaposleni;
import hotel.HotelListePodataka;
import podaci.NivoStrucneSpreme;
import podaci.Pol;
import podaci.Zaposleni;
import prikaz.IspisZapis;

class MenadzerZaposleniTest {
	
	MenadzerZaposleni menadzerZaposleni = new MenadzerZaposleni();
	IspisZapis main = new IspisZapis();
	@Test
	void testDodavajneZaposlenih() {
		main.zapisPodataka();
		boolean nadjen1 = false;
		boolean nadjen2 = false;
		menadzerZaposleni.dodavanjeZaposlenih("Marko", "Markovic", Pol.MUSKI, Zaposleni.SOBARICA, "01.01.1990", 123456789, "Adresa 1", "marko", "marko", NivoStrucneSpreme.OSNOVNA, 5);
		for (Osoblje radnik : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (radnik.getKorisnickoIme().equals("marko")) {
				assertEquals("Marko", radnik.getIme());
				assertEquals("Markovic", radnik.getPrezime());
				assertEquals(Pol.MUSKI, radnik.getPol());
				assertEquals(Zaposleni.SOBARICA, radnik.getTip());
				assertEquals("01.01.1990", radnik.getDatumRodjenja());
				assertEquals(123456789, radnik.getTelefon());
				assertEquals("Adresa 1", radnik.getAdresa());
				assertEquals("marko", radnik.getKorisnickoIme());
				assertEquals("marko", radnik.getLozinka());
				assertEquals(NivoStrucneSpreme.OSNOVNA, radnik.getStrucnaSprema());
				assertEquals(5, radnik.getStaz());
				nadjen1 = true;
				break;
			}
		}
		for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
			if (sobarica.getKorisnickoIme().equals("marko")) {
				assertEquals("Marko", sobarica.getIme());
				assertEquals("Markovic", sobarica.getPrezime());
				assertEquals(Pol.MUSKI, sobarica.getPol());
				assertEquals(Zaposleni.SOBARICA, sobarica.getTip());
				assertEquals("01.01.1990", sobarica.getDatumRodjenja());
				assertEquals(123456789, sobarica.getTelefon());
				assertEquals("Adresa 1", sobarica.getAdresa());
				assertEquals("marko", sobarica.getKorisnickoIme());
				assertEquals("marko", sobarica.getLozinka());
				assertEquals(NivoStrucneSpreme.OSNOVNA, sobarica.getStrucnaSprema());
				assertEquals(5, sobarica.getStaz());
				nadjen2 = true;
				break;
			}
		}
		assertTrue(nadjen1);
		assertTrue(nadjen2);
	}
	@Test
	void testBrisanjeZaposlenih() {
		main.zapisPodataka();
		boolean nadjen1 = false;
		boolean nadjen2 = false;
		menadzerZaposleni.dodavanjeZaposlenih("Miroslav", "Miroslavljević", Pol.MUSKI, Zaposleni.SOBARICA, "01.01.1990", 123456789, "Adresa 1", "miro", "miro", NivoStrucneSpreme.OSNOVNA, 5);
		Osoblje admin = null;
	    for (Osoblje adminko : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (adminko.getKorisnickoIme().equals("dalenik")) {
				admin = adminko;
				break;
			}
	    }
		menadzerZaposleni.brisanjeZaposlenih(admin, "miro");
		for (Osoblje radnik : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (radnik.getKorisnickoIme().equals("miro")) {
				nadjen1 = true;
				break;
			}
		}
		for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
			if (sobarica.getKorisnickoIme().equals("miro")) {
				nadjen2 = true;
				break;
			}
		}
		assertFalse(nadjen1);
		assertFalse(nadjen2);
	}
	@Test
	void testPromenaTipaZaposlenih() {
		main.zapisPodataka();
		boolean nadjen1 = false;
		boolean nadjen2 = false;
		menadzerZaposleni.promenaTipaZaposlenog("marko", Zaposleni.ADMINISTRATOR);
		for (Osoblje radnik : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (radnik.getKorisnickoIme().equals("marko") && radnik.getTip().equals(Zaposleni.ADMINISTRATOR)) {
				nadjen1 = true;
				break;
			}
		}
		for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
			if (sobarica.getKorisnickoIme().equals("marko")) {
				nadjen2 = true;
				break;
			}
		}
		assertTrue(nadjen1);
		assertFalse(nadjen2);
	}
	@Test
	void testPromenaStazaZaposlenih() {
		main.zapisPodataka();
		boolean nadjen = false;
		menadzerZaposleni.promenaStazaZaposlenog("janči", 10);
		for (Osoblje radnik : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (radnik.getKorisnickoIme().equals("janči") && radnik.getStaz() == 10) {
				nadjen = true;
				break;
			}
		}
		assertTrue(nadjen);
		nadjen = false;
		for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
			if (sobarica.getKorisnickoIme().equals("janči") && sobarica.getStaz() == 10) {
				nadjen = true;
				break;
			}
		}
		assertTrue(nadjen);
	}
	@Test
	void testPromenaStrucneSpremeZaposlenih() {
		main.zapisPodataka();
		boolean nadjen = false;
		menadzerZaposleni.promenaStrucneSpremeZaposlenog("janči", NivoStrucneSpreme.VIŠA);
		for (Osoblje radnik : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (radnik.getKorisnickoIme().equals("janči")
					&& radnik.getStrucnaSprema().equals(NivoStrucneSpreme.VIŠA)) {
				nadjen = true;
				break;
			}
		}
		assertTrue(nadjen);
		nadjen = false;
		for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
			if (sobarica.getKorisnickoIme().equals("janči")
					&& sobarica.getStrucnaSprema().equals(NivoStrucneSpreme.VIŠA)) {
				nadjen = true;
				break;
			}
		}
		assertTrue(nadjen);
	}
	@Test
	void testPromenaLozinkeZaposlenih() {
		main.zapisPodataka();
		boolean nadjen = false;
		menadzerZaposleni.promenaLozinkeZaposlenog("janči", "jana");
		for (Osoblje radnik : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (radnik.getKorisnickoIme().equals("janči") && radnik.getLozinka().equals("jana")) {
				nadjen = true;
				break;
			}
		}
		assertTrue(nadjen);
		nadjen = false;
		for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
			if (sobarica.getKorisnickoIme().equals("janči") && sobarica.getLozinka().equals("jana")) {
				nadjen = true;
				break;
			}
		}
		assertTrue(nadjen);
	}
	@Test
	void testNadjiImeZaposlenih() {
		main.zapisPodataka();
		assertEquals("Jana", menadzerZaposleni.nadjiImeZaposlenog("janči"));
	}
	@Test
	void testNadjiPrezimeZaposlenih() {
		main.zapisPodataka();
		assertEquals("Janić", menadzerZaposleni.nadjiPrezimeZaposlenog("janči"));
	}
	@Test
	void testNadjiPolZaposlenih() {
		main.zapisPodataka();
		assertEquals(Pol.ZENSKI, menadzerZaposleni.nadjiPolZaposlenog("janči"));
	}
	@Test
	void testNadjiTipZaposlenih() {
		main.zapisPodataka();
		assertEquals(Zaposleni.SOBARICA, menadzerZaposleni.nadjiTipZaposlenog("janči"));
	}
	@Test
	void testNadjiDatumRodjenjaZaposlenih() {
		main.zapisPodataka();
		assertEquals("11.07.2002.", menadzerZaposleni.nadjiDatumRodjenjaZaposlenog("janči"));
	}
	@Test
	void testNadjiTelefonZaposlenih() {
		main.zapisPodataka();
		assertEquals(8889998889L, menadzerZaposleni.nadjiTelefonZaposlenog("janči"));
	}
	@Test
	void testNadjiAdresuZaposlenih() {
		main.zapisPodataka();
		assertEquals("Vuka Karadžića 22, Beograd", menadzerZaposleni.nadjiAdresuZaposlenog("janči"));
	}
}