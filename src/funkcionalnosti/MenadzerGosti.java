package funkcionalnosti;

import entiteti.Gost;
import hotel.HotelListePodataka;
import podaci.Pol;

public class MenadzerGosti{
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
	public void promenaTelefonaGosta (String korisnickoIme, long novTelefon) {
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
			gost.setKorisnickoIme(novoKorisnickoIme);
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
	public long nadjiTelefonGosta (String korisnickoIme) {
		Gost gost = pronadjiGosta(korisnickoIme);
		if (gost != null) {
			return gost.getTelefon();
		}
		return 0;
	}
	public String nadjiDatumRodjenjaGosta (String korisnickoIme) {
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
