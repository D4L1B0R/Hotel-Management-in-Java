package podaci;

import java.util.ArrayList;
import java.util.HashMap;
import entiteti.Soba;
import hotel.HotelListePodataka;

public class SlobodneSobe {
	private static SlobodneSobe instance;
	private HashMap<Soba, ArrayList<String>> slobodneSobe;
	private HashMap<TipSobe, ArrayList<String>> slobodniTipoviSoba;
	
	private SlobodneSobe() {
		slobodneSobe = new HashMap<>();
		slobodniTipoviSoba = new HashMap<>();
    }
    public static SlobodneSobe getInstance() {
        if (instance == null) {
            instance = new SlobodneSobe();
        }
        return instance;
    }
	public HashMap<Soba, ArrayList<String>> getSlobodneSobe() {
		return slobodneSobe;
	}
	public void setSlobodneSobe(HashMap<Soba, ArrayList<String>> slobodneSobe) {
		this.slobodneSobe = slobodneSobe;
	}
	public HashMap<TipSobe, ArrayList<String>> getSlobodniTipoviSoba() {
		return slobodniTipoviSoba;
	}
	public void setSlobodniTipoviSoba(HashMap<TipSobe, ArrayList<String>> slobodniTipoviSoba) {
		this.slobodniTipoviSoba = slobodniTipoviSoba;
	}
	public ArrayList<TipSobe> proveraTipovaSlobodnihSoba(String datumPocetkaRezervacije, String datumKrajaRezervacije) {
		ArrayList<TipSobe> tipoviSlobodnihSoba = new ArrayList<>();
		for (TipSobe tipSobe : this.slobodniTipoviSoba.keySet()) {
			boolean nadjoh = true;
			if (proveraSlobodnihSoba(tipSobe.getNazivTipaSobe(), datumPocetkaRezervacije, datumKrajaRezervacije)) {
				for (String datumi : this.slobodniTipoviSoba.get(tipSobe)) {
					String [] period = datumi.split(",");
					if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.poredjenjeDatuma(datumPocetkaRezervacije, datumKrajaRezervacije, period[0], period[1]) == false) {
						nadjoh = false;
						break;
					}
				}
				if (nadjoh == true) {
					tipoviSlobodnihSoba.add(tipSobe);
				}
			}
		}
        return tipoviSlobodnihSoba;
	}
	public boolean proveraSlobodnihSoba(String tipSobe, String datumPocetka, String datumKraja) {
		for (Soba soba : this.slobodneSobe.keySet()) {
			if (soba.getTip().getNazivTipaSobe().equals(tipSobe)) {
				boolean nadjoh = true;
				for (String datumi : this.slobodneSobe.get(soba)) {
					String [] period = datumi.split(",");
					if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.poredjenjeDatuma(datumPocetka, datumKraja, period[0], period[1]) == false) {
						nadjoh = false;
						break;
					}
				}
				if (nadjoh == true) {
					return true;
				}
			}
		}
		return false;
	}
	public ArrayList<Soba> pregledSlobodnihSoba(String tipSobe, String datumPočetkaRezervacije, String datumKrajaRezervacije) {
		ArrayList<Soba> spisakSlobodnihSoba = new ArrayList<>();
		for (Soba soba : this.slobodneSobe.keySet()) {
			if (soba.getTip().getNazivTipaSobe().equals(tipSobe)) {
				boolean nadjoh = true;
				for (String datumi : this.slobodneSobe.get(soba)) {
					String [] period = datumi.split(",");
					if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.poredjenjeDatuma(datumPočetkaRezervacije, datumKrajaRezervacije, period[0], period[1]) == false) {
						nadjoh = false;
						break;
					}
				}
				if (nadjoh == true) {
					spisakSlobodnihSoba.add(soba);
				}
			}
		}
		return spisakSlobodnihSoba;
	}
	public String toString(Soba soba) {
		String str = "";
		boolean prvi = true;
		if (slobodneSobe.get(soba) != null) {
			for (String s : slobodneSobe.get(soba)) {
				str += s + "~";
				prvi = false;
			}
		}
		if (prvi == true) {
			str = "0";
		}
		return soba.getBrojSobe() + "|" + str;
	}
	public String toString(TipSobe tip) {
		String str = "";
		boolean prvi = true;
		if (slobodniTipoviSoba.get(tip) != null) {
			for (String s : slobodniTipoviSoba.get(tip)) {
				str += s + "~";
				prvi = false;
			}
		}
		if (prvi == true) {
			str = "0";
		}
		return tip.getNazivTipaSobe() + "|" + str;
	}
	public void fromString(String str) {
		String[] sobe = str.split("\\|");
		ArrayList<String> a = new ArrayList<>();
		String[] sobica = sobe[1].split("~");
		if	(!sobe[1].equals("0")) {
			for (String s : sobica) {
				if (!s.equals("")) {
					a.add(s);
				}
			}
		}
		Soba objSoba = null;
		for (Soba sob : HotelListePodataka.getInstance().getListaSoba()) {
			if (sob.getBrojSobe() == Integer.parseInt(sobe[0])) {
				objSoba = sob;
				break;
			}
		}
		slobodneSobe.put(objSoba, a);
	}
	public void fromString2(String str) {
		String[] tipovi = str.split("\\|");
		ArrayList<String> a = new ArrayList<>();
		String[] tipic = tipovi[1].split("~");
		if (!tipovi[1].equals("0")) {
			for (String s : tipic) {
				if (!s.equals("")) {
					a.add(s);
				}
			}
		}
		TipSobe objTip= null;
		for (TipSobe sob : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
			if (sob.getNazivTipaSobe().equals(tipovi[0])) {
				objTip = sob;
				break;
			}
		}
		slobodniTipoviSoba.put(objTip, a);
	}
}