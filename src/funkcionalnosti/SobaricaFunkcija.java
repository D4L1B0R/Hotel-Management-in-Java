package funkcionalnosti;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import entiteti.Soba;
import entiteti.Sobarica;
import hotel.HotelListePodataka;
import podaci.Izvestaji;
import podaci.StatusSobe;

public class SobaricaFunkcija {
	public void ociscenaSoba(Sobarica sobaricaHotela, int brojSobe) {
		Sobarica sobaricaObjekat = null;
		for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
			if (sobarica.getKorisnickoIme().equals(sobaricaHotela.getKorisnickoIme())) {
				sobaricaObjekat = sobarica;
				break;
			}
		}
		for (Soba soba : sobaricaObjekat.getSobeZaCiscenje()) {
			if (soba.getBrojSobe() == brojSobe) {
				soba.setStatus(StatusSobe.SLOBODNA);
				ArrayList<Soba> sobe = sobaricaObjekat.getSobeZaCiscenje();
				sobe.remove(soba);
				sobaricaObjekat.setSobeZaCiscenje(sobe);
				int br = Izvestaji.getInstance().getOcisceneSobe().get(sobaricaObjekat).get(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")).toString());
				Izvestaji.getInstance().getOcisceneSobe().get(sobaricaObjekat).put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")).toString(), br+1);
				break;
			}
		}
	}
	public ArrayList<Soba> vidiObaveze(Sobarica sobarica) {
		return sobarica.getSobeZaCiscenje();
	}
}