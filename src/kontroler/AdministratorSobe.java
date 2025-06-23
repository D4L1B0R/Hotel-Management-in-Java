package kontroler;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import entiteti.Osoblje;
import entiteti.Rezervacija;
import entiteti.Soba;
import funkcionalnosti.MenadzerSobe;
import hotel.HotelListePodataka;
import podaci.SpecijalnePogodnosti;
import podaci.StatusSobe;
import podaci.TipSobe;
import prikaz.StranicaAdministratora;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdministratorSobe {
    private JTextField[] textFields;
    private JComboBox<TipSobe> tipSobeComboBox;
    private JComboBox<StatusSobe> statusSobeComboBox;
    private JLabel errorLabel1, errorLabel2, errorLabel3;
    private DefaultTableModel tableModel;
    private List<Soba> sobeList;
    private String[] columnNames;
    private JTable table;
    private JPanel panel;
    private JButton deleteButton;
    private Osoblje administrator;
    private StranicaAdministratora admin;
    private DefaultTableModel tableModelRezervacije;
    private JPanel checkBoxPanel;

    public AdministratorSobe(StranicaAdministratora admin, JPanel checkBoxPanel, DefaultTableModel tableModelRezervacije , Osoblje administrator, JTextField[] textFields, JComboBox<TipSobe> tipSobeComboBox, JComboBox<StatusSobe> statusSobeComboBox, JLabel errorLabel1, JLabel errorLabel2, JLabel errorLabel3, DefaultTableModel tableModel, List<Soba> sobeList, String[] columnNames, JTable table, JPanel panel, JButton deleteButton) {
        this.textFields = textFields;
        this.tipSobeComboBox = tipSobeComboBox;
        this.statusSobeComboBox = statusSobeComboBox;
        this.setErrorLabel1(errorLabel1);
        this.setErrorLabel2(errorLabel2);
        this.errorLabel3 = errorLabel3;
        this.tableModel = tableModel;
        this.setSobeList(sobeList);
        this.columnNames = columnNames;
        this.table = table;
        this.setPanel(panel);
        this.setDeleteButton(deleteButton);
        this.administrator = administrator;
        this.admin = admin;
        this.tableModelRezervacije = tableModelRezervacije;
        this.checkBoxPanel = checkBoxPanel;
        
        this.initializeTableSelectionListener();
        
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                while (tableModel.getRowCount() > 0) {
                    tableModel.removeRow(0);
                }

                for (Soba soba : sobeList) {
                    String rezervacijaIds = soba.getListaRezervacija().stream()
                        .map(rezervacija -> Integer.toString(rezervacija.getId()))
                        .collect(Collectors.joining(","));
                    Object[] rowData = {soba.getBrojSobe(), soba.getTip(), soba.getStatus(), rezervacijaIds};
                    tableModel.addRow(rowData);
                }
            }
        });
    }

    public ActionListener getAddButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate the form data
            	for (int i = 0; i < textFields.length - 1; i++) {
            	    JTextField textField = textFields[i];
            	    if (textField.getText().isEmpty()) {
            	        JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
            	        return;
            	    }
            	}
				for (JComboBox<?> comboBox : new JComboBox[] { tipSobeComboBox, statusSobeComboBox }) {
					if (comboBox.getSelectedItem() == null) {
						JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
                boolean problem = false;
				if (validacija()) {
					problem = true;
				}
				
				errorLabel3.setText("Nije moguće upisati rezervaciju prilikom inicijalizacije sobe!");
				if (HotelListePodataka.getInstance().getListaSoba() != null) {
					for (Soba s : HotelListePodataka.getInstance().getListaSoba()) {
						if (s.getBrojSobe() == Integer.parseInt(textFields[0].getText())) {
							errorLabel1.setText("Broj sobe već postoji!");
							problem = true;
							break;
						}
					}
				}
				if (problem) {
					return;
				}
				errorLabel2.setText("Nije moguće upisati rezervaciju prilikom inicijalizacije sobe!");
                // Add the new row to the table
                Object[] rowData = new Object[columnNames.length];
                rowData[0] = textFields[0].getText();
				rowData[1] = tipSobeComboBox.getSelectedItem();
                rowData[2] = statusSobeComboBox.getSelectedItem();
                rowData[3] = null;
                tableModel.addRow(rowData);
                MenadzerSobe ms = new MenadzerSobe();
                StatusSobe status;
        		if (rowData[2].equals(StatusSobe.SLOBODNA.toString())){
        			status = StatusSobe.SLOBODNA;
        		} else if (rowData[2].equals(StatusSobe.SPREMANJE.toString())){
        			status = StatusSobe.SPREMANJE;
        		} else {
        			status = StatusSobe.ZAUZETA;
        		}
        		ArrayList<SpecijalnePogodnosti> spec = new ArrayList<>();

        		// Iterate over the components of the checkBoxPanel
        		for (Component component : checkBoxPanel.getComponents()) {
        		    if (component instanceof JCheckBox) {
        		        JCheckBox checkBox = (JCheckBox) component;
        		        if (checkBox.isSelected()) {
        		            SpecijalnePogodnosti tip = SpecijalnePogodnosti.valueOf(checkBox.getText().toUpperCase());
        		            spec.add(tip);
        		        }
        		    }
        		}
                ms.dodajSobu(administrator, Integer.parseInt(rowData[0].toString()), rowData[1].toString(), status, spec);
                // Clear the form fields
                for (JTextField textField : textFields) {
                    textField.setText("");
                }
                if (tipSobeComboBox.getItemCount() > 0) {
                	tipSobeComboBox.setSelectedIndex(0);
                }
                if (statusSobeComboBox.getItemCount() > 0) {
                	statusSobeComboBox.setSelectedIndex(0);
                }
                errorLabel1.setText("");
                errorLabel2.setText("");
                errorLabel3.setText("");
            }
        };
    }
    
    public ActionListener getIzmeniButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = table.getSelectedRow();
                if (selectedRowIndex >= 0) {
                    Soba selectedSoba = sobeList.get(selectedRowIndex);

                    for (int i = 0; i < textFields.length - 1; i++) {
                	    JTextField textField = textFields[i];
                	    if (textField.getText().isEmpty()) {
                	        JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
                	        return;
                	    }
                	}
                    
                    for (JComboBox<?> comboBox : new JComboBox[] { tipSobeComboBox, statusSobeComboBox }) {
                        if (comboBox.getSelectedItem() == null) {
                        	JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
							return;
                    	                        }
                    }
                    
                    boolean problem = false;
					if (validacija()) {
						problem = true;
					}
					try {
						Integer.parseInt(textFields[0].getText());
						if (selectedSoba.getBrojSobe() != Integer.parseInt(textFields[0].getText())) {
							if (HotelListePodataka.getInstance().getListaSoba() != null) {
								for (Soba s : HotelListePodataka.getInstance().getListaSoba()) {
									if (s.getBrojSobe() == Integer.parseInt(textFields[0].getText())) {
										errorLabel1.setText("Broj sobe već postoji!");
										problem = true;
										break;
									}
								}
							}
						}
					} catch (NumberFormatException ex) {
						errorLabel1.setText("Unesite broj");
						problem = true;
					}
					boolean nadjen = false;
					if (textFields[1].getText().length() > 0) {
						for (String s : textFields[1].getText().split(",")) {
							if (HotelListePodataka.getInstance().getListaRezervacija() != null) {
								for (Rezervacija r : HotelListePodataka.getInstance().getListaRezervacija()) {
									if (r.getId() == Integer.parseInt(s)) {
										nadjen = true;
									}
								}
							} else {
								errorLabel2.setText("Ne postoje trenutne rezervacije!");
								textFields[1].setText("");
							}
							if (!nadjen) {
				                errorLabel2.setText("Ne postoji rezervacija za taj ID!");
				                problem = true;
				                break;
							}
						}
					}
					if (problem) {
						
						return;
					}
                    // Update the selected Soba object with the new values
                    selectedSoba.setBrojSobe(Integer.parseInt(textFields[0].getText()));
                    selectedSoba.setTip((TipSobe) tipSobeComboBox.getSelectedItem());
                    selectedSoba.setStatus((StatusSobe) statusSobeComboBox.getSelectedItem());
                    ArrayList<Rezervacija> rezervacije = new ArrayList<Rezervacija>();
					for (String s : textFields[1].getText().split(",")) {
						if (s.equals(""))
							continue;
						if (HotelListePodataka.getInstance().getListaRezervacija() != null) {
							for (Rezervacija r : HotelListePodataka.getInstance().getListaRezervacija()) {
								if (r.getId() == Integer.parseInt(s)) {
									rezervacije.add(r);
								}
							}
						} else {
							Rezervacija rez = Rezervacija.fromString(s);
							rezervacije.add(rez);
						}
					}
                    selectedSoba.setListaRezervacija(rezervacije);
                    ArrayList<SpecijalnePogodnosti> spec = new ArrayList<>();
                    
                    for (Component component : checkBoxPanel.getComponents()) {
                        if (component instanceof JCheckBox) {
                            JCheckBox checkBox = (JCheckBox) component;
                            if (checkBox.isSelected()) {
                                SpecijalnePogodnosti tip = SpecijalnePogodnosti.valueOf(checkBox.getText().toUpperCase());
                                spec.add(tip);
                            }
                        }
                    }
                    selectedSoba.setPogodnosti(spec);
                    
                    tableModel.setValueAt(selectedSoba.getBrojSobe(), selectedRowIndex, 0);
                    tableModel.setValueAt(selectedSoba.getTip(), selectedRowIndex, 1);
                    tableModel.setValueAt(selectedSoba.getStatus(), selectedRowIndex, 2);					
					String rezervacijaIds = selectedSoba.getListaRezervacija().stream()
					    .map(rezervacija -> Integer.toString(rezervacija.getId()))
					    .collect(Collectors.joining(","));
					tableModel.setValueAt(rezervacijaIds, selectedRowIndex, 3);
                    admin.refreshRezervacije(tableModelRezervacije, checkBoxPanel);
                    for (JTextField textField : textFields) {
                        textField.setText("");
                    }
                    errorLabel1.setText("");
                    errorLabel2.setText("");
                    errorLabel3.setText("");
                    System.out.println(HotelListePodataka.getInstance().getListaSoba());
                }
            }
        };
    }

    public void initializeTableSelectionListener() {
        // Add a ListSelectionListener to the table
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRowIndex = table.getSelectedRow();
                if (selectedRowIndex >= 0) {
                    Soba selectedSoba = sobeList.get(selectedRowIndex);

                    errorLabel1.setText("");
                    errorLabel2.setText("");
                    errorLabel3.setText("");
                    
                    textFields[0].setText(String.valueOf(selectedSoba.getBrojSobe()));
                    tipSobeComboBox.setSelectedItem(selectedSoba.getTip().getNazivTipaSobe().toString());
                    statusSobeComboBox.setSelectedItem(selectedSoba.getStatus().toString());
                    textFields[1].setText(selectedSoba.getListaRezervacija().stream()
                    	    .map(rezervacija -> Integer.toString(rezervacija.getId()))
                    	    .collect(Collectors.joining(",")));
                    
                    for (Component component : checkBoxPanel.getComponents()) {
                        if (component instanceof JCheckBox) {
                            JCheckBox checkBox = (JCheckBox) component;
                            checkBox.setSelected(false);
                        }
                    }

                    // Check the checkboxes that correspond to the SpecijalnePogodnosti of the selected Soba
                    for (SpecijalnePogodnosti pogodnost : selectedSoba.getPogodnosti()) {
                        for (Component component : checkBoxPanel.getComponents()) {
                            if (component instanceof JCheckBox) {
                                JCheckBox checkBox = (JCheckBox) component;
                                if (checkBox.getText().equalsIgnoreCase(pogodnost.name())) {
                                    checkBox.setSelected(true);
                                }
                            }
                        }
                    }
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
                // Delete the selected row from the table
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                	int brojSobe = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                    tableModel.removeRow(selectedRow);
                    MenadzerSobe ms = new MenadzerSobe();
                    ms.obrisiSobu(brojSobe);
                    // Clear the form fields
					for (String s : textFields[1].getText().split(",")) {
						if (s.equals("")) {
							continue;
						} else {
							if (HotelListePodataka.getInstance().getListaRezervacija() != null) {
								for (Rezervacija r : HotelListePodataka.getInstance().getListaRezervacija()) {
									if (r.getId() == Integer.parseInt(s)) {
										for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
											if (soba.getBrojSobe() == brojSobe) {
												ArrayList<Rezervacija> rezervacije = soba.getListaRezervacija();
												rezervacije.remove(r);
												soba.setListaRezervacija(rezervacije);
											}
										}
									}
								}
							}
						}
					}
                    for (JTextField textField : textFields) {
                        textField.setText("");
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Molimo izaberite red za brisanje.", "Greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
    }
    
    public boolean validacija() {
    	errorLabel1.setText("");
        errorLabel2.setText("");
        errorLabel3.setText("");
        
    	boolean problem = false;
		try {
			Integer.parseInt(textFields[0].getText());
		} catch (NumberFormatException ex) {
			errorLabel1.setText("Unesite broj");
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

	public JLabel getErrorLabel2() {
		return errorLabel2;
	}

	public void setErrorLabel2(JLabel errorLabel2) {
		this.errorLabel2 = errorLabel2;
	}

	public JLabel getErrorLabel1() {
		return errorLabel1;
	}

	public void setErrorLabel1(JLabel errorLabel1) {
		this.errorLabel1 = errorLabel1;
	}

	public List<Soba> getSobeList() {
		return sobeList;
	}

	public void setSobeList(List<Soba> sobeList) {
		this.sobeList = sobeList;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public JButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(JButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	public JComboBox<StatusSobe> getStatusSobeComboBox() {
		return statusSobeComboBox;
	}

	public void setStatusSobeComboBox(JComboBox<StatusSobe> statusSobeComboBox) {
		this.statusSobeComboBox = statusSobeComboBox;
	}

	public JPanel getCheckBoxPanel() {
		return checkBoxPanel;
	}

	public void setCheckBoxPanel(JPanel checkBoxPanel) {
		this.checkBoxPanel = checkBoxPanel;
	}
}