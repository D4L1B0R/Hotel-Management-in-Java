package dodatneFunkcionalnosti;

import entiteti.Rezervacija;
import hotel.HotelListePodataka;

public abstract class GeneratorBrojeva {
    private static int trenutniBroj;
    
    public static void set() {
    	for (Rezervacija rez : HotelListePodataka.getInstance().getListaRezervacija()) {
    		if (rez.getId() > trenutniBroj) {
    			trenutniBroj = rez.getId();
    		}
    	}
    }

    public static synchronized int generatorBrojeva() {
    	set();
        trenutniBroj++;
        return trenutniBroj;
    }
}