package podaci;

import java.util.HashMap;

public class Cenovnik{
	private static Cenovnik instance;
	private HashMap<TipSobe, HashMap<String, Double>> ceneTipovaSoba = new HashMap<>();
	private HashMap<DodatneUsluge, HashMap<String, Double>> ceneDodatnihUsluga = new HashMap<>();
 	
    public static Cenovnik getInstance() {
        if (instance == null) {
            instance = new Cenovnik();
        }
        return instance;
    }
	public double getPocetnaCena(String tipSobe, String datumPocetka, String datumKraja) {
		double ukupnaCena = 0;
		for (TipSobe vrsteSoba : ceneTipovaSoba.keySet()) {
			if (vrsteSoba.getNazivTipaSobe().equals(tipSobe)) {
				for (String datumiVaženja : dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.listaDatumaZaPeriod(datumPocetka, datumKraja)) {
					for (String datumiPoSobi : ceneTipovaSoba.get(vrsteSoba).keySet()) {
						String [] period = datumiPoSobi.split(",");
						if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(period[0], datumiVaženja) > 0 && dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(datumiVaženja, period[1]) > 0) {
							ukupnaCena += ceneTipovaSoba.get(vrsteSoba).get(datumiPoSobi);
						}
					}
				}
			}
		}
		return ukupnaCena;
	}
	public double getDodatneUsluge(String dodatnaUsluga, String datumPocetka, String datumKraja) {
		double ukupnaCena = 0;
		for (DodatneUsluge vrsteSoba : ceneDodatnihUsluga.keySet()) {
			if (vrsteSoba.getDodatneUsluge().equals(dodatnaUsluga)) {
				for (String datumiVaženja : dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.listaDatumaZaPeriod(datumPocetka, datumKraja)) {
					for (String datumiPoSobi : ceneDodatnihUsluga.get(vrsteSoba).keySet()) {
						String [] period = datumiPoSobi.split(",");
						if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(period[0], datumiVaženja) > 0 && dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(datumiVaženja, period[1]) > 0) {
							ukupnaCena += ceneDodatnihUsluga.get(vrsteSoba).get(datumiPoSobi);
						}
					}
				}
			}
		}
		return ukupnaCena;
	}
	public HashMap<TipSobe, HashMap<String, Double>> getCeneTipovaSoba() {
		return ceneTipovaSoba;
	}
	public void setCeneTipovaSoba(HashMap<TipSobe, HashMap<String, Double>> ceneTipovaSoba) {
		this.ceneTipovaSoba = ceneTipovaSoba;
	}
	public HashMap<DodatneUsluge, HashMap<String, Double>> getDodatneUsluge() {
		return ceneDodatnihUsluga;
	}
	public void setDodatneUsluge(HashMap<DodatneUsluge, HashMap<String, Double>> dodatneUsluge) {
		this.ceneDodatnihUsluga = dodatneUsluge;
	}
	public String toString(TipSobe tipSobe, String datum) {
		return tipSobe.getNazivTipaSobe() + "|" + datum + "|" + ceneTipovaSoba.get(tipSobe).get(datum);
	}
	public String toString(DodatneUsluge dodatneUsluge, String datum) {
		return dodatneUsluge.getDodatneUsluge() + "|" + datum + "|" + ceneDodatnihUsluga.get(dodatneUsluge).get(datum);
	}
	public void fromString(String str) {
	    String [] tip = str.split("\\|");
	    HashMap <String, Double> a = new HashMap<>();
	    a.put(tip[1], Double.parseDouble(tip[2]));
	    TipSobe tipic = null;

	    if (ceneTipovaSoba != null) {
	        for (TipSobe tipovi : ceneTipovaSoba.keySet()) {
	            if (tipovi.getNazivTipaSobe().equals(tip[0])) {
	                tipic = tipovi;
	                ceneTipovaSoba.get(tipovi).putAll(a);
	                return;
	            }
	        }
	    }

	    tipic = new TipSobe(tip[0]);
	    ceneTipovaSoba.put(tipic, a);
	}
	public void fromString2(String str) {
	    String [] usluge = str.split("\\|");
	    HashMap <String, Double> a = new HashMap<>();
	    a.put(usluge[1], Double.parseDouble(usluge[2]));
	    DodatneUsluge uslugica = null;

	    if (ceneDodatnihUsluga != null) {
	        for (DodatneUsluge du : ceneDodatnihUsluga.keySet()) {
	            if (du.getDodatneUsluge().equals(usluge[0])) {
	                uslugica = du;
	                ceneDodatnihUsluga.get(du).putAll(a);
	                return;
	            }
	        }
	    }

	    uslugica = new DodatneUsluge(usluge[0]);
	    ceneDodatnihUsluga.put(uslugica, a);
	}
}
