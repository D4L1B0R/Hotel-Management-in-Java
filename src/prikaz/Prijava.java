package prikaz;

import kontroler.StranicaPrijave;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class Prijava extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private Runnable onClose;

    public Prijava(Runnable onClose) {
        this.setOnClose(onClose);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (onClose != null) {
                    onClose.run();
                }
            }
        });
        setTitle("Stranica Prijave");
        setSize(500, 500);

        contentPane = new JPanel(new GridBagLayout()) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Create a subtle striped pattern
                Graphics2D g2d = (Graphics2D) g;
                Color lightGray = new Color(230, 230, 230);
                Color white = Color.WHITE;
                for (int i = 0; i < getHeight(); i += 40) {
                    g2d.setColor(i % 80 == 0 ? lightGray : white);
                    g2d.fillRect(0, i, getWidth(), 40);
                }
            }
        };
        setContentPane(contentPane);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Hotel San Label
        JLabel lblHotelSan = new JLabel("Hotel San");
        lblHotelSan.setFont(new Font("Tahoma", Font.BOLD, 36));
        contentPane.add(lblHotelSan, gbc);

        // Username Label and Text Field
        JLabel lblUsername = new JLabel("Korisničko ime:");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
        contentPane.add(lblUsername, gbc);

        textField = new JTextField(20);
        contentPane.add(textField, gbc);

        // Password Label and Password Field
        JLabel lblPassword = new JLabel("Lozinka:");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
        contentPane.add(lblPassword, gbc);

        passwordField = new JPasswordField(20);
        contentPane.add(passwordField, gbc);
        
        // Message Label
        messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);
        contentPane.add(messageLabel, gbc);

        StranicaPrijave stranicaPrijave = new StranicaPrijave();

        // Login Button
        JButton btnLogin = new JButton("Prijava");
        btnLogin.addActionListener(stranicaPrijave.getLoginActionListener(textField, passwordField, messageLabel, this));
        contentPane.add(btnLogin, gbc);

        
        setLocationRelativeTo(null); // Center the frame on the screen
    }

	public void resetFormFields() {
		textField.setText("");
		passwordField.setText("");
	}

	public Runnable getOnClose() {
		return onClose;
	}

	public void setOnClose(Runnable onClose) {
		this.onClose = onClose;
	}
}