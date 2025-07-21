package perpustakaan;

import java.awt.EventQueue;
import perpustakaan.BackgroundPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.awt.event.ActionEvent;

public class pengunjung {

	private JFrame frame;
	private JTextField textname;
	private JTextField textalamat;
	private JTextField textstatus;
	private JTextField textket;

	public JFrame getFrame() {
		return frame;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					pengunjung window = new pengunjung();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public pengunjung() {
		initialize();
	}

	PreparedStatement pst;

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 863, 628);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		BackgroundPanel panel = new BackgroundPanel();
		panel.setBounds(10, 10, 829, 571);	
		panel.setLayout(null);
		frame.getContentPane().add(panel);

		JLabel lblNewLabel = new JLabel("Selamat Datang");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblNewLabel.setBounds(251, 26, 338, 78);
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Daftar Pengunjung Perpustakaan");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(295, 96, 245, 36);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Nama Lengkap :");
		lblNewLabel_2.setForeground(new Color(0, 0, 0));
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_2.setBounds(66, 165, 170, 25);
		panel.add(lblNewLabel_2);

		JLabel lblNewLabel_2_1 = new JLabel("Alamat             :");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_2_1.setBounds(66, 229, 164, 25);
		panel.add(lblNewLabel_2_1);

		JLabel lblNewLabel_2_2 = new JLabel("Status              :");
		lblNewLabel_2_2.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_2_2.setBounds(66, 295, 170, 25);
		panel.add(lblNewLabel_2_2);

		JLabel lblNewLabel_2_3 = new JLabel("Keperluan       :");
		lblNewLabel_2_3.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_2_3.setBounds(66, 358, 157, 25);
		panel.add(lblNewLabel_2_3);

		textname = new JTextField();
		textname.setBounds(244, 165, 234, 25);
		panel.add(textname);
		textname.setColumns(10);

		textalamat = new JTextField();
		textalamat.setColumns(10);
		textalamat.setBounds(244, 229, 234, 25);
		panel.add(textalamat);

		textstatus = new JTextField();
		textstatus.setColumns(10);
		textstatus.setBounds(244, 295, 234, 25);
		panel.add(textstatus);

		textket = new JTextField();
		textket.setColumns(10);
		textket.setBounds(244, 358, 234, 25);
		panel.add(textket);

		JButton btnNewButton = new JButton("Kirim");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Connection con = dbConnect.getConnection();

				String nama_pgj, alamat, status, ket_pgj;
				nama_pgj = textname.getText();
				alamat = textalamat.getText();
				status = textstatus.getText();
				ket_pgj = textket.getText();

				if (nama_pgj.isEmpty() || alamat.isEmpty() || status.isEmpty() || ket_pgj.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}

				try {
					if (con != null) {
						Date tgl_pgj = Date.valueOf(LocalDate.now());

						pst = con.prepareStatement("INSERT INTO pengunjung(nama_pgj, alamat, status, ket_pgj, tgl_pgj) VALUES(?,?,?,?,?)");
						pst.setString(1, nama_pgj);
						pst.setString(2, alamat);
						pst.setString(3, status);
						pst.setString(4, ket_pgj);
						pst.setDate(5, tgl_pgj);

						pst.executeUpdate();

						JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");

						textname.setText("");
						textalamat.setText("");
						textstatus.setText("");
						textket.setText("");
						textname.requestFocus();
					} else {
						JOptionPane.showMessageDialog(null, "Koneksi ke database gagal!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (SQLException el) {
					el.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(66, 476, 135, 46);
		panel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Masuk");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login window = new login();
				window.getFrame().setVisible(true);
				if (frame != null) {
					frame.dispose();
				}
			}
		});
		btnNewButton_1.setBounds(699, 489, 85, 21);
		panel.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("Join Member");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				member window = new member(); 
				window.getFrame().setVisible(true); 
				if (frame != null) {
					frame.dispose();
				}
			}
		});
		btnNewButton_2.setBounds(385, 476, 135, 46);
		panel.add(btnNewButton_2);
	}
}