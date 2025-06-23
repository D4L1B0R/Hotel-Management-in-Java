package entiteti;

import podaci.Pol;

public class Ljudi {
	protected String ime;
	protected String prezime;
	protected Pol pol;
	protected String datumRodjenja;
	protected long telefon;
	protected String adresa;
	protected String korisnickoIme;
	protected String lozinka;
	
	public Ljudi (String ime, String prezime, Pol pol, String datumRodjenja, long telefon, String adresa, String korisnickoIme, String lozinka) {
		this.ime = ime;
		this.prezime = prezime;
		this.pol = pol;
		this.datumRodjenja = datumRodjenja;
		this.telefon = telefon;
		this.adresa = adresa;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
	}
	public String getIme () {
		return this.ime;
	}
	public void setIme (String vrednost) {
		this.ime = vrednost;
	}
	public String getPrezime () {
		return this.prezime;
	}
	public void setPrezime (String vrednost) {
		this.prezime = vrednost;
	}
	public Pol getPol () {
		return this.pol;
	}
	public void setPol (Pol vrednost) {
		this.pol = vrednost;
	}
	public String getDatumRodjenja () {
		return this.datumRodjenja;
	}
	public void setDatumRodjenja (String vrednost) {
		this.datumRodjenja = vrednost;
	}
	public long getTelefon () {
		return this.telefon;
	}
	public void setTelefon (long vrednost) {
		this.telefon = vrednost;
	}
	public String getAdresa () {
		return this.adresa;
	}
	public void setAdresa (String vrednost) {
		this.adresa = vrednost;
	}
	public String getKorisnickoIme () {
		return this.korisnickoIme;
	}
	public void setKorisnickoIme (String vrednost) {
		this.korisnickoIme = vrednost;
	}
	public String getLozinka () {
		return this.lozinka;
	}
	public void setLozinka(String vrednost) {
		this.lozinka = vrednost;
	}
}
