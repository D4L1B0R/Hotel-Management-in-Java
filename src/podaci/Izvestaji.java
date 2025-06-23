package podaci;

import java.util.ArrayList;
import java.util.HashMap;
import entiteti.Soba;
import entiteti.Sobarica;
import hotel.HotelListePodataka;

public class Izvestaji {
	private HashMap<Sobarica, HashMap<String, Integer>> ocisceneSobe;
	private HashMap<String, Integer[]> obradaRezervacija;
	private HashMap<Soba, ArrayList<String>> nocenjeSoba;
	
	private static Izvestaji instance;

	private Izvestaji() {
		ocisceneSobe = new HashMap<>();
		obradaRezervacija = new HashMap<>();
		nocenjeSoba = new HashMap<>();
	}
	
	public static Izvestaji getInstance() {
		if (instance == null) {
			instance = new Izvestaji();
		}
		return instance;
	}
	
	public String toStringNocenje(Soba soba) {
		String str = "";
		boolean prvi = true;
		for (String s : nocenjeSoba.get(soba)) {
            str += s + "~";
            prvi = false;
        }
		if (prvi) {
			str = "0";
		}
		return soba.getBrojSobe() + "|" + str;
	}
	
	public String toStringOcisceneSobe(Sobarica sobarica, String datum) {
		return sobarica.getKorisnickoIme() + "|" + datum + "|" + ocisceneSobe.get(sobarica).get(datum);
	}
	
	public String toStringObradaRezervacija(String datum) {
		return datum + "|" + obradaRezervacija.get(datum)[0] + "|" + obradaRezervacija.get(datum)[1] + "|" + obradaRezervacija.get(datum)[2];
	}
	
	public void fromStringNocenje(String str) {
		String[] tokens = str.split("\\|");
		for (Soba sobica : HotelListePodataka.getInstance().getListaSoba()) {
			if (sobica.getBrojSobe() == Integer.parseInt(tokens[0])) {
				ArrayList<String> lista = new ArrayList<>();
				if (!tokens[1].equals("0")) {
					for (String s : tokens[1].split("~")) {
						if (s.equals("")) {
							continue;
						}
						lista.add(s);
					}
				}
				nocenjeSoba.put(sobica, lista);
			}
		}
	}
	
	public void fromStringRezervacije(String str) {
	    String[] tokens = str.split("\\|");
	    Integer[] niz = new Integer[3];
	    if (tokens[1] != null && tokens[2] != null && tokens[3] != null) {
	        if (isNumeric(tokens[1]) && isNumeric(tokens[2]) && isNumeric(tokens[3])) {
	            niz[0] = Integer.parseInt(tokens[1]);
	            niz[1] = Integer.parseInt(tokens[2]);
	            niz[2] = Integer.parseInt(tokens[3]);
	            obradaRezervacija.put(tokens[0], niz);
	        }
	    }
	}
	
	private boolean isNumeric(String str) {
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	
	public void fromStringSobe(String str) {
		String[] tokens = str.split("\\|");
		for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
			if (sobarica.getKorisnickoIme().equals(tokens[0])) {
				HashMap<String, Integer> mapa = new HashMap<>();
				mapa.put(tokens[1], Integer.parseInt(tokens[2]));
				ocisceneSobe.put(sobarica, mapa);
			}
		}
	}
	
	public HashMap<Sobarica, HashMap<String, Integer>> getOcisceneSobe() {
		return ocisceneSobe;
	}

	public void setOcisceneSobe(HashMap<Sobarica, HashMap<String, Integer>> ocisceneSobe) {
		this.ocisceneSobe = ocisceneSobe;
	}

	public HashMap<String, Integer[]> getObradaRezervacija() {
		return obradaRezervacija;
	}

	public void setObradaRezervacija(HashMap<String, Integer[]> obradaRezervacija) {
		this.obradaRezervacija = obradaRezervacija;
	}

	public HashMap<Soba, ArrayList<String>> getNocenjeSoba() {
		return nocenjeSoba;
	}

	public void setNocenjeSoba(HashMap<Soba, ArrayList<String>> nocenjeSoba) {
		this.nocenjeSoba = nocenjeSoba;
	}
}