package kontroler;

import hotel.HotelListePodataka;
import podaci.Zaposleni;
import prikaz.StranicaAdministratora;
import prikaz.StranicaGosta;
import prikaz.StranicaRecepcionera;
import prikaz.StranicaSobarice;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import entiteti.Gost;
import entiteti.Osoblje;

public class StranicaPrijave {


public ActionListener getLoginActionListener(JTextField textField, JPasswordField passwordField, JLabel messageLabel, JFrame currentFrame) {
    return new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String enteredUsername = textField.getText();
            String enteredPassword = new String(passwordField.getPassword());

            for (Osoblje osoblje : HotelListePodataka.getInstance().getListaZaposlenih()) {
                if (osoblje.getKorisnickoIme().equals(enteredUsername) && osoblje.getLozinka().equals(enteredPassword)) {
                    System.out.println("Pronadjen " + osoblje.getTip().toString().toLowerCase() + ": " + osoblje.getIme() + " " + osoblje.getPrezime());
                    messageLabel.setText(""); // Clear the message label
                    if (osoblje.getTip().equals(Zaposleni.ADMINISTRATOR)) {
                        currentFrame.setVisible(false); // Hide the current window
                        JFrame adminFrame = new JFrame(); // Create a new frame
                        StranicaAdministratora stranicaAdministratora = new StranicaAdministratora(osoblje, adminFrame, currentFrame);
                        adminFrame.setContentPane(stranicaAdministratora.getTabbedPane()); // Set the content pane
                        adminFrame.pack();
                        adminFrame.setVisible(true); // Show the new frame
                    } else if (osoblje.getTip().equals(Zaposleni.RECEPCIONER)){
                    	currentFrame.setVisible(false); // Hide the current window
                        JFrame recepcionerFrame = new JFrame(); // Create a new frame
                        StranicaRecepcionera stranicaRecepcionera = new StranicaRecepcionera(osoblje, recepcionerFrame, currentFrame);
                        recepcionerFrame.setContentPane(stranicaRecepcionera.getTabbedPane()); // Set the content pane
                        recepcionerFrame.pack();
                        recepcionerFrame.setVisible(true);
					} else if (osoblje.getTip().equals(Zaposleni.SOBARICA)) {
						currentFrame.setVisible(false); // Hide the current window
                        JFrame sobaricaFrame = new JFrame(); // Create a new frame
                        StranicaSobarice stranicaSobarice = new StranicaSobarice(osoblje, sobaricaFrame, currentFrame);
                        sobaricaFrame.setContentPane(stranicaSobarice.getTabbedPane()); // Set the content pane
                        sobaricaFrame.pack();
                        sobaricaFrame.setVisible(true);
					}
                    return;
                }
            }

            for (Gost gost : HotelListePodataka.getInstance().getListaGostiju()) {
                if (gost.getKorisnickoIme().equals(enteredUsername) && gost.getLozinka().equals(enteredPassword)) {
                    System.out.println("Pronadjen gost: " + gost.getIme() + " " + gost.getPrezime());
                    messageLabel.setText(""); // Clear the message label
                    currentFrame.setVisible(false); // Hide the current window
                    JFrame gostFrame = new JFrame(); // Create a new frame
                    StranicaGosta stranicaGosta = new StranicaGosta(gost, gostFrame, currentFrame);
                    gostFrame.setContentPane(stranicaGosta.getTabbedPane()); // Set the content pane
                    gostFrame.pack();
                    gostFrame.setVisible(true);
                    return;
                }
            }
            // If no user is found, update the message label
            messageLabel.setText("Korisnik nije pronadjen!");
        }
    };
}

}
