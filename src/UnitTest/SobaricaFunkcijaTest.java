package UnitTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import entiteti.Soba;
import entiteti.Sobarica;
import funkcionalnosti.RecepcionerSobe;
import funkcionalnosti.SobaricaFunkcija;
import hotel.HotelListePodataka;
import podaci.Izvestaji;
import prikaz.IspisZapis;

class SobaricaFunkcijaTest {

	IspisZapis main = new IspisZapis();
	void f() {
		for (Sobarica sobarica : Izvestaji.getInstance().getOcisceneSobe().keySet()) {
			if (!Izvestaji.getInstance().getOcisceneSobe().get(sobarica).containsKey(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")))) {
				Izvestaji.getInstance().getOcisceneSobe().get(sobarica).put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), 0);
			}
		}
	}
	@Test
	void testOcisnjenaSoba() {
		main.zapisPodataka();
		f();
		SobaricaFunkcija sobaricaFunkcija = new SobaricaFunkcija();
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == 101) {
				assertEquals(soba.getStatus().toString(), "SLOBODNA");
				RecepcionerSobe funkcija = new RecepcionerSobe();
				funkcija.oslobadjanjeSobe(1, 101);
				break;
			}
		}
		Sobarica sobaricaJana = null;
		for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
			if (sobarica.getKorisnickoIme().equals("janči")) {
				sobaricaJana = sobarica;
				break;
			}
		}
		sobaricaFunkcija.ociscenaSoba(sobaricaJana, 101);
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == 101) {
				assertEquals(soba.getStatus().toString(), "SLOBODNA");
				break;
			}
		}
	}

}
