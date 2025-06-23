package prikaz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import entiteti.Gost;
import entiteti.Rezervacija;
import entiteti.Soba;
import funkcionalnosti.GostFunkcija;
import hotel.HotelListePodataka;
import kontroler.GostFunkcionalnosti;
import kontroler.LicnoGost;
import podaci.Cenovnik;
import podaci.DodatneUsluge;
import podaci.SlobodneSobe;
import podaci.SpecijalnePogodnosti;
import podaci.TipSobe;

public class StranicaGosta {
    private JTabbedPane tabbedPane;
    private JPanel gostPanel;
    private JLabel totalCenaLabel = new JLabel();
    private JComboBox<TipSobe> tipSobeComboBox = new JComboBox<>();

    public JPanel getGostPanel() {
        return gostPanel;
    }

    public StranicaGosta(Gost trenutniGost, JFrame currentFrame, JFrame previousFrame) {
        tabbedPane = new JTabbedPane();
        gostPanel = new JPanel();
        gostPanel.add(tabbedPane);
        currentFrame.setTitle("Gost: " + trenutniGost.getIme() + " " + trenutniGost.getPrezime());
        currentFrame.setSize(600, 600); // Set the default size of the JFrame
        currentFrame.setLocationRelativeTo(null); // Center the JFrame on the screen

        // Add a WindowListener to the currentFrame
        currentFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                previousFrame.setVisible(true); // Set the visibility of the prijavaFrame to true when the currentFrame is closed
                if (previousFrame instanceof Prijava) {
                    ((Prijava) previousFrame).resetFormFields();
                }
            }
        });
        // Gost panel
        JPanel rezervacijaPanel = new JPanel(new BorderLayout());
        rezervacijaPanel.setPreferredSize(new Dimension(500, 600));
        String[] columnNames = {"ID", "Broj osoba", "Datum početka", "Datum kraja", "Tip sobe", "Dodatne usluge", "Status", "Trošak"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
	        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
	        public boolean isCellEditable(int row, int column) {
	            // This causes all cells to be not editable
	            return false;
	        }
	    };
        JTable table = new JTable(tableModel);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.fireTableDataChanged();

		double totalCena = 0;
		
		for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
		    if (rezervacija.getGost().getKorisnickoIme().equals(trenutniGost.getKorisnickoIme())) {
		        String dodatneUsluge = rezervacija.getDodatneUsluge().stream()
		                .map(DodatneUsluge::toString)
		                .collect(Collectors.joining(", "));
		        Object[] rowData = {rezervacija.getId(), rezervacija.getBrojOsoba(), rezervacija.getDatumPocetkaRezervacije(), rezervacija.getDatumKrajaRezervacije(), rezervacija.getTipSobe(), dodatneUsluge, rezervacija.getStatus(), rezervacija.getCena()};
		        tableModel.addRow(rowData);
		        totalCena += rezervacija.getCena();
		    }
		}

        JTable rezervacijaTable = new JTable(tableModel);
        JScrollPane scrollPaneRezervacija = new JScrollPane(rezervacijaTable);
        scrollPaneRezervacija.setPreferredSize(new Dimension(480, 200));
        rezervacijaPanel.add(scrollPaneRezervacija, BorderLayout.CENTER);
        
		
        JPanel formAndErrorPanelRezervacija = new JPanel();
        formAndErrorPanelRezervacija.setLayout(new BoxLayout(formAndErrorPanelRezervacija, BoxLayout.PAGE_AXIS));
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalCenaLabel = new JLabel("Ukupna cena: " + totalCena);
        totalCenaLabel.setForeground(java.awt.Color.BLUE);
        labelPanel.add(totalCenaLabel);
        formAndErrorPanelRezervacija.add(labelPanel);
        JPanel formPanelRezervacija = new JPanel(new GridLayout(0, 2));
        JTextField[] textFieldsRezervacija = new JTextField[columnNames.length - 5];

        formAndErrorPanelRezervacija.add(formPanelRezervacija);
        formAndErrorPanelRezervacija.add(Box.createVerticalStrut(10));

        int textFieldIndexRezervacija = 0;
        JLabel errorLabel1Rezervacija = new JLabel();
        JLabel errorLabel2Rezervacija = new JLabel();
        JLabel errorLabel3Rezervacija = new JLabel();
        
        errorLabel1Rezervacija.setForeground(java.awt.Color.RED);
        errorLabel2Rezervacija.setForeground(java.awt.Color.RED);
        errorLabel3Rezervacija.setForeground(java.awt.Color.RED);
        formPanelRezervacija.setLayout(new GridBagLayout());

        GridBagConstraints gbcRezervacija = new GridBagConstraints();
        gbcRezervacija.anchor = GridBagConstraints.WEST;
        gbcRezervacija.fill = GridBagConstraints.HORIZONTAL;
        gbcRezervacija.weightx = 1.0;

        TipSobe[] tipoviSoba;
        if (Cenovnik.getInstance().getCeneTipovaSoba() == null) {
            tipoviSoba = null;
        } else {
            tipoviSoba = Cenovnik.getInstance().getCeneTipovaSoba().keySet().toArray(new TipSobe[0]);
        }
        tipSobeComboBox = new JComboBox<>(tipoviSoba);
        JList<DodatneUsluge> dodatneUslugeList = new JList<>();
        
        for (int i = 0; i < columnNames.length; i++) {
            // Skip the "ID" and "Gost" fields
            if (columnNames[i].equals("ID") || columnNames[i].equals("Trošak") || columnNames[i].equals("Status")) {
                continue;
            }
            columnNames[i] += ":";

            gbcRezervacija.gridx = 0;
            gbcRezervacija.gridy = 2 * i;
            formPanelRezervacija.add(new JLabel(columnNames[i]), gbcRezervacija);

            gbcRezervacija.gridx = 1;
            if (i == 4) {
                formPanelRezervacija.add(tipSobeComboBox, gbcRezervacija);
            } else if (i == 5 && Cenovnik.getInstance().getDodatneUsluge() != null) {
                dodatneUslugeList = new JList<>(Cenovnik.getInstance().getDodatneUsluge().keySet().toArray(new DodatneUsluge[0]));
                dodatneUslugeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                formPanelRezervacija.add(new JScrollPane(dodatneUslugeList), gbcRezervacija);
            } else {
                JTextField textField = new JTextField(10);
                formPanelRezervacija.add(textField, gbcRezervacija);
                textFieldsRezervacija[textFieldIndexRezervacija] = textField;
                textFieldIndexRezervacija++;
            }
            gbcRezervacija.gridy++; // Move to the next row for the strut
            formPanelRezervacija.add(Box.createVerticalStrut(10), gbcRezervacija);

            if (i == 1) formPanelRezervacija.add(errorLabel1Rezervacija, gbcRezervacija);
            if (i == 2) formPanelRezervacija.add(errorLabel2Rezervacija, gbcRezervacija);
            if (i == 3) formPanelRezervacija.add(errorLabel3Rezervacija, gbcRezervacija);
        }
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        for (SpecijalnePogodnosti tip : SpecijalnePogodnosti.values()) {
            JCheckBox checkBox = new JCheckBox(tip.toString());
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
            		updateTipSobeDropdown(checkBoxPanel, textFieldsRezervacija);
                }
            });
            checkBoxPanel.add(checkBox);
        }
        
        JPanel bottomPanelRezervacija = new JPanel(new BorderLayout());
        bottomPanelRezervacija.add(checkBoxPanel, BorderLayout.NORTH);
        bottomPanelRezervacija.add(formAndErrorPanelRezervacija, BorderLayout.CENTER);

        rezervacijaPanel.add(bottomPanelRezervacija, BorderLayout.PAGE_END);
        
        rezervacijaPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        GostFunkcija gost = new GostFunkcija();
        GostFunkcionalnosti gf = new GostFunkcionalnosti(trenutniGost, this, totalCenaLabel, gost, table, tableModel, textFieldsRezervacija, errorLabel1Rezervacija, errorLabel2Rezervacija, errorLabel3Rezervacija, tipSobeComboBox, dodatneUslugeList, currentFrame, previousFrame);
		
		JButton pregledButton = new JButton("Pregled");
		pregledButton.addActionListener(gf.getPregledListener());
		
		JButton rezervacijaButton = new JButton("Rezerviši");
		rezervacijaButton.addActionListener(gf.getRezervacijaListener());
		
		JButton otkazivanjeButton = new JButton("Otkaži");
		otkazivanjeButton.addActionListener(gf.getOtkazivanjeListener());
		
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(pregledButton);
        buttonPanel.add(rezervacijaButton);
        buttonPanel.add(otkazivanjeButton);

        formAndErrorPanelRezervacija.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Rezervacije", rezervacijaPanel);
        
        // Sopstveni podaci panel
        JPanel gostPanel2 = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(gostPanel2);

        JPanel formAndErrorPanel2 = new JPanel();
        formAndErrorPanel2.setLayout(new BoxLayout(formAndErrorPanel2, BoxLayout.PAGE_AXIS));

        String[] columnNames2 = {"Ime", "Prezime", "Pol", "Datum rodjenja", "Broj telefona", "Adresa"};
        JPanel formPanel2 = new JPanel(new GridLayout(0, 2));
        JTextField[] textFields2 = new JTextField[columnNames2.length - 1];
        JComboBox<podaci.Pol> polComboBox2 = new JComboBox<>(podaci.Pol.values());

        formAndErrorPanel2.add(formPanel2);
        formAndErrorPanel2.add(Box.createVerticalStrut(10));

        JLabel uspesno = new JLabel();
        uspesno.setForeground(java.awt.Color.GREEN);

        int textFieldIndex2 = 0;
        JLabel errorLabeli1 = new JLabel();
        JLabel errorLabeli2 = new JLabel();

        formPanel2.setLayout(new GridBagLayout());
        formAndErrorPanel2.add(uspesno);
        formAndErrorPanel2.add(Box.createVerticalStrut(20));
        formAndErrorPanel2.add(formPanel2);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.anchor = GridBagConstraints.WEST;
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.weightx = 1.0;
        gbc2.weighty = 1.0;

        for (int i = 0; i < columnNames2.length; i++) {
            columnNames2[i] += ":";

            gbc2.gridx = 0;
            gbc2.gridy = 2 * i;
            formPanel2.add(new JLabel(columnNames2[i]), gbc2);

            if (i == 2) {
                gbc2.gridx = 1;
                formPanel2.add(polComboBox2, gbc2);
            } else {
                textFields2[textFieldIndex2] = new JTextField(10);
                gbc2.gridx = 1;
                formPanel2.add(textFields2[textFieldIndex2], gbc2);
                textFieldIndex2++;
            }
            gbc2.gridy++;
            formPanel2.add(Box.createVerticalStrut(10), gbc2);
            if (i == 4) formPanel2.add(errorLabeli1, gbc2);
            if (i == 3) formPanel2.add(errorLabeli2, gbc2);
        }

        JPanel bottomPanel2 = new JPanel(new BorderLayout());
        bottomPanel2.add(formAndErrorPanel2, BorderLayout.NORTH);
        gostPanel2.add(bottomPanel2, BorderLayout.PAGE_END);

        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton izmeniButton2 = new JButton("Izmeni");
        buttonPanel2.add(izmeniButton2);
        formAndErrorPanel2.add(buttonPanel2);

        LicnoGost lg = new LicnoGost(trenutniGost, textFields2, errorLabeli1, errorLabeli2, uspesno, columnNames2, gostPanel2);
        izmeniButton2.addActionListener(lg.getIzmeniButtonListener());

        tabbedPane.addTab("Lično", centerPanel);
    }
    
    public void refresh(DefaultTableModel tableModel) {
    	double totalCena = 0;

    	for (int i = 0; i < tableModel.getRowCount(); i++) {
    	    double cena = (double) tableModel.getValueAt(i, 7);
    	    totalCena += cena;
    	}
    	totalCenaLabel.setText("Ukupna cena: " + totalCena);
    }
    
    // Method to update the TipSobe dropdown based on the selected SpecijalnePogodnosti checkboxes
    private void updateTipSobeDropdown(JPanel checkBoxPanel, JTextField[] textFieldsRezervacija) {
        ArrayList<SpecijalnePogodnosti> selectedTips = new ArrayList<>();
        for (Component component : checkBoxPanel.getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                if (checkBox.isSelected()) {
                    selectedTips.add(SpecijalnePogodnosti.valueOf(checkBox.getText()));
                }
            }
        }
        ArrayList<TipSobe> filteredTipSobe = new ArrayList<>();
		if (selectedTips.isEmpty()) {
			tipSobeComboBox.setModel(new DefaultComboBoxModel<>(
			Cenovnik.getInstance().getCeneTipovaSoba().keySet().toArray(new TipSobe[0])));
			return;
		}
        if (tipSobeComboBox != null) {
			for (Soba soba : SlobodneSobe.getInstance().getSlobodneSobe().keySet()) {
				if (soba.getPogodnosti().containsAll(Arrays.asList(selectedTips.toArray(new SpecijalnePogodnosti[0])))) {
					filteredTipSobe.add(soba.getTip());
				}
			}
        }
        tipSobeComboBox.setModel(new DefaultComboBoxModel<>(filteredTipSobe.toArray(new TipSobe[0])));
    }
    
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
 }