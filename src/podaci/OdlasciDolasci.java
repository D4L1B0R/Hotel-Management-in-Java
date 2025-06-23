package podaci;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import entiteti.Rezervacija;
import hotel.HotelListePodataka;

public class OdlasciDolasci {
	private static OdlasciDolasci instance;
	private HashMap<String, ArrayList<Rezervacija>> dnevniDolasci;
	private HashMap<String, ArrayList<Rezervacija>> dnevniOdlasci;
	private HashMap<String, Integer> dnevnaZauzetost;
	
	private OdlasciDolasci() {
		dnevniDolasci = new HashMap<>();
		dnevniOdlasci = new HashMap<>();
		dnevnaZauzetost = new HashMap<>();
    }
    public static OdlasciDolasci getInstance() {
        if (instance == null) {
            instance = new OdlasciDolasci();
        }
        return instance;
    }
	public void inicirajDolaskeOdlaskeZauzetost() {
		ArrayList<Rezervacija> danasnjaListaRezervacija = new ArrayList<Rezervacija>();
		dnevniDolasci.put(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), danasnjaListaRezervacija);
		dnevniOdlasci.put(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), danasnjaListaRezervacija);
		dnevnaZauzetost.put(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), 0);
	}

	public HashMap<String, ArrayList<Rezervacija>> getDnevniDolasci() {
		return dnevniDolasci;
	}

	public void setDnevniDolasci(HashMap<String, ArrayList<Rezervacija>> dnevniDolasci) {
		this.dnevniDolasci = dnevniDolasci;
	}

	public HashMap<String, ArrayList<Rezervacija>> getDnevniOdlasci() {
		return dnevniOdlasci;
	}

	public void setDnevniOdlasci(HashMap<String, ArrayList<Rezervacija>> dnevniOdlasci) {
		this.dnevniOdlasci = dnevniOdlasci;
	}

	public HashMap<String, Integer> getDnevnaZauzetost() {
		return dnevnaZauzetost;
	}

	public void setDnevnaZauzetost(HashMap<String, Integer> dnevnaZauzetost) {
		this.dnevnaZauzetost = dnevnaZauzetost;
	}
	public String toString(String datum) {
		ArrayList <Rezervacija> rezervacija = new ArrayList <Rezervacija>();
		for (String datumi : dnevniOdlasci.keySet()) {
			if (datumi.equals(datum)) {
				rezervacija = dnevniOdlasci.get(datumi);
			}
		}
		String rezStr = "";
		boolean a = false;
		for (Rezervacija rez : rezervacija) {
			rezStr += rez.getId() + ".";
			a = true;
		}
		if (!a) {
			rezStr = "0";
		}
		return datum + "|" + rezStr;
	}
	public String toString2(String datum) {
		ArrayList <Rezervacija> rezervacija = new ArrayList <Rezervacija>();
		for (String datumi : dnevniDolasci.keySet()) {
			if (datumi.equals(datum)) {
				rezervacija = dnevniDolasci.get(datumi);
			}
		}
		String rezStr = "";
		boolean a = false;
		for (Rezervacija rez : rezervacija) {
			rezStr += rez.getId() + ".";
			a = true;
		}
		if (!a) {
			rezStr = "0";
		}
		return datum + "|" + rezStr;
	}
	public String toString3(String datum) {
		int br = 0;
		for (String datumi : dnevnaZauzetost.keySet()) {
			if (datumi.equals(datum)) {
				br = dnevnaZauzetost.get(datumi);
			}
		}
		return datum + "|" + br;
	}
	public void fromString(String str) {
		String [] odlasci = str.split("\\|");
		ArrayList<Rezervacija> rez = new ArrayList<>();
		String [] rezervacije = odlasci[1].split(".");
		if (!odlasci[1].equals("0")) {
			for (String s : rezervacije) {
				if (!s.equals("")) {
					for (Rezervacija rezerve : HotelListePodataka.getInstance().getListaRezervacija()) {
						if (rezerve.getId() == Integer.parseInt(s)) {
							rez.add(rezerve);
							break;
						}
					}
				}
			}
		}
		dnevniOdlasci.put(odlasci[0], rez);
	}
	public void fromString2(String str) {
		String [] dolasci = str.split("\\|");
		ArrayList<Rezervacija> rez = new ArrayList<>();
		String [] rezervacije = dolasci[1].split(".");
		if (!dolasci[1].equals("0")) {
			for (String s : rezervacije) {
				if (!s.equals("")) {
					for (Rezervacija rezerve : HotelListePodataka.getInstance().getListaRezervacija()) {
						if (rezerve.getId() == Integer.parseInt(s)) {
							rez.add(rezerve);
							break;
						}
					}
				}
			}
		}
		dnevniDolasci.put(dolasci[0], rez);
	}
	public void fromString3(String str) {
		String [] zauzetost = str.split("\\|");
		int br = 0;
		for (String datum : dnevnaZauzetost.keySet()) {
			if (datum.equals(zauzetost[0])) {
				br = dnevnaZauzetost.get(datum);
				break;
			}
		}
		dnevnaZauzetost.put(zauzetost[0], br);
	}
}
