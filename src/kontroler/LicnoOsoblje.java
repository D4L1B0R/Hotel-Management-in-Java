package kontroler;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import entiteti.Osoblje;
import podaci.Pol;

public class LicnoOsoblje {
    private JTextField[] textFields;
    private JComboBox<Pol> polComboBox;
    private JLabel errorLabel;
    private JPanel zaposleniPanel;
    private JLabel errorLabel1;
    private JLabel errorLabel2;
    private JLabel uspesno;
    private String[] columnNames;
    private Osoblje selectedZaposleni;

    public LicnoOsoblje(Osoblje recepcioner, JTextField[] textFields, JComboBox<Pol> polComboBox, JLabel errorLabel1, JLabel errorLabel2, JLabel uspesno, String[] columnNames, JPanel zaposleniPanel) { // Modify this line
        this.setTextFields(textFields);
        this.setPolComboBox(polComboBox);
        this.setErrorLabel(errorLabel1);
        this.setErrorLabel(errorLabel2);
        this.columnNames = columnNames;
        this.setZaposleniPanel(zaposleniPanel);
        this.errorLabel1 = errorLabel1;
        this.errorLabel2 = errorLabel2;
        this.setUspesno(uspesno);
        this.selectedZaposleni = recepcioner;
        
        textFields[0].setText(selectedZaposleni.getIme());
        textFields[1].setText(selectedZaposleni.getPrezime());
        polComboBox.setSelectedItem(selectedZaposleni.getPol());
        textFields[2].setText(selectedZaposleni.getDatumRodjenja());
        textFields[3].setText(String.valueOf(selectedZaposleni.getTelefon()));
        textFields[4].setText(selectedZaposleni.getAdresa());
        textFields[5].setText(selectedZaposleni.getLozinka());
    }
    
    public ActionListener getIzmeniButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	errorLabel1.setText("");
                errorLabel2.setText("");
                uspesno.setText("");
                
                // Validation
                for (JTextField textField : textFields) {
                    if (textField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                boolean problem = false;
                if (!textFields[3].getText().matches("\\d{10}")) {
                    errorLabel1.setForeground(Color.RED);
                    errorLabel1.setText("Broj telefona mora imati 10 cifara.");
                    problem = true;
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
                dateFormat.setLenient(false);
                try {
                    dateFormat.parse(textFields[2].getText());
                    String dateString = textFields[2].getText();
                    if (!dateString.matches("\\d{2}\\.\\d{2}\\.\\d{4}\\.")) {
                    	errorLabel2.setForeground(Color.RED);
                        errorLabel2.setText("Datum rodjenja mora biti u formatu (dd.MM.yyyy.).");
                        problem = true;
                    }
                    if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(textFields[2].getText(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) < 0) {
        				errorLabel2.setForeground(Color.RED);
        				errorLabel2.setText("Datum rodjenja ne može biti u buducnosti.");
        				problem = true;
                    }
                } catch (ParseException ex) {
                    errorLabel2.setForeground(Color.RED);
                    errorLabel2.setText("Datum rodjenja nije u ispravnom formatu (dd.MM.yyyy.).");
                    problem = true;
                }
                
				if (problem) {
					return;
				}

                // When the "Izmeni" button is clicked again, update the Osoblje object and the table row with the new values
                String ime = textFields[0].getText();
                String prezime = textFields[1].getText();
                Pol pol = (Pol) polComboBox.getSelectedItem();
                String datumRodjenja = textFields[2].getText();
                long telefon = Long.parseLong(textFields[3].getText());
                String adresa = textFields[4].getText();
                String lozinka = textFields[5].getText();
                
                selectedZaposleni.setIme(ime);
                selectedZaposleni.setPrezime(prezime);
                selectedZaposleni.setPol(pol);
                selectedZaposleni.setDatumRodjenja(datumRodjenja);
                selectedZaposleni.setTelefon(telefon);
                selectedZaposleni.setAdresa(adresa);
                selectedZaposleni.setLozinka(lozinka);
                
                uspesno.setText("Uspešno ste izmenili podatke.");
				for (JTextField textField : textFields) {
					textField.setText("");
				}
                
            }
        };
    }

	public JTextField[] getTextFields() {
		return textFields;
	}

	public void setTextFields(JTextField[] textFields) {
		this.textFields = textFields;
	}

	public JComboBox<Pol> getPolComboBox() {
		return polComboBox;
	}

	public void setPolComboBox(JComboBox<Pol> polComboBox) {
		this.polComboBox = polComboBox;
	}

	public JLabel getErrorLabel() {
		return errorLabel;
	}

	public void setErrorLabel(JLabel errorLabel) {
		this.errorLabel = errorLabel;
	}

	public JPanel getZaposleniPanel() {
		return zaposleniPanel;
	}

	public void setZaposleniPanel(JPanel zaposleniPanel) {
		this.zaposleniPanel = zaposleniPanel;
	}
	
	public void setErrorLabel1(JLabel errorLabel1) {
        this.errorLabel1 = errorLabel1;
    }

    public void setErrorLabel2(JLabel errorLabel2) {
        this.errorLabel2 = errorLabel2;
    }

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public JLabel getUspesno() {
		return uspesno;
	}

	public void setUspesno(JLabel uspesno) {
		this.uspesno = uspesno;
	}
}