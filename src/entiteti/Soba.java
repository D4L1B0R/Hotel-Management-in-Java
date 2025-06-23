package entiteti;

import java.util.ArrayList;

import hotel.HotelListePodataka;
import podaci.Cenovnik;
import podaci.SpecijalnePogodnosti;
import podaci.StatusSobe;
import podaci.TipSobe;

public class Soba {
	private int brojSobe;
	private TipSobe tip;
	private StatusSobe status;
	private ArrayList<Rezervacija> listaRezervacija = new ArrayList<Rezervacija>();
	private ArrayList<SpecijalnePogodnosti> pogodnosti = new ArrayList<SpecijalnePogodnosti>();
	
	public Soba(int brojSobe, TipSobe tip) {
		this.setBrojSobe(brojSobe);
		this.setTip(tip);
		this.setStatus(StatusSobe.SLOBODNA);
		this.setListaRezervacija(listaRezervacija);
	}
	public Soba(int brojSobe, TipSobe tip, StatusSobe status, ArrayList<SpecijalnePogodnosti> pogodnost) {
		this.setBrojSobe(brojSobe);
		this.setTip(tip);
		this.setStatus(status);
		this.setListaRezervacija(listaRezervacija);
		this.setPogodnosti(pogodnost);
	}
	public Soba(int brojSobe, TipSobe tip, ArrayList<Rezervacija> listaRezervacija) {
		this.setBrojSobe(brojSobe);
		this.setTip(tip);
		this.setStatus(StatusSobe.SLOBODNA);
		this.setListaRezervacija(listaRezervacija);
	}
	public Soba(int brojSobe, TipSobe tip, StatusSobe status, ArrayList<SpecijalnePogodnosti> pogodnost, ArrayList<Rezervacija> listaRezervacija) {
		this.setBrojSobe(brojSobe);
		this.setTip(tip);
		this.setStatus(status);
		this.setListaRezervacija(listaRezervacija);
		this.setPogodnosti(pogodnost);
	}
	public StatusSobe getStatus() {
		return status;
	}

	public void setStatus(StatusSobe status) {
		this.status = status;
	}

	public TipSobe getTip() {
		return tip;
	}

	public void setTip(TipSobe tip) {
		this.tip = tip;
	}

	public int getBrojSobe() {
		return brojSobe;
	}

	public void setBrojSobe(int brojSobe) {
		this.brojSobe = brojSobe;
	}
	public ArrayList<Rezervacija> getListaRezervacija() {
		return listaRezervacija;
	}
	public void setListaRezervacija(ArrayList<Rezervacija> listaRezervacija) {
		this.listaRezervacija = listaRezervacija;
	}
	@Override
    public String toString() {
		String listaRez = "";
		if (listaRezervacija != null) {
			for (Rezervacija rezervacija : listaRezervacija) {
				listaRez += rezervacija.getId() + ".";
			}
		}
		String str = "";
		boolean prvi = true;
		if (pogodnosti != null) {
			for (SpecijalnePogodnosti pogodnost : pogodnosti) {
				str += pogodnost + ",";
				prvi = false;
			}
		}
		if (prvi) {
			str = "0";
		}
        return brojSobe + "|" + tip.getNazivTipaSobe() + "|" + status + "|" + listaRez + "|" + str;
    }
	public static Soba fromString(String str) {
		String[] parts = str.split("\\|");
		StatusSobe status;
		if (parts[2].equals("SLOBODNA")){
			status = StatusSobe.SLOBODNA;
		} else if (parts[2].equals("SPREMANJE")){
			status = StatusSobe.SPREMANJE;
		} else {
			status = StatusSobe.ZAUZETA;
		}
		TipSobe tip = null;
		for (TipSobe tipovi : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
			if (tipovi.getNazivTipaSobe().equals(parts[1])) {
				tip = tipovi;
				break;
			}
		}
		ArrayList<SpecijalnePogodnosti> pogodnosti = new ArrayList<SpecijalnePogodnosti>();
		if (parts[4].equals("0")) {
			return new Soba(Integer.parseInt(parts[0]), tip, status, pogodnosti);
		}
		for (String pogodnost : parts[4].split(",")){
			if (pogodnost.equals("")) {
				continue;
			}
            pogodnosti.add(SpecijalnePogodnosti.valueOf(pogodnost));
        }
		ArrayList<Rezervacija> listaRezervacija = new ArrayList<Rezervacija>();
		for (String rezervacija : parts[3].split("\\.")){
            if (rezervacija.equals("")) {
                continue;
            }
            for (Rezervacija rez : HotelListePodataka.getInstance().getListaRezervacija()) {
				if (rez.getId() == Integer.parseInt(rezervacija)) {
					listaRezervacija.add(rez);
				}
            }
        }
		return new Soba(Integer.parseInt(parts[0]), tip, status, pogodnosti, listaRezervacija);
    }
	public ArrayList<SpecijalnePogodnosti> getPogodnosti() {
		return pogodnosti;
	}
	public void setPogodnosti(ArrayList<SpecijalnePogodnosti> pogodnosti) {
		this.pogodnosti = pogodnosti;
	}
}
