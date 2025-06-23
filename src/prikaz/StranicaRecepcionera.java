package prikaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import entiteti.Gost;
import entiteti.Osoblje;
import entiteti.Rezervacija;
import entiteti.Soba;
import hotel.HotelListePodataka;
import kontroler.AdministratorGosti;
import kontroler.LicnoOsoblje;
import kontroler.RecepcionerRezervacije;
import kontroler.RecepcionerSobe;
import podaci.DodatneUsluge;
import podaci.OdlasciDolasci;

public class StranicaRecepcionera {
    private JTabbedPane tabbedPane;
    private JPanel recepcionerPanel;
    private DefaultTableModel tableModelRezervacija = new DefaultTableModel();
    private DefaultTableModel tableModelSobe = new DefaultTableModel();
    private JComboBox<Integer> rezervacijaComboBox = new JComboBox<>();

    public JPanel getRecepcionerPanel() {
        return recepcionerPanel;
    }

    public StranicaRecepcionera(Osoblje recepcioner, JFrame currentFrame, JFrame previousFrame) {
        tabbedPane = new JTabbedPane();
        recepcionerPanel = new JPanel();
        recepcionerPanel.add(tabbedPane);
        currentFrame.setTitle("Recepcioner: " + recepcioner.getIme() + " " + recepcioner.getPrezime());
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

        // Gosti panel
        JPanel gostiPanel = new JPanel(new BorderLayout());
        String[] columnNamesGosti = {"Ime", "Prezime", "Pol", "Datum rodjenja", "Broj telefona", "Adresa", "Korisničko ime"};
        DefaultTableModel tableModelGosti = new DefaultTableModel(columnNamesGosti, 0) {
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
        java.util.List<Gost> gostiList = HotelListePodataka.getInstance().getListaGostiju(); // Keep track of the original list
        for (Gost gost : gostiList) {
            Object[] rowData = {gost.getIme(), gost.getPrezime(), gost.getPol(), gost.getDatumRodjenja(), gost.getTelefon(), gost.getAdresa(), gost.getKorisnickoIme()};
            tableModelGosti.addRow(rowData);
        }
        JTable gostiTable = new JTable(tableModelGosti);
        JScrollPane scrollPaneGosti = new JScrollPane(gostiTable);
        scrollPaneGosti.setPreferredSize(new Dimension(480, 200));
        gostiPanel.add(scrollPaneGosti, BorderLayout.CENTER);

        JPanel formAndErrorPanelGosti = new JPanel();
        formAndErrorPanelGosti.setLayout(new BoxLayout(formAndErrorPanelGosti, BoxLayout.PAGE_AXIS)); // Set the layout to BoxLayout.PAGE_AXIS

        JPanel formPanelGosti = new JPanel(new GridLayout(0, 2)); // Use a GridLayout for the form panel
        JTextField[] textFieldsGosti = new JTextField[columnNamesGosti.length - 1]; // Adjust for the JComboBoxes
        JComboBox<podaci.Pol> polComboBoxGosti = new JComboBox<>(podaci.Pol.values());

        formAndErrorPanelGosti.add(formPanelGosti);
        formAndErrorPanelGosti.add(Box.createVerticalStrut(10));

        int textFieldIndexGosti = 0; // Add this line
        JLabel errorLabel1Gosti = new JLabel();
        JLabel errorLabel2Gosti = new JLabel();
        JLabel errorLabel3Gosti = new JLabel();

        formPanelGosti.setLayout(new GridBagLayout());

        GridBagConstraints gbcGosti = new GridBagConstraints();

        gbcGosti.anchor = GridBagConstraints.WEST;
        gbcGosti.fill = GridBagConstraints.HORIZONTAL;
        gbcGosti.weightx = 1.0; // Add this line

        for (int i = 0; i < columnNamesGosti.length; i++) {
            columnNamesGosti[i] += ":";

            gbcGosti.gridx = 0;
            gbcGosti.gridy = 2 * i;
            formPanelGosti.add(new JLabel(columnNamesGosti[i]), gbcGosti);

            if (i == 2) {
                gbcGosti.gridx = 1;
                formPanelGosti.add(polComboBoxGosti, gbcGosti);
            } else {
                textFieldsGosti[textFieldIndexGosti] = new JTextField(10);
                gbcGosti.gridx = 1;
                formPanelGosti.add(textFieldsGosti[textFieldIndexGosti], gbcGosti);
                textFieldIndexGosti++;
            }
            gbcGosti.gridy++; // Move to the next row for the strut
            formPanelGosti.add(Box.createVerticalStrut(10), gbcGosti);
            // Add space after each form field
            if (i == 4 || i == 3 || i == 6) {
                if (i == 4) formPanelGosti.add(errorLabel1Gosti, gbcGosti);
                if (i == 3) formPanelGosti.add(errorLabel2Gosti, gbcGosti);
                if (i == 6) formPanelGosti.add(errorLabel3Gosti, gbcGosti);
            }
        }

        JPanel bottomPanelGosti = new JPanel(new BorderLayout());
        bottomPanelGosti.add(formAndErrorPanelGosti, BorderLayout.NORTH);
        gostiPanel.add(bottomPanelGosti, BorderLayout.PAGE_END);

        JPanel buttonPanelGosti = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // Add button to add data to the table
        JButton addButtonGosti = new JButton("Dodaj");
        buttonPanelGosti.add(addButtonGosti);
        // Add button to delete row from the table
        JButton deleteButtonGosti = new JButton("Obriši");
        JButton izmeniButtonGosti = new JButton("Izmeni");
        buttonPanelGosti.add(izmeniButtonGosti);
        buttonPanelGosti.add(deleteButtonGosti);
        formAndErrorPanelGosti.add(buttonPanelGosti);

        AdministratorGosti ag = new AdministratorGosti(textFieldsGosti, polComboBoxGosti, errorLabel1Gosti, errorLabel2Gosti, errorLabel3Gosti, tableModelGosti, gostiList, columnNamesGosti, gostiTable, gostiPanel, deleteButtonGosti);
        addButtonGosti.addActionListener(ag.getAddButtonListener());
        deleteButtonGosti.addActionListener(ag.getDeleteButtonListener());
        izmeniButtonGosti.addActionListener(ag.getIzmeniButtonListener());
        
        // Rezervacija panel
        JPanel rezervacijaPanel = new JPanel(new BorderLayout());
        String[] columnNamesRezervacija = {"ID", "Status", "Datum dolaska", "Datum odlaska", "Soba", "Gost", "Broj osoba", "Cena", "Dodatne usluge", "Tip sobe"};
        tableModelRezervacija = new DefaultTableModel(columnNamesRezervacija, 0) {
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
        java.util.List<Rezervacija> rezervacijaList = HotelListePodataka.getInstance().getListaRezervacija(); // Keep track of the original list
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
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            tableModelRezervacija.addRow(rowData.toArray());
        }
        JLabel potvrdaLabel = new JLabel();
        potvrdaLabel.setForeground(java.awt.Color.GREEN);
        JTable rezervacijaTable = new JTable(tableModelRezervacija);
        DefaultTableModel rez = (DefaultTableModel) rezervacijaTable.getModel();
        rez.fireTableDataChanged();
        JScrollPane scrollPaneRezervacija = new JScrollPane(rezervacijaTable);
        scrollPaneRezervacija.setPreferredSize(new Dimension(480, 200));
        rezervacijaPanel.add(scrollPaneRezervacija, BorderLayout.CENTER);
        // Create the radio buttons
        JRadioButton tipSobeRadioButton = new JRadioButton("Tip sobe");
        JRadioButton brojSobeRadioButton = new JRadioButton("Broj sobe");
        JRadioButton cenaRadioButton = new JRadioButton("Cena");
        JRadioButton dodatneUslugeRadioButton = new JRadioButton("Dodatne usluge");
        JButton sortMax = new JButton("Sortiraj (max cena)");
        JButton sortMin = new JButton("Sortiraj (min cena)");

        // Group the radio buttons
        ButtonGroup filterGroup = new ButtonGroup();
        filterGroup.add(tipSobeRadioButton);
        filterGroup.add(brojSobeRadioButton);
        filterGroup.add(cenaRadioButton);
        filterGroup.add(dodatneUslugeRadioButton);

        // Add the radio buttons to the panel
        JPanel panel = new JPanel(new GridLayout(2,4));
        JPanel radioButtonPanel = new JPanel(new GridLayout(1, 4));
        radioButtonPanel.add(tipSobeRadioButton);
        radioButtonPanel.add(brojSobeRadioButton);
        radioButtonPanel.add(cenaRadioButton);
        radioButtonPanel.add(dodatneUslugeRadioButton);
        JPanel sortButtonPanel = new JPanel(new GridLayout(1, 2));
        sortButtonPanel.add(sortMax);
        sortButtonPanel.add(sortMin);

        panel.add(sortButtonPanel,BorderLayout.NORTH);
        panel.add(radioButtonPanel, BorderLayout.CENTER);
        rezervacijaPanel.add(panel, BorderLayout.NORTH);
        rezervacijaPanel.add(scrollPaneRezervacija, BorderLayout.CENTER);

        JTextField filterTextField = new JTextField(10);
        JButton filterButton = new JButton("Filter");
        JButton refreshButton = new JButton("Obnovi");

        // Add the text field and button to the panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.add(filterTextField);
        filterPanel.add(filterButton);

        rezervacijaPanel.add(filterPanel, BorderLayout.SOUTH);
        JButton potvrdaRezervacijeButton = new JButton("Potvrda");
        JButton azuriranjeRezervacijaButton = new JButton("Ažuriraj");
        JButton dodatneUslugeButton = new JButton("Dodatne usluge");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbcDugmad = new GridBagConstraints();
        buttonPanel.setLayout(layout);

        // Add the buttons to the panel
        gbcDugmad.gridx = 0;
        gbcDugmad.gridy = 0;
        buttonPanel.add(filterTextField, gbcDugmad);
        gbcDugmad.gridx = 1;
        buttonPanel.add(filterButton, gbcDugmad);
        gbcDugmad.gridx = 2;
        buttonPanel.add(refreshButton, gbcDugmad);
        gbcDugmad.gridx = 3;
        buttonPanel.add(potvrdaRezervacijeButton, gbcDugmad);
        gbcDugmad.gridx = 4;
        buttonPanel.add(azuriranjeRezervacijaButton, gbcDugmad);
        gbcDugmad.gridx = 5;
        buttonPanel.add(dodatneUslugeButton, gbcDugmad);
        
        gbcDugmad.gridwidth = 6;
        gbcDugmad.gridx = 0;
        gbcDugmad.gridy = 1;
        buttonPanel.add(potvrdaLabel, gbcDugmad);

        rezervacijaPanel.add(buttonPanel, BorderLayout.PAGE_END);
        
        RecepcionerRezervacije ar = new RecepcionerRezervacije(sortMax, sortMin, currentFrame, dodatneUslugeButton, tipSobeRadioButton, brojSobeRadioButton, cenaRadioButton, dodatneUslugeRadioButton, filterTextField, filterButton, rezervacijaComboBox, this, tableModelSobe, rezervacijaList, potvrdaLabel, recepcioner, potvrdaRezervacijeButton, azuriranjeRezervacijaButton, tableModelRezervacija, rezervacijaTable, rezervacijaPanel);
        filterButton.addActionListener(ar.getFilterButtonListener());
        potvrdaRezervacijeButton.addActionListener(ar.getPotvrdaRezervacijeButtonListener());
        azuriranjeRezervacijaButton.addActionListener(ar.getAzuriranjeRezervacijaButtonListener());
        refreshButton.addActionListener(ar.getRefreshButtonListener());
        dodatneUslugeButton.addActionListener(ar.getDodatneUslugeButtonListener());
        sortMax.addActionListener(ar.getSortingMax());
        sortMin.addActionListener(ar.getSortingMin());
        
        // Sobe panel
        JPanel sobePanel = new JPanel(new BorderLayout());
        String[] columnNamesSobe = {"Broj Sobe", "Tip sobe", "Status", "Rezervacije"};
        JPanel tableAndComboBoxPanel = new JPanel();
        tableAndComboBoxPanel.setLayout(new BoxLayout(tableAndComboBoxPanel, BoxLayout.Y_AXIS));
        tableModelSobe = new DefaultTableModel(columnNamesSobe, 0) {
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
        java.util.List<Soba> sobeList = HotelListePodataka.getInstance().getListaSoba(); // Keep track of the original list
        for (Soba soba : sobeList) {
       	    String rezervacijaIds = soba.getListaRezervacija().stream()
       	        .map(rezervacija -> Integer.toString(rezervacija.getId()))
       	        .collect(Collectors.joining(","));
       	    Object[] rowData = {soba.getBrojSobe(), soba.getTip(), soba.getStatus(), rezervacijaIds};
       	    tableModelSobe.addRow(rowData);
       	}
        JTable sobeTable = new JTable(tableModelSobe);
        DefaultTableModel sob = (DefaultTableModel) sobeTable.getModel();
        sob.fireTableDataChanged();
        JScrollPane scrollPaneSobe = new JScrollPane(sobeTable);
        scrollPaneSobe.setPreferredSize(new Dimension(480, 200));
        tableAndComboBoxPanel.add(scrollPaneSobe);
        
        JPanel formAndErrorPanelSobe = new JPanel();
        formAndErrorPanelSobe.setLayout(new GridBagLayout()); // Set the layout to GridBagLayout

        GridBagConstraints constraints = new GridBagConstraints();

        // Create a JLabel
        JLabel rezervacijaIdLabel = new JLabel("ID rezervacije: ");
        
        JComboBox<String> rezervacijaSobeComboBox = new JComboBox<>();
        rezervacijaSobeComboBox.addItem("ID--gost--početak i kraj rezervacije--cena");
        // Set the constraints for the JLabel and add it to the panel
        constraints.gridx = 0; // Column 0
        constraints.gridy = 1; // Row 1
        constraints.anchor = GridBagConstraints.EAST; // Right alignment
        formAndErrorPanelSobe.add(rezervacijaIdLabel, constraints);

        // Create a JTextField for manual input
        JTextField rezervacijaIdField = new JTextField(10);

        // Set the constraints for the JTextField and add it to the panel
        constraints.gridx = 1; // Column 1
        constraints.gridy = 1; // Row 1
        constraints.weightx = 1.0; // This component gets all the extra horizontal space
        constraints.weighty = 1.0; // This component gets all the extra vertical space
        constraints.fill = GridBagConstraints.BOTH; // The component grows to fill its display area horizontally and vertically
        formAndErrorPanelSobe.add(scrollPaneSobe, constraints);

        tableAndComboBoxPanel.add(formAndErrorPanelSobe);
        sobePanel.add(tableAndComboBoxPanel);
    	
        potvrdaLabel = new JLabel();
        potvrdaLabel.setForeground(java.awt.Color.GREEN);
        JPanel buttonPanelSobe = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanelSobe.add(rezervacijaSobeComboBox);
        buttonPanelSobe.add(rezervacijaIdLabel, constraints);
        buttonPanelSobe.add(rezervacijaIdField, constraints);
        buttonPanelSobe.add(potvrdaLabel);
        JButton popuniButtonSobe = new JButton("Popuni");
        buttonPanelSobe.add(popuniButtonSobe);
        
        JButton oslobodiButtonSobe = new JButton("Oslobodi");
        buttonPanelSobe.add(oslobodiButtonSobe);
        sobePanel.add(buttonPanelSobe, BorderLayout.SOUTH);
        
        RecepcionerSobe rs = new RecepcionerSobe(rezervacijaSobeComboBox, rezervacijaIdField, this, tableModelRezervacija, potvrdaLabel, rezervacijaComboBox, recepcioner, tableModelSobe, sobeList, sobeTable, sobePanel, popuniButtonSobe, oslobodiButtonSobe);
        popuniButtonSobe.addActionListener(rs.getPopuniButtonListener());
        oslobodiButtonSobe.addActionListener(rs.getOslobodiButtonListener());
        tabbedPane.addTab("Sobe", sobePanel);
        
        // Sopstveni podaci panel
        JPanel zaposleniPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(zaposleniPanel);

        JPanel formAndErrorPanel = new JPanel();
        formAndErrorPanel.setLayout(new BoxLayout(formAndErrorPanel, BoxLayout.PAGE_AXIS)); // Set the layout to BoxLayout.PAGE_AXIS
        
        String[] columnNames = {"Ime", "Prezime", "Pol", "Tip", "Datum rodjenja", "Broj telefona", "Adresa", "Korisničko ime", "Lozinka", "Stručna sprema", "Staž"};
        JPanel formPanel = new JPanel(new GridLayout(0, 2)); // Use a GridLayout for the form panel
        JTextField[] textFields = new JTextField[columnNames.length - 5]; // Adjust for the JComboBoxes
        JComboBox<podaci.Pol> polComboBox = new JComboBox<>(podaci.Pol.values());

        formAndErrorPanel.add(formPanel);
        formAndErrorPanel.add(Box.createVerticalStrut(10));
        
        JLabel uspesno = new JLabel();
        uspesno.setForeground(java.awt.Color.GREEN);

        int textFieldIndex = 0; // Add this line
        JLabel errorLabel1 = new JLabel();
        JLabel errorLabel2 = new JLabel();
        
        formPanel.setLayout(new GridBagLayout());
        formAndErrorPanel.add(uspesno);
        formAndErrorPanel.add(Box.createVerticalStrut(20));
        formAndErrorPanel.add(formPanel);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; 
        

        for (int i = 0; i < columnNames.length; i++) {
        	if (i == 3 || i == 7 || i == 9 || i == 10) {
        		continue;
        	}
            columnNames[i] += ":";

            gbc.gridx = 0;
            gbc.gridy = 2 * i;
            formPanel.add(new JLabel(columnNames[i]), gbc);

            if (i == 2) {
                gbc.gridx = 1;
                formPanel.add(polComboBox, gbc);
            } else {
                textFields[textFieldIndex] = new JTextField(10);
                gbc.gridx = 1;
                formPanel.add(textFields[textFieldIndex], gbc);
                textFieldIndex++;
            }
            gbc.gridy++; // Move to the next row for the strut
            formPanel.add(Box.createVerticalStrut(10), gbc);
            if (i == 5) formPanel.add(errorLabel1, gbc);
            if (i == 4) formPanel.add(errorLabel2, gbc);
        }
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formAndErrorPanel, BorderLayout.NORTH);
        zaposleniPanel.add(bottomPanel, BorderLayout.PAGE_END);

        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton izmeniButton = new JButton("Izmeni");
        buttonPanel2.add(izmeniButton);
        formAndErrorPanel.add(buttonPanel2);

        LicnoOsoblje az = new LicnoOsoblje(recepcioner, textFields, polComboBox, errorLabel1, errorLabel2, uspesno, columnNames, zaposleniPanel);
        izmeniButton.addActionListener(az.getIzmeniButtonListener());
        
		tabbedPane.addTab("Gosti", gostiPanel);
        tabbedPane.addTab("Rezervacije", rezervacijaPanel);
        tabbedPane.addTab("Lično", centerPanel);
        
        String[] columnNames2 = {"Datum", "Rezervacije"};
        
	     DefaultTableModel tableModelOdlasci = new DefaultTableModel(columnNames2, 0);
	     DefaultTableModel tableModelDolasci = new DefaultTableModel(columnNames2, 0);
	     DefaultTableModel tableModelZauzetost = new DefaultTableModel(columnNames2, 0);
	

			for (String odlasciDolasci : OdlasciDolasci.getInstance().getDnevniDolasci().keySet()) {
			    ArrayList<Rezervacija> rezervacijeList = OdlasciDolasci.getInstance().getDnevniDolasci().get(odlasciDolasci);
			    String str = rezervacijeList.stream()
                	.map(Rezervacija::toString)
                    .collect(Collectors.joining(", "));
			    Object[] rowData = {odlasciDolasci, str};
			    tableModelOdlasci.addRow(rowData);
			}
	     for (String odlasciDolasci : OdlasciDolasci.getInstance().getDnevniOdlasci().keySet()) {
	    	    ArrayList<Rezervacija> rezervacijeList = OdlasciDolasci.getInstance().getDnevniOdlasci().get(odlasciDolasci);
	    	    String str = rezervacijeList.stream()
                    .map(Rezervacija::toString)
                    .collect(Collectors.joining(","));
	    	    Object[] rowData = {odlasciDolasci, str};
	    	    tableModelDolasci.addRow(rowData);
	    	}
	     for (String odlasciDolasci : OdlasciDolasci.getInstance().getDnevnaZauzetost().keySet()) {
	         Object[] rowData = {odlasciDolasci, OdlasciDolasci.getInstance().getDnevnaZauzetost().get(odlasciDolasci)};
	         tableModelZauzetost.addRow(rowData);
	     }
	
	     // Create JTables for each table model
	     JTable odlasciTable = new JTable(tableModelOdlasci);
	     JTable dolasciTable = new JTable(tableModelDolasci);
	     JTable zauzetostTable = new JTable(tableModelZauzetost);
	
	     // Create JScrollPanes for each table
	     JScrollPane scrollPaneOdlasci = new JScrollPane(odlasciTable);
	     JScrollPane scrollPaneDolasci = new JScrollPane(dolasciTable);
	     JScrollPane scrollPaneZauzetost = new JScrollPane(zauzetostTable);
	
	     // Create a JPanel for each table
	     JPanel odlasciPanel = new JPanel(new BorderLayout());
	     JPanel dolasciPanel = new JPanel(new BorderLayout());
	     JPanel zauzetostPanel = new JPanel(new BorderLayout());
	
	     // Add JScrollPanes to JPanel
	     odlasciPanel.add(scrollPaneOdlasci, BorderLayout.CENTER);
	     dolasciPanel.add(scrollPaneDolasci, BorderLayout.CENTER);
	     zauzetostPanel.add(scrollPaneZauzetost, BorderLayout.CENTER);
	
	     // Add JPanel to JTabbedPane
	     tabbedPane.addTab("Odlasci", odlasciPanel);
	     tabbedPane.addTab("Dolasci", dolasciPanel);
	     tabbedPane.addTab("Zauzetost", zauzetostPanel);
	}
    
	public void refreshRezervacije(DefaultTableModel tableModelRezervacija) {
		if (tableModelRezervacija.getRowCount() > 0) {
			tableModelRezervacija.setRowCount(0);
		}
		java.util.List<Rezervacija> rezervacijaList = HotelListePodataka.getInstance().getListaRezervacija(); // Keep track of the original list
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
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            tableModelRezervacija.addRow(rowData.toArray());
        }
    }
	
	public void refreshSobe(DefaultTableModel tableModelSobe) {
		if (tableModelSobe.getRowCount() > 0) {
			tableModelSobe.setRowCount(0); // Remove all rows from the table
		}
		java.util.List<Soba> sobeList = HotelListePodataka.getInstance().getListaSoba(); // Keep track of the original
																							// list
		for (Soba soba : sobeList) {
			String rezervacijaIds = soba.getListaRezervacija().stream()
					.map(rezervacija -> Integer.toString(rezervacija.getId())).collect(Collectors.joining(","));
			Object[] rowData = { soba.getBrojSobe(), soba.getTip(), soba.getStatus(), rezervacijaIds };
			tableModelSobe.addRow(rowData);
		}
	}
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

	public DefaultTableModel getTableModelRezervacija() {
		return tableModelRezervacija;
	}

	public void setTableModelRezervacija(DefaultTableModel tableModelRezervacija) {
		this.tableModelRezervacija = tableModelRezervacija;
	}

	public DefaultTableModel getTableModelSobe() {
		return tableModelSobe;
	}

	public void setTableModelSobe(DefaultTableModel tableModelSobe) {
		this.tableModelSobe = tableModelSobe;
	}
 }
