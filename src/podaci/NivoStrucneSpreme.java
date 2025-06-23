package podaci;

public enum NivoStrucneSpreme {
	OSNOVNA(1),
	SREDNJA(1.2),
	VIŠA(1.5);
	
	public double vrednost;
    NivoStrucneSpreme(double vrednost) {
        this.vrednost = vrednost;
    }
    
}
