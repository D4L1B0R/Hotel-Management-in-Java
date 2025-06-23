package UnitTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import entiteti.Gost;
import entiteti.Rezervacija;
import entiteti.Soba;
import funkcionalnosti.GostFunkcija;
import funkcionalnosti.RecepcionerRezervacije;
import hotel.HotelListePodataka;
import podaci.DodatneUsluge;
import podaci.Izvestaji;
import podaci.StatusRezervacije;
import prikaz.IspisZapis;

class RecepcionerRezervacijeTest {

	RecepcionerRezervacije recepcionerRezervacije = new RecepcionerRezervacije();
	IspisZapis main = new IspisZapis();
	void f() {
		Integer[] niz = new Integer[3];
		niz[0] = 0;
		niz[1] = 0;
		niz[2] = 0;
		if (!Izvestaji.getInstance().getObradaRezervacija().containsKey(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
			Izvestaji.getInstance().getObradaRezervacija().put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), niz);
		}
	}
	@Test
	void testPotvrdaRezervacije() {
	    main.zapisPodataka();
	    f();
	    Gost g = new Gost("Pera", "Peric", null, "pera", 0, "pera", "pera", null);
	    GostFunkcija gf = new GostFunkcija();
	    HotelListePodataka.getInstance().getListaGostiju().add(g);
	    gf.rezervacijaSobe(g, 2, "01.08.2024.", "05.08.2024.", "trokrevetna (2+1)", new ArrayList<DodatneUsluge>());
	    boolean nadjena = false;
	    for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
	        if (rezervacija.getId() == 5) {
	            nadjena = true;
	            break;
	        }
	    }
	    assertTrue(nadjena);
	    recepcionerRezervacije.potvrdaRezervacije(5);
	    for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
	        if (rezervacija.getId() == 5) {
	            assertEquals(StatusRezervacije.POTVRĐENA, rezervacija.getStatus());
	            break;
	        }
	    }
	}
	@Test
	void testAzuriranjeRezervacija() {
		main.zapisPodataka();
		f();
		// ISTEKLA REZERVACIJA JE ODBIJENA
		Gost g = new Gost("Pera", "Peric", null, "pera", 0, "pera", "jova", null);
	    GostFunkcija gf = new GostFunkcija();
	    HotelListePodataka.getInstance().getListaGostiju().add(g);
	    gf.rezervacijaSobe(g, 2, "01.01.2024.", "05.01.2024.", "trokrevetna (2+1)", new ArrayList<DodatneUsluge>());
		recepcionerRezervacije.azuriranjeRezervacija();
		boolean nadjena = false;
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacija.getId() == 5) {
				assertEquals(StatusRezervacije.ODBIJENA, rezervacija.getStatus());
				nadjena = true;
				break;
			}
        }
		assertTrue(nadjena);
	}
	@Test
	void testPregledSlobodnihSoba() {
		main.zapisPodataka();
		f();
		// SOBA KOJA JE U TOM TRENUTKU SLOBODNA (JEDINA SOBA KOJA JE SLOBODNA)
		ArrayList<Soba> slobodneSobe = recepcionerRezervacije.pregledSlobodnihSoba("trokrevetna (2+1)", "01.09.2024.", "05.09.2024.");
		assertEquals(101, slobodneSobe.get(0).getBrojSobe());
	}
}