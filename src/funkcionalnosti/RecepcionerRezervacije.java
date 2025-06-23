package funkcionalnosti;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import entiteti.Rezervacija;
import entiteti.Soba;
import hotel.HotelListePodataka;
import podaci.Izvestaji;
import podaci.SlobodneSobe;
import podaci.StatusRezervacije;
import podaci.TipSobe;

public class RecepcionerRezervacije {
	private static Rezervacija pronadjiteRezervaciju(int id) {
		for (Rezervacija rezervacije : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacije.getId() == id) {
				return rezervacije;
			}
		}
		return null;  
	} 
	public void potvrdaRezervacije(int id) {
		for (Rezervacija rezervacije : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacije.getId() == id) {
				String datumPocetkaRezervacije = rezervacije.getDatumPocetkaRezervacije();
				String datumKrajaRezervacije = rezervacije.getDatumKrajaRezervacije();
				boolean status = SlobodneSobe.getInstance().proveraSlobodnihSoba(rezervacije.getTipSobe().getNazivTipaSobe(), datumPocetkaRezervacije, datumKrajaRezervacije);
				Rezervacija rezervacija = pronadjiteRezervaciju(id);
				if (status == true && rezervacija != null && (rezervacija.getCena() - rezervacija.getCenaDodatnihUsluga()) > 0) {
					rezervacija.setStatus(StatusRezervacije.POTVRĐENA);
					Integer[] potvrdjena = Izvestaji.getInstance().getObradaRezervacija().get(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
					potvrdjena[0] += 1;
					Izvestaji.getInstance().getObradaRezervacija().put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), potvrdjena);
					for (TipSobe tipoviSoba : SlobodneSobe.getInstance().getSlobodniTipoviSoba().keySet()) {
						if (tipoviSoba.equals(rezervacije.getTipSobe())) {
							ArrayList<String> listaDatuma = SlobodneSobe.getInstance().getSlobodniTipoviSoba().get(tipoviSoba);
							listaDatuma.add(rezervacije.getDatumPocetkaRezervacije() + "," + rezervacije.getDatumKrajaRezervacije());
							HashMap<TipSobe, ArrayList<String>> slobodneSobe = SlobodneSobe.getInstance().getSlobodniTipoviSoba();
							slobodneSobe.put(tipoviSoba, listaDatuma);
							SlobodneSobe.getInstance().setSlobodniTipoviSoba(slobodneSobe);
							break;
						}
					}
				} else {
					rezervacija.setStatus(StatusRezervacije.ODBIJENA);
					rezervacija.setCena(0);
					Integer[] potvrdjena = Izvestaji.getInstance().getObradaRezervacija().get(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
					potvrdjena[1] += 1;
					Izvestaji.getInstance().getObradaRezervacija().put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), potvrdjena);
				}
				break;
			}
		}
	}
	public void azuriranjeRezervacija() {
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacija.getStatus().equals(StatusRezervacije.POTVRĐENA) && rezervacija.getSoba() == null && dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(rezervacija.getDatumPocetkaRezervacije(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) < 0) {
				rezervacija.setStatus(StatusRezervacije.ODBIJENA);
				Integer[] obrada = Izvestaji.getInstance().getObradaRezervacija().get(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
				obrada[1] += 1;
				Izvestaji.getInstance().getObradaRezervacija().put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), obrada);
				rezervacija.setCena(0);
			}
		}
	}
	
	public ArrayList<Soba> pregledSlobodnihSoba(String tipSobe, String datumPocetkaRezervacije, String datumKrajaRezervacije) {
		return SlobodneSobe.getInstance().pregledSlobodnihSoba(tipSobe, datumPocetkaRezervacije, datumKrajaRezervacije);
	}
}
