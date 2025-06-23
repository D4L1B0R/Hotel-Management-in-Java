package kontroler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import entiteti.Osoblje;
import entiteti.Rezervacija;
import entiteti.Soba;
import hotel.HotelListePodataka;
import prikaz.StranicaRecepcionera;
import java.util.List;

public class RecepcionerSobe {
    private Osoblje recepcioner;
    private DefaultTableModel tableModelSobe;
    private List<Soba> sobeList;
    private JTable sobeTable;
    private JPanel sobePanel;
    private JButton popuniButtonSobe;
    private JButton oslobodiButtonSobe;
    private JComboBox<Integer> rezervacijaComboBox;
    private JLabel potvrda;
    private DefaultTableModel tableModelRezervacija;
    private StranicaRecepcionera stranicaRecepcionera;
    private JTextField text;
    private JComboBox<String> rez;

    public RecepcionerSobe(JComboBox<String> rez, JTextField text, StranicaRecepcionera recepcionerko, DefaultTableModel tableModelRezervacija, JLabel potvrda, JComboBox<Integer> sobeRez, Osoblje recepcioner, DefaultTableModel tableModelSobe, List<Soba> sobeList, JTable sobeTable, JPanel sobePanel, JButton popuniButtonSobe, JButton oslobodiButtonSobe) {
        this.setRecepcioner(recepcioner);
        this.setTableModelSobe(tableModelSobe);
        this.setSobeList(sobeList);
        this.setSobeTable(sobeTable);
        this.setSobePanel(sobePanel);
        this.setPopuniButtonSobe(popuniButtonSobe);
        this.setOslobodiButtonSobe(oslobodiButtonSobe);
        this.setRezervacijaComboBox(new JComboBox<Integer>());
        this.setPotvrda(potvrda);
        this.setTableModelRezervacija(tableModelRezervacija);
        this.setStranicaRecepcionera(recepcionerko);
        this.setText(text);
        this.setRez(rez);
        
        this.initializeTableSelectionListener();
    }
    

	private void initializeTableSelectionListener() {
	    sobeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	        @Override
	        public void valueChanged(ListSelectionEvent e) {
	            int selectedRowIndex = sobeTable.getSelectedRow();
	            if (selectedRowIndex >= 0) {
	                Soba selectedSoba = sobeList.get(selectedRowIndex);
	                for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
	                    if (rezervacija.getSoba() != null) {
	                        if (selectedSoba.getBrojSobe() == rezervacija.getSoba().getBrojSobe()) {
	                            text.setText(Integer.toString(rezervacija.getId()));
	                        }
	                    }
	                }
	                rez.removeAllItems();
	        	    rez.addItem("ID--gost--početak i kraj rezervacije--cena");
	                for (Rezervacija rezervacija : selectedSoba.getListaRezervacija()) {
                        rez.addItem(rezervacija.getId() + "-" + rezervacija.getGost().getIme() + " " + rezervacija.getGost().getPrezime() + "-" + rezervacija.getDatumPocetkaRezervacije() + " " + rezervacija.getDatumKrajaRezervacije() + "-" + rezervacija.getCena());
	                }
	            }
	        }
	    });
	}

	public ActionListener getPopuniButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	potvrda.setText("");
                int selectedRow = sobeTable.getSelectedRow();
                if (selectedRow != -1) {
                    int brojSobe = Integer.parseInt(sobeTable.getValueAt(selectedRow, 0).toString());
                    String selectedRezervacija = text.getText();
                    boolean nadjen = false;
                    try {
                    	Integer.parseInt(selectedRezervacija);
                    } catch (NumberFormatException ex) {
                    	JOptionPane.showMessageDialog(null, "ID rezervacije mora biti broj.");
                    }
                    for (Rezervacija rez : HotelListePodataka.getInstance().getListaRezervacija()) {
                    	if (rez.getId() == Integer.parseInt(selectedRezervacija)) {
                    		nadjen = true;
                    	}
                    }
                    for (Soba sobe : HotelListePodataka.getInstance().getListaSoba()) {
						if (sobe.getBrojSobe() == brojSobe) {
							for (Rezervacija rez : HotelListePodataka.getInstance().getListaRezervacija()) {
		                    	if (rez.getId() == Integer.parseInt(selectedRezervacija)) {
		                    		if (sobe.getListaRezervacija().contains(rez)) {
										JOptionPane.showMessageDialog(null, "Soba je već popunjena.");
										return;
									}
		                    	}
		                    }
						}
                	}
                    if (!nadjen) {
                    	JOptionPane.showMessageDialog(null, "Nismo uspeli da pronađemo željenu rezervaciju.");
                    }
					boolean found = false;
					for (Soba soba : sobeList) {
						if (soba.getBrojSobe() == brojSobe) {
                           for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
								if (rezervacija.getId() == Integer.parseInt(selectedRezervacija)) {
									if (soba.getTip().getNazivTipaSobe().equals(rezervacija.getTipSobe().getNazivTipaSobe())) {
										found = true;
										break;
									}
								}
							}
                        }
                    }
					if (!found) {
						potvrda.setText("Izabrana rezervacija nije validna za izabranu sobu!");
                        potvrda.setForeground(java.awt.Color.RED);
                        return;
					}
                    // poziv funkcionalnosti za popunjavanje sobe i osvezavanje
                    funkcionalnosti.RecepcionerSobe recepcionerko = new funkcionalnosti.RecepcionerSobe();
                    recepcionerko.popunjavanjeSobe(Integer.parseInt(selectedRezervacija), brojSobe);
                    potvrda.setText("Soba je uspešno popunjena!");
                    stranicaRecepcionera.refreshRezervacije(tableModelRezervacija);
                    stranicaRecepcionera.refreshSobe(tableModelSobe);
                }
            }
        };
    }

    public ActionListener getOslobodiButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	potvrda.setText("");
                int selectedRow = sobeTable.getSelectedRow();
                if (selectedRow != -1) {
                    int brojSobe = Integer.parseInt(sobeTable.getValueAt(selectedRow, 0).toString());
                    String selectedRezervacija = text.getText();
                    boolean nadjen = false;
                    try {
                    	Integer.parseInt(selectedRezervacija);
                    } catch (NumberFormatException ex) {
                    	JOptionPane.showMessageDialog(null, "ID rezervacije mora biti broj.");
                    }
                    for (Rezervacija rez : HotelListePodataka.getInstance().getListaRezervacija()) {
                    	if (rez.getId() == Integer.parseInt(selectedRezervacija)) {
                    		nadjen = true;
                    	}
                    }
                    if (!nadjen) {
                    	JOptionPane.showMessageDialog(null, "Nismo uspeli da pronađemo željenu rezervaciju.");
                    }
                    funkcionalnosti.RecepcionerSobe recepcionerko = new funkcionalnosti.RecepcionerSobe();
                    recepcionerko.oslobadjanjeSobe(Integer.parseInt(selectedRezervacija), brojSobe);
                    potvrda.setText("Soba je uspešno oslobodjena!");
                    stranicaRecepcionera.refreshRezervacije(tableModelRezervacija);
                    stranicaRecepcionera.refreshSobe(tableModelSobe);
                }
            }
        };
    }

	public Osoblje getRecepcioner() {
		return recepcioner;
	}

	public void setRecepcioner(Osoblje recepcioner) {
		this.recepcioner = recepcioner;
	}

	public DefaultTableModel getTableModelSobe() {
		return tableModelSobe;
	}

	public void setTableModelSobe(DefaultTableModel tableModelSobe) {
		this.tableModelSobe = tableModelSobe;
	}

	public List<Soba> getSobeList() {
		return sobeList;
	}

	public void setSobeList(List<Soba> sobeList) {
		this.sobeList = sobeList;
	}

	public JPanel getSobePanel() {
		return sobePanel;
	}

	public void setSobePanel(JPanel sobePanel) {
		this.sobePanel = sobePanel;
	}

	public JTable getSobeTable() {
		return sobeTable;
	}

	public void setSobeTable(JTable sobeTable) {
		this.sobeTable = sobeTable;
	}

	public JButton getPopuniButtonSobe() {
		return popuniButtonSobe;
	}

	public void setPopuniButtonSobe(JButton popuniButtonSobe) {
		this.popuniButtonSobe = popuniButtonSobe;
	}

	public JButton getOslobodiButtonSobe() {
		return oslobodiButtonSobe;
	}

	public void setOslobodiButtonSobe(JButton oslobodiButtonSobe) {
		this.oslobodiButtonSobe = oslobodiButtonSobe;
	}
	
	public JComboBox<Integer> getRezervacijaComboBox() {
		return rezervacijaComboBox;
	}
	
	public void setRezervacijaComboBox(JComboBox<Integer> rezervacijaComboBox) {
		this.rezervacijaComboBox = rezervacijaComboBox;
	}

	public JLabel getPotvrda() {
		return potvrda;
	}

	public void setPotvrda(JLabel potvrda) {
		this.potvrda = potvrda;
	}

	public DefaultTableModel getTableModelRezervacija() {
		return tableModelRezervacija;
	}

	public void setTableModelRezervacija(DefaultTableModel tableModelRezervacija) {
		this.tableModelRezervacija = tableModelRezervacija;
	}

	public StranicaRecepcionera getStranicaRecepcionera() {
		return stranicaRecepcionera;
	}

	public void setStranicaRecepcionera(StranicaRecepcionera stranicaRecepcionera) {
		this.stranicaRecepcionera = stranicaRecepcionera;
	}

	public JTextField getText() {
		return text;
	}

	public void setText(JTextField text) {
		this.text = text;
	}

	public JComboBox<String> getRez() {
		return rez;
	}

	public void setRez(JComboBox<String> rez) {
		this.rez = rez;
	}
}