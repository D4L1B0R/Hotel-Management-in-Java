package kontroler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import entiteti.Gost;
import entiteti.Rezervacija;
import entiteti.Soba;
import hotel.HotelListePodataka;
import podaci.DodatneUsluge;
import podaci.StatusRezervacije;
import podaci.TipSobe;
import prikaz.StranicaAdministratora;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdministratorRezervacije {
    private JTextField[] textFields;
    private JLabel errorLabel1, errorLabel2, errorLabel3, errorLabel4, errorLabel5;
    private DefaultTableModel tableModel;
    private List<Rezervacija> rezervacijaList;
    private JTable rezervacijaTable;
    private JPanel rezervacijaPanel;
    private JButton deleteButton;
    private String[] columnNames;
    private JComboBox<StatusRezervacije> statusComboBox;
    private JComboBox<TipSobe> tipSobeComboBox;
    private JList<DodatneUsluge> dodatneUslugeComboBox;
    private StranicaAdministratora admin;
    private DefaultTableModel tableModelSobe;

    public AdministratorRezervacije(StranicaAdministratora admin ,DefaultTableModel tableModelSobe, JComboBox<TipSobe> tipSobeComboBox, JComboBox<StatusRezervacije> statusComboBox, JList<DodatneUsluge> dodatneUslugeComboBox, JTextField[] textFields, JLabel errorLabel1, JLabel errorLabel2, JLabel errorLabel3, JLabel errorLabel4, JLabel errorLabel5, DefaultTableModel tableModel, List<Rezervacija> rezervacijaList, String [] columnNames, JTable rezervacijaTable, JPanel rezervacijaPanel, JButton deleteButton) {
        this.textFields = textFields;
        this.setStatusComboBox(statusComboBox);
        this.setTipSobeComboBox(tipSobeComboBox);
        this.setDodatneUslugeComboBox(dodatneUslugeComboBox);
        this.setErrorLabel1(errorLabel1);
        this.setErrorLabel2(errorLabel2);
        this.setErrorLabel3(errorLabel3);
        this.setErrorLabel4(errorLabel4);
        this.setErrorLabel5(errorLabel5);
        this.tableModel = tableModel;
        this.rezervacijaList = rezervacijaList;
        this.rezervacijaTable = rezervacijaTable;
        this.setRezervacijaPanel(rezervacijaPanel);
        this.setDeleteButton(deleteButton);
        this.setColumnNames(columnNames);
        this.setAdmin(admin);
        this.setTableModelSobe(tableModelSobe);
        
        initializeTableSelectionListener();
        
        rezervacijaPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // Clear the existing data
                while (tableModel.getRowCount() > 0) {
                	tableModel.removeRow(0);
                }

                // Add the data from the list
                for (Rezervacija rezervacija : rezervacijaList) {
                    List<Object> rowData = new ArrayList<>();
                    for (Field field : Rezervacija.class.getDeclaredFields()) {
                        field.setAccessible(true); // You need this if fields are private
                        try {
                        	if (field.getName().equals("soba") && field.get(rezervacija) != null) {
                                Soba soba = (Soba) field.get(rezervacija);
                                if (soba != null) {
                                    rowData.add(soba.getBrojSobe());
        						} else {
        							rowData.add("");
        						}
        					} else if (field.getName().equals("gost") && field.get(rezervacija) != null) {
        						Gost gost = (Gost) field.get(rezervacija);
        						if (gost != null) {
        							rowData.add(gost.getKorisnickoIme());
        						} else {
        							rowData.add("");
        						}
                            } else if (field.getName().equals("dodatneUsluge")) {
                                @SuppressWarnings("unchecked")
                                List<DodatneUsluge> dodatneUslugeList = (List<DodatneUsluge>) field.get(rezervacija);
                                String dodatneUslugeString = dodatneUslugeList.stream()
                                    .map(DodatneUsluge::getDodatneUsluge)
                                    .collect(Collectors.joining(","));
                                rowData.add(dodatneUslugeString);
                            } else {
                                rowData.add(field.get(rezervacija));
                            }
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                    }
                    tableModel.addRow(rowData.toArray());
                }
            }
        });
    }

    public ActionListener getAddButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate the form data
                for (JTextField textField : textFields) {
                    if (textField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                String[] rowData = new String[columnNames.length];
                int textFieldIndex = 0;
                for (int i = 0; i < columnNames.length; i++) {
                	if (i == 0 || i == 7) {
                		continue;
                	}
                	if (i == 1) {
                		rowData[1] = ((StatusRezervacije) statusComboBox.getSelectedItem()).name();
                	} else if (i == 9){
                		rowData[9] = ((TipSobe) tipSobeComboBox.getSelectedItem()).getNazivTipaSobe();
                	} else if (i == 8) {
            		    if (dodatneUslugeComboBox.isSelectionEmpty()) {
            		        rowData[8] = "";
            		    } else {
            		        List<DodatneUsluge> selectedDodatneUsluge = dodatneUslugeComboBox.getSelectedValuesList();
            		        rowData[8] = selectedDodatneUsluge.stream()
            		            .map(DodatneUsluge::getDodatneUsluge)
            		            .collect(Collectors.joining(","));
            		    }
                	} else {
	                    rowData[i] = textFields[textFieldIndex].getText();
	                    textFieldIndex++;
                	}
                }
                
				if (validacija()) {
					return;
				}
				ArrayList<DodatneUsluge> dodatneUslugeList = new ArrayList<>();
				for (String el : rowData[8].split(",")) {
					dodatneUslugeList.add(new DodatneUsluge(el));
				}
				Gost gost = null;
				for (Gost gost1 : HotelListePodataka.getInstance().getListaGostiju()) {
					if (gost1.getKorisnickoIme().equals(textFields[3].getText())) {
						gost = gost1;
						break;
					}
				}
				Soba sobica = new Soba(Integer.parseInt(rowData[4]), TipSobe.fromString(rowData[9]));
                Rezervacija newRezervacija = new Rezervacija(StatusRezervacije.valueOf(rowData[1]), rowData[2], rowData[3], sobica, gost, Integer.parseInt(rowData[6]), dodatneUslugeList, TipSobe.fromString(rowData[9]));
                tableModel.addRow(newRezervacija.toString().split("\\|"));
                System.out.println(Arrays.toString(newRezervacija.toString().split("\\|")));
                rezervacijaList.add(newRezervacija);
                admin.refreshSobe(tableModelSobe);

                // Clear the text fields
                for (JTextField textField : textFields) {
                    textField.setText("");
                }
                errorLabel1.setText("");
                errorLabel2.setText("");
                errorLabel3.setText("");
                errorLabel4.setText("");
                errorLabel5.setText("");
            }
        };
    }
    
    public ActionListener getIzmeniButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = rezervacijaTable.getSelectedRow();
                if (selectedRowIndex >= 0) {
                    // Get the selected Osoblje object
                    Rezervacija selectedOsoblje = rezervacijaList.get(selectedRowIndex);

                    // Validate the form data
                    for (JTextField textField : textFields) {
                        if (textField.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

					if (validacija()) {
						return;
					}
					
					selectedOsoblje.setStatus((StatusRezervacije) statusComboBox.getSelectedItem());
					selectedOsoblje.setDatumPocetkaRezervacije(textFields[0].getText());
					selectedOsoblje.setDatumKrajaRezervacije(textFields[1].getText());
					selectedOsoblje.setTipSobe((TipSobe) tipSobeComboBox.getSelectedItem());
					selectedOsoblje.setSoba(new Soba(Integer.parseInt(textFields[2].getText()), (TipSobe) tipSobeComboBox.getSelectedItem()));
					selectedOsoblje.setBrojOsoba(Integer.parseInt(textFields[4].getText()));
					List<DodatneUsluge> selectedDodatneUsluge = dodatneUslugeComboBox.getSelectedValuesList();
					selectedOsoblje.setDodatneUsluge(new ArrayList<>(selectedDodatneUsluge));

                    // Update the table row with the new values
					tableModel.setValueAt(selectedOsoblje.getStatus(), selectedRowIndex, 1);
					tableModel.setValueAt(selectedOsoblje.getDatumPocetkaRezervacije(), selectedRowIndex, 2);
					tableModel.setValueAt(selectedOsoblje.getDatumKrajaRezervacije(), selectedRowIndex, 3);
					tableModel.setValueAt(selectedOsoblje.getSoba().getBrojSobe(), selectedRowIndex, 4);
					tableModel.setValueAt(selectedOsoblje.getGost().getKorisnickoIme(), selectedRowIndex, 5);
					tableModel.setValueAt(selectedOsoblje.getBrojOsoba(), selectedRowIndex, 6);
					List<DodatneUsluge> selectedDodatneUsluge2 = selectedOsoblje.getDodatneUsluge();
					tableModel.setValueAt(selectedDodatneUsluge2.stream()
					    .map(DodatneUsluge::getDodatneUsluge)
					    .collect(Collectors.joining(",")), selectedRowIndex, 8);
					tableModel.setValueAt(selectedOsoblje.getTipSobe(), selectedRowIndex, 9);
					admin.refreshSobe(tableModelSobe);

					// Clear the text fields
					for (JTextField textField : textFields) {
						textField.setText("");
					}
                }
                errorLabel1.setText("");
                errorLabel2.setText("");
                errorLabel3.setText("");
                errorLabel4.setText("");
                errorLabel5.setText("");
            }
        };
    }
    
    public void initializeTableSelectionListener() {
        // Add a ListSelectionListener to the table
        rezervacijaTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRowIndex = rezervacijaTable.getSelectedRow();
                if (selectedRowIndex >= 0) {
                    // Get the selected Rezervacija object
                    Rezervacija selectedRezervacija = rezervacijaList.get(selectedRowIndex);
                    
                    errorLabel1.setText("");
                    errorLabel2.setText("");
                    errorLabel3.setText("");
                    errorLabel4.setText("");
                    errorLabel5.setText("");
                    
                    // Populate the form fields with the data from the selected Rezervacija object
                    statusComboBox.setSelectedItem(selectedRezervacija.getStatus().toString());
                    textFields[0].setText(selectedRezervacija.getDatumPocetkaRezervacije());
                    textFields[1].setText(selectedRezervacija.getDatumKrajaRezervacije());
					if (selectedRezervacija.getSoba() != null) {
						textFields[2].setText(String.valueOf(selectedRezervacija.getSoba().getBrojSobe()));
					}
                    textFields[3].setText(selectedRezervacija.getGost().getKorisnickoIme());
                    if (textFields[4].getText() != "") {
                    	textFields[4].setText(String.valueOf(selectedRezervacija.getBrojOsoba()));
                    }
                    List<DodatneUsluge> selectedDodatneUsluge = selectedRezervacija.getDodatneUsluge();
                    int[] selectedIndices = new int[selectedDodatneUsluge.size()];
                    for (int i = 0; i < selectedDodatneUsluge.size(); i++) {
                        selectedIndices[i] = dodatneUslugeComboBox.getNextMatch(selectedDodatneUsluge.get(i).getDodatneUsluge(), 0, javax.swing.text.Position.Bias.Forward);
                    }
                    dodatneUslugeComboBox.setSelectedIndices(selectedIndices);
                }
            }
        });
    }

    public ActionListener getDeleteButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	errorLabel1.setText("");
                errorLabel2.setText("");
                errorLabel3.setText("");
                errorLabel4.setText("");
                errorLabel5.setText("");
                int selectedRow = rezervacijaTable.getSelectedRow();
                if (selectedRow != -1) {
                    int response = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da obrišete ove podatke?", "Potvrda", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
						for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
							if (soba.getBrojSobe() == Integer.parseInt(textFields[2].getText())) {
								for (Rezervacija rezervacija : soba.getListaRezervacija()) {
                                    if (rezervacija.getId() == Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString())) {
                                        soba.getListaRezervacija().remove(rezervacija);
                                        break;
                                    }
								}
								break;
							}
						}
						tableModel.removeRow(selectedRow);
                        rezervacijaList.remove(selectedRow); // Remove the corresponding object from the list
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
        errorLabel4.setText("");
        errorLabel5.setText("");
        
        boolean problem = false;
        try {
            String startDateString = textFields[0].getText().trim();
            String endDateString = textFields[1].getText().trim();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
            dateFormat.setLenient(false);

            java.util.Date startDate = dateFormat.parse(startDateString);
            java.util.Date endDate = dateFormat.parse(endDateString);

            if (startDate.after(endDate)) {
                errorLabel1.setText("Datum početka mora biti pre datuma kraja.");
                problem = true;
            }
            if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(textFields[0].getText(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) > 0) {
				errorLabel1.setText("Datum početka mora krenuti od današnjeg dana.");
				problem = true;
			}
			if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(textFields[1].getText(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) > 0) {
				errorLabel2.setText("Datum kraja mora krenuti od današnjeg dana.");
				problem = true;
			}
        } catch (ParseException ex) {
            errorLabel1.setText("Datumi moraju biti u formatu (dd.MM.yyyy.).");
            errorLabel2.setText("Datumi moraju biti u formatu (dd.MM.yyyy.).");
            problem = true;
        }

         // Check if the room number exists
         int roomNumber = 0;
         try {
             roomNumber = Integer.parseInt(textFields[2].getText());
         } catch (NumberFormatException ex) {
             errorLabel3.setText("Broj sobe mora biti ceo broj.");
             problem = true;
         }
         Soba soba = null;
         for (Soba sobe : HotelListePodataka.getInstance().getListaSoba()) {
             if (sobe.getBrojSobe() == roomNumber) {
                 soba = sobe;
                 break;
             }
         }
         if (soba == null) {
             errorLabel3.setText("Broj sobe ne postoji.");
             problem = true;
         } else  {
			 if (!soba.getTip().getNazivTipaSobe().equals(tipSobeComboBox.getSelectedItem().toString())) {
				 errorLabel3.setText("Tip sobe ne odgovara broju sobe.");
				 problem = true;
			 }
		 }
         try {
             Integer.parseInt(textFields[2].getText());
         } catch (NumberFormatException ex) {
        	 errorLabel3.setText("Broj sobe mora biti ceo broj.");
        	 problem = true;
         }
        boolean nađen = false;
		for (Gost gost : HotelListePodataka.getInstance().getListaGostiju()) {
			if (gost.getKorisnickoIme().equals(textFields[3].getText())) {
				nađen = true;
				break;
			}
		}
		if (!nađen) {
			errorLabel4.setText("Korisnik sa unetim korisničkim imenom ne postoji.");
			problem = true;
		}
		try {
			Integer.parseInt(textFields[4].getText());
		} catch (NumberFormatException ex) {
			errorLabel5.setText("Broj osoba mora biti ceo broj.");
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
            	} else if (field.toString().equals(textFields[4].getText())) {
                    errorLabel5.setText("Nije dozvoljeno korišćenje simbola '|'.");
            	}
                problem = true;
            }
        }
    	return problem;
    }
    public JComboBox<StatusRezervacije> getStatusComboBox() {
		return this.statusComboBox;
	}

	public void setStatusComboBox(JComboBox<StatusRezervacije> statusComboBox) {
		this.statusComboBox = statusComboBox;
	}
	
	public JList<DodatneUsluge> getDodatneUslugeComboBox() {
		return this.dodatneUslugeComboBox;
	}

	public void setDodatneUslugeComboBox(JList<DodatneUsluge> dodatneUslugeComboBox) {
		this.dodatneUslugeComboBox = dodatneUslugeComboBox;
	}

	public JLabel getErrorLabel1() {
		return errorLabel1;
	}

	public void setErrorLabel1(JLabel errorLabel1) {
		this.errorLabel1 = errorLabel1;
	}

	public JLabel getErrorLabel2() {
		return errorLabel2;
	}

	public void setErrorLabel2(JLabel errorLabel2) {
		this.errorLabel2 = errorLabel2;
	}

	public JLabel getErrorLabel3() {
		return errorLabel3;
	}

	public void setErrorLabel3(JLabel errorLabel3) {
		this.errorLabel3 = errorLabel3;
	}

	public JLabel getErrorLabel4() {
		return errorLabel4;
	}

	public void setErrorLabel4(JLabel errorLabel4) {
		this.errorLabel4 = errorLabel4;
	}

	public JPanel getRezervacijaPanel() {
		return rezervacijaPanel;
	}

	public void setRezervacijaPanel(JPanel rezervacijaPanel) {
		this.rezervacijaPanel = rezervacijaPanel;
	}

	public JButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(JButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public JComboBox<TipSobe> getTipSobeComboBox() {
		return tipSobeComboBox;
	}

	public void setTipSobeComboBox(JComboBox<TipSobe> tipSobeComboBox) {
		this.tipSobeComboBox = tipSobeComboBox;
	}

	public JLabel getErrorLabel5() {
		return errorLabel5;
	}

	public void setErrorLabel5(JLabel errorLabel5) {
		this.errorLabel5 = errorLabel5;
	}

	public StranicaAdministratora getAdmin() {
		return admin;
	}

	public void setAdmin(StranicaAdministratora admin) {
		this.admin = admin;
	}

	public DefaultTableModel getTableModelSobe() {
		return tableModelSobe;
	}

	public void setTableModelSobe(DefaultTableModel tableModelSobe) {
		this.tableModelSobe = tableModelSobe;
	}
}