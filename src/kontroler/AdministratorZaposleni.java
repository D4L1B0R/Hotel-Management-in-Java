package kontroler;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import entiteti.Gost;
import entiteti.Osoblje;
import entiteti.Sobarica;
import funkcionalnosti.MenadzerZaposleni;
import hotel.HotelListePodataka;
import podaci.Pol;
import podaci.Zaposleni;
import prikaz.StranicaAdministratora;
import podaci.NivoStrucneSpreme;

public class AdministratorZaposleni {
    private JTextField[] textFields;
    private JComboBox<Pol> polComboBox;
    private JComboBox<Zaposleni> tipComboBox;
    private JComboBox<NivoStrucneSpreme> spremaComboBox;
    private JLabel errorLabel;
    private DefaultTableModel tableModel;
    private java.util.List<Osoblje> osobljeList;
    private String[] columnNames;
    private JTable zaposleniTable;
    private JPanel zaposleniPanel;
    private JButton deleteButton;
    private JLabel errorLabel1;
    private JLabel errorLabel2;
    private JLabel errorLabel3;
    private JLabel errorLabel4;
    private StranicaAdministratora admin;
    private Osoblje administrator;

    public AdministratorZaposleni(Osoblje administrator, StranicaAdministratora admin, JTextField[] textFields, JComboBox<Pol> polComboBox, JComboBox<Zaposleni> tipComboBox, JComboBox<NivoStrucneSpreme> spremaComboBox, JLabel errorLabel1, JLabel errorLabel2, JLabel errorLabel3, JLabel errorLabel4, DefaultTableModel tableModel, java.util.List<Osoblje> osobljeList, String[] columnNames, JTable zaposleniTable, JPanel zaposleniPanel, JButton deleteButton) { // Modify this line
        this.setTextFields(textFields);
        this.setPolComboBox(polComboBox);
        this.setTipComboBox(tipComboBox);
        this.setSpremaComboBox(spremaComboBox);
        this.setErrorLabel(errorLabel1);
        this.setErrorLabel(errorLabel2);
        this.setErrorLabel(errorLabel3);
        this.setTableModel(tableModel);
        this.osobljeList = osobljeList;
        this.columnNames = columnNames;
        this.zaposleniTable = zaposleniTable;
        this.setZaposleniPanel(zaposleniPanel);
        this.setDeleteButton(deleteButton);
        this.errorLabel1 = errorLabel1;
        this.errorLabel2 = errorLabel2;
        this.errorLabel3 = errorLabel3;
        this.errorLabel4 = errorLabel4;
        this.setAdmin(admin);
        this.administrator = administrator;
        
        initializeTableSelectionListener();
    }

    public ActionListener getAddButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	for (JTextField textField : textFields) {
                    if (textField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            	String[] rowData = new String[columnNames.length + 1];
                int textFieldIndex = 0;
                for (int i = 0; i < columnNames.length; i++) {
                	if (i == 10) {
                		continue;
                	}
                    if (i == 2) {
                        rowData[i] = ((podaci.Pol) polComboBox.getSelectedItem()).name();
                    } else if (i == 3) {
                        rowData[i] = ((podaci.Zaposleni) tipComboBox.getSelectedItem()).name();
                    } else if (i == 8) {
                        rowData[i] = ((podaci.NivoStrucneSpreme) spremaComboBox.getSelectedItem()).name();
                    } else {
                        rowData[i] = textFields[textFieldIndex].getText();
                        textFieldIndex++;
                    }
                }
                boolean problem = false;
		        if (validacija()) {
		        	problem = true;
		        }
		   		for (Osoblje radnik : HotelListePodataka.getInstance().getListaZaposlenih()) {
		   			if (textFields[5].getText().equals(radnik.getKorisnickoIme())) {
		   				errorLabel4.setForeground(Color.RED);
		   				errorLabel4.setText("Korisničko ime već postoji.");
		   				problem = true;
		   			}
		   		}
		   		for (Gost gost : HotelListePodataka.getInstance().getListaGostiju()) {
		   			if (textFields[5].getText().equals(gost.getKorisnickoIme())) {
		   				errorLabel4.setForeground(Color.RED);
		   				errorLabel4.setText("Korisničko ime već postoji.");
		   				problem = true;
		   			}
		   		}
		   		if (problem) {
		   			return;
		   		}
                
                // Create a new Osoblje object with the input data and add it to the osobljeList
	           MenadzerZaposleni menadzer = new MenadzerZaposleni();
               Osoblje radnik = menadzer.dodavanjeZaposlenih(rowData[0], rowData[1], podaci.Pol.valueOf(rowData[2]), podaci.Zaposleni.valueOf(rowData[3]), rowData[4], Long.parseLong(rowData[5]), rowData[6], rowData[7],"lozinka", podaci.NivoStrucneSpreme.valueOf(rowData[8]), Integer.parseInt(rowData[9]));
               rowData[10] = String.valueOf(radnik.getPlata());
               tableModel.addRow(rowData);
               textFields[0].setText("");
               textFields[1].setText("");
               polComboBox.setSelectedIndex(0);
               tipComboBox.setSelectedIndex(0);
               textFields[2].setText("");
               textFields[3].setText("");
               textFields[4].setText("");
               textFields[5].setText("");
               spremaComboBox.setSelectedIndex(0);
               textFields[6].setText("");
               errorLabel1.setText("");
               errorLabel2.setText("");
               errorLabel3.setText("");
            }
        };
    }
    public void initializeTableSelectionListener() {
        // Add a ListSelectionListener to the table
        zaposleniTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRowIndex = zaposleniTable.getSelectedRow();
                if (selectedRowIndex >= 0) {
                    // Get the selected Zaposleni object
                    Osoblje selectedZaposleni = osobljeList.get(selectedRowIndex);
                    
                    errorLabel1.setText("");
                    errorLabel2.setText("");
                    errorLabel3.setText("");
                    errorLabel4.setText("");

                    // Populate the form fields with the data from the selected Zaposleni object
                    textFields[0].setText(selectedZaposleni.getIme());
                    textFields[1].setText(selectedZaposleni.getPrezime());
                    polComboBox.setSelectedItem(selectedZaposleni.getPol());
                    tipComboBox.setSelectedItem(selectedZaposleni.getTip());
                    textFields[2].setText(selectedZaposleni.getDatumRodjenja());
                    textFields[3].setText(String.valueOf(selectedZaposleni.getTelefon()));
                    textFields[4].setText(selectedZaposleni.getAdresa());
                    textFields[5].setText(selectedZaposleni.getKorisnickoIme());
                    spremaComboBox.setSelectedItem(selectedZaposleni.getStrucnaSprema());
                    textFields[6].setText(String.valueOf(selectedZaposleni.getStaz()));
                }
            }
        });
    }
    
    public ActionListener getIzmeniButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = zaposleniTable.getSelectedRow();
                if (selectedRowIndex >= 0) {
                    // Get the selected Osoblje object
                    Osoblje selectedOsoblje = osobljeList.get(selectedRowIndex);

                    // Validation
                    for (int i = 0; i < textFields.length - 1; i++) {
                        JTextField textField = textFields[i];
                        if (textField.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
					if (validacija()) {
						return;
					}

                    // When the "Izmeni" button is clicked again, update the Osoblje object and the table row with the new values
                    String ime = textFields[0].getText();
                    String prezime = textFields[1].getText();
                    Pol pol = (Pol) polComboBox.getSelectedItem();
                    Zaposleni tip = (Zaposleni) tipComboBox.getSelectedItem();
                    String datumRodjenja = textFields[2].getText();
                    long telefon = Long.parseLong(textFields[3].getText());
                    String adresa = textFields[4].getText();
                    NivoStrucneSpreme strucnaSprema = (NivoStrucneSpreme) spremaComboBox.getSelectedItem();
                    int staz = Integer.parseInt(textFields[6].getText());

                    selectedOsoblje.setIme(ime);
                    selectedOsoblje.setPrezime(prezime);
                    selectedOsoblje.setPol(pol);
                    selectedOsoblje.setTip(tip);
                    selectedOsoblje.setDatumRodjenja(datumRodjenja);
                    selectedOsoblje.setTelefon(telefon);
                    selectedOsoblje.setAdresa(adresa);
                    selectedOsoblje.setStrucnaSprema(strucnaSprema);
                    selectedOsoblje.setStaz(staz);
                    selectedOsoblje.setPlata(30000*(strucnaSprema.vrednost + staz/10));
                    
					if (tip == Zaposleni.SOBARICA) {
						for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
							if (sobarica.getKorisnickoIme().equals(selectedOsoblje.getKorisnickoIme())) {
								sobarica.setIme(ime);
								sobarica.setPrezime(prezime);
								sobarica.setPol(pol);
								sobarica.setDatumRodjenja(datumRodjenja);
								sobarica.setTelefon(telefon);
								sobarica.setAdresa(adresa);
								sobarica.setStrucnaSprema(strucnaSprema);
								sobarica.setStaz(staz);
								sobarica.setPlata(30000 * (strucnaSprema.vrednost + staz / 10));
								break;
							}
						}
					}
					if (tableModel.getValueAt(selectedRowIndex, 3).equals("SOBARICA") && tip != Zaposleni.SOBARICA) {
						for (Sobarica sobarica : HotelListePodataka.getInstance().getListaSobarica()) {
                            if (sobarica.getKorisnickoIme().equals(selectedOsoblje.getKorisnickoIme())) {
                                HotelListePodataka.getInstance().getListaSobarica().remove(sobarica);
                                break;
                            }
                        }
					}
					if (tip == Zaposleni.SOBARICA && !tableModel.getValueAt(selectedRowIndex, 3).equals("SOBARICA")) {
						MenadzerZaposleni menadzer = new MenadzerZaposleni();
						menadzer.brisanjeZaposlenih(administrator, tableModel.getValueAt(selectedRowIndex, 7).toString());
						menadzer.dodavanjeZaposlenih(ime, prezime, pol, tip, datumRodjenja, telefon, adresa, tableModel.getValueAt(selectedRowIndex, 7).toString(), "lozinka", strucnaSprema, staz);
					}
                    tableModel.setValueAt(ime, selectedRowIndex, 0);
                    tableModel.setValueAt(prezime, selectedRowIndex, 1);
                    tableModel.setValueAt(pol, selectedRowIndex, 2);
                    tableModel.setValueAt(tip, selectedRowIndex, 3);
                    tableModel.setValueAt(datumRodjenja, selectedRowIndex, 4);
                    tableModel.setValueAt(telefon, selectedRowIndex, 5);
                    tableModel.setValueAt(adresa, selectedRowIndex, 6);
                    tableModel.setValueAt(strucnaSprema, selectedRowIndex, 8);
                    tableModel.setValueAt(staz, selectedRowIndex, 9);
                    tableModel.setValueAt(30000*(strucnaSprema.vrednost + staz/10), selectedRowIndex, 10);
                    textFields[0].setText("");
                    textFields[1].setText("");
                    polComboBox.setSelectedIndex(0);
                    tipComboBox.setSelectedIndex(0);
                    textFields[2].setText("");
                    textFields[3].setText("");
                    textFields[4].setText("");
                    textFields[5].setText("");
                    spremaComboBox.setSelectedIndex(0);
                    textFields[6].setText("");
                    errorLabel1.setText("");
                    errorLabel2.setText("");
                    errorLabel3.setText("");
                    errorLabel4.setText("Nije dozvoljeno menjanje korisničkog imena.");
                }
            }
        };
    }

    public ActionListener getDeleteButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	errorLabel1.setText("");
                errorLabel2.setText("");
                errorLabel3.setText("");
                errorLabel4.setText("");
                int selectedRow = zaposleniTable.getSelectedRow();
                if (selectedRow != -1) {
                    int response = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da obrišete ove podatke?", "Potvrda", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                    	MenadzerZaposleni menadzer = new MenadzerZaposleni();
                    	String korisnickoIme = osobljeList.get(selectedRow).getKorisnickoIme();
                    	menadzer.brisanjeZaposlenih(administrator, korisnickoIme);
                        tableModel.removeRow(selectedRow);// Remove the corresponding object from the list
                        textFields[0].setText("");
                        textFields[1].setText("");
                        polComboBox.setSelectedIndex(0);
                        tipComboBox.setSelectedIndex(0);
                        textFields[2].setText("");
                        textFields[3].setText("");
                        textFields[4].setText("");
                        textFields[5].setText("");
                        spremaComboBox.setSelectedIndex(0);
                        textFields[6].setText("");
                    }
                }
            }
        };
    }
    
    public boolean validacija() {
    	 errorLabel1.setText("");
         errorLabel2.setText("");
         errorLabel3.setText("");
         errorLabel4.setText("");

         // Validation
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

         try {
             int staz = Integer.parseInt(textFields[6].getText());
             if (staz < 0) {
                 errorLabel3.setForeground(Color.RED);
                 errorLabel3.setText("Staž mora biti pozitivan broj.");
                 problem = true;
             }
         } catch (NumberFormatException ex) {
             errorLabel3.setForeground(Color.RED);
             errorLabel3.setText("Staž mora biti broj.");
             problem = true;
         }
         for (JTextField field : textFields) {
             if (field.getText().contains("|")) {
             	if (field.toString().equals(textFields[0].getText())) {
 					errorLabel1.setText("Nije dozvoljeno korišćenje simbola '|'.");
             	} else if (field.toString().equals(textFields[1].getText())) {
 					errorLabel2.setText("Nije dozvoljeno korišćenje simbola '|'.");
 				} else if (field.toString().equals(textFields[2].getText())) {
 					errorLabel3.setText("Nije dozvoljeno korišćenje simbola '|'.");
             	} else if (field.toString().equals(textFields[3].getText())) {
                     errorLabel4.setText("Nije dozvoljeno korišćenje simbola '|'.");
             	}
                 problem = true;
             }
         }
         return problem;
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

	public JComboBox<NivoStrucneSpreme> getSpremaComboBox() {
		return spremaComboBox;
	}

	public void setSpremaComboBox(JComboBox<NivoStrucneSpreme> spremaComboBox) {
		this.spremaComboBox = spremaComboBox;
	}

	public JComboBox<Zaposleni> getTipComboBox() {
		return tipComboBox;
	}

	public void setTipComboBox(JComboBox<Zaposleni> tipComboBox) {
		this.tipComboBox = tipComboBox;
	}

	public JLabel getErrorLabel() {
		return errorLabel;
	}

	public void setErrorLabel(JLabel errorLabel) {
		this.errorLabel = errorLabel;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(DefaultTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public JPanel getZaposleniPanel() {
		return zaposleniPanel;
	}

	public void setZaposleniPanel(JPanel zaposleniPanel) {
		this.zaposleniPanel = zaposleniPanel;
	}

	public JButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(JButton deleteButton) {
		this.deleteButton = deleteButton;
	}
	
	public void setErrorLabel1(JLabel errorLabel1) {
        this.errorLabel1 = errorLabel1;
    }

    public void setErrorLabel2(JLabel errorLabel2) {
        this.errorLabel2 = errorLabel2;
    }

    public void setErrorLabel3(JLabel errorLabel3) {
        this.errorLabel3 = errorLabel3;
    }
    public void setErrorLabel4(JLabel errorLabel4) {
        this.errorLabel4 = errorLabel4;
    }

	public StranicaAdministratora getAdmin() {
		return admin;
	}

	public void setAdmin(StranicaAdministratora admin) {
		this.admin = admin;
	}
}