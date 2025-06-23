package kontroler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import entiteti.Osoblje;
import entiteti.Soba;
import entiteti.Sobarica;

public class Izvestaji {
    private Osoblje administrator;
    private JTextField pocetkaField;
    private JTextField krajaField;
    private JRadioButton prihodiRashodiRadioButton;
    private JRadioButton brojNocenjaRadioButton;
    private JRadioButton rezervacijeRadioButton;
    private JRadioButton ocisceneSobeRadioButton;
    private JLabel error1;
    private JLabel error2;

    public Izvestaji(JLabel error1, JLabel error2, Osoblje administrator, JTextField pocetkaField, JTextField krajaField, JRadioButton prihodiRashodiRadioButton, JRadioButton brojNocenjaRadioButton, JRadioButton rezervacijeRadioButton, JRadioButton ocisceneSobeRadioButton) {
        this.setAdministrator(administrator);
        this.pocetkaField = pocetkaField;
        this.krajaField = krajaField;
        this.prihodiRashodiRadioButton = prihodiRashodiRadioButton;
        this.brojNocenjaRadioButton = brojNocenjaRadioButton;
        this.rezervacijeRadioButton = rezervacijeRadioButton;
        this.ocisceneSobeRadioButton = ocisceneSobeRadioButton;
        this.error1 = error1;
        this.error2 = error2;
    }

    public ActionListener getPretragaButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pocetka = pocetkaField.getText();
                String kraja = krajaField.getText();
                
                error1.setText("");
                error2.setText("");
                
                boolean problem = false;
                try {
                    String startDateString = pocetka;
                    String endDateString = kraja;

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
                    dateFormat.setLenient(false);

                    java.util.Date startDate = dateFormat.parse(startDateString);
                    java.util.Date endDate = dateFormat.parse(endDateString);

                    if (startDate.after(endDate)) {
                        error1.setText("Datum početka mora biti pre datuma kraja.");
                        problem = true;
                    }
                    if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(pocetka, LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) < 0) {
        				error1.setText("Datum početka mora biti pre današnjeg dana.");
        				problem = true;
        			}
        			if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(kraja, LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) < 0) {
        				error2.setText("Datum kraja mora biti pre današnjeg dana.");
        				problem = true;
        			}
                } catch (ParseException ex) {
                    error1.setText("Unesite datum u formatu dd.MM.yyyy.");
                    error2.setText("Unesite datum u formatu dd.MM.yyyy.");
                    problem = true;
                }
                
				if (problem) {
					return;
				}
				
                if (prihodiRashodiRadioButton.isSelected()) {
                	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                	LocalDate startDate = LocalDate.parse(pocetka, formatter);
                	LocalDate endDate = LocalDate.parse(kraja, formatter);
                	double prihodi = 0.0;
                	for (String datum : podaci.PrihodiRashodi.getInstance().getDnevniPrihodi().keySet()) {
                	    LocalDate currentDatum = LocalDate.parse(datum, formatter);
                	    if ((currentDatum.isAfter(startDate) || currentDatum.isEqual(startDate)) && (currentDatum.isBefore(endDate) || currentDatum.isEqual(endDate))) {
                	        prihodi += podaci.PrihodiRashodi.getInstance().getDnevniPrihodi().get(datum);
                	    }
                	}
                	double rashodi = 0.0;
					for (String datum : podaci.PrihodiRashodi.getInstance().getDnevniRashodi().keySet()) {
						LocalDate currentDatum = LocalDate.parse(datum, formatter);
						if ((currentDatum.isAfter(startDate) || currentDatum.isEqual(startDate)) && (currentDatum.isBefore(endDate) || currentDatum.isEqual(endDate))) {
							rashodi += podaci.PrihodiRashodi.getInstance().getDnevniRashodi().get(datum);
						}
					}
                	JOptionPane.showMessageDialog(null, "Ukupni prihodi u periodu od " + pocetka + " do " + kraja + " iznose " + prihodi + " RSD, dok su rashodi iznosili " + rashodi + " RSD.");
                } else if (brojNocenjaRadioButton.isSelected()) {
                	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                	LocalDate startDate = LocalDate.parse(pocetka, formatter);
                	LocalDate endDate = LocalDate.parse(kraja, formatter);

                	JFrame frame = new JFrame("Broj noćenja i prihod po sobi");
                	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                	String[] columnNames = {"Soba", "Broj noćenja", "Prihod"};
                	DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
                	JTable table = new JTable(tableModel);
                	frame.add(new JScrollPane(table));

                	for (Soba soba : podaci.Izvestaji.getInstance().getNocenjeSoba().keySet()) {
                	    int brojNocenja = 0;
                	    double prihod = 0;
                	    for (String datum : podaci.Izvestaji.getInstance().getNocenjeSoba().get(soba)) {
                	        LocalDate currentDatumStart = LocalDate.parse(datum.split(",")[0], formatter);
                	        LocalDate currentDatumEnd = LocalDate.parse(datum.split(",")[1], formatter);
                	        if ((currentDatumStart.isAfter(startDate) || currentDatumStart.isEqual(startDate))
                	            && (currentDatumStart.isBefore(endDate) || currentDatumStart.isEqual(endDate))
                	            && (currentDatumEnd.isAfter(startDate) || currentDatumEnd.isEqual(startDate))
                	            && (currentDatumEnd.isBefore(endDate) || currentDatumEnd.isEqual(endDate))) {

                	            brojNocenja += podaci.Izvestaji.getInstance().getObradaRezervacija().get(datum)[0];
                	            prihod += podaci.Izvestaji.getInstance().getObradaRezervacija().get(datum)[1]; // Assuming the second element is the revenue
                	        }
                	    }

                	    // Add a row to the table for each Soba
                	    Object[] rowData = {soba.getBrojSobe(), brojNocenja, prihod};
                	    tableModel.addRow(rowData);
                	}

                	// Display the frame
                	frame.pack();
                	frame.setVisible(true);
                } else if (rezervacijeRadioButton.isSelected()) {
                	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                	LocalDate startDate = LocalDate.parse(pocetka, formatter);
                	LocalDate endDate = LocalDate.parse(kraja, formatter);
                	int potvrdjeneRezervacije = 0;
                	int odbijeneRezervacije = 0;
                	int otkazaneRezervacije = 0;
					for (String datum : podaci.Izvestaji.getInstance().getObradaRezervacija().keySet()) {
						LocalDate currentDatum = LocalDate.parse(datum, formatter);
						if ((currentDatum.isAfter(startDate) || currentDatum.isEqual(startDate)) && (currentDatum.isBefore(endDate) || currentDatum.isEqual(endDate))) {
							potvrdjeneRezervacije += podaci.Izvestaji.getInstance().getObradaRezervacija().get(datum)[0];
							odbijeneRezervacije += podaci.Izvestaji.getInstance().getObradaRezervacija().get(datum)[1];
							otkazaneRezervacije += podaci.Izvestaji.getInstance().getObradaRezervacija().get(datum)[2];
						}
					}
					JOptionPane.showMessageDialog(null, "Ukupan broj obrađenih rezervacija u periodu od " + pocetka + " do " + kraja + " iznosi " + String.valueOf(potvrdjeneRezervacije + odbijeneRezervacije + otkazaneRezervacije) + ", od kojih je " + potvrdjeneRezervacije + " potvrđeno, " + odbijeneRezervacije + " odbijeno i " + otkazaneRezervacije + " otkazano.");
                	
                } else if (ocisceneSobeRadioButton.isSelected()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                    LocalDate startDate = LocalDate.parse(pocetka, formatter);
                    LocalDate endDate = LocalDate.parse(kraja, formatter);

                    // Create a new frame and table
                    JFrame frame = new JFrame("Očišćene sobe");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    String[] columnNames = {"Sobarica", "Broj očišćenih soba"};
                    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
                    JTable table = new JTable(tableModel);
                    frame.add(new JScrollPane(table));

                    for (Sobarica sobarica : podaci.Izvestaji.getInstance().getOcisceneSobe().keySet()) {
                        int ocisceneSobe = 0;
                        for (String datum : podaci.Izvestaji.getInstance().getOcisceneSobe().get(sobarica).keySet()) {
                            LocalDate currentDatum = LocalDate.parse(datum, formatter);
                            if ((currentDatum.isAfter(startDate) || currentDatum.isEqual(startDate)) && (currentDatum.isBefore(endDate) || currentDatum.isEqual(endDate))) {
                                ocisceneSobe += podaci.Izvestaji.getInstance().getOcisceneSobe().get(sobarica).get(datum);
                            }
                        }

                        // Add a row to the table for each Sobarica
                        Object[] rowData = {sobarica.getKorisnickoIme(), ocisceneSobe};
                        tableModel.addRow(rowData);
                    }

                    // Display the frame
                    frame.pack();
                    frame.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "Izaberite vrstu izveštaja.");
				}
            }
        };
    }

	public Osoblje getAdministrator() {
		return administrator;
	}

	public void setAdministrator(Osoblje administrator) {
		this.administrator = administrator;
	}

	public JLabel getError1() {
		return error1;
	}

	public void setError1(JLabel error1) {
		this.error1 = error1;
	}

	public JLabel getError2() {
		return error2;
	}

	public void setError2(JLabel error2) {
		this.error2 = error2;
	}
}