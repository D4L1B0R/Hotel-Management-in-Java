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
import hotel.HotelListePodataka;
import podaci.NivoStrucneSpreme;
import podaci.Pol;
import podaci.Zaposleni;

public class AdministratorGosti {
	private JTextField[] textFields;
    private JComboBox<Pol> polComboBox;
    private JComboBox<Zaposleni> tipComboBox;
    private JComboBox<NivoStrucneSpreme> spremaComboBox;
    private JLabel errorLabel;
    private DefaultTableModel tableModel;
    private java.util.List<Gost> gostiList;
    private String[] columnNames;
    private JTable gostiTable;
    private JPanel gostiPanel;
    private JButton deleteButton;
    private JLabel errorLabel1;
    private JLabel errorLabel2;
    private JLabel errorLabel3;

    public AdministratorGosti(JTextField[] textFields, JComboBox<Pol> polComboBox, JLabel errorLabel1, JLabel errorLabel2, JLabel errorLabel3, DefaultTableModel tableModel, java.util.List<Gost> gostiList, String[] columnNames, JTable gostiTable, JPanel gostiPanel, JButton deleteButton){ // Modify this line
        this.setTextFields(textFields);
        this.setPolComboBox(polComboBox);
        this.setTipComboBox(tipComboBox);
        this.setSpremaComboBox(spremaComboBox);
        this.setErrorLabel(errorLabel1);
        this.setErrorLabel(errorLabel2);
        this.setErrorLabel(errorLabel3);
        this.setTableModel(tableModel);
        this.setGostiList(gostiList);
        this.columnNames = columnNames;
        this.gostiTable = gostiTable;
        this.gostiPanel = gostiPanel;
        this.setDeleteButton(deleteButton);
        this.errorLabel1 = errorLabel1;
        this.errorLabel2 = errorLabel2;
        this.errorLabel3 = errorLabel3;
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
	            String[] rowData = new String[columnNames.length];
	            int textFieldIndex = 0;
	            for (int i = 0; i < columnNames.length; i++) {
	                if (i == 2) {
	                    rowData[i] = ((Pol) polComboBox.getSelectedItem()).name();
	                } else {
	                    rowData[i] = textFields[textFieldIndex].getText();
	                    textFieldIndex++;
	                }
	            }
	            boolean problem = false;
	            if (validacija()) {
	            	return;
	            }
	            String korisnickoIme = textFields[5].getText();
	            for (Osoblje osoblje : HotelListePodataka.getInstance().getListaZaposlenih()) {
	                if (osoblje.getKorisnickoIme().equals(korisnickoIme)) {
	                    errorLabel3.setForeground(Color.RED);
	                    errorLabel3.setText("Korisničko ime već postoji.");
	                    problem = true;
	                }
	            }
	            for (Gost gost : HotelListePodataka.getInstance().getListaGostiju()) {
	                if (gost.getKorisnickoIme().equals(korisnickoIme)) {
	                    errorLabel3.setForeground(Color.RED);
	                    errorLabel3.setText("Korisničko ime već postoji.");
	                    problem = true;
	                }
	            } 
				if (problem) {
					return;
				}
	            
	            tableModel.addRow(rowData);
	
	            // Create a new Gost object with the input data and add it to the gostiList
	            Gost newGost = new Gost(rowData[0], rowData[1], Pol.valueOf(rowData[2]), rowData[3], Long.parseLong(rowData[4]), rowData[5], rowData[6], "Lozinka");
	            gostiList.add(newGost);
	            for (JTextField textField : textFields) {
                    textField.setText("");
                }
	            errorLabel1.setText("");
	            errorLabel2.setText("");
	            errorLabel3.setText("");
	        }
	    };
	}
    
    public void initializeTableSelectionListener() {
        // Add a ListSelectionListener to the table
        gostiTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedRowIndex = gostiTable.getSelectedRow();
                if (selectedRowIndex >= 0) {
                    // Get the selected Gost object
                    Gost selectedGost = gostiList.get(selectedRowIndex);
                    
                    errorLabel1.setText("");
    	            errorLabel2.setText("");
    	            errorLabel3.setText("");

                    // Populate the form fields with the data from the selected Gost object
                    textFields[0].setText(selectedGost.getIme());
                    textFields[1].setText(selectedGost.getPrezime());
                    polComboBox.setSelectedItem(selectedGost.getPol());
                    textFields[2].setText(selectedGost.getDatumRodjenja());
                    textFields[3].setText(String.valueOf(selectedGost.getTelefon()));
                    textFields[4].setText(selectedGost.getAdresa());
                    textFields[5].setText(selectedGost.getKorisnickoIme());
                }
            }
        });
    }
	
    public ActionListener getIzmeniButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	for (int i = 0; i < textFields.length - 1; i++) {
            	    JTextField textField = textFields[i];
            	    if (textField.getText().isEmpty()) {
            	        JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
            	        return;
            	    }
            	}
                int selectedRowIndex = gostiTable.getSelectedRow();
                if (selectedRowIndex >= 0) {
                    // Get the selected Gost object
                    Gost selectedGost = gostiList.get(selectedRowIndex);
                    
					if (validacija()) {
						return;
					}
                    
                    // Use the data from the form fields to update the selected Gost object and the table row
                    String ime = textFields[0].getText();
                    String prezime = textFields[1].getText();
                    Pol pol = (Pol) polComboBox.getSelectedItem();
                    String datumRodjenja = textFields[2].getText();
                    long telefon = Long.parseLong(textFields[3].getText());
                    String adresa = textFields[4].getText();

                    selectedGost.setIme(ime);
                    selectedGost.setPrezime(prezime);
                    selectedGost.setPol(pol);
                    selectedGost.setDatumRodjenja(datumRodjenja);
                    selectedGost.setTelefon(telefon);
                    selectedGost.setAdresa(adresa);

                    tableModel.setValueAt(ime, selectedRowIndex, 0);
                    tableModel.setValueAt(prezime, selectedRowIndex, 1);
                    tableModel.setValueAt(pol, selectedRowIndex, 2);
                    tableModel.setValueAt(datumRodjenja, selectedRowIndex, 3);
                    tableModel.setValueAt(telefon, selectedRowIndex, 4);
                    tableModel.setValueAt(adresa, selectedRowIndex, 5);
                    for (JTextField textField : textFields) {
                        textField.setText("");
                    }
                    errorLabel3.setText("Nije moguće menjati korisničko ime.");
                    errorLabel2.setText("");
                    errorLabel1.setText("");
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
	            int selectedRow = gostiTable.getSelectedRow();
	            if (selectedRow != -1) {
	                int response = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da obrišete ovog gosta?", "Brisanje gosta", JOptionPane.YES_NO_OPTION);
	                if (response == JOptionPane.YES_OPTION) {
	                    tableModel.removeRow(selectedRow);
	                    gostiList.remove(selectedRow); // Remove the corresponding object from the list
	                    for (JTextField textField : textFields) {
	                        textField.setText("");
	                    }
	                }
	            }
	        }
	    };
	}
	
	public boolean validacija() {
		
		errorLabel1.setText("");
        errorLabel2.setText("");
        errorLabel3.setText("");
        
        boolean problem = false;

        if (!textFields[3].getText().matches("\\d{10}")) {
            errorLabel1.setForeground(Color.RED);
            errorLabel1.setText("Telefon nije validan. Unesite 10 cifara.");
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
            errorLabel2.setText("Datum rodjenja nije validan. Unesite u formatu dd.MM.yyyy.");
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
	public java.util.List<Gost> getGostiList() {
		return gostiList;
	}
	public void setGostiList(java.util.List<Gost> gostiList) {
		this.gostiList = gostiList;
	}
	public JPanel getGostiPanel() {
		return gostiPanel;
	}
	public void setGostiPanel(JPanel gostiPanel) {
		this.gostiPanel = gostiPanel;
	}
}