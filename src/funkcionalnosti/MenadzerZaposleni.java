package funkcionalnosti;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import entiteti.Osoblje;
import entiteti.Sobarica;
import hotel.HotelListePodataka;
import podaci.Pol;
import podaci.Zaposleni;
import podaci.Izvestaji;
import podaci.NivoStrucneSpreme;

public class MenadzerZaposleni {
	public Osoblje dodavanjeZaposlenih(String ime, String prezime, Pol pol, Zaposleni tip, String datumRodjenja, long telefon, String adresa, String korisnickoIme, String lozinka, NivoStrucneSpreme strucnaSprema, int staz) {
		Osoblje radnik = new Osoblje(ime, prezime, pol, tip, datumRodjenja, telefon, adresa, korisnickoIme, lozinka, strucnaSprema, staz);
		ArrayList<Osoblje> zaposleni = HotelListePodataka.getInstance().getListaZaposlenih();
		zaposleni.add(radnik);
		HotelListePodataka.getInstance().setListaZaposlenih(zaposleni);
		if (tip.equals(Zaposleni.SOBARICA)) {
			Sobarica sobarica = new Sobarica(ime, prezime, pol, tip, datumRodjenja, telefon, adresa, korisnickoIme, lozinka, strucnaSprema, staz);
			ArrayList<Sobarica> sobarice = HotelListePodataka.getInstance().getListaSobarica();
			sobarice.add(sobarica);
			HotelListePodataka.getInstance().setListaSobarica(sobarice);
			HashMap<Sobarica, HashMap<String, Integer>> a = Izvestaji.getInstance().getOcisceneSobe();
			HashMap<String, Integer> b = new HashMap<>();
			b.put(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")), 0);
			a.put(sobarica, b);
			Izvestaji.getInstance().setOcisceneSobe(a);
		}
		return radnik;
	}
	public void prikazSvihZaposlenih() {
		System.out.println("Zaposleni su: ");
		for(Osoblje radnik : HotelListePodataka.getInstance().getListaZaposlenih()) {
			System.out.println(radnik.getIme() + " " + radnik.getPrezime());
		}
		System.out.println();
	}
	public void brisanjeZaposlenih (Osoblje admin, String korisnickoIme) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			ArrayList<Osoblje> zaposleni = HotelListePodataka.getInstance().getListaZaposlenih();
			zaposleni.remove(radnik);
			HotelListePodataka.getInstance().setListaZaposlenih(zaposleni);
			System.out.println(admin.getKorisnickoIme() + ", uspešno ste obrisali zaposlenog (" + radnik.getIme() + " " + radnik.getPrezime() + ") tipa " + radnik.getTip() + ".\n");
		}
		if (radnik.getTip().equals(Zaposleni.SOBARICA)) {
			for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
				if(sobarica.getKorisnickoIme().equals(korisnickoIme)) {
					ArrayList<Sobarica> sobarice = HotelListePodataka.getInstance().getListaSobarica();
					sobarice.remove(sobarica);
					HotelListePodataka.getInstance().setListaSobarica(sobarice);
					HashMap<Sobarica, HashMap<String, Integer>> a = Izvestaji.getInstance().getOcisceneSobe();
					a.remove(sobarica);
					Izvestaji.getInstance().setOcisceneSobe(a);
					break;
				}
			}
		}
	}
	private Osoblje pronadjiZaposlenog(String korisnickoIme){
	    for (Osoblje radnik : HotelListePodataka.getInstance().getListaZaposlenih()) {
	        if (radnik.getKorisnickoIme().equals(korisnickoIme)) {
	            return radnik;
	        }
	    } return null;
	}
	public void promenaPrezimenaZaposlenog (String korisnickoIme, String novoPrezime) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			radnik.setPrezime(novoPrezime);
		}
		if (radnik.getTip().equals(Zaposleni.SOBARICA)) {
			for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
				if (sobarica.getKorisnickoIme().equals(korisnickoIme)) {
					sobarica.setPrezime(novoPrezime);
					break;
				}
			}
		}
	}
	public void promenaImenaZaposlenog (String korisnickoIme, String novoIme) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			radnik.setIme(novoIme);
		}
		if (radnik.getTip().equals(Zaposleni.SOBARICA)) {
			for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
				if (sobarica.getKorisnickoIme().equals(korisnickoIme)) {
					sobarica.setIme(novoIme);
					break;
				}
			}
		}
	}
	public void promenaPolaZaposlenog (String korisnickoIme, Pol noviPol) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			radnik.setPol(noviPol);
		}
		if (radnik.getTip().equals(Zaposleni.SOBARICA)) {
			for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
				if (sobarica.getKorisnickoIme().equals(korisnickoIme)) {
					sobarica.setPol(noviPol);
					break;
				}
			}
		}
	}
	public void promenaDatumaRodjenjaZaposlenog (String korisnickoIme, String novDatumRodjenja) {
		if (novDatumRodjenja.matches("\\d{4}-\\d{2}-\\d{2}")) {
			Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
			if (radnik != null) {
				radnik.setDatumRodjenja(novDatumRodjenja);
			}
			if (radnik.getTip().equals(Zaposleni.SOBARICA)) {
				for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
					if (sobarica.getKorisnickoIme().equals(korisnickoIme)) {
						sobarica.setDatumRodjenja(novDatumRodjenja);
						break;
					}
				}
			}
		}
	}
	public void promenaTelefonaZaposlenog (String korisnickoIme, int novTelefon) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			radnik.setTelefon(novTelefon);
		}
		if (radnik.getTip().equals(Zaposleni.SOBARICA)) {
			for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
				if (sobarica.getKorisnickoIme().equals(korisnickoIme)) {
					sobarica.setTelefon(novTelefon);
					break;
				}
			}
		}
	}
	public void promenaAdreseZaposlenog (String korisnickoIme, String novaAdresa) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			radnik.setAdresa(novaAdresa);
		}
		if (radnik.getTip().equals(Zaposleni.SOBARICA)) {
			for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
				if (sobarica.getKorisnickoIme().equals(korisnickoIme)) {
					sobarica.setAdresa(novaAdresa);
					break;
				}
			}
		}
	}
	public void promenaKorisnickogImenaZaposlenog (String korisnickoIme, String novoKorisnickoIme) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		Osoblje noviRadnik = pronadjiZaposlenog(novoKorisnickoIme);
		if (radnik != null && noviRadnik == null) {
			radnik.setIme(novoKorisnickoIme);
		}
		if (radnik.getTip().equals(Zaposleni.SOBARICA)) {
			for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
				if (sobarica.getKorisnickoIme().equals(korisnickoIme)) {
					sobarica.setKorisnickoIme(novoKorisnickoIme);
					break;
				}
			}
		}
	}
	public void promenaLozinkeZaposlenog (String korisnickoIme, String novaLozinka) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			radnik.setLozinka(novaLozinka);
		}
		if (radnik.getTip().equals(Zaposleni.SOBARICA)) {
			for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
				if (sobarica.getKorisnickoIme().equals(korisnickoIme)) {
					sobarica.setLozinka(novaLozinka);
					break;
				}
			}
		}
	}
	public void promenaTipaZaposlenog (String korisnickoIme, Zaposleni novTip) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik.getTip().equals(Zaposleni.SOBARICA) && !novTip.equals(Zaposleni.SOBARICA)) {
			for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
				if(sobarica.getKorisnickoIme().equals(korisnickoIme)) {
					ArrayList<Sobarica> sobarice = HotelListePodataka.getInstance().getListaSobarica();
					sobarice.remove(sobarica);
					HotelListePodataka.getInstance().setListaSobarica(sobarice);
					break;
				}
			}
		}
		else if (!radnik.getTip().equals(Zaposleni.SOBARICA) && novTip.equals(Zaposleni.SOBARICA)) {
			for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
				if(sobarica.getKorisnickoIme().equals(korisnickoIme)) {
					ArrayList<Sobarica> sobarice = HotelListePodataka.getInstance().getListaSobarica();
					sobarice.add(sobarica);
					HotelListePodataka.getInstance().setListaSobarica(sobarice);
					break;
				}
			}
		}
		if (radnik != null) {
			radnik.setTip(novTip);
		}
	}
	public void promenaStazaZaposlenog (String korisnickoIme, int novStaz) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			radnik.setStaz(novStaz);
		}
		if (radnik.getTip().equals(Zaposleni.SOBARICA)) {
			for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
				if (sobarica.getKorisnickoIme().equals(korisnickoIme)) {
					sobarica.setStaz(novStaz);
					break;
				}
			}
		}
	}
	public void promenaStrucneSpremeZaposlenog (String korisnickoIme, NivoStrucneSpreme novaStrucnaSprema) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			radnik.setStrucnaSprema(novaStrucnaSprema);
		}
		if (radnik.getTip().equals(Zaposleni.SOBARICA)) {
			for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
				if (sobarica.getKorisnickoIme().equals(korisnickoIme)) {
					sobarica.setStrucnaSprema(novaStrucnaSprema);
					break;
				}
			}
		}
	}
	public String nadjiImeZaposlenog (String korisnickoIme) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			return radnik.getIme();
		}
		return null;
	}
	public String nadjiPrezimeZaposlenog (String korisnickoIme) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			return radnik.getPrezime();
		}
		return null;
	}
	public Pol nadjiPolZaposlenog (String korisnickoIme) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			return radnik.getPol();
		}
		return null;
	}
	public String nadjiAdresuZaposlenog (String korisnickoIme) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			return radnik.getAdresa();
		}
		return null;
	}
	public long nadjiTelefonZaposlenog (String korisnickoIme) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			return radnik.getTelefon();
		}
		return 0;
	}
	public String nadjiDatumRodjenjaZaposlenog (String korisnickoIme) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			return radnik.getDatumRodjenja();
		}
		return null;
	}
	public String nadjiLozinkuZaposlenog (String korisnickoIme) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			return radnik.getLozinka();
		}
		return null;
	}
	public Zaposleni nadjiTipZaposlenog (String korisnickoIme) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			return radnik.getTip();
		}
		return null;
	}
	public int nadjiStazZaposlenog (String korisnickoIme) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			return radnik.getStaz();
		}
		return 0;
	}
	public NivoStrucneSpreme nadjiStrucnuSpremuZaposlenog (String korisnickoIme) {
		Osoblje radnik = pronadjiZaposlenog(korisnickoIme);
		if (radnik != null) {
			return radnik.getStrucnaSprema();
		}
		return null;
	}
}