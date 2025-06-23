package entiteti;

import podaci.Cenovnik;
import podaci.DodatneUsluge;
import podaci.StatusRezervacije;
import podaci.TipSobe;
import java.util.ArrayList;
import hotel.HotelListePodataka;

public class Rezervacija {
	private int id;
	private StatusRezervacije status;
	private String datumPocetkaRezervacije;
	private String datumKrajaRezervacije;
	private Soba soba;
	private Gost gost;
	private int brojOsoba;
	private double cena;
	private ArrayList<DodatneUsluge> dodatneUsluge;
	private TipSobe tipSobe;
	private double cenaDodatnihUsluga;
	
	public Rezervacija (StatusRezervacije status, String datumPocetkaRezervacije, String datumKrajaRezervacije, Soba soba, Gost gost, int brojOsoba, ArrayList<DodatneUsluge> dodatneUsluge, TipSobe tipSobe) {
		this.id = dodatneFunkcionalnosti.GeneratorBrojeva.generatorBrojeva();
		this.status = status;
		this.datumPocetkaRezervacije = datumPocetkaRezervacije;
		this.datumKrajaRezervacije = datumKrajaRezervacije;
		this.gost = gost;
		this.cena = Cenovnik.getInstance().getPocetnaCena(tipSobe.getNazivTipaSobe(), datumPocetkaRezervacije, datumKrajaRezervacije);
		this.soba = soba;
		this.brojOsoba = brojOsoba;
		this.dodatneUsluge = dodatneUsluge;
		this.tipSobe = tipSobe;
		for (DodatneUsluge dodatnaUsluga : dodatneUsluge) {
			double vr =Cenovnik.getInstance().getDodatneUsluge(dodatnaUsluga.getDodatneUsluge(), datumPocetkaRezervacije, datumKrajaRezervacije);
			this.cenaDodatnihUsluga += vr;
			this.cena += vr;
		}
	}
	// REZERVACIJA GOSTIJU
	public Rezervacija (Gost gost, int brojOsoba, String datumPocetkaRezervacije, String datumKrajaRezervacije, String tipSobe, ArrayList<DodatneUsluge> dodatneUsluge) {
		this.id = dodatneFunkcionalnosti.GeneratorBrojeva.generatorBrojeva();
		this.status = StatusRezervacije.CEKANJE;
		this.datumPocetkaRezervacije = datumPocetkaRezervacije;
		this.datumKrajaRezervacije = datumKrajaRezervacije;
		this.gost = gost;
		this.cena = Cenovnik.getInstance().getPocetnaCena(tipSobe, datumPocetkaRezervacije, datumKrajaRezervacije);
		this.brojOsoba = brojOsoba;
		this.dodatneUsluge = dodatneUsluge;
		TipSobe tipSobe_obj;
		for (TipSobe tipSoba : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
			if (tipSoba.getNazivTipaSobe().equals(tipSobe)) {
				tipSobe_obj = tipSoba;
				this.tipSobe = tipSobe_obj;
				break;
			}
		}
		for (DodatneUsluge dodatnaUsluga : dodatneUsluge) {
			double vr =Cenovnik.getInstance().getDodatneUsluge(dodatnaUsluga.getDodatneUsluge(), datumPocetkaRezervacije, datumKrajaRezervacije);
			this.cenaDodatnihUsluga += vr;
			this.cena += vr;
		}
	}
	public Rezervacija (StatusRezervacije status, String datumPocetkaRezervacije, String datumKrajaRezervacije, Soba soba, Gost gost, int brojOsoba, ArrayList<DodatneUsluge> dodatneUsluge, TipSobe tipSobe, Double cena) {
		this.id = dodatneFunkcionalnosti.GeneratorBrojeva.generatorBrojeva();
		this.status = status;
		this.datumPocetkaRezervacije = datumPocetkaRezervacije;
		this.datumKrajaRezervacije = datumKrajaRezervacije;
		this.gost = gost;
		this.cena = cena;
		this.soba = soba;
		this.brojOsoba = brojOsoba;
		this.dodatneUsluge = dodatneUsluge;
		this.tipSobe = tipSobe;
	}
	public TipSobe getTipSobe() {
		return tipSobe;
	}
	public void setTipSobe(TipSobe noviTipSobe) {
		this.tipSobe = noviTipSobe;
	}
	public StatusRezervacije getStatus() {
		return status;
	}
	public void setStatus(StatusRezervacije status) {
		this.status = status;
	}
	public String getDatumPocetkaRezervacije() {
		return datumPocetkaRezervacije;
	}
	public void setDatumPocetkaRezervacije(String datumPocetkaRezervacije) {
		this.datumPocetkaRezervacije = datumPocetkaRezervacije;
	}
	public String getDatumKrajaRezervacije() {
		return datumKrajaRezervacije;
	}
	public void setDatumKrajaRezervacije(String datumKrajaRezervacije) {
		this.datumKrajaRezervacije = datumKrajaRezervacije;
	}
	public Soba getSoba() {
		return soba;
	}
	public void setSoba(Soba soba) {
		this.soba = soba;
	}
	public Gost getGost() {
		return gost;
	}
	public void setGost(Gost gost) {
		this.gost = gost;
	}
	public int getBrojOsoba() {
		return brojOsoba;
	}
	public void setBrojOsoba(int brojOsoba) {
		this.brojOsoba = brojOsoba;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getCena() {
		return cena;
	}
	public void setCena(double cena) {
		this.cena = cena;
	}
	public ArrayList<DodatneUsluge> getDodatneUsluge() {
		return dodatneUsluge;
	}
	public void setDodatneUsluge(ArrayList<DodatneUsluge> dodatneUsluge) {
		this.dodatneUsluge = dodatneUsluge;
	}
	@Override
    public String toString() {
		String stringDodatneUsluge = "";
		for (DodatneUsluge dodatnaUsluga : dodatneUsluge) {
			stringDodatneUsluge += dodatnaUsluga.getDodatneUsluge() + ".";
		}
		String gostic = "";
		if (gost != null) {
			gostic = gost.getKorisnickoIme();
		}
		int brSobe = 0;
		if (soba != null) {
			brSobe = soba.getBrojSobe();
		}
        return id + "|" + status + "|" + datumPocetkaRezervacije + "|" + datumKrajaRezervacije + "|" + brSobe + "|" + gostic + "|" + brojOsoba + "|" + cena + "|" + stringDodatneUsluge + "|" + tipSobe.getNazivTipaSobe();
    }
	public static Rezervacija fromString(String str) {
		String[] parts = str.split("\\|");
		StatusRezervacije status;
		if (parts[1].equals("CEKANJE")){
			status = StatusRezervacije.CEKANJE;
		} else if (parts[1].equals("ODBIJENA")){
			status = StatusRezervacije.ODBIJENA;
		} else if (parts[1].equals("POTVRĐENA")){
			status = StatusRezervacije.POTVRĐENA;
		} else {
			status = StatusRezervacije.OTKAZANA;
		}
		Soba soba = null;
		for (Soba sobe : HotelListePodataka.getInstance().getListaSoba()) {
			if (sobe.getBrojSobe() == Integer.parseInt(parts[4])) {
				soba = sobe;
			}
		}
			
		Gost trazeniGost = null;
		if (HotelListePodataka.getInstance().getListaGostiju() != null) {
			for (Gost gost : HotelListePodataka.getInstance().getListaGostiju()) {
				if (gost.getKorisnickoIme().equals(parts[5])) {
					trazeniGost = gost;
				}
			}
		}
		TipSobe tip = null;
		if (Cenovnik.getInstance().getCeneTipovaSoba() != null) {
			for (TipSobe tipovi : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
				if (tipovi.getNazivTipaSobe().equals(parts[9])) {
					tip = tipovi;
				}
			}
		}
		ArrayList<DodatneUsluge> dodatneUsluge = new ArrayList<>();
		String [] usluge = parts[8].split("\\.");
		for (String deo : usluge) {
			for (DodatneUsluge du : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
				if (du.getDodatneUsluge().equals(deo)) {
					dodatneUsluge.add(du);
				}
			}
		}
		Rezervacija rezervacija = new Rezervacija(status, parts[2], parts[3], soba, trazeniGost, Integer.parseInt(parts[6]), dodatneUsluge, tip, Double.parseDouble(parts[7]));
		if (soba != null) {
			ArrayList<Rezervacija> rez = soba.getListaRezervacija();
			rez.add(rezervacija);
			soba.setListaRezervacija(rez);
		}
		return rezervacija;
    }
	public double getCenaDodatnihUsluga() {
		return cenaDodatnihUsluga;
	}
	public void setCenaDodatnihUsluga(double cenaDodatnihUsluga) {
		this.cenaDodatnihUsluga = cenaDodatnihUsluga;
	}
}