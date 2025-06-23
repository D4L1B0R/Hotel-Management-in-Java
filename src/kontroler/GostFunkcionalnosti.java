package kontroler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import entiteti.Gost;
import entiteti.Rezervacija;
import funkcionalnosti.GostFunkcija;
import hotel.HotelListePodataka;
import podaci.DodatneUsluge;
import podaci.StatusRezervacije;
import podaci.TipSobe;
import prikaz.StranicaGosta;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GostFunkcionalnosti {
    private GostFunkcija gost;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField[] textFieldsRezervacija;
    private JLabel errorLabel1Rezervacija;
    private JLabel errorLabel2Rezervacija;
    private JLabel errorLabel3Rezervacija;
    private JComboBox<TipSobe> tipSobeComboBox;
    private JList<DodatneUsluge> dodatneUslugeList;
    private JFrame currentFrame;
    private JFrame previousFrame;
    private JLabel totalCenaLabel;
    private StranicaGosta gostic;
    private Gost trenutniGost;

    public GostFunkcionalnosti(Gost trenutniGost, StranicaGosta gostic, JLabel totalCenaLabel, GostFunkcija gost, JTable table, DefaultTableModel tableModel, JTextField[] textFieldsRezervacija, JLabel errorLabel1Rezervacija, JLabel errorLabel2Rezervacija, JLabel errorLabel3Rezervacija, JComboBox<TipSobe> tipSobeComboBox, JList<DodatneUsluge> dodatneUslugeList, JFrame currentFrame, JFrame previousFrame) {
        this.setGost(gost);
        this.setTable(table);
        this.setTableModel(tableModel);
        this.setTextFieldsRezervacija(textFieldsRezervacija);
        this.setErrorLabel1Rezervacija(errorLabel1Rezervacija);
        this.setErrorLabel2Rezervacija(errorLabel2Rezervacija);
        this.setErrorLabel3Rezervacija(errorLabel3Rezervacija);
        this.setTipSobeComboBox(tipSobeComboBox);
        this.setDodatneUslugeList(dodatneUslugeList);
        this.setCurrentFrame(currentFrame);
        this.setPreviousFrame(previousFrame);
        this.setTotalCenaLabel(totalCenaLabel);
        this.gostic = gostic;
        this.trenutniGost = trenutniGost;
    }

    public ActionListener getPregledListener() {
        return e -> {
            // Create a new JFrame for input
            JFrame inputFrame = new JFrame("Unesite datume");
            inputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            inputFrame.setSize(400, 400);
            inputFrame.setLayout(new GridBagLayout());

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 0.5;
            constraints.gridx = 0;
            constraints.gridy = 0;

            // Create JLabels and JTextFields for the start and end dates
            JLabel startDateLabel = new JLabel("Datum početka (dd.MM.yyyy):");
            JTextField startDateField = new JTextField();
            JLabel endDateLabel = new JLabel("Datum kraja (dd.MM.yyyy):");
            JTextField endDateField = new JTextField();

            // Create a JButton for submission
            JButton submitButton = new JButton("Potvrdi");
            
            JLabel errorLabel1 = new JLabel();
            JLabel errorLabel2 = new JLabel();
            errorLabel1.setForeground(java.awt.Color.RED);
            errorLabel2.setForeground(java.awt.Color.RED);
            
            JComboBox<TipSobe> tipSobeComboBox = new JComboBox<>();

            // Add the components to the JFrame
            inputFrame.add(startDateLabel, constraints);

            constraints.gridy = 1;
            inputFrame.add(startDateField, constraints);

            constraints.gridy = 2;
            inputFrame.add(errorLabel1, constraints);

            constraints.gridy = 3;
            inputFrame.add(endDateLabel, constraints);

            constraints.gridy = 4;
            inputFrame.add(endDateField, constraints);

            constraints.gridy = 5;
            inputFrame.add(errorLabel2, constraints);

            constraints.gridy = 6;
            inputFrame.add(submitButton, constraints);
            
            constraints.gridy = 7;
            inputFrame.add(tipSobeComboBox, constraints);

            inputFrame.setLocationRelativeTo(null); // Center the JFrame
            inputFrame.setVisible(true);
            
            errorLabel1.setText("");
            errorLabel2.setText("");
            // Set the action for the submit button
            submitButton.addActionListener(e1 -> {
                boolean problem = false;
                try {
                    String startDateString = startDateField.getText();
                    String endDateString = endDateField.getText();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
                    dateFormat.setLenient(false);

                    java.util.Date startDate = dateFormat.parse(startDateString);
                    java.util.Date endDate = dateFormat.parse(endDateString);

                    if (startDate.after(endDate)) {
                        errorLabel1.setText("Datum početka mora biti pre datuma kraja.");
                        problem = true;
                    }
                } catch (ParseException ex) {
                    errorLabel1.setText("Datumi moraju biti u formatu (dd.MM.yyyy.).");
                    errorLabel2.setText("Datumi moraju biti u formatu (dd.MM.yyyy.).");
                    problem = true;
                }

                if (!problem) { 

                    // Call the method and append the results to the JTextArea
                    ArrayList<TipSobe> result = gost.pregledSlobodnihTipovaSoba(startDateField.getText(), endDateField.getText());
                    for (TipSobe tipSobe : result) {
                    	tipSobeComboBox.addItem(tipSobe);
                    }
                }
            });
        };
    }
    
    public ActionListener getRezervacijaListener() {
        return e -> {
            // Get data from the form
        	boolean problem = false;
            try {
                String startDateString = textFieldsRezervacija[1].getText();
                String endDateString = textFieldsRezervacija[2].getText();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
                dateFormat.setLenient(false);

                java.util.Date startDate = dateFormat.parse(startDateString);
                java.util.Date endDate = dateFormat.parse(endDateString);

                if (startDate.after(endDate)) {
                    errorLabel2Rezervacija.setText("Datum početka mora biti pre datuma kraja.");
                    problem = true;
                }
                if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(textFieldsRezervacija[1].getText(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) > 0) {
                	errorLabel2Rezervacija.setText("Datum početka mora krenuti od današnjeg dana.");
    				problem = true;
    			}
    			if (dodatneFunkcionalnosti.RacunanjeVremenskeRazlike.IzracunajRazlikuVremena(textFieldsRezervacija[2].getText(), LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))) > 0) {
    				errorLabel3Rezervacija.setText("Datum kraja mora krenuti od današnjeg dana.");
    				problem = true;
    			}
            } catch (ParseException ex) {
                errorLabel2Rezervacija.setText("Datumi moraju biti u formatu (dd.MM.yyyy.).");
                errorLabel3Rezervacija.setText("Datumi moraju biti u formatu (dd.MM.yyyy.).");
                problem = true;
            }
			try {
				Integer.parseInt(textFieldsRezervacija[0].getText());
			} catch (NumberFormatException ex) {
				errorLabel1Rezervacija.setText("Broj osoba mora biti ceo broj.");
				problem = true;
			}
			if (problem) {
				return;
			}
            int brojOsoba = Integer.parseInt(textFieldsRezervacija[0].getText());
            String datumPocetkaRezervacije = textFieldsRezervacija[1].getText();
            String datumKrajaRezervacije = textFieldsRezervacija[2].getText();
            TipSobe tipSobe = (TipSobe) tipSobeComboBox.getSelectedItem();
            List<DodatneUsluge> dodatneUsluge = dodatneUslugeList.getSelectedValuesList();
            ArrayList<DodatneUsluge> dodatneUslugeArrayList = new ArrayList<>(dodatneUsluge);
            // Convert tipSobe to the expected type
            String tipSobeString = tipSobe.toString();
            
            Rezervacija rezervacija = gost.rezervacijaSobe(trenutniGost, brojOsoba, datumPocetkaRezervacije, datumKrajaRezervacije, tipSobeString, dodatneUslugeArrayList);
            // Add a new row to the table
            Object[] rowData = {rezervacija.getId(), brojOsoba, datumPocetkaRezervacije, datumKrajaRezervacije, tipSobe, dodatneUsluge.stream().map(DodatneUsluge::toString).collect(Collectors.joining(", ")), StatusRezervacije.CEKANJE, rezervacija.getCena()};
            tableModel.addRow(rowData);
            gostic.refresh(tableModel);
			
			for (JTextField textField : textFieldsRezervacija) {
				textField.setText("");
			}
			errorLabel1Rezervacija.setText("");
			errorLabel2Rezervacija.setText("");
        };
    }

    public ActionListener getOtkazivanjeListener() {
        return e -> {
            // Get the selected row
            int selectedRow = table.getSelectedRow();

            // Remove the selected row from the table
            if (selectedRow >= 0) {
            	int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            	System.out.println(id);
            	for (Rezervacija rezervacija : HotelListePodataka.getInstance().getListaRezervacija()) {
            		if (rezervacija.getId() == id) {
            			if (rezervacija.getStatus() == StatusRezervacije.POTVRĐENA && rezervacija.getSoba() != null) {
            				JOptionPane.showMessageDialog(currentFrame, "Već ste se prijavili kod recepcionera.", "Error", JOptionPane.ERROR_MESSAGE);
            				return;
            			}
            			break;
            		}
            	}
                tableModel.removeRow(selectedRow);
                gost.otkazivanjeSobe(id);
            }
        };
    }

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(DefaultTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public JLabel getErrorLabel1Rezervacija() {
		return errorLabel1Rezervacija;
	}

	public void setErrorLabel1Rezervacija(JLabel errorLabel1Rezervacija) {
		this.errorLabel1Rezervacija = errorLabel1Rezervacija;
	}

	public JTextField[] getTextFieldsRezervacija() {
		return textFieldsRezervacija;
	}

	public void setTextFieldsRezervacija(JTextField[] textFieldsRezervacija) {
		this.textFieldsRezervacija = textFieldsRezervacija;
	}

	public GostFunkcija getGost() {
		return gost;
	}

	public void setGost(GostFunkcija gost) {
		this.gost = gost;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public JLabel getErrorLabel2Rezervacija() {
		return errorLabel2Rezervacija;
	}

	public void setErrorLabel2Rezervacija(JLabel errorLabel2Rezervacija) {
		this.errorLabel2Rezervacija = errorLabel2Rezervacija;
	}

	public JLabel getErrorLabel3Rezervacija() {
		return errorLabel3Rezervacija;
	}

	public void setErrorLabel3Rezervacija(JLabel errorLabel3Rezervacija) {
		this.errorLabel3Rezervacija = errorLabel3Rezervacija;
	}

	public JComboBox<TipSobe> getTipSobeComboBox() {
		return tipSobeComboBox;
	}

	public void setTipSobeComboBox(JComboBox<TipSobe> tipSobeComboBox) {
		this.tipSobeComboBox = tipSobeComboBox;
	}

	public JList<DodatneUsluge> getDodatneUslugeList() {
		return dodatneUslugeList;
	}

	public void setDodatneUslugeList(JList<DodatneUsluge> dodatneUslugeList) {
		this.dodatneUslugeList = dodatneUslugeList;
	}

	public JFrame getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(JFrame currentFrame) {
		this.currentFrame = currentFrame;
	}

	public JFrame getPreviousFrame() {
		return previousFrame;
	}

	public void setPreviousFrame(JFrame previousFrame) {
		this.previousFrame = previousFrame;
	}

	public JLabel getTotalCenaLabel() {
		return totalCenaLabel;
	}

	public void setTotalCenaLabel(JLabel totalCenaLabel) {
		this.totalCenaLabel = totalCenaLabel;
	}
}