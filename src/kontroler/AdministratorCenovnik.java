package kontroler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import entiteti.Osoblje;
import funkcionalnosti.MenadzerCenovnik;
import funkcionalnosti.MenadzerSobe;
import podaci.Cenovnik;
import podaci.DodatneUsluge;
import podaci.TipSobe;
import prikaz.StranicaAdministratora;

public class AdministratorCenovnik {
    private JFormattedTextField[] fieldsCene;
    private JFormattedTextField[] fieldsUsluge;
    private DefaultTableModel tableModelCeneTipovaSoba;
    private DefaultTableModel tableModelDodatneUsluge;
    private JTable ceneTipovaSobaTable;
    private JTable dodatneUslugeTable;
    private JLabel errorLabelCene1;
    private JLabel errorLabelCene2;
    private JLabel errorLabelCene3;
    private JLabel errorLabelCene4;
    private JLabel errorLabelUsluge1;
    private JLabel errorLabelUsluge2;
    private JLabel errorLabelUsluge3;
    private JLabel errorLabelUsluge4;
    private JTable tableCene;
    private JTable tableUsluge;
    private Map<TipSobe, Map<String, Double>> ceneList;
    private Map<DodatneUsluge, Map<String, Double>> uslugeList;
    private JComboBox<TipSobe> tipSobeComboBoxCene;
    private JComboBox<DodatneUsluge> uslugeComboBox;
    private Osoblje administrator;
    private StranicaAdministratora admin;
    
    public AdministratorCenovnik(StranicaAdministratora admin, Osoblje administrator, JComboBox <DodatneUsluge> uslugeComboBox, JComboBox <TipSobe> tipSobeComboBoxCene, Map<TipSobe, Map<String, Double>> ceneList, Map<DodatneUsluge, Map<String, Double>> uslugeList, JTable ceneTable, JTable uslugeTable, JFormattedTextField[] fieldsCene, JFormattedTextField[] fieldsUsluge, DefaultTableModel tableModelCeneTipovaSoba, DefaultTableModel tableModelDodatneUsluge, JTable ceneTipovaSobaTable, JTable dodatneUslugeTable, JLabel errorLabelCene1, JLabel errorLabelCene2, JLabel errorLabelCene3, JLabel errorLabelCene4, JLabel errorLabelUsluge1, JLabel errorLabelUsluge2, JLabel errorLabelUsluge3, JLabel errorLabelUsluge4) {
        this.fieldsCene = fieldsCene;
        this.fieldsUsluge = fieldsUsluge;
        this.tableModelCeneTipovaSoba = tableModelCeneTipovaSoba;
        this.tableModelDodatneUsluge = tableModelDodatneUsluge;
        this.ceneTipovaSobaTable = ceneTipovaSobaTable;
        this.dodatneUslugeTable = dodatneUslugeTable;
        this.setErrorLabelCene1(errorLabelCene1);
        this.setErrorLabelCene2(errorLabelCene2);
        this.setErrorLabelCene3(errorLabelCene3);
        this.setErrorLabelCene4(errorLabelCene4);
        this.setErrorLabelUsluge1(errorLabelUsluge1);
        this.setErrorLabelUsluge2(errorLabelUsluge2);
        this.setErrorLabelUsluge3(errorLabelUsluge3);
        this.setErrorLabelUsluge4(errorLabelUsluge4);
        this.tableCene = ceneTable;
        this.tableUsluge = uslugeTable;
        this.ceneList = ceneList;
        this.uslugeList = uslugeList;
        this.tipSobeComboBoxCene = tipSobeComboBoxCene;
        this.uslugeComboBox = uslugeComboBox;
        this.administrator = administrator;
        this.admin = admin;
        
        this.initializeTableSelectionListenerCene();
        this.initializeTableSelectionListenerUsluge();
    }
        
        public ActionListener getAddButtonListenerCene() {
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	
                	for (JTextField textField : fieldsCene) {
                        if (textField.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    
					if (validacijaCene()) {
						return;
					}
                    // Get the values from the fields
                    String tipSobe = fieldsCene[0].getText();
                    String datumPocetka = fieldsCene[1].getText();
                    String datumKraja = fieldsCene[2].getText();
                    Double cena = Double.parseDouble(fieldsCene[3].getText());

                    // Create a new row
                    Object[] rowData = {tipSobe, datumPocetka, datumKraja, cena};

                    // Add the row to the table model
                    tableModelCeneTipovaSoba.addRow(rowData);
                    MenadzerSobe menadzerSobe = new MenadzerSobe();
                    menadzerSobe.inicijalizujPocetnuCenuSobe(administrator, tipSobe, datumPocetka, datumKraja, cena);
					for (JTextField field : fieldsCene) {
						field.setText("");
					}
					admin.refreshCene(tableModelCeneTipovaSoba, tipSobeComboBoxCene);
					errorLabelCene1.setText("");
					errorLabelCene2.setText("");
					errorLabelCene3.setText("");
					errorLabelCene4.setText("");
                }
            };
        }
        
        public ActionListener getDeleteButtonListenerCene() {
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	errorLabelCene1.setText("");
                    errorLabelCene2.setText("");
                    errorLabelCene3.setText("");
                    errorLabelCene4.setText("");
                    int selectedRow = ceneTipovaSobaTable.getSelectedRow();

                    // Remove the selected row from the table model
                    if (selectedRow != -1) {
                        tableModelCeneTipovaSoba.removeRow(selectedRow);
                        if (Cenovnik.getInstance().getCeneTipovaSoba() != null) {
							for (TipSobe tipSobe : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
								if (tipSobe.getNazivTipaSobe().equals(fieldsCene[0].getText())) {
									Cenovnik.getInstance().getCeneTipovaSoba().remove(tipSobe);
									break;
								}
							}
                        }
                        for (JTextField field : fieldsCene) {
    						field.setText("");
    					}
                        admin.refreshCene(tableModelCeneTipovaSoba, tipSobeComboBoxCene);
                    }
                }
            };
        }
        
        public void initializeTableSelectionListenerCene() {
            tableCene.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    int selectedRowIndex = tableCene.getSelectedRow();
                    if (selectedRowIndex != -1  && selectedRowIndex < ceneList.size()) {
                        TipSobe selectedTip = ceneList.keySet().toArray(new TipSobe[0])[selectedRowIndex];
                        fieldsCene[0].setText(selectedTip.getNazivTipaSobe());
                        String usluga = ceneList.get(selectedTip).keySet().iterator().next();
                        String[] splitUsluga = usluga.split(",");
                        fieldsCene[1].setText(splitUsluga[0]);
                        fieldsCene[2].setText(splitUsluga[1]);
                        fieldsCene[3].setText(String.valueOf(ceneList.get(selectedTip).get(usluga)));
                    }
                }
            });
        }

        public ActionListener getIzmeniButtonListenerCene() {
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Validate the form data
                    for (JTextField textField : fieldsCene) {
                        if (textField.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    int selectedRow = ceneTipovaSobaTable.getSelectedRow();

                    // Check if a row is selected
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "Molimo izaberite red.", "Greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if (validacijaCene()) {
                        return;
                    }
                    
                    // Get the selected TipSobe from the ceneList
                    TipSobe selectedTip = ceneList.keySet().toArray(new TipSobe[0])[selectedRow];

                    // Remove the existing key-value pair from the ceneList and Cenovnik instance
                    ceneList.remove(selectedTip);
                    Cenovnik.getInstance().getCeneTipovaSoba().remove(selectedTip);

                    // Update the selected row with the values from the fields
                    selectedTip = new TipSobe(fieldsCene[0].getText());
                    String usluga = fieldsCene[1].getText() + "," + fieldsCene[2].getText();
                    Double cena;
                    try {
                        cena = Double.parseDouble(fieldsCene[3].getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Cena mora biti broj.", "Greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Create a new inner HashMap and add it to the ceneList
                    HashMap<String, Double> innerMap = new HashMap<>();
                    innerMap.put(usluga, cena);
                    ceneList.put(selectedTip, innerMap);

                    // Update the Cenovnik instance
                    Cenovnik.getInstance().getCeneTipovaSoba().put(selectedTip, innerMap);

                    // Update the table model
                    tableModelCeneTipovaSoba.setValueAt(fieldsCene[0].getText(), selectedRow, 0);
                    tableModelCeneTipovaSoba.setValueAt(fieldsCene[1].getText(), selectedRow, 1);
                    tableModelCeneTipovaSoba.setValueAt(fieldsCene[2].getText(), selectedRow, 2);
                    tableModelCeneTipovaSoba.setValueAt(cena, selectedRow, 3);
                    for (JTextField field : fieldsCene) {
						field.setText("");
					}
                    admin.refreshCene(tableModelCeneTipovaSoba, tipSobeComboBoxCene);
					errorLabelCene1.setText("");
					errorLabelCene2.setText("");
					errorLabelCene3.setText("");
					errorLabelCene4.setText("");
                }
            };
        }
        
		public boolean validacijaCene() {
            errorLabelCene1.setText("");
            errorLabelCene2.setText("");
            errorLabelCene3.setText("");
            errorLabelCene4.setText("");
            
            boolean problem = false;
            try {
				Double.parseDouble(fieldsCene[3].getText());
			} catch (NumberFormatException e) {
				errorLabelCene4.setText("Unesite broj.");
				problem = true;
			}
			
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
            dateFormat.setLenient(false);
            try {
                String startDateString = fieldsCene[1].getText();
                String endDateString = fieldsCene[2].getText();

                // Check if the dates strictly follow the "dd.MM.yyyy." format
                if (!startDateString.matches("\\d{2}\\.\\d{2}\\.\\d{4}\\.") || !endDateString.matches("\\d{2}\\.\\d{2}\\.\\d{4}\\.")) {
                    errorLabelCene2.setText("Datumi moraju biti u formatu (dd.MM.yyyy.).");
                    errorLabelCene3.setText("Datumi moraju biti u formatu (dd.MM.yyyy.).");
                    problem = true;
                } else {
                    java.util.Date startDate = dateFormat.parse(startDateString);
                    java.util.Date endDate = dateFormat.parse(endDateString);
                    if (startDate.after(endDate)) {
                        errorLabelCene2.setText("Datum početka mora biti pre datuma kraja.");
                        problem = true;
                    }
                    if (Cenovnik.getInstance().getCeneTipovaSoba() != null) {
    	                for (TipSobe tip : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
    	                	if (tip.getNazivTipaSobe().equals(fieldsCene[0].getText())) {
    	                		for (String datum : Cenovnik.getInstance().getCeneTipovaSoba().get(tip).keySet()) {
    	                			if (!dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.poredjenjeDatuma(fieldsCene[1].getText(), fieldsCene[2].getText(), datum.split(",")[0], datum.split(",")[1])) {
    	                				errorLabelCene2.setText("Postoji već uneta cena za taj period.");
    	                				errorLabelCene3.setText("Postoji već uneta cena za taj period.");
    	                				problem = true;
    	                				break;
    	                			}
    	                		}
    	                	}
    	                }
                    }
                    if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(fieldsCene[1].getText(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) > 0) {
    					errorLabelCene2.setText("Datum početka mora krenuti od današnjeg dana.");
    					problem = true;
    				}
    				if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(fieldsCene[2].getText(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) > 0) {
    					errorLabelCene3.setText("Datum kraja mora krenuti od današnjeg dana.");
    					problem = true;
    				}
                }
            } catch (ParseException ex) {
                errorLabelCene2.setText("Datumi moraju biti u formatu (dd.MM.yyyy.).");
                errorLabelCene3.setText("Datumi moraju biti u formatu (dd.MM.yyyy.).");
                problem = true;
            }
            
            if (Cenovnik.getInstance().getCeneTipovaSoba() != null) {
				for (TipSobe tipSobe : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
					for (String datum : Cenovnik.getInstance().getCeneTipovaSoba().get(tipSobe).keySet()) {
						if (tipSobe.getNazivTipaSobe().equals(fieldsCene[0].getText()) && datum.equals(fieldsCene[1].getText() + "," + fieldsCene[2].getText())) {
							errorLabelCene1.setText("Tip sobe već postoji za dati period.");
							problem = true;
							break;
						}
					}
				}
            }
            for (JTextField field : fieldsCene) {
                if (field.getText().contains("|")) {
                	if (field.toString().equals(fieldsCene[0].getText())) {
						errorLabelCene1.setText("Nije dozvoljeno korišćenje simbola '|'.");
                	} else if (field.toString().equals(fieldsCene[1].getText())) {
						errorLabelCene2.setText("Nije dozvoljeno korišćenje simbola '|'.");
					} else if (field.toString().equals(fieldsCene[2].getText())) {
						errorLabelCene3.setText("Nije dozvoljeno korišćenje simbola '|'.");
                	} else if (field.toString().equals(fieldsCene[3].getText())) {
                		errorLabelCene4.setText("Nije dozvoljeno korišćenje simbola '|'.");
                	}
                    problem = true;
                }
            }
			return problem;
		}
		
		public ActionListener getAddButtonListenerUsluge() {
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	
                	for (JTextField textField : fieldsUsluge) {
                        if (textField.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    
					if (validacijaUsluge()) {
						return;
					}
                    // Get the values from the fields
                    String usluga = fieldsUsluge[0].getText();
                    String datumPocetka = fieldsUsluge[1].getText();
                    String datumKraja = fieldsUsluge[2].getText();
                    Double cena = Double.parseDouble(fieldsUsluge[3].getText());

                    // Create a new row
                    Object[] rowData = {usluga, datumPocetka, datumKraja, cena};

                    // Add the row to the table model
                    tableModelDodatneUsluge.addRow(rowData);
                    Cenovnik.getInstance().fromString2(rowData[0].toString() + "|" +  rowData[1].toString() + "," + rowData[2].toString() + "|" + rowData[3].toString());
                    for (JTextField field : fieldsUsluge) {
                        field.setText("");
                    }
                    admin.refreshUsluge(tableModelDodatneUsluge, uslugeComboBox);
                    errorLabelUsluge1.setText("");
                    errorLabelUsluge2.setText("");
                    errorLabelUsluge3.setText("");
                    errorLabelUsluge4.setText("");
                }
            };
        }
        

		public void initializeTableSelectionListenerUsluge() {
		    tableUsluge.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		        @Override
		        public void valueChanged(ListSelectionEvent e) {
		            int selectedRowIndex = tableUsluge.getSelectedRow();
		            if (selectedRowIndex != -1 && selectedRowIndex < uslugeList.size()) {
		                DodatneUsluge selectedUsluga = uslugeList.keySet().toArray(new DodatneUsluge[0])[selectedRowIndex];
		                fieldsUsluge[0].setText(selectedUsluga.getDodatneUsluge());
		                String usluga = uslugeList.get(selectedUsluga).keySet().iterator().next();
		                String[] splitUsluga = usluga.split(",");
		                fieldsUsluge[1].setText(splitUsluga[0]);
		                fieldsUsluge[2].setText(splitUsluga[1]);
		                fieldsUsluge[3].setText(String.valueOf(uslugeList.get(selectedUsluga).get(usluga)));
		            }
		        }
		    });
		}
		
		public ActionListener getIzmeniButtonListenerUsluge() { return new ActionListener() { @Override public void actionPerformed(ActionEvent e) { // Validate the form data for (JTextField textField : fieldsUsluge) { if (textField.getText().isEmpty()) { JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE); return; } }

	        int selectedRow = dodatneUslugeTable.getSelectedRow();

	        // Check if a row is selected
	        if (selectedRow == -1) {
	            JOptionPane.showMessageDialog(null, "Molimo izaberite red.", "Greška", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        
	        for (JTextField textField : fieldsUsluge) {
                if (textField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

	        if (validacijaUsluge()) {
	            return;
	        }

	        // Get the selected DodatneUsluge from the uslugeList
	        DodatneUsluge selectedUsluga = uslugeList.keySet().toArray(new DodatneUsluge[0])[selectedRow];

	        // Remove the existing key-value pair from the uslugeList and Cenovnik instance
	        uslugeList.remove(selectedUsluga);
	        Cenovnik.getInstance().getDodatneUsluge().remove(selectedUsluga);

	        // Update the selected row with the values from the fields
	        selectedUsluga.setDodatneUsluge(fieldsUsluge[0].getText());

	        String usluga = fieldsUsluge[1].getText() + "," + fieldsUsluge[2].getText();
	        Double cena;
	        try {
	            cena = Double.parseDouble(fieldsUsluge[3].getText());
	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(null, "Cena mora biti broj.", "Greška", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        // Create a new inner HashMap and add it to the uslugeList
	        HashMap<String, Double> innerMap = new HashMap<>();
	        innerMap.put(usluga, cena);
	        uslugeList.put(selectedUsluga, innerMap);

	        // Update the Cenovnik instance
	        Cenovnik.getInstance().getDodatneUsluge().put(selectedUsluga, innerMap);

	        // Update the table model
	        dodatneUslugeTable.setValueAt(fieldsUsluge[0].getText(), selectedRow, 0);
	        dodatneUslugeTable.setValueAt(fieldsUsluge[1].getText(), selectedRow, 1);
	        dodatneUslugeTable.setValueAt(fieldsUsluge[2].getText(), selectedRow, 2);
	        dodatneUslugeTable.setValueAt(cena, selectedRow, 3);
	        for (JTextField field : fieldsUsluge) {
                field.setText("");
	        }
	        admin.refreshUsluge(tableModelDodatneUsluge, uslugeComboBox);
            errorLabelUsluge1.setText("");
            errorLabelUsluge2.setText("");
            errorLabelUsluge3.setText("");
            errorLabelUsluge4.setText("");
	    }
	};
	}

        public ActionListener getDeleteButtonListenerUsluge() {
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	errorLabelUsluge1.setText("");
                    errorLabelUsluge2.setText("");
                    errorLabelUsluge3.setText("");
                    errorLabelUsluge4.setText("");
                    int selectedRow = dodatneUslugeTable.getSelectedRow();

                    // Remove the selected row from the table model
                    if (selectedRow != -1) {
                        tableModelDodatneUsluge.removeRow(selectedRow);
                        if (Cenovnik.getInstance().getDodatneUsluge() != null) {
							for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
								if (usluga.getDodatneUsluge().equals(fieldsCene[0].getText())) {
									Cenovnik.getInstance().getDodatneUsluge().remove(usluga);
								}
							}
                        }
                        for (JTextField field : fieldsUsluge) {
    						field.setText("");
                        }
                        admin.refreshUsluge(tableModelDodatneUsluge, uslugeComboBox);
                    }
                }
            };
        }
        
        public boolean validacijaUsluge() {
        	errorLabelCene1.setText("");
            errorLabelCene2.setText("");
            errorLabelCene3.setText("");
            errorLabelCene4.setText("");
            
            boolean problem = false;
            try {
				Double.parseDouble(fieldsUsluge[3].getText());
			} catch (NumberFormatException e) {
				errorLabelUsluge4.setText("Unesite broj.");
				problem = true;
			}
			
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
            dateFormat.setLenient(false);
            try {
                String startDateString = fieldsUsluge[1].getText();
                String endDateString = fieldsUsluge[2].getText();

                // Check if the dates strictly follow the "dd.MM.yyyy." format
                if (!startDateString.matches("\\d{2}\\.\\d{2}\\.\\d{4}\\.") || !endDateString.matches("\\d{2}\\.\\d{2}\\.\\d{4}\\.")) {
                    errorLabelUsluge2.setText("Datumi moraju biti u formatu (dd.MM.yyyy.).");
                    errorLabelUsluge3.setText("Datumi moraju biti u formatu (dd.MM.yyyy.).");
                    problem = true;
                } else {
                    java.util.Date startDate = dateFormat.parse(startDateString);
                    java.util.Date endDate = dateFormat.parse(endDateString);
                    if (startDate.after(endDate)) {
                        errorLabelUsluge2.setText("Datum početka mora biti pre datuma kraja.");
                        problem = true;
                    }
                    for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
                    	if (usluga.getDodatneUsluge().equals(fieldsCene[0].getText())) {
                    		for (String datum : Cenovnik.getInstance().getDodatneUsluge().get(usluga).keySet()) {
                    			if (!dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.poredjenjeDatuma(fieldsUsluge[1].getText(), fieldsUsluge[2].getText(), datum.split(",")[0], datum.split(",")[1])) {
                    				errorLabelUsluge2.setText("Postoji već uneta cena za taj period.");
                    				errorLabelUsluge3.setText("Postoji već uneta cena za taj period.");
                    				problem = true;
                    				break;
                    			}
                    		}
                    	}
                    }
        			if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(fieldsUsluge[1].getText(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) > 0) {
        				errorLabelUsluge2.setText("Datum početka mora krenuti od današnjeg dana.");
        				problem = true;
        			}
        			if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(fieldsUsluge[2].getText(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) > 0) {
        				errorLabelUsluge3.setText("Datum kraja mora krenuti od današnjeg dana.");
        				problem = true;
        			}
                }
            } catch (ParseException ex) {
                errorLabelUsluge2.setText("Datumi moraju biti u formatu (dd.MM.yyyy.).");
                errorLabelUsluge3.setText("Datumi moraju biti u formatu (dd.MM.yyyy.).");
                problem = true;
            }
            
            if (Cenovnik.getInstance().getCeneTipovaSoba() != null) {
				for (DodatneUsluge usluga: Cenovnik.getInstance().getDodatneUsluge().keySet()) {
					for (String datum : Cenovnik.getInstance().getDodatneUsluge().get(usluga).keySet()) {
						if (usluga.getDodatneUsluge().equals(fieldsUsluge[0].getText()) && datum.equals(fieldsUsluge[1].getText() + "," + fieldsUsluge[2].getText())) {
							errorLabelUsluge1.setText("Dodatna usluga za dati period već postoji.");
							problem = true;
						}
					}
				}
            }
            
            for (JTextField field : fieldsUsluge) {
                if (field.getText().contains("|")) {
                	if (field.toString().equals(fieldsUsluge[0].getText())) {
						errorLabelUsluge1.setText("Nije dozvoljeno korišćenje simbola '|'.");
                	} else if (field.toString().equals(fieldsUsluge[1].getText())) {
						errorLabelUsluge2.setText("Nije dozvoljeno korišćenje simbola '|'.");
					} else if (field.toString().equals(fieldsUsluge[2].getText())) {
						errorLabelUsluge3.setText("Nije dozvoljeno korišćenje simbola '|'.");
                	} else if (field.toString().equals(fieldsUsluge[3].getText())) {
                		errorLabelUsluge4.setText("Nije dozvoljeno korišćenje simbola '|'.");
                	}
                    problem = true;
                }
            }
            return problem;
		}

		public ActionListener getIzmeniNazivButtonListenerCene() {
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	TipSobe selectedTipSobe = (TipSobe) tipSobeComboBoxCene.getSelectedItem();
                    String novTip = JOptionPane.showInputDialog("Unesite novo ime za: " + selectedTipSobe);
					for (TipSobe tipSobe : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
						if (tipSobe.getNazivTipaSobe().equals(novTip)) {
							JOptionPane.showMessageDialog(null, "Tip sobe već postoji.", "Greška", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}

                    if (novTip != null && !novTip.contains("|")) {
                        MenadzerSobe menadzerSobe = new MenadzerSobe();
                        menadzerSobe.podesiPocetnuSobu(selectedTipSobe.getNazivTipaSobe(), novTip);
                        admin.refreshCene(tableModelCeneTipovaSoba, tipSobeComboBoxCene);
                    }
                }
            };
        }
		
		public ActionListener getIzmeniNazivButtonListenerUsluge() {
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	DodatneUsluge selectedUsluga = (DodatneUsluge) uslugeComboBox.getSelectedItem();
                    String novaUsluga = JOptionPane.showInputDialog("Unesite novo ime za: " + selectedUsluga);
					for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
						if (usluga.getDodatneUsluge().equals(novaUsluga)) {
							JOptionPane.showMessageDialog(null, "Dodatna usluga već postoji.", "Greška", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
                   
                    if (novaUsluga != null && !novaUsluga.contains("|")) {
                        MenadzerCenovnik menadzerUsluga = new MenadzerCenovnik();
                        menadzerUsluga.podesiDodatnuUslugu(selectedUsluga.getDodatneUsluge(), novaUsluga);
                        admin.refreshUsluge(tableModelDodatneUsluge, uslugeComboBox);
                    }
                }
            };
        }
        
		public JLabel getErrorLabelCene1() {
			return errorLabelCene3;
		}

		public void setErrorLabelCene1(JLabel errorLabelCene1) {
			this.errorLabelCene1 = errorLabelCene1;
		}
		
		public JLabel getErrorLabelCene3() {
			return errorLabelCene3;
		}

		public void setErrorLabelCene3(JLabel errorLabelCene3) {
			this.errorLabelCene3 = errorLabelCene3;
		}

		public JLabel getErrorLabelCene4() {
			return errorLabelCene4;
		}

		public void setErrorLabelCene4(JLabel errorLabelCene4) {
			this.errorLabelCene4 = errorLabelCene4;
		}

		public JLabel getErrorLabelUsluge1() {
			return errorLabelUsluge1;
		}

		public void setErrorLabelUsluge1(JLabel errorLabelUsluge1) {
			this.errorLabelUsluge1 = errorLabelUsluge1;
		}

		public JLabel getErrorLabelUsluge2() {
			return errorLabelUsluge2;
		}

		public void setErrorLabelUsluge2(JLabel errorLabelUsluge2) {
			this.errorLabelUsluge2 = errorLabelUsluge2;
		}

		public JLabel getErrorLabelUsluge3() {
			return errorLabelUsluge3;
		}

		public void setErrorLabelUsluge3(JLabel errorLabelUsluge3) {
			this.errorLabelUsluge3 = errorLabelUsluge3;
		}

		public JLabel getErrorLabelUsluge4() {
			return errorLabelUsluge4;
		}

		public void setErrorLabelUsluge4(JLabel errorLabelUsluge4) {
			this.errorLabelUsluge4 = errorLabelUsluge4;
		}

		public JLabel getErrorLabelCene2() {
			return errorLabelCene2;
		}

		public void setErrorLabelCene2(JLabel errorLabelCene2) {
			this.errorLabelCene2 = errorLabelCene2;
		}
		
		public JTable getTableCene() {
			return tableCene;
		}
		
		public void setTableCene(JTable tableCene) {
			this.tableCene = tableCene;
		}
		
		public JTable getTableUsluge() {
			return tableUsluge;
		}

		public void setTableUsluge(JTable tableUsluge) {
			this.tableUsluge = tableUsluge;
		}

		public Map<TipSobe, Map<String, Double>> getCeneList() {
			return this.ceneList;
		}
		
		public void setCeneList(Map<TipSobe, Map<String, Double>> ceneList) {
			this.ceneList = ceneList;
		}
		
		public Map<DodatneUsluge, Map<String, Double>> getUslugeList() {
			return this.uslugeList;
		}

		public void setUslugeList(Map<DodatneUsluge, Map<String, Double>> uslugeList) {
			this.uslugeList = uslugeList;
		}

		public Osoblje getAdminsitrator() {
			return administrator;
		}

		public void setAdministrator(Osoblje adminstrator) {
			this.administrator = adminstrator;
		}

		public JComboBox<DodatneUsluge> getUslugeComboBox() {
			return uslugeComboBox;
		}

		public void setUslugeComboBox(JComboBox<DodatneUsluge> uslugeComboBox) {
			this.uslugeComboBox = uslugeComboBox;
		}

		public StranicaAdministratora getAdmin() {
			return admin;
		}

		public void setAdmin(StranicaAdministratora admin) {
			this.admin = admin;
		}
    }