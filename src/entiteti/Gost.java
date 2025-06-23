package entiteti;

import podaci.Pol;

public class Gost extends Ljudi{
	public Gost (String ime, String prezime, Pol pol, String datumRodjenja, long telefon, String adresa, String korisnickoIme, String lozinka) {
		super(ime, prezime, pol, datumRodjenja, telefon, adresa, korisnickoIme, lozinka);
	}

	@Override
    public String toString() {
        return ime + "|" + prezime + "|" + pol + "|" + datumRodjenja + "|" + telefon + "|" + adresa + "|" + korisnickoIme + "|" + lozinka;
    }
	public static Gost fromString(String str) {
		String[] parts = str.split("\\|");
		Pol pol;
		if (parts[2].equals("MUSKI")){
			pol = Pol.MUSKI;
		} else {
			pol = Pol.ZENSKI;
		}
		return new Gost(parts[0], parts[1], pol, parts[3], Long.parseLong(parts[4]), parts[5], parts[6], parts[7]);
    }
}
	