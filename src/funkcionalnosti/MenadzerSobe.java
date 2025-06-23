package funkcionalnosti;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import podaci.Cenovnik;
import podaci.Izvestaji;
import podaci.PrihodiRashodi;
import podaci.SlobodneSobe;
import podaci.SpecijalnePogodnosti;
import podaci.StatusSobe;
import podaci.TipSobe;
import entiteti.Osoblje;
import entiteti.Rezervacija;
import entiteti.Soba;
import hotel.HotelListePodataka;

public class MenadzerSobe {
	public TipSobe dobijTipSobe(String tipSobe) {
		if (Cenovnik.getInstance().getCeneTipovaSoba() != null) {
			for (TipSobe tipSoba : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
				if (tipSoba.getNazivTipaSobe().equals(tipSobe)) {
					return tipSoba;
				}
			}
		}
		TipSobe tipSoba = new TipSobe(tipSobe);
		return tipSoba;
	}
	public String dobijDatum (String tipSobe, String datumPocetka, String datumKraja) {
		for (TipSobe tipSoba : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
			if (tipSoba.getNazivTipaSobe().equals(tipSobe)) {
				for(String datumi : Cenovnik.getInstance().getCeneTipovaSoba().get(tipSoba).keySet()) {
					if (datumi.equals(datumPocetka + "," + datumKraja)) {
						return datumi;
					}
				}
			}
		}
		System.out.println("Nismo uspeli da pronađemo željeni sadržaj.\n");
		return null;
	}
	public void podesiPočetnuCenuSobe(Osoblje admin, String tipSobe, String datumPocetka, String datumKraja, double cena) {
		TipSobe tipSoba = dobijTipSobe(tipSobe);
		String datumi = dobijDatum(tipSobe, datumPocetka, datumKraja);
		HashMap<String, Double> tipoviSobaDatumi = Cenovnik.getInstance().getCeneTipovaSoba().get(tipSoba);
		tipoviSobaDatumi.put(datumi, cena);
		HashMap<TipSobe, HashMap<String, Double>> tipoviSoba = Cenovnik.getInstance().getCeneTipovaSoba();
		tipoviSoba.put(tipSoba, tipoviSobaDatumi);
		Cenovnik.getInstance().setCeneTipovaSoba(tipoviSoba);
		System.out.println(admin.getKorisnickoIme() + ", uspešno ste podesili cenu " + cena + " tipa sobe " + tipSobe + " početkom " + datumPocetka + " do " + datumKraja + ".\n");
	}
	public void inicijalizujPocetnuCenuSobe(Osoblje admin, String tipSobe, String datumPocetka, String datumKraja, double cena) {
		TipSobe tipSoba = dobijTipSobe(tipSobe);
		HashMap<String, Double> datum = new HashMap<>();
		datum.put(datumPocetka + "," + datumKraja, cena);
		HashMap<TipSobe, HashMap<String, Double>> tipoviSoba = Cenovnik.getInstance().getCeneTipovaSoba();
		tipoviSoba.put(tipSoba, datum);
		System.out.println(admin.getKorisnickoIme() + ", uspešno ste podesili cenu " + cena + " tipa sobe " + tipSobe + " početkom " + datumPocetka + " do " + datumKraja + ".\n");
		ArrayList<String> datumiTipova = new ArrayList<String>();
		datumiTipova.add(datumPocetka + "," + datumKraja);
		HashMap<TipSobe, ArrayList<String>> slobodneSobe = SlobodneSobe.getInstance().getSlobodniTipoviSoba();
		slobodneSobe.put(tipSoba, datumiTipova);
		SlobodneSobe.getInstance().setSlobodniTipoviSoba(slobodneSobe);
	}
	public void podesiPocetnuSobu(String tipSobe, String noviTipSobe) {
		TipSobe tipSoba = dobijTipSobe(tipSobe);
		tipSoba.setNazivTipaSobe(noviTipSobe);
	}
	public void podesiDatumPocetneSobe(String tipSobe, String pocetniDatumTipaSobe, String krajnjiDatumTipaSobe, String noviPocetniDatumTipaSobe, String noviKrajnjiDatumTipaSobe) {
		TipSobe tipSoba = dobijTipSobe(tipSobe);
		String datumi = dobijDatum(tipSobe, pocetniDatumTipaSobe, krajnjiDatumTipaSobe);
		HashMap<String, Double> recnikDatuma = Cenovnik.getInstance().getCeneTipovaSoba().get(tipSoba);
		recnikDatuma.put(noviPocetniDatumTipaSobe + "," + noviKrajnjiDatumTipaSobe, Cenovnik.getInstance().getCeneTipovaSoba().get(tipSoba).get(datumi));
		HashMap<TipSobe, HashMap<String, Double>> tipoviSoba = Cenovnik.getInstance().getCeneTipovaSoba();
		tipoviSoba.put(tipSoba, recnikDatuma);
		Cenovnik.getInstance().setCeneTipovaSoba(tipoviSoba);
	}
	public void dodajPočetnuSobu(Osoblje admin, String tipSobe, String datumPocetka, String datumKraja, double cena) {
		HashMap<String, Double> datum = new HashMap<>();
		datum.put(datumPocetka + "," + datumKraja, cena);
		HashMap<TipSobe, HashMap<String, Double>> tipoviSoba = Cenovnik.getInstance().getCeneTipovaSoba();
		TipSobe tipSoba = dobijTipSobe(tipSobe);
		tipoviSoba.put(tipSoba, datum);
		Cenovnik.getInstance().setCeneTipovaSoba(tipoviSoba);
		System.out.println(admin.getKorisnickoIme() + ", uspešno ste uneli tip sobe " + tipSobe + " sa početkom " + datumPocetka + " i završetkom " + datumKraja + " cene " + cena + ".\n");
	}
	public void inicijalizujPocetnuSobu(Osoblje admin, String tipSobe) {
		HashMap<String, Double> datum = new HashMap<>();
		HashMap<TipSobe, HashMap<String, Double>> tipoviSoba = Cenovnik.getInstance().getCeneTipovaSoba();
		TipSobe tipSoba = dobijTipSobe(tipSobe);
		tipoviSoba.put(tipSoba, datum);
		Cenovnik.getInstance().setCeneTipovaSoba(tipoviSoba);
		HashMap<String, Double> prihodiPoTipuSobe = new HashMap<>();
		prihodiPoTipuSobe.put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), 0.0);
		PrihodiRashodi.getInstance().getPrihodiPoTipuSobe().put(tipSoba, prihodiPoTipuSobe);
		System.out.println(admin.getKorisnickoIme() + ", uspešno ste uneli tip sobe " + tipSobe + ".\n");
	}
	public double nadjiPocetnuCenuSobe(String tipSobe, String datumPocetka, String datumKraja) {
		TipSobe tipSoba = dobijTipSobe(tipSobe);
		String datumi = dobijDatum(tipSobe, datumPocetka, datumKraja);
		if (tipSoba != null && datumi != null) {
			return Cenovnik.getInstance().getCeneTipovaSoba().get(tipSoba).get(datumi);
		}
		return 0;
	}
	public void izbrisiPocetnuSobu(String tipSobe) {
		TipSobe tipSoba = dobijTipSobe(tipSobe);
		HashMap<TipSobe, HashMap<String, Double>> tipoviSobe = Cenovnik.getInstance().getCeneTipovaSoba();
		tipoviSobe.remove(tipSoba);
		Cenovnik.getInstance().setCeneTipovaSoba(tipoviSobe);
		PrihodiRashodi.getInstance().getPrihodiPoTipuSobe().remove(tipSoba);
	}
	public void izbrisiDatumPocetneSobe(String tipSobe, String datumPocetka, String datumKraja) {
		TipSobe tipSoba = dobijTipSobe(tipSobe);
		String datumi = dobijDatum(tipSobe, datumPocetka, datumKraja);
		HashMap<String, Double> tipoviSobaDatumi = Cenovnik.getInstance().getCeneTipovaSoba().get(tipSoba);
		tipoviSobaDatumi.remove(datumi);
		HashMap<TipSobe, HashMap<String, Double>> usluge = Cenovnik.getInstance().getCeneTipovaSoba();
		usluge.put(tipSoba, tipoviSobaDatumi);
		Cenovnik.getInstance().setCeneTipovaSoba(usluge);
	}
	public boolean proveraSlobodnihSoba(String tipSobe, String datumPocetka, String datumKraja) {
		return SlobodneSobe.getInstance().proveraSlobodnihSoba(tipSobe, datumPocetka, datumKraja);
	}
	public ArrayList<TipSobe> proveraTipovaSlobodnihSoba(String datumPocetkaRezervacije, String datumKrajaRezervacije) {
		return SlobodneSobe.getInstance().proveraTipovaSlobodnihSoba(datumPocetkaRezervacije, datumKrajaRezervacije);
	}
	public void dodajSobu (Osoblje admin, int noviBrojSobe, String tip, StatusSobe status, ArrayList<SpecijalnePogodnosti> pogodnosti) {
		boolean postoji = true;
		for (Soba sobe : HotelListePodataka.getInstance().getListaSoba()) {
			if (sobe.getBrojSobe() == noviBrojSobe) {
				postoji = false;
			}  
		}
		if (postoji == false) {
			System.out.println("Taj broj sobe je zauzet!\n");
		}
		else {
			for (TipSobe tipSoba: Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
				if (tipSoba.getNazivTipaSobe().equals(tip)) {
					Soba soba = new Soba(noviBrojSobe, tipSoba, status, pogodnosti);
					ArrayList<Soba> sobe = HotelListePodataka.getInstance().getListaSoba();
					sobe.add(soba);
					HotelListePodataka.getInstance().setListaSoba(sobe);
					ArrayList<String> datumi = new ArrayList<>();
					HashMap<Soba, ArrayList<String>> slobodneSobe = SlobodneSobe.getInstance().getSlobodneSobe();
					slobodneSobe.put(soba, datumi);
					SlobodneSobe.getInstance().setSlobodneSobe(slobodneSobe);
					HashMap<TipSobe, ArrayList<String>> slobodneSobeTipovi = SlobodneSobe.getInstance().getSlobodniTipoviSoba();
					slobodneSobeTipovi.put(tipSoba, datumi);
					SlobodneSobe.getInstance().setSlobodniTipoviSoba(slobodneSobeTipovi);
					Izvestaji.getInstance().getNocenjeSoba().put(soba, new ArrayList<String>());
					System.out.println(admin.getKorisnickoIme() + ", uspešno ste dodali sobu tipa (" + tip + ") broja "  + noviBrojSobe + ".\n");
					break;
				}
			}
		}
	}
	public void obrisiSobu (int brojSobe) {
		Iterator<Soba> iterator = HotelListePodataka.getInstance().getListaSoba().iterator();
	    while (iterator.hasNext()) {
	        Soba soba = iterator.next();
	        if (soba.getBrojSobe() == brojSobe) {
	            iterator.remove();
	            HashMap<Soba, ArrayList<String>> slobodneSobe = SlobodneSobe.getInstance().getSlobodneSobe();
	            slobodneSobe.remove(soba);
	            SlobodneSobe.getInstance().setSlobodneSobe(slobodneSobe);
	            HashMap<TipSobe, ArrayList<String>> slobodneSobeTipovi = SlobodneSobe.getInstance().getSlobodniTipoviSoba();
	            slobodneSobeTipovi.remove(soba.getTip());
	            SlobodneSobe.getInstance().setSlobodniTipoviSoba(slobodneSobeTipovi);
	            Izvestaji.getInstance().getNocenjeSoba().remove(soba);
	        }
	    }
	}
	public boolean proveraBrojaSobe(int noviBrojSobe) {
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == noviBrojSobe) {
				return false;
			}
		}
		return true;
	}
	public void promenaBrojaSobe(int brojSobe, int noviBrojSobe) {
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == brojSobe) {
				if(proveraBrojaSobe(noviBrojSobe) == true) {
					soba.setBrojSobe(noviBrojSobe);
				}
			}
		}
	}
	public void promenaTipaSobe(Osoblje admin, int brojSobe, String noviTipSobe) {
		boolean nađena = false;
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == brojSobe) {
				System.out.println(admin.getKorisnickoIme() + ", uspešno ste promenili tip sobe " + brojSobe + " iz " + soba.getTip() + " u " + noviTipSobe + ".\n");
				for (TipSobe tipSoba: Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
					if (tipSoba.getNazivTipaSobe().equals(noviTipSobe)) {
						soba.setTip(tipSoba);
						break;
					}
				}
				nađena = true;
			}
		}
		if (nađena == false) {
			System.out.println("Nismo uspeli da pronađemo željeni broj sobe u sistemu!\n");
		}
	}
	public void promenaStatusaSobe(int brojSobe, StatusSobe noviStatus) {
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == brojSobe) {
				soba.setStatus(noviStatus);
			}
		}
	}
	public StatusSobe nadjiStatusSobe(int brojSobe) {
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == brojSobe) {
				return soba.getStatus();
			}
		}
		return null;
	}
	public TipSobe nadjiTipSobe(int brojSobe) {
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == brojSobe) {
				return soba.getTip();
			}
		}
		return null;
	}
	public ArrayList<Rezervacija> getListaRezervacija(int brojSobe){
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == brojSobe) {
				return soba.getListaRezervacija();
			}
		}
		return null;
	}
	public void setListaRezervacija(int brojSobe, ArrayList<Rezervacija> rezervacije){
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == brojSobe) {
				soba.setListaRezervacija(rezervacije);
			}
		}
	}
}