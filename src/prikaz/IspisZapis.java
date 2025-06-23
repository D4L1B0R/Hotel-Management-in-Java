package prikaz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import entiteti.Gost;
import entiteti.Osoblje;
import entiteti.Rezervacija;
import entiteti.Soba;
import entiteti.Sobarica;
import hotel.HotelListePodataka;
import podaci.Cenovnik;
import podaci.DodatneUsluge;
import podaci.Izvestaji;
import podaci.OdlasciDolasci;
import podaci.PrihodiRashodi;
import podaci.SlobodneSobe;
import podaci.TipSobe;

// ISPISI
public class IspisZapis {
	// PODACI
	String filePathTipoviSoba = "src/zapisi/TipoviSoba.txt";
	String filePathDodatneUsluge = "src/zapisi/DodatneUsluge.txt";
	String filePathSlobodneSobe = "src/zapisi/SlobodneSobe.txt";
	String filePathSlobodniTipoviSoba = "src/zapisi/SlobodniTipoviSoba.txt";
	// ENTITETI
	String filePathGosti = "src/zapisi/Gosti.txt";
	String filePathOsoblje = "src/zapisi/Osoblje.txt";
	String filePathRezervacije = "src/zapisi/Rezervacije.txt";
	String filePathSobarice = "src/zapisi/Sobarice.txt";
	String filePathSobe = "src/zapisi/Sobe.txt";
	// IZVEŠTAJI
	String filePathPrihodi = "src/zapisi/Prihodi.txt";
	String filePathRashodi = "src/zapisi/Rashodi.txt";
	String filePathOdlasci = "src/zapisi/Odlasci.txt";
	String filePathDolasci = "src/zapisi/Dolasci.txt";
	String filePathZauzetost = "src/zapisi/Zauzetost.txt";
	String filePathObradaRezervacija = "src/zapisi/ObradaRezervacija.txt";
	String filePathNocenjeSobe = "src/zapisi/NocenjeSobe.txt";
	String filePathOcisceneSobe = "src/zapisi/OcisceneSobe.txt";
	String filePathPrihodiPoTipuSobe = "src/zapisi/ZaradaPoTipovima.txt";
	public void ispisPodataka() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathTipoviSoba))) {
	        if (Cenovnik.getInstance().getCeneTipovaSoba() != null) {
	            for (TipSobe funkcija : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
	                for (String datum : Cenovnik.getInstance().getCeneTipovaSoba().get(funkcija).keySet()) {
	                    writer.write(Cenovnik.getInstance().toString(funkcija, datum));
	                    writer.newLine(); // This will ensure each TipSobe is written to a new line
	                }
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathDodatneUsluge))) {
			if (Cenovnik.getInstance().getDodatneUsluge() != null) {
				for (DodatneUsluge funkcija : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
					for (String datum : Cenovnik.getInstance().getDodatneUsluge().get(funkcija).keySet()) {
		                writer.write(Cenovnik.getInstance().toString(funkcija, datum));
		                writer.newLine();
		            }
	            }
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathSlobodneSobe))) {
			if (SlobodneSobe.getInstance().getSlobodneSobe() != null) {
	            for (Soba funkcija : SlobodneSobe.getInstance().getSlobodneSobe().keySet()) {
	                writer.write(SlobodneSobe.getInstance().toString(funkcija));
	                writer.newLine();
	            }
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathSlobodniTipoviSoba))) {
			if (SlobodneSobe.getInstance().getSlobodniTipoviSoba() != null) {
	            for (TipSobe funkcija : SlobodneSobe.getInstance().getSlobodniTipoviSoba().keySet()) {
	                writer.write(SlobodneSobe.getInstance().toString(funkcija));
	                writer.newLine();
	            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathPrihodi))) {
			if (PrihodiRashodi.getInstance().getDnevniPrihodi() != null) {
	            for (String funkcija : PrihodiRashodi.getInstance().getDnevniPrihodi().keySet()) {
	                writer.write(PrihodiRashodi.getInstance().toString(funkcija));
	                writer.newLine();
	            }
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathRashodi))) {
			if (PrihodiRashodi.getInstance().getDnevniRashodi() != null) {
	            for (String funkcija : PrihodiRashodi.getInstance().getDnevniRashodi().keySet()) {
	                writer.write(PrihodiRashodi.getInstance().toString2(funkcija));
	                writer.newLine();
	            }
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathOdlasci))) {
			if (OdlasciDolasci.getInstance().getDnevniOdlasci() != null) {
	            for (String funkcija : OdlasciDolasci.getInstance().getDnevniOdlasci().keySet()) {
	                writer.write(OdlasciDolasci.getInstance().toString(funkcija));
	                writer.newLine();
	            }
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathDolasci))) {
			if (OdlasciDolasci.getInstance().getDnevniDolasci() != null) {
	            for (String funkcija : OdlasciDolasci.getInstance().getDnevniDolasci().keySet()) {
	                writer.write(OdlasciDolasci.getInstance().toString(funkcija));
	                writer.newLine();
	            }
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathZauzetost))) {
			if (OdlasciDolasci.getInstance().getDnevnaZauzetost() != null) {
	            for (String funkcija : OdlasciDolasci.getInstance().getDnevnaZauzetost().keySet()) {
	                writer.write(OdlasciDolasci.getInstance().toString(funkcija));
	                writer.newLine();
	            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathGosti))) {
            for (Gost funkcija : HotelListePodataka.getInstance().getListaGostiju()) {
                writer.write(funkcija.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathOsoblje))) {
            for (Osoblje funkcija : HotelListePodataka.getInstance().getListaZaposlenih()) {
                writer.write(funkcija.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathSobe))) {
            for (Soba funkcija : HotelListePodataka.getInstance().getListaSoba()) {
                writer.write(funkcija.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathRezervacije))) {
            for (Rezervacija funkcija : HotelListePodataka.getInstance().getListaRezervacija()) {
                writer.write(funkcija.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathSobarice))) {
            for (Sobarica funkcija : HotelListePodataka.getInstance().getListaSobarica()) {
                writer.write(funkcija.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathNocenjeSobe))) {
        	if (Izvestaji.getInstance().getNocenjeSoba() != null) {
                for (Soba funkcija : Izvestaji.getInstance().getNocenjeSoba().keySet()) {
                    writer.write(Izvestaji.getInstance().toStringNocenje(funkcija));
                    writer.newLine();
                }
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathOcisceneSobe))) {
			if (Izvestaji.getInstance().getOcisceneSobe() != null) {
				for (Sobarica funkcija : Izvestaji.getInstance().getOcisceneSobe().keySet()) {
					for (String datum : Izvestaji.getInstance().getOcisceneSobe().get(funkcija).keySet()) {
						writer.write(Izvestaji.getInstance().toStringOcisceneSobe(funkcija, datum));
						writer.newLine();
					}
				}
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathObradaRezervacija))) {
			if (Izvestaji.getInstance().getObradaRezervacija() != null) {
				for (String datum : Izvestaji.getInstance().getObradaRezervacija().keySet()) {
					writer.write(Izvestaji.getInstance().toStringObradaRezervacija(datum));
					writer.newLine();
				}
			}
        } catch (IOException e) {
            e.printStackTrace();
        }        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathPrihodiPoTipuSobe))) {
			if (PrihodiRashodi.getInstance().getPrihodiPoTipuSobe() != null) {
				for (TipSobe tip : PrihodiRashodi.getInstance().getPrihodiPoTipuSobe().keySet()) {
					for (String datum : PrihodiRashodi.getInstance().getPrihodiPoTipuSobe().get(tip).keySet()) {
						writer.write(PrihodiRashodi.getInstance().toString3(tip, datum));
						writer.newLine();
					}
				}
			}
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }
	// ZAPISI
	public void zapisPodataka() {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePathTipoviSoba))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	Cenovnik.getInstance().fromString(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathDodatneUsluge))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	Cenovnik.getInstance().fromString2(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathGosti))) {
            String line;
            ArrayList <Gost> a = HotelListePodataka.getInstance().getListaGostiju();
            while ((line = reader.readLine()) != null) {
            	Gost funkcija = Gost.fromString(line);
                a.add(funkcija);
            }
            HotelListePodataka.getInstance().setListaGostiju(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathOsoblje))) {
            String line;
            ArrayList <Osoblje> a = HotelListePodataka.getInstance().getListaZaposlenih();
            while ((line = reader.readLine()) != null) {
            	Osoblje funkcija = Osoblje.fromString(line);
                a.add(funkcija);
            }
            HotelListePodataka.getInstance().setListaZaposlenih(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathSobe))) {
            String line;
            ArrayList <Soba> a = HotelListePodataka.getInstance().getListaSoba();
            while ((line = reader.readLine()) != null) {
            	Soba funkcija = Soba.fromString(line);
                a.add(funkcija);
            }
            HotelListePodataka.getInstance().setListaSoba(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathRezervacije))) {
            String line;
            ArrayList <Rezervacija> a = HotelListePodataka.getInstance().getListaRezervacija();
            while ((line = reader.readLine()) != null) {
            	Rezervacija funkcija = Rezervacija.fromString(line);
                a.add(funkcija);
            }
            HotelListePodataka.getInstance().setListaRezervacija(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathSobarice))) {
            String line;
            ArrayList<Sobarica> a = HotelListePodataka.getInstance().getListaSobarica();
            while ((line = reader.readLine()) != null) {
            	Sobarica funkcija = Sobarica.fromString(line);
                a.add(funkcija);
            }
            HotelListePodataka.getInstance().setListaSobarica(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathSlobodneSobe))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	SlobodneSobe.getInstance().fromString(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathSlobodniTipoviSoba))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	SlobodneSobe.getInstance().fromString2(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathPrihodi))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	PrihodiRashodi.getInstance().fromString(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathRashodi))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	PrihodiRashodi.getInstance().fromString2(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathOdlasci))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	OdlasciDolasci.getInstance().fromString(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathDolasci))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	OdlasciDolasci.getInstance().fromString2(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathZauzetost))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	OdlasciDolasci.getInstance().fromString3(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathNocenjeSobe))) {
            String line;
			while ((line = reader.readLine()) != null) {
				Izvestaji.getInstance().fromStringNocenje(line);
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathOcisceneSobe))) {
            String line;
            while ((line = reader.readLine()) != null) {
				Izvestaji.getInstance().fromStringSobe(line);
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathObradaRezervacija))) {
            String line;
            while ((line = reader.readLine()) != null) {
				Izvestaji.getInstance().fromStringRezervacije(line);
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathPrihodiPoTipuSobe))) {
            String line;
            while ((line = reader.readLine()) != null) {
				PrihodiRashodi.getInstance().fromString3(line);
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}