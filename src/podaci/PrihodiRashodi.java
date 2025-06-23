package podaci;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class PrihodiRashodi {
	private static PrihodiRashodi instance;
	private HashMap<String, Double> dnevniPrihodi;
	private HashMap<String, Double> dnevniRashodi;
	private HashMap<TipSobe, HashMap<String, Double>> prihodiPoTipuSobe;
	
	private PrihodiRashodi() {
		dnevniPrihodi = new HashMap<>();
		dnevniRashodi = new HashMap<>();
		prihodiPoTipuSobe = new HashMap<>();
    }
    public static PrihodiRashodi getInstance() {
        if (instance == null) {
            instance = new PrihodiRashodi();
        }
        return instance;
    }
	public void inicirajDnevnePrihodeRashode() {
		dnevniPrihodi.put(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), 0.0);
		dnevniRashodi.put(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), 0.0);
	}
	public HashMap<String, Double> getDnevniPrihodi() {
		return dnevniPrihodi;
	}
	public void setDnevniPrihodi(HashMap<String, Double> dnevniPrihodi) {
		this.dnevniPrihodi = dnevniPrihodi;
	}
	public HashMap<String, Double> getDnevniRashodi() {
		return dnevniRashodi;
	}
	public void setDnevniRashodi(HashMap<String, Double> dnevniRashodi) {
		this.dnevniRashodi = dnevniRashodi;
	}
	public String toString(String datum) {
		double cena = 0;
		for (String s : dnevniPrihodi.keySet()) {
			if (s.equals(datum)) {
				cena = dnevniPrihodi.get(datum);
			}
		}
		return datum + "|" + cena;
	}
	public String toString2(String datum) {
		double cena = 0;
		for (String s : dnevniRashodi.keySet()) {
			if (s.equals(datum)) {
				cena = dnevniRashodi.get(datum);
			}
		}
		return datum + "|" + cena;
	}
	public String toString3(TipSobe tipSobe, String datum) {
		return tipSobe.getNazivTipaSobe() + "|" + datum + "|" + PrihodiRashodi.getInstance().getPrihodiPoTipuSobe().get(tipSobe).get(datum);
	}
	public void fromString(String str) {
		String [] prihod = str.split("\\|");
		dnevniPrihodi.put(prihod[0], Double.parseDouble(prihod[1]));
	}
	public void fromString2(String str) {
		String [] rashodi = str.split("\\|");
		dnevniRashodi.put(rashodi[0], Double.parseDouble(rashodi[1]));
	}
	public void fromString3(String str) {
		String[] prihod = str.split("\\|");
		for (TipSobe tipSobe : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
			if (tipSobe.getNazivTipaSobe().equals(prihod[0])) {
				if (!prihodiPoTipuSobe.containsKey(tipSobe))
					prihodiPoTipuSobe.put(tipSobe, new HashMap<>());
				prihodiPoTipuSobe.get(tipSobe).put(prihod[1], Double.parseDouble(prihod[2]));
				break;
			}
		}
	}
	public HashMap<TipSobe, HashMap<String, Double>> getPrihodiPoTipuSobe() {
		return prihodiPoTipuSobe;
	}
	public void setPrihodiPoTipuSobe(HashMap<TipSobe, HashMap<String, Double>> prihodiPoTipuSobe) {
		this.prihodiPoTipuSobe = prihodiPoTipuSobe;
	}
}