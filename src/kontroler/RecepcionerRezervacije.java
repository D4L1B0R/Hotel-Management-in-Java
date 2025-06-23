package kontroler;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import entiteti.Gost;
import entiteti.Osoblje;
import entiteti.Rezervacija;
import entiteti.Soba;
import hotel.HotelListePodataka;
import podaci.Cenovnik;
import podaci.DodatneUsluge;
import prikaz.StranicaRecepcionera;

public class RecepcionerRezervacije {
    private JButton potvrdaRezervacijeButton;
    private JButton azuriranjeRezervacijaButton;
    private JButton filterButton;
    private JButton sortMax;
    private JButton sortMin;
    private JPanel rezervacijaPanel;
    private DefaultTableModel tableModelRezervacije;
    private JTable rezervacijaTable;
    private funkcionalnosti.RecepcionerRezervacije recepcionerRezervacije;
    private JLabel potvrdaLabel;
    private List<Rezervacija> rezervacijaList;
    private StranicaRecepcionera stranicaRecepcionera;
    private DefaultTableModel tableModelSobe;
    private JTextField filterTextField;
    private JRadioButton tipSobeRadioButton;
    private JRadioButton brojSobeRadioButton;
    private JRadioButton cenaRadioButton;
    private JRadioButton dodatneUslugeRadioButton;
    private JFrame currentFrame;

    public RecepcionerRezervacije(JButton sortMax, JButton sortMin, JFrame currentFrame, JButton dodatneUslugeButton, JRadioButton tipSobeRadioButton, JRadioButton brojSobeRadioButton, JRadioButton cenaRadioButton, JRadioButton dodatneUslugeRadioButton, JTextField filterTextField, JButton filterButton, JComboBox<Integer> rezervacijaComboBox, StranicaRecepcionera recepcionerko, DefaultTableModel tableModelSobe, List<Rezervacija> rezervacijaList, JLabel potvrdaLabel, Osoblje recepcioner, JButton potvrdaRezervacijeButton, JButton azuriranjeRezervacijaButton, DefaultTableModel tableModelRezervacije, JTable rezervacijaTable, JPanel rezervacijaPanel) {
        this.setPotvrdaRezervacijeButton(potvrdaRezervacijeButton);
        this.setAzuriranjeRezervacijaButton(azuriranjeRezervacijaButton);
        funkcionalnosti.RecepcionerRezervacije recepcionerRezervacije = new funkcionalnosti.RecepcionerRezervacije();
        this.setRecepcionerRezervacije(recepcionerRezervacije);
        this.setTableModelRezervacije(tableModelRezervacije);
        this.setRezervacijaTable(rezervacijaTable);
        this.setRezervacijaPanel(rezervacijaPanel);
        this.setPotvrdaLabel(potvrdaLabel);
        this.setRezervacijaList(rezervacijaList);
        this.setStranicaRecepcionera(recepcionerko);
        this.setTableModelSobe(tableModelSobe);
        this.setFilterButton(filterButton);
        this.filterTextField = filterTextField;
        this.tipSobeRadioButton = tipSobeRadioButton;
        this.brojSobeRadioButton = brojSobeRadioButton;
        this.cenaRadioButton = cenaRadioButton;
        this.dodatneUslugeRadioButton = dodatneUslugeRadioButton;
        this.currentFrame = currentFrame;
        this.setSortMax(sortMax);
        this.setSortMin(sortMin);
        
        rezervacijaPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // Clear the existing data
                while (tableModelRezervacije.getRowCount() > 0) {
                	tableModelRezervacije.removeRow(0);
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
                    tableModelRezervacije.addRow(rowData.toArray());
                }
            }
        });
    }

    public ActionListener getPotvrdaRezervacijeButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	potvrdaLabel.setText("");
                int selectedRow = rezervacijaTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = Integer.parseInt(rezervacijaTable.getValueAt(selectedRow, 0).toString());
                    recepcionerRezervacije.potvrdaRezervacije(id);
                    potvrdaLabel.setText("Rezervacija je potvrdjena.");
                    stranicaRecepcionera.refreshSobe(tableModelSobe);
                    stranicaRecepcionera.refreshRezervacije(tableModelRezervacije);
                }
            }
        };
    }

    public ActionListener getAzuriranjeRezervacijaButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	potvrdaLabel.setText("");
                recepcionerRezervacije.azuriranjeRezervacija();
                potvrdaLabel.setText("Ažuriranje rezervacije je uspešno.");
                stranicaRecepcionera.refreshSobe(tableModelSobe);
                stranicaRecepcionera.refreshRezervacije(tableModelRezervacije);
            }
        };
    }
    
    public ActionListener getRefreshButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stranicaRecepcionera.refreshRezervacije(tableModelRezervacije);
            }
        };
    }
    public ActionListener getDodatneUslugeButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = rezervacijaTable.getSelectedRow();
                if (selectedRow != -1) { // If a row is selected
                    Rezervacija selectedRezervacija = rezervacijaList.get(selectedRow);

                    // Create a new JDialog that contains a JList of all DodatneUsluge
                    JDialog dodatneUslugeDialog = new JDialog(currentFrame, "Dodatne usluge", true);
                    DefaultListModel<DodatneUsluge> listModel = new DefaultListModel<>();
                    for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) { // Replace with your method to get all DodatneUsluge
                        listModel.addElement(usluga);
                    }
                    JList<DodatneUsluge> uslugeList = new JList<>(listModel);
                    dodatneUslugeDialog.add(new JScrollPane(uslugeList), BorderLayout.CENTER);

                    // Add a button to the JDialog
                    JPanel buttonPanel = new JPanel();
                    JButton updateButton = new JButton("Ažuriraj usluge");
                    buttonPanel.add(updateButton);
                    dodatneUslugeDialog.add(buttonPanel, BorderLayout.SOUTH);

                    // Add ActionListener to the button
                    updateButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Update the selected Rezervacija with the new DodatneUsluge
                            ArrayList<DodatneUsluge> selectedUsluge = (ArrayList<DodatneUsluge>) uslugeList.getSelectedValuesList();
                            selectedRezervacija.setDodatneUsluge(selectedUsluge); // Replace with your method to update the DodatneUsluge of a Rezervacija
                            dodatneUslugeDialog.dispose();
                        }
                    });

                    dodatneUslugeDialog.pack();
                    dodatneUslugeDialog.setLocationRelativeTo(currentFrame);
                    dodatneUslugeDialog.setVisible(true);
                    recepcionerRezervacije.azuriranjeRezervacija();
                }
            }
        };
    }
    public ActionListener getFilterButtonListener() {
    	return new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	    	if (brojSobeRadioButton.isSelected()) {
	            	try {
    					Integer.parseInt(filterTextField.getText());
    				} catch (NumberFormatException ex) {
    					JOptionPane.showMessageDialog(null, "Unesite broj za filtriranje po broju sobe!");
    					return;
    				}
	                String brojSobeFilter = filterTextField.getText();
	
	                while (tableModelRezervacije.getRowCount() > 0) {
	                    tableModelRezervacije.removeRow(0);
	                }
	
	                for (Rezervacija rezervacija : rezervacijaList) {
	                    Soba soba = (Soba) rezervacija.getSoba();
	                    if (soba != null && (brojSobeFilter.isEmpty() || String.valueOf(soba.getBrojSobe()).contains(brojSobeFilter))) {
	                        List<Object> rowData = new ArrayList<>();
	                        podaci(rowData, rezervacija);
	                        tableModelRezervacije.addRow(rowData.toArray());
	                    }
	                }
    	    	} else if (tipSobeRadioButton.isSelected()) {
	                String tipSobeFilter = filterTextField.getText();

	                while (tableModelRezervacije.getRowCount() > 0) {
	                    tableModelRezervacije.removeRow(0);
	                }

	                for (Rezervacija rezervacija : rezervacijaList) {
	                    if (rezervacija.getTipSobe().getNazivTipaSobe().contains(tipSobeFilter)) {
	                        List<Object> rowData = new ArrayList<>();
	                        podaci(rowData, rezervacija);
	                        tableModelRezervacije.addRow(rowData.toArray());
	                    }
	                }
    	        } else if (dodatneUslugeRadioButton.isSelected()) {
	                String dodatneUslugeFilter = filterTextField.getText();
	                
	                while (tableModelRezervacije.getRowCount() > 0) {
	                    tableModelRezervacije.removeRow(0);
	                }

	                for (Rezervacija rezervacija : rezervacijaList) {
	                	for (DodatneUsluge dodatneUsluge : rezervacija.getDodatneUsluge()) {
    	                    if (dodatneUsluge.getDodatneUsluge().contains(dodatneUslugeFilter)) {
    	                        List<Object> rowData = new ArrayList<>();
    	                        podaci(rowData, rezervacija);
    	                        tableModelRezervacije.addRow(rowData.toArray());
    	                    }
	                	}
	                }
    	        } else if (cenaRadioButton.isSelected()) {
    	        	// Create a new JDialog
    	        	JDialog dialog = new JDialog();
    	        	dialog.setTitle("Unesite interval cena: ");
    	        	dialog.setModal(true);

    	        	JPanel pricePanel = new JPanel();
    	        	GridBagLayout layout = new GridBagLayout();
    	        	pricePanel.setLayout(layout);
    	        	GridBagConstraints gbc = new GridBagConstraints();

    	        	// Create the text fields and labels
    	        	JTextField minPriceField = new JTextField(5);
    	        	JTextField maxPriceField = new JTextField(5);
    	        	JLabel errorMin = new JLabel();
    	        	JLabel errorMax = new JLabel();

    	        	errorMin.setForeground(java.awt.Color.red);
    	        	errorMax.setForeground(java.awt.Color.red);

    	        	// Create the button
    	        	JButton submitButton = new JButton("Pretraga");
    	        	submitButton.addActionListener(new ActionListener() {
    	        	    @Override
    	        	    public void actionPerformed(ActionEvent e) {
    	        	    	double minPrice = 0;
    	        	    	double maxPrice = 0;
    	        	    	boolean problem = false;
    	        	        try {
    	        	            minPrice = Double.parseDouble(minPriceField.getText());
    	        	        } catch (NumberFormatException ex) {
								errorMin.setText("Unesite broj!");
								problem = true;
							}
    	        	        try {
    	        	            maxPrice = Double.parseDouble(maxPriceField.getText());
    	        	        } catch (NumberFormatException ex) {
								errorMax.setText("Unesite broj!");
								problem = true;
							}
    	        	        if (problem) {
	                            return;
    	        	        }
    	        	        
	        	            while (tableModelRezervacije.getRowCount() > 0) {
	        	                tableModelRezervacije.removeRow(0);
	        	            }

	        	            // Filter the table
	        	            for (Rezervacija rezervacija : rezervacijaList) {
	        	                if (rezervacija.getCena() >= minPrice && rezervacija.getCena() <= maxPrice) {
	        	                    List<Object> rowData = new ArrayList<>();
	        	                    podaci(rowData, rezervacija);
	        	                    tableModelRezervacije.addRow(rowData.toArray());
	        	                }
	        	            }
	        	            
	        	            errorMin.setText("");
	        	            errorMax.setText("");
	        	            dialog.dispose();
    	        	    }
    	        	});

    	        	// Add the components to the panel
    	        	gbc.gridx = 0;
    	        	gbc.gridy = 0;
    	        	pricePanel.add(new JLabel("Min:"), gbc);

    	        	gbc.gridx = 1;
    	        	pricePanel.add(minPriceField, gbc);

    	        	gbc.gridx = 2;
    	        	pricePanel.add(errorMin, gbc);

    	        	gbc.gridx = 0;
    	        	gbc.gridy = 1;
    	        	pricePanel.add(new JLabel("Max:"), gbc);

    	        	gbc.gridx = 1;
    	        	pricePanel.add(maxPriceField, gbc);

    	        	gbc.gridx = 2;
    	        	pricePanel.add(errorMax, gbc);

    	        	gbc.gridx = 1;
    	        	gbc.gridy = 2;
    	        	pricePanel.add(submitButton, gbc);

    	        	// Add the panel to the dialog
    	        	dialog.add(pricePanel);
    	        	dialog.pack();
    	        	dialog.setLocationRelativeTo(null);
    	        	dialog.setVisible(true);
    	        } else {
	                JOptionPane.showMessageDialog(null, "Odaberite jedno od ponuđenih polja za filtriranje!");
	                stranicaRecepcionera.refreshRezervacije(tableModelRezervacije);
    	        }
    	    }
    	};
    }
    
    public ActionListener getSortingMax() {
    	return new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	    	ArrayList<Rezervacija> lista = HotelListePodataka.getInstance().getListaRezervacija();
    	    	for (int i = 0; i < lista.size(); i++) {
    	    		for (int j = 0; j < lista.size(); j++) {
    	    			if (lista.get(i).getCena() > lista.get(j).getCena()) {
    	    				Rezervacija temp = lista.get(j);
    	    				lista.set(j, lista.get(i));
    	    				lista.set(i, temp);
    	    			}
    	    		}
    	    	}
    	    	for (Rezervacija rez : lista) {
    	    		List<Object> rowData = new ArrayList<>();
                    podaci(rowData, rez);
                    tableModelRezervacije.addRow(rowData.toArray());
    	    	}
    	    	stranicaRecepcionera.refreshRezervacije(tableModelRezervacije);
    	    }
    	};
    }
    
    public ActionListener getSortingMin() {
    	return new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	    	ArrayList<Rezervacija> lista = HotelListePodataka.getInstance().getListaRezervacija();
    	    	for (int i = 0; i < lista.size(); i++) {
    	    		for (int j = 0; j < lista.size(); j++) {
    	    			if (lista.get(i).getCena() < lista.get(j).getCena()) {
    	    				Rezervacija temp = lista.get(j);
    	    				lista.set(j, lista.get(i));
    	    				lista.set(i, temp);
    	    			}
    	    		}
    	    	}
    	    	for (Rezervacija rez : lista) {
    	    		List<Object> rowData = new ArrayList<>();
                    podaci(rowData, rez);
                    tableModelRezervacije.addRow(rowData.toArray());
    	    	}
    	    	stranicaRecepcionera.refreshRezervacije(tableModelRezervacije);
    	    }
    	};
    }
    
	public List<Object> podaci(List<Object> rowData, Rezervacija rezervacija) {
		rowData.add(rezervacija.getId());
        rowData.add(rezervacija.getStatus());
        rowData.add(rezervacija.getDatumPocetkaRezervacije());
        rowData.add(rezervacija.getDatumKrajaRezervacije());
        if (rezervacija.getSoba() != null) {
        	rowData.add(rezervacija.getSoba().getBrojSobe());
        } else {
        	rowData.add("");
        }
        rowData.add(rezervacija.getGost().getKorisnickoIme());
        rowData.add(rezervacija.getBrojOsoba());
        rowData.add(rezervacija.getCena());
        if (rezervacija.getDodatneUsluge() != null) {
        	 String dodatneUsluge = rezervacija.getDodatneUsluge().stream()
		        .map(DodatneUsluge::toString)
		        .collect(Collectors.joining(","));
		    rowData.add(dodatneUsluge);
		} else {
			rowData.add("");
		}
        rowData.add(rezervacija.getTipSobe());
        return rowData;
    	
    }
	public JButton getPotvrdaRezervacijeButton() {
		return potvrdaRezervacijeButton;
	}

	public void setPotvrdaRezervacijeButton(JButton potvrdaRezervacijeButton) {
		this.potvrdaRezervacijeButton = potvrdaRezervacijeButton;
	}

	public JButton getAzuriranjeRezervacijaButton() {
		return azuriranjeRezervacijaButton;
	}

	public void setAzuriranjeRezervacijaButton(JButton azuriranjeRezervacijaButton) {
		this.azuriranjeRezervacijaButton = azuriranjeRezervacijaButton;
	}

	public funkcionalnosti.RecepcionerRezervacije getRecepcionerRezervacije() {
		return recepcionerRezervacije;
	}

	public void setRecepcionerRezervacije(funkcionalnosti.RecepcionerRezervacije recepcionerRezervacije) {
		this.recepcionerRezervacije = recepcionerRezervacije;
	}

	public JPanel getRezervacijaPanel() {
		return rezervacijaPanel;
	}

	public void setRezervacijaPanel(JPanel rezervacijaPanel) {
		this.rezervacijaPanel = rezervacijaPanel;
	}

	public DefaultTableModel getTableModelRezervacije() {
		return tableModelRezervacije;
	}

	public void setTableModelRezervacije(DefaultTableModel tableModelRezervacije) {
		this.tableModelRezervacije = tableModelRezervacije;
	}

	public JTable getRezervacijaTable() {
		return rezervacijaTable;
	}

	public void setRezervacijaTable(JTable rezervacijaTable) {
		this.rezervacijaTable = rezervacijaTable;
	}
	
	public JLabel getPotvrdaLabel() {
		return potvrdaLabel;
	}
	
	public void setPotvrdaLabel(JLabel potvrdaLabel) {
		this.potvrdaLabel = potvrdaLabel;
	}

	public List<Rezervacija> getRezervacijaList() {
		return rezervacijaList;
	}

	public void setRezervacijaList(List<Rezervacija> rezervacijaList) {
		this.rezervacijaList = rezervacijaList;
	}

	public StranicaRecepcionera getStranicaRecepcionera() {
		return stranicaRecepcionera;
	}

	public void setStranicaRecepcionera(StranicaRecepcionera stranicaRecepcionera) {
		this.stranicaRecepcionera = stranicaRecepcionera;
	}

	public DefaultTableModel getTableModelSobe() {
		return tableModelSobe;
	}

	public void setTableModelSobe(DefaultTableModel tableModelSobe) {
		this.tableModelSobe = tableModelSobe;
	}

	public JButton getFilterButton() {
		return filterButton;
	}

	public void setFilterButton(JButton filterButton) {
		this.filterButton = filterButton;
	}

	public JButton getSortMax() {
		return sortMax;
	}

	public void setSortMax(JButton sortMax) {
		this.sortMax = sortMax;
	}

	public JButton getSortMin() {
		return sortMin;
	}

	public void setSortMin(JButton sortMin) {
		this.sortMin = sortMin;
	}
}