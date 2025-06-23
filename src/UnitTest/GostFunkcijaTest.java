package UnitTest;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import entiteti.Gost;
import entiteti.Rezervacija;
import funkcionalnosti.GostFunkcija;
import hotel.HotelListePodataka;
import podaci.Cenovnik;
import podaci.DodatneUsluge;
import podaci.StatusRezervacije;
import podaci.TipSobe;
import prikaz.IspisZapis;

class GostFunkcijaTest {

    private GostFunkcija gostFunkcija = new GostFunkcija();
    IspisZapis main = new IspisZapis();
    
    @Test
    public void testPregledSlobodnihTipovaSoba() {
    	main.zapisPodataka();
        String datumPocetka = "01.01.2024.";
        String datumKraja = "31.12.2024.";
        ArrayList<TipSobe> slobodneSobe = new ArrayList<>();
		for (TipSobe tipSobe : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
			if (tipSobe.getNazivTipaSobe().equals("trokrevetna (2+1)")) {
				slobodneSobe.add(tipSobe);
			}
		}
        ArrayList<TipSobe> rezultat = gostFunkcija.pregledSlobodnihTipovaSoba(datumPocetka, datumKraja);
        assertEquals(slobodneSobe, rezultat);
    }
    @Test
    public void testRezervacijaSobe() {
    	main.zapisPodataka();
        int brojOsoba = 2;
        String datumPocetkaRezervacije = "03.07.2024.";
        String datumKrajaRezervacije = "05.07.2024.";
        String tipSobe = "trokrevetna (2+1)";
        ArrayList<DodatneUsluge> dodatneUsluge = new ArrayList<>();
        Gost gost = null;
        for (Gost gosti : HotelListePodataka.getInstance().getListaGostiju()) {
        	if (gosti.getKorisnickoIme().equals("anči")) {
        		gost = gosti;
        		break;
        	}
        }
        Rezervacija result = gostFunkcija.rezervacijaSobe(gost, brojOsoba, datumPocetkaRezervacije, datumKrajaRezervacije, tipSobe, dodatneUsluge);
        
        assertNotNull(result);
        assertEquals(brojOsoba, result.getBrojOsoba());
        assertEquals(datumPocetkaRezervacije, result.getDatumPocetkaRezervacije());
        assertEquals(datumKrajaRezervacije, result.getDatumKrajaRezervacije());
        assertEquals(tipSobe, result.getTipSobe().getNazivTipaSobe());
        assertEquals(dodatneUsluge, result.getDodatneUsluge());
    }
    @Test
	public void testOtkazivanjeSobe() {
		main.zapisPodataka();
		int id = 2;
		Rezervacija rez = null;
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacija.getId() == id) {
				rez = rezervacija;
				break;
			}
		}
		assertEquals(StatusRezervacije.POTVRĐENA, rez.getStatus());
		gostFunkcija.otkazivanjeSobe(id);
		assertEquals(StatusRezervacije.OTKAZANA, rez.getStatus());
	}
    @Test
	public void testPregledTrenutnihRezervacija() {
		main.zapisPodataka();
		ArrayList<Rezervacija> rezervacijeGosta = new ArrayList<>();
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
			if (rezervacija.getGost().getKorisnickoIme().equals("anči")) {
				rezervacijeGosta.add(rezervacija);
			}
		}
		Gost gostic = null;
		for (Gost gost : HotelListePodataka.getInstance().getListaGostiju()) {
			if (gost.getKorisnickoIme().equals("anči")) {
				gostic = gost;
				break;
			}
		}
		// PREGLED REZERVACIJA ANE ANIĆ (UZETA ZA PRIMER)
		assertEquals(rezervacijeGosta, gostFunkcija.pregledTrenutnihRezervacija(gostic));
	}
}