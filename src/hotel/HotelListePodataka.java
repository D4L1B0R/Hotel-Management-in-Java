package hotel;

import java.util.ArrayList;
import entiteti.Gost;
import entiteti.Osoblje;
import entiteti.Rezervacija;
import entiteti.Soba;
import entiteti.Sobarica;

public class HotelListePodataka {
	private static HotelListePodataka instance;
	private ArrayList<Gost> listaGostiju;
	private ArrayList<Rezervacija> listaRezervacija;
	private ArrayList<Soba> listaSoba;
	private ArrayList<Osoblje> listaZaposlenih;
	private ArrayList<Sobarica> listaSobarica;
	
	private HotelListePodataka() {
		listaGostiju = new ArrayList<>();
		listaRezervacija = new ArrayList<>();
		listaSoba = new ArrayList<>();
		listaZaposlenih = new ArrayList<>();
		listaSobarica = new ArrayList<>();
    }
    public static HotelListePodataka getInstance() {
        if (instance == null) {
            instance = new HotelListePodataka();
        }
        return instance;
    }
	public ArrayList<Gost> getListaGostiju() {
		return listaGostiju;
	}
	public void setListaGostiju(ArrayList<Gost> listaGostiju) {
		this.listaGostiju = listaGostiju;
	}
	public ArrayList<Rezervacija> getListaRezervacija() {
		return listaRezervacija;
	}
	public void setListaRezervacija(ArrayList<Rezervacija> listaRezervacija) {
		this.listaRezervacija = listaRezervacija;
	}
	public ArrayList<Soba> getListaSoba() {
		return listaSoba;
	}
	public void setListaSoba(ArrayList<Soba> listaSoba) {
		this.listaSoba = listaSoba;
	}
	public ArrayList<Osoblje> getListaZaposlenih() {
		return listaZaposlenih;
	}
	public void setListaZaposlenih(ArrayList<Osoblje> listaZaposlenih) {
		this.listaZaposlenih = listaZaposlenih;
	}
	public ArrayList<Sobarica> getListaSobarica() {
		return listaSobarica;
	}
	public void setListaSobarica(ArrayList<Sobarica> listaSobarica) {
		this.listaSobarica = listaSobarica;
	}
}