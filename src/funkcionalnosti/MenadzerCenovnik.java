package funkcionalnosti;

import java.util.ArrayList;
import java.util.HashMap;

import entiteti.Osoblje;
import podaci.Cenovnik;
import podaci.DodatneUsluge;
import podaci.PrihodiRashodi;

public class MenadzerCenovnik {
	public DodatneUsluge dobijDodatnuUslugu(String dodatnaUsluga) {
		for (DodatneUsluge dodatnaUsluge : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
			if (dodatnaUsluge.getDodatneUsluge().equals(dodatnaUsluga)) {
				return dodatnaUsluge;
			}
		}
		DodatneUsluge dodatneUsluge = new DodatneUsluge(dodatnaUsluga);
		return dodatneUsluge;
	}
	public String dobijDatum (String dodatnaUsluga, String datumPocetka, String datumKraja) {
		for (DodatneUsluge dodatnaUsluge : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
			if (dodatnaUsluge.getDodatneUsluge().equals(dodatnaUsluga)) {
				for(String datumi : Cenovnik.getInstance().getDodatneUsluge().get(dodatnaUsluge).keySet()) {
					if (datumi.equals(datumPocetka + "," + datumKraja)) {
						return datumi;
					}
				}
			}
		}
		System.out.println("Nismo uspeli da pronađemo željeni sadržaj.\n");
		return null;
	}
	public void podesiCenuDodatnihUsluga(Osoblje admin, String dodatnaUsluga, String datumPocetka, String datumKraja, double cena) {
		DodatneUsluge dodatneUsluge = dobijDodatnuUslugu(dodatnaUsluga);
		String datumi = dobijDatum(dodatnaUsluga, datumPocetka, datumKraja);
		HashMap<String, Double> cenaSobePoDatumu = Cenovnik.getInstance().getDodatneUsluge().get(dodatneUsluge);
		cenaSobePoDatumu.put(datumi, cena);
		HashMap<DodatneUsluge, HashMap<String, Double>> rečnikDodatneUsluge = Cenovnik.getInstance().getDodatneUsluge();
		rečnikDodatneUsluge.put(dodatneUsluge, cenaSobePoDatumu);
		Cenovnik.getInstance().setDodatneUsluge(rečnikDodatneUsluge);
		System.out.println(admin.getKorisnickoIme() + ", uspešno ste podesili cenu " + cena + " dodatne usluge " + dodatnaUsluga + " početkom " + datumPocetka + " do " + datumKraja + ".\n");
	}
	public void inicijalizujCenuDodatnihUsluga(Osoblje admin, String dodatnaUsluga, String datumPocetka, String datumKraja, double cena) {
		DodatneUsluge dodatneUsluge = dobijDodatnuUslugu(dodatnaUsluga);
		HashMap<String, Double> datum = new HashMap<>();
		datum.put(datumPocetka + "," + datumKraja, cena);
		HashMap<DodatneUsluge, HashMap<String, Double>> rečnikDodatneUsluge = Cenovnik.getInstance().getDodatneUsluge();
		rečnikDodatneUsluge.put(dodatneUsluge, datum);
		Cenovnik.getInstance().setDodatneUsluge(rečnikDodatneUsluge);
		System.out.println(admin.getKorisnickoIme() + ", uspešno ste podesili cenu " + cena + " dodatne usluge " + dodatnaUsluga + " početkom " + datumPocetka + " do " + datumKraja + ".\n");
	}
	public void podesiDodatnuUslugu(String dodatnaUsluga, String novaUsluga) {
		DodatneUsluge uslugica = dobijDodatnuUslugu(dodatnaUsluga);
		uslugica.setDodatneUsluge(novaUsluga);
	}
	public void podesiDatumDodatneUsluge(String dodatnaUsluga, String početniDatumDodatneUsluge, String krajnjiDatumDodatneUsluge, String noviPocetniDatumTipaSobe, String noviKrajnjiDatumTipaSobe) {
		DodatneUsluge dodatneUsluge = dobijDodatnuUslugu(dodatnaUsluga);
		String datumi = dobijDatum(dodatnaUsluga, početniDatumDodatneUsluge, krajnjiDatumDodatneUsluge);
		HashMap<String, Double> recnikDatuma = Cenovnik.getInstance().getDodatneUsluge().get(dodatneUsluge);
		recnikDatuma.put(noviPocetniDatumTipaSobe + "," + noviKrajnjiDatumTipaSobe, Cenovnik.getInstance().getDodatneUsluge().get(dodatneUsluge).get(datumi));
		recnikDatuma.remove(datumi);
		HashMap<DodatneUsluge, HashMap<String, Double>> rečnikDodatneUsluge = Cenovnik.getInstance().getDodatneUsluge();
		rečnikDodatneUsluge.put(dodatneUsluge, recnikDatuma);
		Cenovnik.getInstance().setDodatneUsluge(rečnikDodatneUsluge);
	}
	public void dodajDodatnuUslugu(Osoblje admin, String dodatnaUsluga, String datumPocetka, String datumKraja, double cena) {
		HashMap<String, Double> datum = new HashMap<>();
		datum.put(datumPocetka + "," + datumKraja, cena);
		HashMap<DodatneUsluge, HashMap<String, Double>> rečnikDodatneUsluge = Cenovnik.getInstance().getDodatneUsluge();
		DodatneUsluge dodatneUsluge = dobijDodatnuUslugu(dodatnaUsluga);
		rečnikDodatneUsluge.put(dodatneUsluge, datum);
		Cenovnik.getInstance().setDodatneUsluge(rečnikDodatneUsluge);
		System.out.println(admin.getKorisnickoIme() + ", uspešno ste uneli dodatnu uslugu " + dodatnaUsluga + " sa početkom " + datumPocetka + " i završetkom " + datumKraja + " cene " + cena + ".\n");
	}
	public void inicijalizujDodatnuUslugu(Osoblje admin, String dodatnaUsluga) {
		HashMap<String, Double> datum = new HashMap<>();
		HashMap<DodatneUsluge, HashMap<String, Double>> rečnikDodatneUsluge = Cenovnik.getInstance().getDodatneUsluge();
		DodatneUsluge dodatneUsluge = dobijDodatnuUslugu(dodatnaUsluga);
		rečnikDodatneUsluge.put(dodatneUsluge, datum);
		Cenovnik.getInstance().setDodatneUsluge(rečnikDodatneUsluge);
		System.out.println(admin.getKorisnickoIme() + ", spešno ste uneli dodatnu uslugu " + dodatnaUsluga + ".\n");
	}
	public double nadjiCenuDodatneUsluge(String dodatnaUsluga, String datumPocetka, String datumKraja) {
		DodatneUsluge dodatneUsluge = dobijDodatnuUslugu(dodatnaUsluga);
		String datumi = dobijDatum(dodatnaUsluga, datumPocetka, datumKraja);
		if (datumi != null && dodatneUsluge != null){
			return Cenovnik.getInstance().getDodatneUsluge().get(dodatneUsluge).get(datumi);
		}
		return 0;
	}
	public void izbrisiDodatnuUslugu(Osoblje admin, String dodatnaUsluga) {
		DodatneUsluge dodatneUsluge = dobijDodatnuUslugu(dodatnaUsluga);
		HashMap<DodatneUsluge, HashMap<String, Double>> rečnikDodatneUsluge = Cenovnik.getInstance().getDodatneUsluge();
		rečnikDodatneUsluge.remove(dodatneUsluge);
		Cenovnik.getInstance().setDodatneUsluge(rečnikDodatneUsluge);
		System.out.println(admin.getKorisnickoIme() + ", uspešno ste obrisali dodatnu uslugu " + dodatnaUsluga + ".\n");
	}
	public void izbrisiDatumDodatneUsluge(String dodatnaUsluga, String datumPocetka, String datumKraja) {
		DodatneUsluge dodatneUsluge = dobijDodatnuUslugu(dodatnaUsluga);
		String datumi = dobijDatum(dodatnaUsluga, datumPocetka, datumKraja);
		HashMap<String, Double> rečnikDodatneUsluge = Cenovnik.getInstance().getDodatneUsluge().get(dodatneUsluge);
		rečnikDodatneUsluge.remove(datumi);
		HashMap<DodatneUsluge, HashMap<String, Double>> usluge = Cenovnik.getInstance().getDodatneUsluge();
		usluge.put(dodatneUsluge, rečnikDodatneUsluge);
		Cenovnik.getInstance().setDodatneUsluge(usluge);
	}
	public ArrayList<Double> vidiPrihode(String datumPocetka, String datumKraja) {
		ArrayList<Double> dnevniPrihodi = new ArrayList<>();
		for (String dani : PrihodiRashodi.getInstance().getDnevniPrihodi().keySet()) {
			if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(datumPocetka, dani) >= 0 && dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(dani, datumKraja) >= 0) {
				dnevniPrihodi.add(PrihodiRashodi.getInstance().getDnevniPrihodi().get(dani));
			}
		}
		return dnevniPrihodi;
	}
	public ArrayList<Double> vidiRashode(String datumPocetka, String datumKraja) {
		ArrayList<Double> dnevniRashodi = new ArrayList<>();
		for (String dani : PrihodiRashodi.getInstance().getDnevniRashodi().keySet()) {
			if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(datumPocetka, dani) >= 0 && dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(dani, datumKraja) >= 0) {
				dnevniRashodi.add(PrihodiRashodi.getInstance().getDnevniRashodi().get(dani));
			}
		}
		return dnevniRashodi;
	}
}
