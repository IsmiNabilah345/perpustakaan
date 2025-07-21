package perpustakaan;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class login {

	private JFrame frame;
	private JTextField textname;
	private JPasswordField textpw;

	public JFrame getFrame() {
		return frame;
	}

	/**f
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login window = new login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public login() {
		initialize();
	}
	
	Connection con;
	PreparedStatement pst;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(128, 128, 128));
		frame.setBounds(100, 100, 863, 629);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		BackgroundPanel bgPanel = new BackgroundPanel();
		bgPanel.setLayout(null);
		frame.setContentPane(bgPanel);

		JLabel lblNewLabel = new JLabel("Halo min!");
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		lblNewLabel.setBounds(340, 64, 170, 61);
		frame.getContentPane().add(lblNewLabel);

		textname = new JTextField();
		textname.setBounds(303, 225, 246, 46);
		frame.getContentPane().add(textname);
		textname.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Username :");
		lblNewLabel_1.setForeground(new Color(0, 0, 0));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(371, 189, 111, 26);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Password :");
		lblNewLabel_1_1.setForeground(new Color(0, 0, 0));
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_1.setBounds(382, 315, 98, 26);
		frame.getContentPane().add(lblNewLabel_1_1);

		textpw = new JPasswordField();
		textpw = new JPasswordField();
		textpw.setBounds(303, 351, 246, 46);
		frame.getContentPane().add(textpw);
		textpw.setColumns(10);
		textpw.setBounds(303, 351, 246, 46);
		frame.getContentPane().add(textpw);

		JButton btnNewButton = new JButton("Masuk");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = textname.getText();
				String password = textpw.getText();

				try {
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/perpustakaan", "root", ""); 
					PreparedStatement pst = con.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?");
					pst.setString(1, username);
					pst.setString(2, password); 

					ResultSet rs = pst.executeQuery();

					if (rs.next()) {
						dashboard window = new dashboard();
						window.getFrame().setVisible(true);
						frame.dispose(); 
					} else {
						JOptionPane.showMessageDialog(null, "Username atau Password salah!", "Error", JOptionPane.ERROR_MESSAGE);
					}

				} catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Koneksi ke database gagal!", "Error", JOptionPane.ERROR_MESSAGE);
					}

			}
		});
		btnNewButton.setBounds(329, 458, 193, 46);
		frame.getContentPane().add(btnNewButton);

		JButton btnNewButton_1 = new JButton("<kembali");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pengunjung window = new pengunjung();
				window.getFrame().setVisible(true);
				
				if (frame != null) {
					frame.dispose();
				}
			}
		});
		btnNewButton_1.setBounds(10, 10, 116, 37);
		frame.getContentPane().add(btnNewButton_1);
	}
}
