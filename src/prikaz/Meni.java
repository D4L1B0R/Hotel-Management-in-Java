package prikaz;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import entiteti.Osoblje;
import entiteti.Rezervacija;
import entiteti.Sobarica;
import hotel.HotelListePodataka;
import podaci.Izvestaji;
import podaci.OdlasciDolasci;
import podaci.PrihodiRashodi;
import podaci.TipSobe;

public class Meni {
	public static void main(String[] args) {
        IspisZapis main = new IspisZapis();
        main.zapisPodataka();
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
		Prijava prijava = new Prijava(() -> {
        	main.ispisPodataka();
            System.exit(0);
        });
        prijava.setVisible(true);
    }
}