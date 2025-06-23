package prikaz;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import javax.swing.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;
import entiteti.Gost;
import entiteti.Osoblje;
import entiteti.Rezervacija;
import entiteti.Soba;
import entiteti.Sobarica;
import hotel.HotelListePodataka;
import kontroler.AdministratorCenovnik;
import kontroler.AdministratorGosti;
import kontroler.AdministratorRezervacije;
import kontroler.AdministratorSobe;
import kontroler.AdministratorZaposleni;
import kontroler.Izvestaji;
import kontroler.LicnoOsoblje;
import podaci.Cenovnik;
import podaci.DodatneUsluge;
import podaci.PrihodiRashodi;
import podaci.SpecijalnePogodnosti;
import podaci.StatusRezervacije;
import podaci.StatusSobe;
import podaci.TipSobe;

public class StranicaAdministratora {
    private JTabbedPane tabbedPane;
    private JPanel adminPanel;
    private JComboBox<TipSobe> tipSobeComboBoxSobe = new JComboBox<>();
    private JComboBox<TipSobe> tipSobeComboBox = new JComboBox<>();
    private JList<DodatneUsluge> dodatneUslugeList = new JList<>();
    private DefaultTableModel tableModelRezervacija = new DefaultTableModel();
    private DefaultTableModel tableModelSobe = new DefaultTableModel();
    private JComboBox<TipSobe> tipSobeComboBoxCene = new JComboBox<>();
    private JComboBox<DodatneUsluge> uslugeComboBoxCene = new JComboBox<>();

    public JPanel getAdminPanel() {
        return adminPanel;
    }

    public StranicaAdministratora(Osoblje administrator, JFrame currentFrame, JFrame previousFrame) {
        tabbedPane = new JTabbedPane();
        adminPanel = new JPanel();
        adminPanel.add(tabbedPane);
        currentFrame.setTitle("Administrator: " + administrator.getIme() + " " + administrator.getPrezime());
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

        // Zaposleni panel
        JPanel zaposleniPanel = new JPanel(new BorderLayout());
        String[] columnNames = {"Ime", "Prezime", "Pol", "Tip", "Datum rodjenja", "Broj telefona", "Adresa", "Korisničko ime", "Stručna sprema", "Staž", "Plata"};
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
        java.util.List<Osoblje> osobljeList = HotelListePodataka.getInstance().getListaZaposlenih(); // Keep track of the original list
        for (Osoblje osoblje : osobljeList) {
            Object[] rowData = {osoblje.getIme(), osoblje.getPrezime(), osoblje.getPol(), osoblje.getTip(), osoblje.getDatumRodjenja(), osoblje.getTelefon(), osoblje.getAdresa(), osoblje.getKorisnickoIme(), osoblje.getStrucnaSprema(), osoblje.getStaz(), osoblje.getPlata()};
            tableModel.addRow(rowData);
        }
        JTable zaposleniTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(zaposleniTable);
        scrollPane.setPreferredSize(new Dimension(480, 200));
        zaposleniPanel.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Zaposleni", zaposleniPanel);

        JPanel formAndErrorPanel = new JPanel();
        formAndErrorPanel.setLayout(new BoxLayout(formAndErrorPanel, BoxLayout.PAGE_AXIS)); // Set the layout to BoxLayout.PAGE_AXIS

        JPanel formPanel = new JPanel(new GridLayout(0, 2)); // Use a GridLayout for the form panel
        JTextField[] textFields = new JTextField[columnNames.length - 4]; // Adjust for the JComboBoxes
        JComboBox<podaci.Pol> polComboBox = new JComboBox<>(podaci.Pol.values());
        JComboBox<podaci.Zaposleni> tipComboBox = new JComboBox<>(podaci.Zaposleni.values());
        JComboBox<podaci.NivoStrucneSpreme> spremaComboBox = new JComboBox<>(podaci.NivoStrucneSpreme.values());

        formAndErrorPanel.add(formPanel);
        formAndErrorPanel.add(Box.createVerticalStrut(10));

        int textFieldIndex = 0; // Add this line
        JLabel errorLabel1 = new JLabel();
        JLabel errorLabel2 = new JLabel();
        JLabel errorLabel3 = new JLabel();
        JLabel errorLabel4 = new JLabel();
        
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Add this line

        for (int i = 0; i < columnNames.length; i++) {
        	if (i == 10) {
        		continue;
        	}
            columnNames[i] += ":";

            gbc.gridx = 0;
            gbc.gridy = 2 * i;
            formPanel.add(new JLabel(columnNames[i]), gbc);

            if (i == 2) {
                gbc.gridx = 1;
                formPanel.add(polComboBox, gbc);
            } else if (i == 3) {
                gbc.gridx = 1;
                formPanel.add(tipComboBox, gbc);
            } else if (i == 8) {
                gbc.gridx = 1;
                formPanel.add(spremaComboBox, gbc);
            } else {
                textFields[textFieldIndex] = new JTextField(10);
                gbc.gridx = 1;
                formPanel.add(textFields[textFieldIndex], gbc);
                textFieldIndex++;
            }
            gbc.gridy++; // Move to the next row for the strut
            formPanel.add(Box.createVerticalStrut(10), gbc);
            // Add space after each form field
            if (i == 5 || i == 4 || i == 7 || i == 9) {
                if (i == 5) formPanel.add(errorLabel1, gbc);
                if (i == 4) formPanel.add(errorLabel2, gbc);
                if (i == 7) formPanel.add(errorLabel4, gbc);
                if (i == 9) formPanel.add(errorLabel3, gbc);
            }
        }
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formAndErrorPanel, BorderLayout.NORTH);
        zaposleniPanel.add(bottomPanel, BorderLayout.PAGE_END);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // Add button to add data to the table
        JButton addButton = new JButton("Dodaj");
        buttonPanel.add(addButton);
        // Add button to delete row from the table
        JButton deleteButton = new JButton("Obriši");
        JButton izmeniButton = new JButton("Izmeni");
        buttonPanel.add(izmeniButton);
        buttonPanel.add(deleteButton);
        formAndErrorPanel.add(buttonPanel);

        AdministratorZaposleni az = new AdministratorZaposleni(administrator, this, textFields, polComboBox, tipComboBox, spremaComboBox, errorLabel1, errorLabel2, errorLabel3, errorLabel4, tableModel, osobljeList, columnNames, zaposleniTable, zaposleniPanel, deleteButton);
        addButton.addActionListener(az.getAddButtonListener());
        deleteButton.addActionListener(az.getDeleteButtonListener());
        izmeniButton.addActionListener(az.getIzmeniButtonListener());

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
                field.setAccessible(true);
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
        JTable rezervacijaTable = new JTable(tableModelRezervacija);
        DefaultTableModel model = (DefaultTableModel) rezervacijaTable.getModel();
        model.fireTableDataChanged();
        JScrollPane scrollPaneRezervacija = new JScrollPane(rezervacijaTable);
        scrollPaneRezervacija.setPreferredSize(new Dimension(480, 200));
        rezervacijaPanel.add(scrollPaneRezervacija, BorderLayout.CENTER);

        JPanel formAndErrorPanelRezervacija = new JPanel();
        formAndErrorPanelRezervacija.setLayout(new BoxLayout(formAndErrorPanelRezervacija, BoxLayout.PAGE_AXIS)); // Set the layout to BoxLayout.PAGE_AXIS

        JPanel formPanelRezervacija = new JPanel(new GridLayout(0, 2)); // Use a GridLayout for the form panel
        JTextField[] textFieldsRezervacija = new JTextField[columnNamesRezervacija.length - 5]; // Adjust for the JComboBoxes

        formAndErrorPanelRezervacija.add(formPanelRezervacija);
        formAndErrorPanelRezervacija.add(Box.createVerticalStrut(10));

        int textFieldIndexRezervacija = 0;
        JLabel errorLabel1Rezervacija = new JLabel();
        JLabel errorLabel2Rezervacija = new JLabel();
        JLabel errorLabel3Rezervacija = new JLabel();
        JLabel errorLabel4Rezervacija = new JLabel();
        JLabel errorLabel5Rezervacija = new JLabel();
        
        errorLabel1Rezervacija.setForeground(java.awt.Color.RED);
        errorLabel2Rezervacija.setForeground(java.awt.Color.RED);
        errorLabel3Rezervacija.setForeground(java.awt.Color.RED);
        errorLabel4Rezervacija.setForeground(java.awt.Color.RED);
        errorLabel5Rezervacija.setForeground(java.awt.Color.RED);
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
        JComboBox<StatusRezervacije> statusComboBox = new JComboBox<>(StatusRezervacije.values());
        dodatneUslugeList = new JList<>();
        
        for (int i = 0; i < columnNamesRezervacija.length; i++) {
            // Skip the "ID" and "Gost" fields
            if (columnNamesRezervacija[i].equals("ID") || columnNamesRezervacija[i].equals("Cena")) {
                continue;
            }

            columnNamesRezervacija[i] += ":";

            gbcRezervacija.gridx = 0;
            gbcRezervacija.gridy = 2 * i;
            formPanelRezervacija.add(new JLabel(columnNamesRezervacija[i]), gbcRezervacija);

            gbcRezervacija.gridx = 1;
            if (i == 1) {
                formPanelRezervacija.add(statusComboBox, gbcRezervacija);
            } else if (i == 9) {
                formPanelRezervacija.add(tipSobeComboBox, gbcRezervacija);
            } else if (i == 8 && Cenovnik.getInstance().getDodatneUsluge() != null) {
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

            if (i == 2) formPanelRezervacija.add(errorLabel1Rezervacija, gbcRezervacija);
            if (i == 3) formPanelRezervacija.add(errorLabel2Rezervacija, gbcRezervacija);
            if (i == 4) formPanelRezervacija.add(errorLabel3Rezervacija, gbcRezervacija);
            if (i == 6) formPanelRezervacija.add(errorLabel4Rezervacija, gbcRezervacija);
            if (i == 5) formPanelRezervacija.add(errorLabel5Rezervacija, gbcRezervacija);
        }

        JPanel bottomPanelRezervacija = new JPanel(new BorderLayout());
        bottomPanelRezervacija.add(formAndErrorPanelRezervacija, BorderLayout.NORTH);
        rezervacijaPanel.add(bottomPanelRezervacija, BorderLayout.PAGE_END);

        JPanel buttonPanelRezervacija = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // Add button to add data to the table
        JButton addButtonRezervacija = new JButton("Dodaj");
        buttonPanelRezervacija.add(addButtonRezervacija);
        // Add button to delete row from the table
        JButton deleteButtonRezervacija = new JButton("Obriši");
        buttonPanelRezervacija.add(deleteButtonRezervacija);
        formAndErrorPanelRezervacija.add(buttonPanelRezervacija);
        JButton izmeniButtonRezervacija = new JButton("Izmeni");
        buttonPanelRezervacija.add(izmeniButtonRezervacija);
        formAndErrorPanelRezervacija.add(buttonPanelRezervacija);

		AdministratorRezervacije ar = new AdministratorRezervacije(this, tableModelSobe, tipSobeComboBox, statusComboBox, dodatneUslugeList, textFieldsRezervacija, errorLabel1Rezervacija, errorLabel2Rezervacija, errorLabel3Rezervacija, errorLabel4Rezervacija, errorLabel5Rezervacija, tableModelRezervacija, rezervacijaList, columnNamesRezervacija, rezervacijaTable, rezervacijaPanel, deleteButtonRezervacija);
		addButtonRezervacija.addActionListener(ar.getAddButtonListener());
		deleteButtonRezervacija.addActionListener(ar.getDeleteButtonListener());
        izmeniButtonRezervacija.addActionListener(ar.getIzmeniButtonListener());
        
		tabbedPane.addTab("Gosti", gostiPanel);
        tabbedPane.addTab("Rezervacije", rezervacijaPanel);
        

     // Sobe panel
     JPanel sobePanel = new JPanel(new BorderLayout());
     String[] columnNamesSobe = {"Broj Sobe", "Tip sobe", "Status", "Rezervacije"};
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
     DefaultTableModel modelSobe = (DefaultTableModel) sobeTable.getModel();
     modelSobe.fireTableDataChanged();
     JScrollPane scrollPaneSobe = new JScrollPane(sobeTable);
     scrollPaneSobe.setPreferredSize(new Dimension(480, 200));
     sobePanel.add(scrollPaneSobe, BorderLayout.CENTER);

     JPanel formAndErrorPanelSobe = new JPanel();
     formAndErrorPanelSobe.setLayout(new BoxLayout(formAndErrorPanelSobe, BoxLayout.PAGE_AXIS)); // Set the layout to BoxLayout.PAGE_AXIS

     JPanel formPanelSobe = new JPanel(new GridLayout(0, 2)); // Use a GridLayout for the form panel
     JTextField[] textFieldsSobe = new JTextField[columnNamesSobe.length - 2]; // Adjust for the JComboBoxes
     tipSobeComboBoxSobe = new JComboBox<>(Cenovnik.getInstance().getCeneTipovaSoba().keySet().toArray(new TipSobe[0]));
     JComboBox<StatusSobe> statusSobeComboBox = new JComboBox<>(StatusSobe.values());

     formAndErrorPanelSobe.add(formPanelSobe);
     formAndErrorPanelSobe.add(Box.createVerticalStrut(10));

     int textFieldIndexSobe = 0; // Add this line
     JLabel errorLabel1Sobe = new JLabel();
     JLabel errorLabel2Sobe = new JLabel();
     JLabel errorLabel3Sobe = new JLabel();
     errorLabel1Sobe.setForeground(java.awt.Color.RED);
     errorLabel2Sobe.setForeground(java.awt.Color.RED);
     errorLabel3Sobe.setForeground(java.awt.Color.RED);

     formPanelSobe.setLayout(new GridBagLayout());

     GridBagConstraints gbcSobe = new GridBagConstraints();

     gbcSobe.anchor = GridBagConstraints.WEST;
     gbcSobe.fill = GridBagConstraints.HORIZONTAL;
     gbcSobe.weightx = 1.0; // Add this line

     for (int i = 0; i < columnNamesSobe.length; i++) {
         columnNamesSobe[i] += ":";

         gbcSobe.gridx = 0;
         gbcSobe.gridy = 2 * i;
         formPanelSobe.add(new JLabel(columnNamesSobe[i]), gbcSobe);

         if (i == 1) {
             gbcSobe.gridx = 1;
             formPanelSobe.add(tipSobeComboBoxSobe, gbcSobe);
         } else if (i == 2) {
        	 gbcSobe.gridx = 1;
             formPanelSobe.add(statusSobeComboBox, gbcSobe);
         } else {
             textFieldsSobe[textFieldIndexSobe] = new JTextField(10);
             gbcSobe.gridx = 1;
             formPanelSobe.add(textFieldsSobe[textFieldIndexSobe], gbcSobe);
             textFieldIndexSobe++;
         }
         gbcSobe.gridy++; // Move to the next row for the strut
         formPanelSobe.add(Box.createVerticalStrut(10), gbcSobe);
         // Add space after each form field
         if (i == 0 || i == 3) {
             if (i == 0) formPanelSobe.add(errorLabel1Sobe, gbcSobe);
             if (i == 3) formPanelSobe.add(errorLabel2Sobe, gbcSobe);
             if (i == 4) formPanelSobe.add(errorLabel3Sobe, gbcSobe);
         }
         if (tipSobeComboBoxSobe.getItemCount() > 0) {
        	 tipSobeComboBoxSobe.setSelectedIndex(0);
         }
         if (statusSobeComboBox.getItemCount() > 0) {
        	 statusSobeComboBox.setSelectedIndex(0);
         }
     }
     JPanel checkBoxPanel = new JPanel();
     checkBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

     for (SpecijalnePogodnosti tip : SpecijalnePogodnosti.values()) {
         JCheckBox checkBox = new JCheckBox(tip.toString());
         checkBoxPanel.add(checkBox);
     }

     JPanel bottomPanelSobe = new JPanel(new BorderLayout());
     bottomPanelSobe.add(checkBoxPanel, BorderLayout.NORTH);
     bottomPanelSobe.add(formAndErrorPanelSobe, BorderLayout.CENTER);

     sobePanel.add(bottomPanelSobe, BorderLayout.PAGE_END);

     JPanel buttonPanelSobe = new JPanel(new FlowLayout(FlowLayout.RIGHT));
     // Add button to add data to the table
     JButton addButtonSobe = new JButton("Dodaj");
     buttonPanelSobe.add(addButtonSobe);
     // Add button to delete row from the table
     JButton deleteButtonSobe = new JButton("Obriši");
     buttonPanelSobe.add(deleteButtonSobe);
     formAndErrorPanelSobe.add(buttonPanelSobe);
     JButton izmeniButtonSobe = new JButton("Izmeni");
     buttonPanelSobe.add(izmeniButtonSobe);
     
     AdministratorSobe as = new AdministratorSobe(this, checkBoxPanel, tableModelRezervacija, administrator, textFieldsSobe, tipSobeComboBoxSobe, statusSobeComboBox, errorLabel1Sobe, errorLabel2Sobe, errorLabel3, tableModelSobe, sobeList, columnNamesSobe, sobeTable, sobePanel, deleteButtonSobe);
     addButtonSobe.addActionListener(as.getAddButtonListener());
     deleteButtonSobe.addActionListener(as.getDeleteButtonListener());
     izmeniButtonSobe.addActionListener(as.getIzmeniButtonListener());

     tabbedPane.addTab("Sobe", sobePanel);

     // Cenovnik panel
     JSplitPane cenovnikPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
     cenovnikPanel.setResizeWeight(0.5);

     JPanel leftPanel = new JPanel(new GridLayout(2, 1));

     // Cene Tipova Soba table
     String[] columnNamesCeneTipovaSoba = {"Tip Sobe", "Datumski početka", "Datum kraja", "Cena"};
     DefaultTableModel tableModelCeneTipovaSoba = new DefaultTableModel(columnNamesCeneTipovaSoba, 0) {
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
     java.util.Map<TipSobe, Map<String, Double>> ceneList = Cenovnik.getInstance().getCeneTipovaSoba().keySet().stream().map(e -> Map.entry(e, Cenovnik.getInstance().getCeneTipovaSoba().get(e))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
     for (Map.Entry<TipSobe, HashMap<String, Double>> entry : Cenovnik.getInstance().getCeneTipovaSoba().entrySet()) {
    	 for (int i = 0; i < entry.getValue().keySet().size(); i++) {
    		 for (int j = 0; j < entry.getValue().values().size(); j++) {
		         Object[] rowData = {entry.getKey(), entry.getValue().keySet().toArray()[i].toString().split(",")[0], entry.getValue().keySet().toArray()[i].toString().split(",")[1], entry.getValue().values().toArray()[j]};
		         tableModelCeneTipovaSoba.addRow(rowData);
    	 	 }
    	 }
     }
     JTable ceneTipovaSobaTable = new JTable(tableModelCeneTipovaSoba);
     JPanel ceneTipovaSobaTablePanel = new JPanel(new BorderLayout());
     JScrollPane ceneTipovaSobaScrollPane = new JScrollPane(ceneTipovaSobaTable);
     ceneTipovaSobaScrollPane.setPreferredSize(new Dimension(480, 100)); // Adjust the height as needed
     ceneTipovaSobaTablePanel.add(ceneTipovaSobaScrollPane, BorderLayout.CENTER);
     leftPanel.add(ceneTipovaSobaTablePanel);
     
     JPanel rightPanel = new JPanel(new GridLayout(2, 1));
     
     // Dodatne Usluge table
     String[] columnNamesDodatneUsluge = {"Usluga", "Datum početka", "Datum kraja", "Cena"};
     DefaultTableModel tableModelDodatneUsluge = new DefaultTableModel(columnNamesDodatneUsluge, 0) {
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
     Map<DodatneUsluge, Map<String, Double>> uslugeList = Cenovnik.getInstance().getDodatneUsluge().entrySet().stream().map(e -> Map.entry(e.getKey(), e.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
     for (Map.Entry<DodatneUsluge, HashMap<String, Double>> entry : Cenovnik.getInstance().getDodatneUsluge().entrySet()) {
    	 for (int i = 0; i < entry.getValue().keySet().size(); i++) {
    		 for (int j = 0; j < entry.getValue().values().size(); j++) {
		         Object[] rowData = {entry.getKey(), entry.getValue().keySet().toArray()[i].toString().split(",")[0], entry.getValue().keySet().toArray()[i].toString().split(",")[1], entry.getValue().values().toArray()[j]};
		         tableModelDodatneUsluge.addRow(rowData);
    	 	 }
    	 }    
     }
     JTable dodatneUslugeTable = new JTable(tableModelDodatneUsluge);
     JPanel dodatneUslugeTablePanel = new JPanel(new BorderLayout());
     JScrollPane dodatneUslugeScrollPane = new JScrollPane(dodatneUslugeTable);
     dodatneUslugeScrollPane.setPreferredSize(new Dimension(480, 100)); // Adjust the height as needed
     dodatneUslugeTablePanel.add(dodatneUslugeScrollPane, BorderLayout.CENTER);
     rightPanel.add(dodatneUslugeTablePanel);
     
     // Cene Tipova Soba form
     JPanel formPanelCeneTipovaSoba = new JPanel(new GridBagLayout());
     GridBagConstraints gbcCene = new GridBagConstraints();
     gbcCene.anchor = GridBagConstraints.WEST;
     gbcCene.fill = GridBagConstraints.HORIZONTAL;
     gbcCene.weightx = 1.0;

     // Add fields and labels
     String[] labelsCene = {"Tip Sobe:", "Datum početka:", "Datum kraja:", "Cena:"};
     JFormattedTextField[] fieldsCene = {new JFormattedTextField(), new JFormattedTextField(), new JFormattedTextField(), new JFormattedTextField()};
     JLabel	errorLabelCene1 = new JLabel();
     JLabel errorLabelCene2 = new JLabel();
     JLabel errorLabelCene3 = new JLabel();
     JLabel errorLabelCene4 = new JLabel();
     
     errorLabelCene1.setForeground(java.awt.Color.RED);
     errorLabelCene2.setForeground(java.awt.Color.RED);
     errorLabelCene3.setForeground(java.awt.Color.RED);
     errorLabelCene4.setForeground(java.awt.Color.RED);
     
     for (int i = 0; i < labelsCene.length; i++) {
         gbcCene.gridx = 0;
         gbcCene.gridy = 2 * i;
         formPanelCeneTipovaSoba.add(new JLabel(labelsCene[i]), gbcCene);
         gbcCene.gridx = 1;
         formPanelCeneTipovaSoba.add(fieldsCene[i], gbcCene);
         gbcCene.gridy++; // Move to the next row for the strut
         formPanelCeneTipovaSoba.add(Box.createVerticalStrut(10), gbcCene);
         if (i == 0) formPanelCeneTipovaSoba.add(errorLabelCene1, gbcCene);
         if (i == 1) formPanelCeneTipovaSoba.add(errorLabelCene2, gbcCene);
         if (i == 2) formPanelCeneTipovaSoba.add(errorLabelCene3, gbcCene);
         if (i == 3) formPanelCeneTipovaSoba.add(errorLabelCene4, gbcCene);
     }

     // Add button panels
     JPanel buttonPanelCene = new JPanel(new FlowLayout(FlowLayout.RIGHT));
     buttonPanelCene.add(new JButton("Dodaj"));
     buttonPanelCene.add(new JButton("Obriši")); 
     buttonPanelCene.add(new JButton("Izmeni"));
     formPanelCeneTipovaSoba.add(buttonPanelCene, gbcCene);
     tipSobeComboBoxCene = new JComboBox<>(Cenovnik.getInstance().getCeneTipovaSoba().keySet().toArray(new TipSobe[0]));
     gbcCene.gridy++;
     formPanelCeneTipovaSoba.add(tipSobeComboBoxCene, gbcCene);
     buttonPanelCene.add(new JButton("Izmeni naziv"));

     cenovnikPanel.add(formPanelCeneTipovaSoba);
     leftPanel.add(formPanelCeneTipovaSoba);
     
     // Dodatne Usluge form
     JPanel formPanelDodatneUsluge = new JPanel(new GridBagLayout());
     GridBagConstraints gbcUsluge = new GridBagConstraints();
     gbcUsluge.anchor = GridBagConstraints.WEST;
     gbcUsluge.fill = GridBagConstraints.HORIZONTAL;
     gbcUsluge.weightx = 1.0;

     // Add fields and labels
     String[] labelsUsluge = {"Usluga:", "Datum početka:", "Datum kraja:", "Cena:"};
     JFormattedTextField[] fieldsUsluge = {new JFormattedTextField(), new JFormattedTextField(), new JFormattedTextField(), new JFormattedTextField()};
     
     JLabel errorLabelUsluga1 = new JLabel();
     JLabel errorLabelUsluga2 = new JLabel();
     JLabel errorLabelUsluga3 = new JLabel();
     JLabel errorLabelUsluga4 = new JLabel();
     
     errorLabelUsluga1.setForeground(java.awt.Color.RED);
     errorLabelUsluga2.setForeground(java.awt.Color.RED);
     errorLabelUsluga3.setForeground(java.awt.Color.RED);
     errorLabelUsluga4.setForeground(java.awt.Color.RED);
     
     for (int i = 0; i < labelsUsluge.length; i++) {
         gbcUsluge.gridx = 0;
         gbcUsluge.gridy = 2 * i;
         formPanelDodatneUsluge.add(new JLabel(labelsUsluge[i]), gbcUsluge);
         gbcUsluge.gridx = 1;
         formPanelDodatneUsluge.add(fieldsUsluge[i], gbcUsluge);
         gbcUsluge.gridy++; // Move to the next row for the strut
         formPanelDodatneUsluge.add(Box.createVerticalStrut(10), gbcUsluge);
         
         if (i == 0) formPanelDodatneUsluge.add(errorLabelUsluga1, gbcUsluge);
         if (i == 1) formPanelDodatneUsluge.add(errorLabelUsluga2, gbcUsluge);
         if (i == 2) formPanelDodatneUsluge.add(errorLabelUsluga3, gbcUsluge);
         if (i == 3) formPanelDodatneUsluge.add(errorLabelUsluga4, gbcUsluge);
     }

     // Add button panels
     JPanel buttonPanelUsluge = new JPanel(new FlowLayout(FlowLayout.RIGHT));
     buttonPanelUsluge.add(new JButton("Dodaj"));
     buttonPanelUsluge.add(new JButton("Obriši"));
     buttonPanelUsluge.add(new JButton("Izmeni"));
     formPanelDodatneUsluge.add(buttonPanelUsluge, gbcUsluge);
     uslugeComboBoxCene = new JComboBox<>(Cenovnik.getInstance().getDodatneUsluge().keySet().toArray(new DodatneUsluge[0]));
     gbcUsluge.gridy++;
     formPanelDodatneUsluge.add(uslugeComboBoxCene, gbcCene);
     buttonPanelUsluge.add(new JButton("Izmeni naziv"));
     
     cenovnikPanel.add(formPanelDodatneUsluge);
     rightPanel.add(formPanelDodatneUsluge);

     cenovnikPanel.setLeftComponent(leftPanel);
     cenovnikPanel.setRightComponent(rightPanel);
     
     AdministratorCenovnik ac = new AdministratorCenovnik(this, administrator, uslugeComboBoxCene, tipSobeComboBoxCene, ceneList, uslugeList, ceneTipovaSobaTable, dodatneUslugeTable, fieldsCene, fieldsUsluge, tableModelCeneTipovaSoba, tableModelDodatneUsluge, ceneTipovaSobaTable, dodatneUslugeTable, errorLabelCene1, errorLabelCene2, errorLabelCene3, errorLabelCene4, errorLabelUsluga1, errorLabelUsluga2, errorLabelUsluga3, errorLabelUsluga4);
     // For the "Cene" section
     ((JButton) buttonPanelCene.getComponent(0)).addActionListener(ac.getAddButtonListenerCene());
     ((JButton) buttonPanelCene.getComponent(1)).addActionListener(ac.getDeleteButtonListenerCene());
     ((JButton) buttonPanelCene.getComponent(2)).addActionListener(ac.getIzmeniButtonListenerCene());
     ((JButton) buttonPanelCene.getComponent(3)).addActionListener(ac.getIzmeniNazivButtonListenerCene());

     // For the "Usluge" section
     ((JButton) buttonPanelUsluge.getComponent(0)).addActionListener(ac.getAddButtonListenerUsluge());
     ((JButton) buttonPanelUsluge.getComponent(1)).addActionListener(ac.getDeleteButtonListenerUsluge());
     ((JButton) buttonPanelUsluge.getComponent(2)).addActionListener(ac.getIzmeniButtonListenerUsluge());
     ((JButton) buttonPanelUsluge.getComponent(3)).addActionListener(ac.getIzmeniNazivButtonListenerUsluge());

     tabbedPane.addTab("Cenovnik", cenovnikPanel);
     
     //IZVEŠTAJI
     JPanel newPanel = new JPanel(new GridBagLayout());
     GridBagConstraints gbcIzveštaj = new GridBagConstraints();
     gbcIzveštaj.insets = new Insets(5, 5, 5, 5);  // Padding između komponenti
     gbcIzveštaj.fill = GridBagConstraints.HORIZONTAL;

     JLabel pocetkaLabel = new JLabel("Datum početka:");
     JTextField pocetkaField = new JTextField(10);
     JLabel krajaLabel = new JLabel("Datum kraja:");
     JTextField krajaField = new JTextField(10);

     JLabel error1 = new JLabel();
     JLabel error2 = new JLabel();
     error1.setForeground(Color.RED);
     error2.setForeground(Color.RED);

     gbcIzveštaj.gridx = 0;
     gbcIzveštaj.gridy = 0;
     newPanel.add(pocetkaLabel, gbcIzveštaj);

     gbcIzveštaj.gridx = 1;
     newPanel.add(pocetkaField, gbcIzveštaj);

     gbcIzveštaj.gridx = 1;
     gbcIzveštaj.gridy = 1;
     newPanel.add(error1, gbcIzveštaj);

     gbcIzveštaj.gridx = 0;
     gbcIzveštaj.gridy = 2;
     newPanel.add(krajaLabel, gbcIzveštaj);

     gbcIzveštaj.gridx = 1;
     newPanel.add(krajaField, gbcIzveštaj);

     gbcIzveštaj.gridx = 1;
     gbcIzveštaj.gridy = 3;
     newPanel.add(error2, gbcIzveštaj);

     JRadioButton prihodiRashodiRadioButton = new JRadioButton("prihodi/rashodi");
     JRadioButton brojNocenjaRadioButton = new JRadioButton("broj noćenja");
     JRadioButton rezervacijeRadioButton = new JRadioButton("rezervacije");
     JRadioButton ocisceneSobeRadioButton = new JRadioButton("očišćene sobe");

     ButtonGroup group = new ButtonGroup();
     group.add(prihodiRashodiRadioButton);
     group.add(brojNocenjaRadioButton);
     group.add(rezervacijeRadioButton);
     group.add(ocisceneSobeRadioButton);

     gbcIzveštaj.gridy = 4;
     newPanel.add(prihodiRashodiRadioButton, gbcIzveštaj);
     gbcIzveštaj.gridy = 5;
     newPanel.add(brojNocenjaRadioButton, gbcIzveštaj);
     gbcIzveštaj.gridy = 6;
     newPanel.add(rezervacijeRadioButton, gbcIzveštaj);
     gbcIzveštaj.gridy = 7;
     newPanel.add(ocisceneSobeRadioButton, gbcIzveštaj);

     JButton pretragaButton = new JButton("Pretraga");
     gbcIzveštaj.gridx = 0;
     gbcIzveštaj.gridy = 8;
     gbcIzveštaj.gridwidth = 2;
     newPanel.add(pretragaButton, gbcIzveštaj);

     Izvestaji iz = new Izvestaji(error1, error2, null, pocetkaField, krajaField, prihodiRashodiRadioButton, brojNocenjaRadioButton, rezervacijeRadioButton, ocisceneSobeRadioButton);
     pretragaButton.addActionListener(iz.getPretragaButtonListener());

     // Kreiranje grafikona
     CategoryChart grafikon = new CategoryChartBuilder()
             .width(400)
             .height(300)
             .title("Prihodi po tipovima soba u proteklih godinu dana")
             .xAxisTitle("Mesec")
             .yAxisTitle("Prihod")
             .build();

     // Definisanje meseci
     List<String> meseci = Arrays.asList("Jan", "Feb", "Mar", "Apr", "Maj", "Jun", "Jul", "Avg", "Sep", "Okt", "Nov", "Dec");

     // Formatter za datume i opseg datuma
     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
     LocalDate krajnjiDatum = LocalDate.now();
     LocalDate pocetniDatum = krajnjiDatum.minusYears(1);

     // Agregiranje prihoda
     Map<Month, Double> ukupniPrihod = new HashMap<>();
     Map<TipSobe, Map<Month, Double>> prihodiPoTipu = new HashMap<>();

     // Inicijalizacija mapa za prihode po tipovima soba
     for (TipSobe tip : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
         prihodiPoTipu.put(tip, new HashMap<>());
     }

     // Obrada dnevnih prihoda
     for (String datumStr : PrihodiRashodi.getInstance().getDnevniPrihodi().keySet()) {
    	    LocalDate datum = LocalDate.parse(datumStr, formatter);

    	    if (!datum.isBefore(pocetniDatum) && !datum.isAfter(krajnjiDatum)) {
    	        Month mesec = datum.getMonth();
    	        
				if (PrihodiRashodi.getInstance().getPrihodiPoTipuSobe() != null) {
	    	        for (TipSobe tip : PrihodiRashodi.getInstance().getPrihodiPoTipuSobe().keySet()) {
	    	            Double prihodPoTipuSobe = PrihodiRashodi.getInstance().getPrihodiPoTipuSobe().get(tip).get(datumStr);
	    	            if (prihodPoTipuSobe != null) {
	    	                prihodiPoTipu.get(tip).merge(mesec, prihodPoTipuSobe, Double::sum);
	    	            }
	    	        }
				}
    	    }
    	}
		for (String datumStr : PrihodiRashodi.getInstance().getDnevniPrihodi().keySet()) {
			LocalDate datum = LocalDate.parse(datumStr, formatter);

			if (!datum.isBefore(pocetniDatum) && !datum.isAfter(krajnjiDatum)) {
				Month mesec = datum.getMonth();
				Double ukupanPrihod = podaci.PrihodiRashodi.getInstance().getDnevniPrihodi().get(datumStr);
				ukupniPrihod.merge(mesec, ukupanPrihod, Double::sum);
			}
		}

     // Dodavanje serija na grafikon
     for (Map.Entry<TipSobe, Map<Month, Double>> entry : prihodiPoTipu.entrySet()) {
         List<Number> mesecniPrihodi = new ArrayList<>();
         for (Month mesec : Month.values()) {
             mesecniPrihodi.add(entry.getValue().getOrDefault(mesec, 0.0));
         }
         grafikon.addSeries(entry.getKey().toString(), meseci, mesecniPrihodi);
     }

     // Dodavanje ukupnog prihoda
     List<Number> ukupniMesecniPrihodi = new ArrayList<>();
     for (Month mesec : Month.values()) {
         ukupniMesecniPrihodi.add(ukupniPrihod.getOrDefault(mesec, 0.0));
     }
     grafikon.addSeries("Ukupno", meseci, ukupniMesecniPrihodi);

     // Kreiranje dva pie grafikona
     PieChart pieChart1 = new PieChartBuilder().width(300).height(300).title("Opterećenje sobarica u proteklih 30 dana").build();
     PieChart pieChart2 = new PieChartBuilder().width(300).height(300).title("Statusi rezervacija u proteklih 30 dana").build();

     LocalDate startDate = LocalDate.now().minusDays(30);
     LocalDate endDate = LocalDate.now();

     for (Sobarica sobarica : podaci.Izvestaji.getInstance().getOcisceneSobe().keySet()) {
         int ocisceneSobe = 0;
         for (String datum : podaci.Izvestaji.getInstance().getOcisceneSobe().get(sobarica).keySet()) {
             LocalDate currentDatum = LocalDate.parse(datum, formatter);
             if ((currentDatum.isAfter(startDate) || currentDatum.isEqual(startDate)) && (currentDatum.isBefore(endDate) || currentDatum.isEqual(endDate))) {
                 ocisceneSobe += podaci.Izvestaji.getInstance().getOcisceneSobe().get(sobarica).get(datum);
             }
         }
         // Add a series to the pie chart for each Sobarica
         pieChart1.addSeries(sobarica.getKorisnickoIme(), ocisceneSobe);
     }
     Map<StatusRezervacije, Integer> statusCounts = new HashMap<>();

	  // Iterate over all reservations
	  for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
	      LocalDate startDate2 = LocalDate.now().minusDays(30);
	      LocalDate endDate2 = LocalDate.now();
	      
	      if ((startDate2.isAfter(endDate2.minusDays(30)) || startDate2.isEqual(endDate2.minusDays(30))) && (endDate.isBefore(LocalDate.now()) || endDate.isEqual(LocalDate.now()))) {
	          statusCounts.merge(rezervacija.getStatus(), 1, Integer::sum);
	      }
	  }
	  for (Map.Entry<StatusRezervacije, Integer> entry : statusCounts.entrySet()) {
	      pieChart2.addSeries(entry.getKey().toString(), entry.getValue());
	  }

     // Create a new GridBagConstraints for the charts
     GridBagConstraints gbcCharts = new GridBagConstraints();
     gbcCharts.gridx = 0;
     gbcCharts.gridy = 9; // Start the charts at the next row after the form components
     gbcCharts.gridwidth = 2; // Span the charts across both columns
     gbcCharts.weightx = 1.0; // Allow the charts to take up the remaining horizontal space
     gbcCharts.weighty = 1.0; // Allow the charts to take up the remaining vertical space
     gbcCharts.fill = GridBagConstraints.BOTH; // Allow the charts to resize both horizontally and vertically

     // Prikaz grafikona
     JPanel mainChartPanel = new JPanel(new BorderLayout());
     JPanel panelZaGrafikon = new XChartPanel<>(grafikon);
     panelZaGrafikon.setPreferredSize(new Dimension(200, 200));
     mainChartPanel.add(panelZaGrafikon, BorderLayout.CENTER);

     // Create a new panel for the pie charts
     JPanel pieChartsPanel = new JPanel(new GridLayout(1, 2)); // Arrange the pie charts in a row
     JPanel panelZaPieGrafikon1 = new XChartPanel<>(pieChart1);
     panelZaPieGrafikon1.setPreferredSize(new Dimension(200, 200));
     JPanel panelZaPieGrafikon2 = new XChartPanel<>(pieChart2);
     panelZaPieGrafikon2.setPreferredSize(new Dimension(200, 200));
     pieChartsPanel.add(panelZaPieGrafikon1);
     pieChartsPanel.add(panelZaPieGrafikon2);

     // Add the chart panels to the main panel
     newPanel.add(mainChartPanel, gbcIzveštaj);
     gbcIzveštaj.gridy++; // Move to the next row
     newPanel.add(pieChartsPanel, gbcIzveštaj);

     tabbedPane.addTab("Izveštaj", newPanel);
     tabbedPane.addTab("Grafik prihoda po tipu sobe", panelZaGrafikon);
     tabbedPane.addTab("Pita grafovi", pieChartsPanel);
     
     // Sopstveni podaci panel
     JPanel zaposleniPanel2 = new JPanel(new BorderLayout());
     JPanel centerPanel = new JPanel(new GridBagLayout());
     centerPanel.add(zaposleniPanel2);

     JPanel formAndErrorPanel2 = new JPanel();
     formAndErrorPanel2.setLayout(new BoxLayout(formAndErrorPanel2, BoxLayout.PAGE_AXIS)); // Set the layout to BoxLayout.PAGE_AXIS
     
     String[] columnNames2 = {"Ime", "Prezime", "Pol", "Tip", "Datum rodjenja", "Broj telefona", "Adresa", "Korisničko ime", "Lozinka", "Stručna sprema", "Staž"};
     JPanel formPanel2 = new JPanel(new GridLayout(0, 2)); // Use a GridLayout for the form panel
     JTextField[] textFields2 = new JTextField[columnNames2.length - 5]; // Adjust for the JComboBoxes
     JComboBox<podaci.Pol> polComboBox2 = new JComboBox<>(podaci.Pol.values());

     formAndErrorPanel2.add(formPanel2);
     formAndErrorPanel2.add(Box.createVerticalStrut(10));
     
     JLabel uspesno = new JLabel();
     uspesno.setForeground(java.awt.Color.GREEN);

     int textFieldIndex2 = 0; // Add this line
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
     	if (i == 3 || i == 7 || i == 9 || i == 10) {
     		continue;
     	}
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
         gbc2.gridy++; // Move to the next row for the strut
         formPanel2.add(Box.createVerticalStrut(10), gbc2);
         if (i == 5) formPanel2.add(errorLabeli1, gbc2);
         if (i == 4) formPanel2.add(errorLabeli2, gbc2);
     }
     
     JPanel bottomPanel2 = new JPanel(new BorderLayout());
     bottomPanel2.add(formAndErrorPanel2, BorderLayout.NORTH);
     zaposleniPanel2.add(bottomPanel2, BorderLayout.PAGE_END);

     JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
     JButton izmeniButton2 = new JButton("Izmeni");
     buttonPanel2.add(izmeniButton2);
     formAndErrorPanel2.add(buttonPanel2);

     LicnoOsoblje lo = new LicnoOsoblje(administrator, textFields2, polComboBox2, errorLabeli1, errorLabeli2, uspesno, columnNames2, zaposleniPanel2);
     izmeniButton2.addActionListener(lo.getIzmeniButtonListener());
     
     tabbedPane.addTab("Lično", centerPanel);
    }
    
    public void refreshUsluge(DefaultTableModel tableModelDodatneUsluge, JComboBox<DodatneUsluge> uslugeComboBoxCene) {
    	if (tableModelDodatneUsluge.getRowCount() > 0) {
    		tableModelDodatneUsluge.setRowCount(0);
    	}

        // Repopulate the table model
        for (Map.Entry<DodatneUsluge, HashMap<String, Double>> entry : Cenovnik.getInstance().getDodatneUsluge().entrySet()) {
            for (int i = 0; i < entry.getValue().keySet().size(); i++) {
                for (int j = 0; j < entry.getValue().values().size(); j++) {
                    Object[] rowData = {entry.getKey(), entry.getValue().keySet().toArray()[i].toString().split(",")[0], entry.getValue().keySet().toArray()[i].toString().split(",")[1], entry.getValue().values().toArray()[j]};
                    tableModelDodatneUsluge.addRow(rowData);
                }
            }
        }
        uslugeComboBoxCene.removeAllItems();
		    for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
		        uslugeComboBoxCene.addItem(usluga);
		    }

        // Notify the table that the data has changed
        tableModelDodatneUsluge.fireTableDataChanged();
    }
    
    public void refreshCene(DefaultTableModel tableModelCene, JComboBox<TipSobe> tipSobeComboBoxCene) {
    	if (tableModelCene.getRowCount() > 0) {
    		tableModelCene.setRowCount(0);
    	}

        // Repopulate the table model
        for (Map.Entry<TipSobe, HashMap<String, Double>> entry : Cenovnik.getInstance().getCeneTipovaSoba().entrySet()) {
	       	 for (int i = 0; i < entry.getValue().keySet().size(); i++) {
	       		 for (int j = 0; j < entry.getValue().values().size(); j++) {
	   		         Object[] rowData = {entry.getKey(), entry.getValue().keySet().toArray()[i].toString().split(",")[0], entry.getValue().keySet().toArray()[i].toString().split(",")[1], entry.getValue().values().toArray()[j]};
	   		         tableModelCene.addRow(rowData);
	       	 	 }
	       	 }
        }
        tipSobeComboBoxCene.removeAllItems();
        for (TipSobe tipSobe : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
            tipSobeComboBoxCene.addItem(tipSobe);
        }
        tipSobeComboBoxSobe.removeAllItems();
		for (TipSobe tipSobe : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
			tipSobeComboBoxSobe.addItem(tipSobe);
		}
		tipSobeComboBox.removeAllItems();
		for (TipSobe tipSobe : Cenovnik.getInstance().getCeneTipovaSoba().keySet()) {
			tipSobeComboBox.addItem(tipSobe);
		}
		DefaultListModel<DodatneUsluge> listModel = new DefaultListModel<>();
		for (DodatneUsluge usluga : Cenovnik.getInstance().getDodatneUsluge().keySet()) {
		    listModel.addElement(usluga);
		}
		dodatneUslugeList.setModel(listModel);
        tableModelCene.fireTableDataChanged();
    }
    
	public void refreshSobe(DefaultTableModel tableModelSobe) {
		if (tableModelSobe.getRowCount() > 0) {
			tableModelSobe.setRowCount(0);
		}

		// Repopulate the table model
		for (Soba soba : HotelListePodataka.getInstance().getListaSoba()) {
			String rezervacijaIds = soba.getListaRezervacija().stream()
					.map(rezervacija -> Integer.toString(rezervacija.getId())).collect(Collectors.joining(","));
			Object[] rowData = { soba.getBrojSobe(), soba.getTip(), soba.getStatus(), rezervacijaIds };
			tableModelSobe.addRow(rowData);
		}

		// Notify the table that the data has changed
		tableModelSobe.fireTableDataChanged();
	}
	
	public void refreshRezervacije(DefaultTableModel tableModelRezervacija, JPanel checkBoxPanel) {
		if (tableModelRezervacija.getRowCount() > 0) {
			tableModelRezervacija.setRowCount(0);
		}

		java.util.List<Rezervacija> rezervacijaList = HotelListePodataka.getInstance().getListaRezervacija(); // Keep track of the original list
        for (Rezervacija rezervacija : rezervacijaList) {
            List<Object> rowData = new ArrayList<>();
            for (Field field : Rezervacija.class.getDeclaredFields()) {
                field.setAccessible(true);
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
							rowData.add(gost.getIme());
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
			for (Component component : checkBoxPanel.getComponents()) {
				if (component instanceof JCheckBox) {
					JCheckBox checkBox = (JCheckBox) component;
					checkBox.setSelected(false);
				}
			}
        }
	}
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

	public JComboBox<TipSobe> getTipSobeComboBoxRezervacije() {
		return tipSobeComboBox;
	}

	public void setTipSobeComboBoxRezervacije(JComboBox<TipSobe> tipSobeComboBoxRezervacije) {
		this.tipSobeComboBox = tipSobeComboBoxRezervacije;
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

	public JComboBox<DodatneUsluge> getUslugeComboBox() {
		return uslugeComboBoxCene;
	}

	public void setUslugeComboBox(JComboBox<DodatneUsluge> uslugeComboBox) {
		this.uslugeComboBoxCene = uslugeComboBox;
	}
}