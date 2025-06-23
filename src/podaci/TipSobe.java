package podaci;

public class TipSobe {
	private String nazivTipaSobe;
	
	public TipSobe(String nazivTipaSobe) {
		this.nazivTipaSobe = nazivTipaSobe;
	}
	public String getNazivTipaSobe() {
		return nazivTipaSobe;
	}

	public void setNazivTipaSobe(String nazivTipaSobe) {
		this.nazivTipaSobe = nazivTipaSobe;
	}
	@Override
    public String toString() {
        return nazivTipaSobe;
    }
    public static TipSobe fromString(String str) {
        return new TipSobe(str);
    }
}
