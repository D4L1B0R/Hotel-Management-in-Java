package entiteti;

import java.util.ArrayList;

import hotel.HotelListePodataka;
import podaci.NivoStrucneSpreme;
import podaci.Pol;
import podaci.Zaposleni;

public class Sobarica extends Osoblje{
	private ArrayList<Soba> sobeZaCiscenje = new ArrayList<>();
	public Sobarica(String ime, String prezime, Pol pol, Zaposleni tip, String datumRodjenja, long telefon, String adresa, String korisnickoIme, String lozinka, NivoStrucneSpreme strucnaSprema, int staz, ArrayList<Soba> sobeZaCiscenje) {
		super(ime, prezime, pol, tip, datumRodjenja, telefon, adresa, korisnickoIme, lozinka, strucnaSprema, staz);
		this.sobeZaCiscenje = sobeZaCiscenje;
	}
	public Sobarica(String ime, String prezime, Pol pol, Zaposleni tip, String datumRodjenja, long telefon, String adresa, String korisnickoIme, String lozinka, NivoStrucneSpreme strucnaSprema, int staz) {
		super(ime, prezime, pol, tip, datumRodjenja, telefon, adresa, korisnickoIme, lozinka, strucnaSprema, staz);
	}
	public ArrayList<Soba> getSobeZaCiscenje() {
		return this.sobeZaCiscenje;
	}
	public void setSobeZaCiscenje(ArrayList<Soba> sobeZaČišćenje) {
		this.sobeZaCiscenje = sobeZaČišćenje;
	}
	@Override
    public String toString() {
		String listaSoba = "0";
		if (sobeZaCiscenje != null) {
			for (Soba sobe : sobeZaCiscenje) {
				listaSoba += sobe.getBrojSobe() + ".";
			}
		}
        return ime + "|" + prezime + "|" + pol + "|" + tip + "|" + datumRodjenja + "|" + telefon + "|" + adresa + "|" + korisnickoIme + "|" + lozinka + "|" + strucnaSprema + "|" + staz + "|" + listaSoba;
    }
	public static Sobarica fromString(String str) {
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
		ArrayList<Soba> listaSobe = new ArrayList<>();
		if (parts[11].equals("0")) {
			return new Sobarica(parts[0], parts[1], pol, zaposleni, parts[4], Long.parseLong(parts[5]), parts[6], parts[7], parts[8], strucnaSprema, Integer.parseInt(parts[10]));
		}
		String [] sobe = parts[11].split(".");
		for (String deo : sobe) {
			for (Soba du : HotelListePodataka.getInstance().getListaSoba()) {
				if (du.getBrojSobe() == Integer.parseInt(deo)) {
					listaSobe.add(du);
				}
			}
		}
		return new Sobarica(parts[0], parts[1], pol, zaposleni, parts[4], Long.parseLong(parts[5]), parts[6], parts[7], parts[8], strucnaSprema, Integer.parseInt(parts[10]), listaSobe);
    }
}