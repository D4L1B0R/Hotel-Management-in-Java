package entiteti;

import podaci.NivoStrucneSpreme;
import podaci.Pol;
import podaci.Zaposleni;

public class Osoblje extends Ljudi{
	protected Zaposleni tip;
	protected NivoStrucneSpreme strucnaSprema;
	protected double plata;
	protected int staz;
	
	public Osoblje (String ime, String prezime, Pol pol, Zaposleni tip, String datumRodjenja, long telefon, String adresa, String korisnickoIme, String lozinka, NivoStrucneSpreme strucnaSprema, int staz) {
		super(ime, prezime, pol, datumRodjenja, telefon, adresa, korisnickoIme, lozinka);
		this.tip = tip;
		this.strucnaSprema = strucnaSprema;
		this.plata = 30000*(strucnaSprema.vrednost + staz/10);
		this.staz = staz;
	}
	public NivoStrucneSpreme getStrucnaSprema () {
		return this.strucnaSprema;
	}
	
	public void setStrucnaSprema(NivoStrucneSpreme vrednost) {
		this.strucnaSprema = vrednost;
	}
	
	public double getPlata () {
		return this.plata;
	}
	
	public void setPlata(double vrednost) {
		this.plata = vrednost;
	}
	
	public int getStaz () {
		return this.staz;
	}
	
	public void setStaz(int vrednost) {
		this.staz = vrednost;
	}

	public Zaposleni getTip() {
		return this.tip;
	}

	public void setTip(Zaposleni tip) {
		this.tip = tip;
	}
	@Override
    public String toString() {
        return ime + "|" + prezime + "|" + pol + "|" + tip + "|" + datumRodjenja + "|" + telefon + "|" + adresa + "|" + korisnickoIme + "|" + lozinka + "|" + strucnaSprema + "|" + staz;
    }
	public static Osoblje fromString(String str) {
		String[] parts = str.split("\\|");
		Pol pol;
		Zaposleni zaposleni;
		NivoStrucneSpreme strucnaSprema;
		if (parts[2].equals("MUSKI")){
			pol = Pol.MUSKI;
		} else {
			pol = Pol.ZENSKI;
		}
		if (parts[3].equals("SOBARICA")) {
			zaposleni = Zaposleni.SOBARICA;
		} else if (parts[3].equals("RECEPCIONER")){
			zaposleni = Zaposleni.RECEPCIONER;
		} else {
			zaposleni = Zaposleni.ADMINISTRATOR;
		}
		if (parts[9].equals("OSNOVNA")) {
			strucnaSprema = NivoStrucneSpreme.OSNOVNA;
		} else if (parts[9].equals("SREDNJA")) {
			strucnaSprema = NivoStrucneSpreme.SREDNJA;
		} else {
			strucnaSprema = NivoStrucneSpreme.VIŠA;
		}
		return new Osoblje(parts[0], parts[1], pol, zaposleni, parts[4], Long.parseLong(parts[5]), parts[6], parts[7], parts[8], strucnaSprema, Integer.parseInt(parts[10]));
    }
}