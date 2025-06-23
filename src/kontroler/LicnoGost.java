package kontroler;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import entiteti.Gost;

public class LicnoGost {
    private JTextField[] textFields;
    private JLabel errorLabel;
    private JPanel gostPanel;
    private JLabel errorLabel1;
    private JLabel errorLabel2;
    private JLabel uspesno;
    private String[] columnNames;
    private Gost selectedGost;

    public LicnoGost(Gost gost, JTextField[] textFields, JLabel errorLabel1, JLabel errorLabel2, JLabel uspesno, String[] columnNames, JPanel gostPanel) {
        this.setTextFields(textFields);
        this.setErrorLabel(errorLabel1);
        this.setErrorLabel(errorLabel2);
        this.setColumnNames(columnNames);
        this.setGostPanel(gostPanel);
        this.errorLabel1 = errorLabel1;
        this.errorLabel2 = errorLabel2;
        this.setUspesno(uspesno);
        this.selectedGost = gost;

        textFields[0].setText(selectedGost.getIme());
        textFields[1].setText(selectedGost.getPrezime());
        textFields[2].setText(selectedGost.getDatumRodjenja());
        textFields[3].setText(String.valueOf(selectedGost.getTelefon()));
        textFields[4].setText(selectedGost.getAdresa());
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
                
                String ime = textFields[0].getText();
                String prezime = textFields[1].getText();
                String datumRodjenja = textFields[2].getText();
                long telefon = Long.parseLong(textFields[3].getText());
                String adresa = textFields[4].getText();

                selectedGost.setIme(ime);
                selectedGost.setPrezime(prezime);
                selectedGost.setDatumRodjenja(datumRodjenja);
                selectedGost.setTelefon(telefon);
                selectedGost.setAdresa(adresa);

                uspesno.setText("Uspešno ste izmenili podatke.");
				for (JTextField textField : textFields) {
					textField.setText("");
				}
            }
        };
    }

	public JLabel getErrorLabel() {
		return errorLabel;
	}

	public void setErrorLabel(JLabel errorLabel) {
		this.errorLabel = errorLabel;
	}

	public JPanel getGostPanel() {
		return gostPanel;
	}

	public void setGostPanel(JPanel gostPanel) {
		this.gostPanel = gostPanel;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
    
	public JTextField[] getTextFields() {
		return textFields;
	}
	
	public void setTextFields(JTextField[] textFields) {
		this.textFields = textFields;
	}
	
	public JLabel getUspesno() {
		return uspesno;
	}
	
	public void setUspesno(JLabel uspesno) {
		this.uspesno = uspesno;
	}
}