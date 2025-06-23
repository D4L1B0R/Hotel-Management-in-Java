package funkcionalnosti;

import java.util.ArrayList;
import entiteti.Gost;
import entiteti.Rezervacija;
import hotel.HotelListePodataka;
import podaci.OdlasciDolasci;
import podaci.Pol;

public class RecepcionerGosti {
	public void dodavanjeGosta (String ime, String prezime, Pol pol, String datumRodjenja, int telefon, String adresa, String korisnickoIme, String lozinka) {
		Gost gost = new Gost(ime, prezime, pol, datumRodjenja, telefon, adresa, korisnickoIme, lozinka);
		ArrayList<Gost> listaGostiju = HotelListePodataka.getInstance().getListaGostiju();
		listaGostiju.add(gost);
		HotelListePodataka.getInstance().setListaGostiju(listaGostiju);
	}
	public void brisanjeGosta (String korisnickoIme) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			ArrayList<Gost> listaGostiju = HotelListePodataka.getInstance().getListaGostiju();
			listaGostiju.remove(gost);
			HotelListePodataka.getInstance().setListaGostiju(listaGostiju);
		}
	}
	public ArrayList<Rezervacija> dnevniDolasci(String dan) {
		for (String dani : OdlasciDolasci.getInstance().getDnevniDolasci().keySet()) {
			if (dani.equals(dan)) {
				return OdlasciDolasci.getInstance().getDnevniDolasci().get(dani);
			}
		}
		return null;
	}
	public ArrayList<Rezervacija> dnevniOdlasci(String dan) {
		for (String dani : OdlasciDolasci.getInstance().getDnevniOdlasci().keySet()) {
			if (dani.equals(dan)) {
				return OdlasciDolasci.getInstance().getDnevniOdlasci().get(dani);
			}
		}
		return null;
	}
	public int dnevnaZauzetost(String dan) {
		for (String dani : OdlasciDolasci.getInstance().getDnevnaZauzetost().keySet()) {
			if (dani.equals(dan)) {
				return OdlasciDolasci.getInstance().getDnevnaZauzetost().get(dani);
			}
		}
		return 0;
	}
	public Gost pronadjiGosta(String korisnickoIme){
	    for (Gost gost : HotelListePodataka.getInstance().getListaGostiju()) {
	        if (gost.getKorisnickoIme().equals(korisnickoIme)) {
	            return gost;
	        }
	    } return null;
	}
	public void promenaPrezimenaGosta (String korisnickoIme, String novoPrezime) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			gost.setPrezime(novoPrezime);
		}
	}
	public void promenaImenaGosta (String korisnickoIme, String novoIme) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			gost.setIme(novoIme);
		}
	}
	public void promenaPolaGosta (String korisnickoIme, Pol noviPol) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			gost.setPol(noviPol);
		}
	}
	public void promenaDatumaRodjenjaGosta (String korisnickoIme, String novDatumRodjenja) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			gost.setDatumRodjenja(novDatumRodjenja);
		}
	}
	public void promenaTelefonaGosta (String korisnickoIme, int novTelefon) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			gost.setTelefon(novTelefon);
		}
	}
	public void promenaAdreseGosta (String korisnickoIme, String novaAdresa) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			gost.setAdresa(novaAdresa);
		}
	}
	public void promenaKorisnickogImenaGosta (String korisnickoIme, String novoKorisnickoIme) {
		Gost gost = pronadjiGosta(korisnickoIme);
		Gost noviGost =  pronadjiGosta(novoKorisnickoIme);
		if (gost != null && noviGost == null) {
			gost.setIme(novoKorisnickoIme);
		}
	}
	public void promenaLozinkeGosta (String korisnickoIme, String novaLozinka) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			gost.setLozinka(novaLozinka);
		}
	}
	public String nadjiImeGosta (String korisnickoIme) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			return gost.getIme();
		}
		return null;
	}
	public String nadjiPrezimeGosta (String korisnickoIme) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			return gost.getPrezime();
		}
		return null;
	}
	public Pol nadjiPolGosta (String korisnickoIme) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			return gost.getPol();
		}
		return null;
	}
	public String nadjiAdresuGosta (String korisnickoIme) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			return gost.getAdresa();
		}
		return null;
	}
	public long nađiTelefonGosta (String korisnickoIme) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			return gost.getTelefon();
		}
		return 0;
	}
	public String nađiDatumRodjenjaGosta (String korisnickoIme) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			return gost.getDatumRodjenja();
		}
		return null;
	}
	public String nađiLozinkuGosta (String korisnickoIme) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			return gost.getLozinka();
		}
		return null;
	}
}