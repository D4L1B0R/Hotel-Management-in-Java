package podaci;

public class DodatneUsluge{
	private String dodatneUsluge;
	
	public DodatneUsluge(String dodatneUsluge) {
		this.dodatneUsluge = dodatneUsluge;
	}
	public String getDodatneUsluge() {
		return dodatneUsluge;
	}

	public void setDodatneUsluge(String dodatneUsluge) {
		this.dodatneUsluge = dodatneUsluge;
	}
	@Override
	public String toString() {
	    return dodatneUsluge;
	}
	public static DodatneUsluge fromString(String str) {
	    return new DodatneUsluge(str);
	}
}