package prikaz;

import hotel.HotelListePodataka;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import entiteti.Osoblje;
import entiteti.Soba;
import entiteti.Sobarica;
import kontroler.LicnoOsoblje;
import kontroler.SobaricaFunkcionalnosti;

public class StranicaSobarice {
    private JTabbedPane tabbedPane;
    private JPanel sobaricaPanel;

    public JPanel getSobaricaPanel() {
        return sobaricaPanel;
    }

    public StranicaSobarice(Osoblje sobarica, JFrame currentFrame, JFrame previousFrame) {
        tabbedPane = new JTabbedPane();
        sobaricaPanel = new JPanel();
        sobaricaPanel.add(tabbedPane);
        currentFrame.setTitle("Sobarica: " + sobarica.getIme() + " " + sobarica.getPrezime());
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
        // Sobarica obaveze panel
        JPanel newPanel = new JPanel(new BorderLayout());
        String[] column = {"Broj sobe"};
        DefaultTableModel tableModel = new DefaultTableModel(column, 0) {
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

        for (Sobarica sobar : HotelListePodataka.getInstance().getListaSobarica()) {
            if (sobar.getKorisnickoIme().equals(sobarica.getKorisnickoIme())) {
                java.util.List<Soba> sobe = sobar.getSobeZaCiscenje();
                for (Soba soba : sobe) {
                    Object[] rowData = {soba.getBrojSobe()};
                    tableModel.addRow(rowData);
                }
                break;
            }
        }

        JTable table = new JTable(tableModel);

        // Add table to a JScrollPane and add JScrollPane to the panel
        JScrollPane scrollPane = new JScrollPane(table);
        newPanel.add(scrollPane, BorderLayout.CENTER);

        JButton removeButton = new JButton("Ukloni");

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(removeButton, BorderLayout.EAST);

        newPanel.add(buttonPanel, BorderLayout.SOUTH);
        SobaricaFunkcionalnosti sf = new SobaricaFunkcionalnosti(sobarica, tableModel, table);
        removeButton.addActionListener(sf.getRemoveButtonListener());

        tabbedPane.addTab("Sobe za čišćenje", newPanel);
        
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

        LicnoOsoblje az = new LicnoOsoblje(sobarica, textFields, polComboBox, errorLabel1, errorLabel2, uspesno, columnNames, zaposleniPanel);
        izmeniButton.addActionListener(az.getIzmeniButtonListener());
        
        tabbedPane.addTab("Lično", centerPanel);
        
     }
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
 }
