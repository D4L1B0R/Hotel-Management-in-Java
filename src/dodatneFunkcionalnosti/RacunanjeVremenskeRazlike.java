package dodatneFunkcionalnosti;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.util.Locale;

public abstract class RacunanjeVremenskeRazlike {
 private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");
    public static int IzracunajRazlikuVremena(String pocetniDatumString, String krajnjiDatumString) {
        Date pocetniDatum = null;
        Date krajnjiDatum = null;
    	try {
            pocetniDatum = sdf.parse(pocetniDatumString);
            krajnjiDatum = sdf.parse(krajnjiDatumString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long razlika = krajnjiDatum.getTime() - pocetniDatum.getTime();
        return (int) (razlika / (1000 * 60 * 60 * 24));
    }
    public static boolean poredjenjeDatuma(String pocetakPrvogPerioda, String krajPrvogPerioda, String pocetakDrugogPerioda, String krajDrugogPerioda) {
        try {
            Date pocetakDatum1 = sdf.parse(pocetakPrvogPerioda);
            Date krajDatum1 = sdf.parse(krajPrvogPerioda);
            Date pocetakDatum2 = sdf.parse(pocetakDrugogPerioda);
            Date krajDatum2 = sdf.parse(krajDrugogPerioda);

            Calendar pocetak1 = Calendar.getInstance();
            pocetak1.setTime(pocetakDatum1);
            Calendar kraj1 = Calendar.getInstance();
            kraj1.setTime(krajDatum1);

            Calendar pocetak2 = Calendar.getInstance();
            pocetak2.setTime(pocetakDatum2);
            Calendar kraj2 = Calendar.getInstance();
            kraj2.setTime(krajDatum2);

            while (!pocetak1.after(kraj1)) {
                if (!pocetak2.after(kraj2)) {
                    if (pocetak1.equals(pocetak2)) {
                        return false;
                    }
                    pocetak2.add(Calendar.DAY_OF_YEAR, 1);
                }
                pocetak2.setTime(pocetakDatum2);
                pocetak1.add(Calendar.DAY_OF_YEAR, 1);
            }
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static ArrayList<String> listaDatumaZaPeriod(String pocetniDatum, String krajnjiDatum) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy.", Locale.getDefault());
        LocalDate pocetakPerioda = LocalDate.parse(pocetniDatum, dtf);
        LocalDate krajPerioda = LocalDate.parse(krajnjiDatum, dtf);
        ArrayList<String> udaljenost = new ArrayList<>();
        LocalDate danasnjiDatum = pocetakPerioda;
        while (!danasnjiDatum.isAfter(krajPerioda)) {
         udaljenost.add(danasnjiDatum.format(dtf));
         danasnjiDatum = danasnjiDatum.plusDays(1);
        }
        return udaljenost;
    }
}