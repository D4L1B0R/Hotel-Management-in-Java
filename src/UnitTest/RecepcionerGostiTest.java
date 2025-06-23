package UnitTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import entiteti.Gost;
import funkcionalnosti.RecepcionerGosti;
import hotel.HotelListePodataka;
import prikaz.IspisZapis;

class RecepcionerGostiTest {
	
	RecepcionerGosti recepcionerGosti = new RecepcionerGosti();
	IspisZapis main = new IspisZapis();
	@Test
	void testDodavanjeGosta() {
		recepcionerGosti.dodavanjeGosta("Test", "Test", null, "01.01.2000", 123456, "Test", "test", "test");
		boolean postoji = false;
		for (Gost gost : HotelListePodataka.getInstance().getListaGostiju()) {
			if (gost.getKorisnickoIme().equals("test")) {
				assertEquals("Test", gost.getIme());
				assertEquals("Test", gost.getPrezime());
				assertEquals("01.01.2000", gost.getDatumRodjenja());
				assertEquals(123456, gost.getTelefon());
				assertEquals("Test", gost.getAdresa());
				assertEquals("test", gost.getKorisnickoIme());
				assertEquals("test", gost.getLozinka());
				postoji = true;
			}
		}
		assertTrue(postoji);
	}
	@Test
	void testBrisanjeGosta() {
		recepcionerGosti.dodavanjeGosta("Test", "Test", null, "01.01.2000", 123456, "Test", "testiranje", "test");
		boolean postoji = false;
		for (Gost gost : HotelListePodataka.getInstance().getListaGostiju()) {
			if (gost.getKorisnickoIme().equals("testiranje")) {
				postoji = true;
				break;
			}
		}
		assertTrue(postoji);
		recepcionerGosti.brisanjeGosta("testiranje");
		postoji = false;
		for (Gost gost : HotelListePodataka.getInstance().getListaGostiju()) {
			if (gost.getKorisnickoIme().equals("testiranje")) {
				postoji = true;
				break;
			}
		}
		assertFalse(postoji);
	}
	@Test
	void testDnevniDolasci() {
		main.zapisPodataka();
		assertNotNull(recepcionerGosti.dnevniDolasci("08.06.2024."));
	}
	@Test
	void testDnevniOdlasci() {
		main.zapisPodataka();
		assertNotNull(recepcionerGosti.dnevniOdlasci("09.06.2024."));
	}
	@Test
	void testDnevnaZauzetost() {
		main.zapisPodataka();
		assertEquals(0, recepcionerGosti.dnevnaZauzetost("08.06.2024."));
	}
}
