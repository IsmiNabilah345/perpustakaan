package perpustakaan;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class adduser {

	private JFrame frame;
	private JTextField textnama;
	private JTextField textusername;
	private JTextField textpw;

	public JFrame getFrame() {
		return frame;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					adduser window = new adduser();
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
	public adduser() {
		initialize();
	}

	PreparedStatement pst;


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 852, 628);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Data Users");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblNewLabel.setBounds(349, 39, 160, 57);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Nama Lengkap :");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(48, 163, 151, 41);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Username        :");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_1.setBounds(48, 234, 151, 41);
		frame.getContentPane().add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_2 = new JLabel("Password         :");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_2.setBounds(48, 309, 151, 41);
		frame.getContentPane().add(lblNewLabel_1_2);

		JLabel lblNewLabel_1_3 = new JLabel("");
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_3.setBounds(48, 390, 139, 41);
		frame.getContentPane().add(lblNewLabel_1_3);

		textnama = new JTextField();
		textnama.setBounds(237, 171, 179, 34);
		frame.getContentPane().add(textnama);
		textnama.setColumns(10);

		textusername = new JTextField();
		textusername.setColumns(10);
		textusername.setBounds(237, 234, 179, 34);
		frame.getContentPane().add(textusername);

		textpw = new JTextField();
		textpw.setColumns(10);
		textpw.setBounds(237, 309, 179, 34);
		frame.getContentPane().add(textpw);

		JButton btnNewButton = new JButton("Kirim");
		btnNewButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        Connection con = dbConnect.getConnection();

		        String nama_user = textnama.getText().trim(); 
		        String username = textusername.getText().trim(); 
		        String password = textpw.getText().trim(); 
		        
		        if (nama_user.isEmpty() || username.isEmpty() || password.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Warning", JOptionPane.WARNING_MESSAGE);
		            return;
		        }

		        try {
		            if (con != null) {
		                pst = con.prepareStatement("INSERT INTO user(nama_user, username, password) VALUES(?,?,?)");
		                pst.setString(1, nama_user);
		                pst.setString(2, username);
		                pst.setString(3, password);
		                pst.executeUpdate();

		                JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");

		                textnama.setText("");
		                textusername.setText("");
		                textpw.setText("");
		            } else {
		                JOptionPane.showMessageDialog(null, "Koneksi ke database gagal!", "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        } catch (SQLException el) {
		            el.printStackTrace();
		        }
		    }
		});
		btnNewButton.setBounds(48, 498, 151, 41);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("<kembali");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				datausers window = new datausers();
				window.getFrame().setVisible(true);
				
				if (frame != null) {
					frame.dispose();
				}
			}
		});
		btnNewButton_1.setBounds(10, 10, 103, 34);
		frame.getContentPane().add(btnNewButton_1);
	}
}
