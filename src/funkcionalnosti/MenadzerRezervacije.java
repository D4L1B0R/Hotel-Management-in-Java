package funkcionalnosti;

import java.util.ArrayList;
import entiteti.Gost;
import entiteti.Rezervacija;
import entiteti.Soba;
import hotel.HotelListePodataka;
import podaci.DodatneUsluge;
import podaci.StatusRezervacije;

public class MenadzerRezervacije {
	public Rezervacija potragaRezervacije(int id) {
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacija.getId() == id) {
				return rezervacija;
			}
		}
		return null;
	}
	public void promenaStatusa(int id, StatusRezervacije noviStatus) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			rezervacija.setStatus(noviStatus);
		}
	}
	public void promenaDatumaPocetkaRezervacije(int id, String noviDatumPocetkaRezervacije) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			rezervacija.setDatumPocetkaRezervacije(noviDatumPocetkaRezervacije);
		}
	}
	public void promenaDatumaKrajaRezervacije(int id, String noviDatumKrajaRezervacije) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			rezervacija.setDatumKrajaRezervacije(noviDatumKrajaRezervacije);
		}
	}
	public void promenaSobe(int id, Soba novaSoba) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			rezervacija.setSoba(novaSoba);
		}
	}
	public void promenaGosta(int id, Gost noviGost) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			rezervacija.setGost(noviGost);
		}
	}
	public void promenaBrojaOsoba(int id, int noviBrojOsoba) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			rezervacija.setBrojOsoba(noviBrojOsoba);
		}
	}
	public void promenaCene(int id, double novaCena) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			rezervacija.setCena(novaCena);
		}
	}
	public void promenaDodatnihUslugaRezervacije(int id, ArrayList<DodatneUsluge> dodatneUsluge) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			rezervacija.setDodatneUsluge(dodatneUsluge);
		}
	}
	public ArrayList<DodatneUsluge> nadjiListuDodatnihUslugaRezervacije(int id) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			return rezervacija.getDodatneUsluge();
		}
		return null;
	}
	public StatusRezervacije nadjiStatus(int id) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			return rezervacija.getStatus();
		}
		return null;
	}
	public String nadjiDatumPocetkaRezervacije(int id) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			return rezervacija.getDatumPocetkaRezervacije();
		}
		return null;
	}
	public String nadjiDatumKrajaRezervacije(int id, String noviDatumKrajaRezervacije) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			return rezervacija.getDatumKrajaRezervacije();
		}
		return null;
	}
	public Soba nadjiSobu(int id) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			return rezervacija.getSoba();
		}
		return null;
	}
	public Gost nadjiGosta(int id) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			return rezervacija.getGost();
		}
		return null;
	}
	public int nadjiBrojOsoba(int id) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			return rezervacija.getBrojOsoba();
		}
		return 0;
	}
	public double nadjiCenu(int id) {
		Rezervacija rezervacija = potragaRezervacije(id);
		if (rezervacija != null) {
			return rezervacija.getCena();
		}
		return 0;
	}
	public ArrayList<Rezervacija> pronadjiRezervacijuPoKorisniku(String imeGosta, String prezimeGosta) {
		ArrayList<Rezervacija> pretragaRezervacijaPoKorisniku = new ArrayList<>();
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacija.getGost().getIme().equals(imeGosta) && rezervacija.getGost().getPrezime().equals(prezimeGosta)) {
				pretragaRezervacijaPoKorisniku.add(rezervacija);
			}
		}
		return pretragaRezervacijaPoKorisniku;
	}
}