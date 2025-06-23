package funkcionalnosti;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import entiteti.Gost;
import entiteti.Rezervacija;
import hotel.HotelListePodataka;
import podaci.DodatneUsluge;
import podaci.Izvestaji;
import podaci.SlobodneSobe;
import podaci.StatusRezervacije;
import podaci.TipSobe;

public class GostFunkcija {
	public ArrayList<TipSobe> pregledSlobodnihTipovaSoba(String datumPocetkaRezervacije, String datumKrajaRezervacije) {
		return SlobodneSobe.getInstance().proveraTipovaSlobodnihSoba(datumPocetkaRezervacije, datumKrajaRezervacije);
	}
	public Rezervacija rezervacijaSobe(Gost gost, int brojOsoba, String datumPocetkaRezervacije, String datumKrajaRezervacije, String tipSobe, ArrayList<DodatneUsluge> dodatneUsluge) {
		Rezervacija rezervacija = new Rezervacija(gost, brojOsoba, datumPocetkaRezervacije, datumKrajaRezervacije, tipSobe, dodatneUsluge);
		ArrayList<Rezervacija> rezervacije = HotelListePodataka.getInstance().getListaRezervacija();
		rezervacije.add(rezervacija);
		HotelListePodataka.getInstance().setListaRezervacija(rezervacije);
		return rezervacija;
	}
	public void otkazivanjeSobe (int id) {
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacija.getId() == id && rezervacija.getSoba() == null) {
				Integer[] potvrdjena = Izvestaji.getInstance().getObradaRezervacija().get(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
				potvrdjena[2] += 1;
				Izvestaji.getInstance().getObradaRezervacija().put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), potvrdjena);
				rezervacija.setStatus(StatusRezervacije.OTKAZANA);
				break;
			}
		}
	}
	public ArrayList<Rezervacija> pregledTrenutnihRezervacija(Gost gost) {
		ArrayList<Rezervacija> rezervacijeGosta = new ArrayList<>();
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacija.getGost().equals(gost)) {
				rezervacijeGosta.add(rezervacija);
			}
		}
		return rezervacijeGosta;
	}
}