package funkcionalnosti;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import entiteti.Rezervacija;
import entiteti.Soba;
import entiteti.Sobarica;
import hotel.HotelListePodataka;
import podaci.Izvestaji;
import podaci.OdlasciDolasci;
import podaci.PrihodiRashodi;
import podaci.SlobodneSobe;
import podaci.StatusSobe;
import podaci.TipSobe;

public class RecepcionerSobe {
	public void popunjavanjeSobe(int id, int brojSobe) {
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == brojSobe && soba.getStatus().equals(StatusSobe.SLOBODNA)) {
				soba.setStatus(StatusSobe.ZAUZETA);
				for (Rezervacija rezervacije : HotelListePodataka.getInstance().getListaRezervacija()) {
					if (rezervacije.getId() == id) {
						ArrayList<Rezervacija> dolasci = OdlasciDolasci.getInstance().getDnevniDolasci().get(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
						dolasci.add(rezervacije);
						rezervacije.setSoba(soba);
						String datumPočetkaRezervacije = rezervacije.getDatumPocetkaRezervacije();
						String datumKrajaRezervacije = rezervacije.getDatumKrajaRezervacije();
						ArrayList<String> datumi = SlobodneSobe.getInstance().getSlobodneSobe().get(soba);
						datumi.add(datumPočetkaRezervacije + "," + datumKrajaRezervacije);
						HashMap<Soba, ArrayList<String>> slobodneSobe = SlobodneSobe.getInstance().getSlobodneSobe();
						slobodneSobe.put(soba, datumi);
						SlobodneSobe.getInstance().setSlobodneSobe(slobodneSobe);
						HashMap<String, ArrayList<Rezervacija>> dolasciLista = OdlasciDolasci.getInstance().getDnevniDolasci();
						dolasciLista.put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), dolasci);
						OdlasciDolasci.getInstance().setDnevniDolasci(dolasciLista);
						int brojZauzetihSoba = OdlasciDolasci.getInstance().getDnevnaZauzetost().get(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
						HashMap<String, Integer> zauzetost = OdlasciDolasci.getInstance().getDnevnaZauzetost();
						zauzetost.put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), brojZauzetihSoba+1);
						OdlasciDolasci.getInstance().setDnevnaZauzetost(zauzetost);
						ArrayList<Rezervacija> listaRezervacija = soba.getListaRezervacija();
						listaRezervacija.add(rezervacije);
						soba.setListaRezervacija(listaRezervacija);
						ArrayList<String> sobeList = Izvestaji.getInstance().getNocenjeSoba().get(soba);
						sobeList.add(datumPočetkaRezervacije + "," + datumKrajaRezervacije);
						Izvestaji.getInstance().getNocenjeSoba().put(soba, sobeList);
						break;
					}
				}
			}
		}
	}
	public void oslobadjanjeSobe(int id, int brojSobe) {
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == brojSobe) {
				soba.setStatus(StatusSobe.SPREMANJE);
				for (Rezervacija rezervacije : HotelListePodataka.getInstance().getListaRezervacija()) {
					if (rezervacije.getId() == id) {
						String datumPocetkaRezervacije = rezervacije.getDatumPocetkaRezervacije();
						String datumKrajaRezervacije = rezervacije.getDatumKrajaRezervacije();
						rezervacije.setSoba(null);
						Double dnevniPrihod = PrihodiRashodi.getInstance().getDnevniPrihodi().get(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) + rezervacije.getCena();
						PrihodiRashodi.getInstance().getDnevniPrihodi().put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), dnevniPrihod);
						Double prihodi = PrihodiRashodi.getInstance().getPrihodiPoTipuSobe().get(rezervacije.getTipSobe()).get(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) + rezervacije.getCena();
						PrihodiRashodi.getInstance().getPrihodiPoTipuSobe().get(rezervacije.getTipSobe()).put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), prihodi);
						ArrayList<String> sobeList = Izvestaji.getInstance().getNocenjeSoba().get(soba);
						sobeList.add(datumPocetkaRezervacije + "," + datumKrajaRezervacije);
						Izvestaji.getInstance().getNocenjeSoba().put(soba, sobeList);
						HashMap<Soba, ArrayList<String>> slobodneSobe = SlobodneSobe.getInstance().getSlobodneSobe();
						ArrayList<String> datumi = SlobodneSobe.getInstance().getSlobodneSobe().get(soba);
						datumi.remove(datumPocetkaRezervacije + "," + datumKrajaRezervacije);
						slobodneSobe.put(soba, datumi);
						SlobodneSobe.getInstance().setSlobodneSobe(slobodneSobe);
						ArrayList<Rezervacija> odlasci = OdlasciDolasci.getInstance().getDnevniOdlasci().get(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
						odlasci.add(rezervacije);
						HashMap<String, ArrayList<Rezervacija>> odlasciLista = OdlasciDolasci.getInstance().getDnevniOdlasci();
						odlasciLista.put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), odlasci);
						OdlasciDolasci.getInstance().setDnevniOdlasci(odlasciLista);
						ArrayList<Rezervacija> listaRezervacija = soba.getListaRezervacija();
						listaRezervacija.remove(rezervacije);
						soba.setListaRezervacija(listaRezervacija);
						Izvestaji.getInstance().getNocenjeSoba().remove(soba);
						for (TipSobe tipoviSoba : SlobodneSobe.getInstance().getSlobodniTipoviSoba().keySet()) {
							if (tipoviSoba.equals(soba.getTip())) {
								ArrayList<String> listaDatuma = SlobodneSobe.getInstance().getSlobodniTipoviSoba().get(tipoviSoba);
								listaDatuma.remove(rezervacije.getDatumPocetkaRezervacije() + "," + rezervacije.getDatumKrajaRezervacije());
								HashMap<TipSobe, ArrayList<String>> tipoviSobaLista = SlobodneSobe.getInstance().getSlobodniTipoviSoba();
								tipoviSobaLista.put(tipoviSoba, listaDatuma);
								SlobodneSobe.getInstance().setSlobodniTipoviSoba(tipoviSobaLista);
							}
						}
					}
				}
				int minimum = 10000;
				Sobarica pronadjena = null;
				for (Sobarica zaposleni : HotelListePodataka.getInstance().getListaSobarica()) {
					if (zaposleni.getSobeZaCiscenje().size() < minimum) {
						pronadjena = zaposleni;
					}
				}
				ArrayList<Soba> sobe = pronadjena.getSobeZaCiscenje();
				sobe.add(soba);
				pronadjena.setSobeZaCiscenje(sobe);
				break;
			}
		}
	}
}