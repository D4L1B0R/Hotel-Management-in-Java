package UnitTest;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import entiteti.Osoblje;
import entiteti.Soba;
import funkcionalnosti.MenadzerSobe;
import hotel.HotelListePodataka;
import podaci.Cenovnik;
import podaci.Izvestaji;
import podaci.SlobodneSobe;
import podaci.SpecijalnePogodnosti;
import podaci.StatusSobe;
import podaci.TipSobe;
import prikaz.IspisZapis;

class MenadzerSobeTest {
	
	MenadzerSobe menadzerSobe = new MenadzerSobe();
	IspisZapis main = new IspisZapis();
	@Test
	void testDobijTipSobe() {
		main.zapisPodataka();
		assertEquals("jednokrevetna (1)", menadzerSobe.dobijTipSobe("jednokrevetna (1)").getNazivTipaSobe());
	}
	@Test
	void testDobijDatum() {
        main.zapisPodataka();
        assertEquals("01.01.2024.,31.12.2024.", menadzerSobe.dobijDatum("dvokrevetna (2)", "01.01.2024.", "31.12.2024."));
	}
	@Test
	void testPodesiCenuTipaSobe() {
	    main.zapisPodataka();
	    Osoblje admin = null;
	    for (Osoblje adminko : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (adminko.getKorisnickoIme().equals("dalenik")) {
				admin = adminko;
				break;
			}
	    }
	    menadzerSobe.podesiPočetnuCenuSobe(admin, "jednokrevetna (1)", "01.01.2024.", "31.12.2024.", 1500);
	    double cena = 0;
	    for (TipSobe tip : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
	        if (tip.getNazivTipaSobe().equals("jednokrevetna (1)")) {
	            for (String datumi : Cenovnik.getInstance().getCeneTipovaSoba().get(tip).keySet()) {
	                if (datumi.equals("01.01.2024.,31.12.2024.")) {
	                    cena = Cenovnik.getInstance().getCeneTipovaSoba().get(tip).get(datumi);
	                    break;
	                }
	            }
	        }
	    }
	    assertEquals(1500.0, cena);
	}

	@Test
	void testInicijalizujCenuTipaSobe() {
	    main.zapisPodataka();
	    Osoblje admin = null;
	    for (Osoblje adminko : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (adminko.getKorisnickoIme().equals("dalenik")) {
				admin = adminko;
				break;
			}
	    }
	    menadzerSobe.inicijalizujPocetnuCenuSobe(admin, "jednokrevetna (1)", "01.01.2025.", "31.10.2025.", 1500);
	    for (TipSobe tip : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
	        if (tip.getNazivTipaSobe().equals("jednokrevetna (1)")) {
	            for (String datumi : Cenovnik.getInstance().getCeneTipovaSoba().get(tip).keySet()) {
	                if (datumi.equals("01.01.2025.,31.10.2025.")) {
	                    assertEquals("jednokrevetna (1)", tip.getNazivTipaSobe());
	                    assertEquals("01.01.2025.,31.10.2025.", datumi);
	                    assertEquals(1500.0, Cenovnik.getInstance().getCeneTipovaSoba().get(tip).get(datumi));
	                    break;
	                }
	            }
	        }
	    }
	}

	@Test
	void testPodesiTipSobe() {
	    main.zapisPodataka();
	    for (TipSobe tip : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
	        if (tip.getNazivTipaSobe().equals("jednokrevetna (1)")) {
	            menadzerSobe.podesiPocetnuSobu("jednokrevetna (1)", "dvokrevetna (2)");
	            assertEquals("dvokrevetna (2)", tip.getNazivTipaSobe());
	            break;
	        }
	    }
	}

	@Test
	void testPodesiDatumTipaSobe() {
	    main.zapisPodataka();
	    for (TipSobe tip : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
	        if (tip.getNazivTipaSobe().equals("jednokrevetna (1)")) {
	            for (String datumi : Cenovnik.getInstance().getCeneTipovaSoba().get(tip).keySet()) {
	                if (datumi.equals("01.01.2024.,31.12.2024.")) {
	                    assertEquals("01.01.2024.,31.12.2024.", datumi);
	                    menadzerSobe.podesiDatumPocetneSobe("jednokrevetna (1)", "01.01.2024.", "31.12.2024.", "01.04.2024.", "31.10.2025.");
	                    break;
	                }
	            }
	        }
	    }
	    for (TipSobe tip : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
	        if (tip.getNazivTipaSobe().equals("jednokrevetna (1)")) {
	            for (String datumi : Cenovnik.getInstance().getCeneTipovaSoba().get(tip).keySet()) {
	                if (datumi.equals("01.04.2024.,31.10.2025.")) {
	                    assertEquals("01.04.2024.,31.10.2025.", datumi);
	                    break;
	                }
	            }
	        }
	    }
	}

	@Test
	void testDodajTipSobe() {
	    main.zapisPodataka();
	    Osoblje admin = null;
	    for (Osoblje adminko : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (adminko.getKorisnickoIme().equals("dalenik")) {
				admin = adminko;
				break;
			}
	    }
	    menadzerSobe.dodajPočetnuSobu(admin, "dvokrevetna (2)", "01.05.2024.", "31.06.2024.", 500);
	    for (TipSobe tip : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
	        if (tip.getNazivTipaSobe().equals("dvokrevetna (2)")) {
	            for (String datumi : Cenovnik.getInstance().getCeneTipovaSoba().get(tip).keySet()) {
	                if (datumi.equals("01.05.2024.,31.06.2024.")) {
	                    assertEquals("dvokrevetna (2)", tip.getNazivTipaSobe());
	                    assertEquals("01.05.2024.,31.06.2024.", datumi);
	                    assertEquals(500.0, Cenovnik.getInstance().getCeneTipovaSoba().get(tip).get(datumi));
	                    break;
	                }
	            }
	        }
	    }
	}

	@Test
	void testInicijalizujTipSobe() {
	    main.zapisPodataka();
	    Osoblje admin = null;
	    for (Osoblje adminko : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (adminko.getKorisnickoIme().equals("dalenik")) {
				admin = adminko;
				break;
			}
	    }
	    menadzerSobe.inicijalizujPocetnuSobu(admin, "dvokrevetna (2)");
	    for (TipSobe tip : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
	        if (tip.getNazivTipaSobe().equals("dvokrevetna (2)")) {
	            assertEquals("dvokrevetna (2)", tip.getNazivTipaSobe());
	            break;
	        }
	    }
	}

	@Test
	void testNadjiCenuTipaSobe() {
	    main.zapisPodataka();
	    assertEquals(2300.0, menadzerSobe.nadjiPocetnuCenuSobe("jednokrevetna (1)", "01.01.2024.", "31.12.2024."));
	}

	@Test
	void testIzbrisiTipSobe() {
	    main.zapisPodataka();
	    boolean obrisan = false;
	    for (TipSobe tip : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
	        if (tip.getNazivTipaSobe().equals("jednokrevetna (1)")) {
	            obrisan = true;
	            break;
	        }
	    }
	    assertEquals(obrisan, true);
	    menadzerSobe.izbrisiPocetnuSobu("jednokrevetna (1)");
	    obrisan = false;
	    for (TipSobe tip : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
	        if (tip.getNazivTipaSobe().equals("jednokrevetna (1)")) {
	            obrisan = true;
	            break;
	        }
	    }
	    assertEquals(obrisan, false);
	}

	@Test
	void testIzbrisiDatumTipaSobe() {
	    main.zapisPodataka();
	    boolean obrisan = false;
	    for (TipSobe tip : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
	        if (tip.getNazivTipaSobe().equals("jednokrevetna (1)")) {
	            for (String datumi : Cenovnik.getInstance().getCeneTipovaSoba().get(tip).keySet()) {
	                if (datumi.equals("01.01.2024.,31.12.2024.")) {
	                    obrisan = true;
	                    break;
	                }
	            }
	        }
	    }
	    assertEquals(obrisan, true);
	    menadzerSobe.izbrisiDatumPocetneSobe("jednokrevetna (1)", "01.01.2024.", "31.12.2024.");
	    obrisan = false;
	    for (TipSobe tip : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
	        if (tip.getNazivTipaSobe().equals("jednokrevetna (1)")) {
	            for (String datumi : Cenovnik.getInstance().getCeneTipovaSoba().get(tip).keySet()) {
	                if (datumi.equals("01.01.2024.,31.12.2024.")) {
	                    obrisan = true;
	                    break;
	                }
	            }
	        }
	    }
	    assertEquals(obrisan, false);
	}
	@Test
	void testProveraSlobodnihSoba() {
		main.zapisPodataka();
		assertEquals(false, menadzerSobe.proveraSlobodnihSoba("jednokrevetna (1)", "01.01.2024.", "31.12.2024."));
	}
	@Test
	void testProveraTipovaSlobodnihSoba1() {
        main.zapisPodataka();
        ArrayList<TipSobe> tipovi = new ArrayList<TipSobe>();
		for (TipSobe tip : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
			if (tip.getNazivTipaSobe().equals("trokrevetna (2+1)")) {
                tipovi.add(tip);
                break;
			}
		}
        assertEquals(tipovi, menadzerSobe.proveraTipovaSlobodnihSoba("01.01.2024.", "31.12.2024."));
	}
	@Test
	void testDodajSobu() {
		main.zapisPodataka();
		Osoblje admin = null;
	    for (Osoblje adminko : HotelListePodataka.getInstance().getListaZaposlenih()) {
			if (adminko.getKorisnickoIme().equals("dalenik")) {
				admin = adminko;
				break;
			}
	    }
		menadzerSobe.dodajSobu(admin, 200, "jednokrevetna (1)", StatusSobe.SPREMANJE, new ArrayList<SpecijalnePogodnosti>());
		boolean nadjen = false;
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == 200) {
				assertEquals("jednokrevetna (1)", soba.getTip().getNazivTipaSobe());
				assertEquals(StatusSobe.SPREMANJE, soba.getStatus());
				assertEquals(0, soba.getPogodnosti().size());
				nadjen = true;
				break;
			}
		}
		boolean nadjen2 = false;
		for (Soba soba : SlobodneSobe.getInstance().getSlobodneSobe().keySet()) {
			if (soba.getBrojSobe() == 200) {
				assertEquals("jednokrevetna (1)", soba.getTip().getNazivTipaSobe());
				assertEquals(StatusSobe.SPREMANJE, soba.getStatus());
				assertEquals(0, soba.getPogodnosti().size());
				nadjen2 = true;
				break;
			}
		}
		boolean nadjen3 = false;
		for (TipSobe tip : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
			if (tip.getNazivTipaSobe().equals("jednokrevetna (1)")) {
				nadjen3 = true;
			}
		}
		assertEquals(true, nadjen);
		assertEquals(true, nadjen2);
		assertEquals(true, nadjen3);
	}
	@Test
	void testIzbrisiSobu() {
        main.zapisPodataka();
        boolean nadjen = false;
        for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
            if (soba.getBrojSobe() == 101) {
            	menadzerSobe.obrisiSobu(101);
                nadjen = true;
                break;
            }
        }
        assertEquals(true, nadjen);
        nadjen = false;
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == 101) {
				nadjen = true;
				break;
			}
		}
		boolean nadjen2 = false;
		for (Soba soba : SlobodneSobe.getInstance().getSlobodneSobe().keySet()) {
			if (soba.getBrojSobe() == 101) {
				nadjen2 = true;
				break;
			}
		}
		boolean nadjen3 = false;
		for (Soba soba : Izvestaji.getInstance().getNocenjeSoba().keySet()) {
			if (soba.getBrojSobe() == 101) {
				nadjen3 = true;
				break;
			}
		}
        assertEquals(false, nadjen);
        assertEquals(false, nadjen2);
        assertEquals(false, nadjen3);
	}
	@Test
	void testProveraBrojaSobe() {
		main.zapisPodataka();
		// PRETHODNO JE BRISANA SOBA SA BROJEM 101
		assertEquals(false, menadzerSobe.proveraBrojaSobe(101));
	}
	@Test
	void testPromenaStatusaSobe() {
		main.zapisPodataka();
		menadzerSobe.promenaStatusaSobe(101, StatusSobe.ZAUZETA);
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == 101) {
				assertEquals(StatusSobe.ZAUZETA, soba.getStatus());
				break;
			}
		}
	}
	@Test
	void testPromenaBrojaSobe() {
		main.zapisPodataka();
		menadzerSobe.promenaBrojaSobe(101, 202);
		boolean nadjen = false;
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			if (soba.getBrojSobe() == 202) {
				assertEquals(202, soba.getBrojSobe());
				nadjen = true;
				break;
			}
		}
		assertEquals(true, nadjen);
	}
	@Test
	void testNadjiStatusSobe() {
		main.zapisPodataka();
		assertEquals(StatusSobe.SLOBODNA, menadzerSobe.nadjiStatusSobe(101));
	}
	@Test
	void testNadjiTipSobe() {
		main.zapisPodataka();
		assertEquals("trokrevetna (2+1)", menadzerSobe.nadjiTipSobe(101).getNazivTipaSobe());
	}
 }
