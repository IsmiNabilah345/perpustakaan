package perpustakaan;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class dashboard {

	private JFrame frame;
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
					dashboard window = new dashboard();
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
	public dashboard() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 855, 627);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		BackgroundPanel bgPanel = new BackgroundPanel();
		bgPanel.setLayout(null);
		frame.setContentPane(bgPanel);

		JLabel lblNewLabel = new JLabel("Data Buku, Pengunjung dan Users");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		lblNewLabel.setBounds(123, 55, 607, 70);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Buku");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(311, 171, 44, 25);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Pengunjung");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_1.setBounds(249, 236, 106, 25);
		frame.getContentPane().add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_2 = new JLabel("Users");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_2.setBounds(299, 303, 56, 25);
		frame.getContentPane().add(lblNewLabel_1_2);

		JButton btnNewButton = new JButton("Lihat Data Buku");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				databuku window = new databuku(); 
				window.getFrame().setVisible(true);

				if (frame != null) {
					frame.dispose();
				}
			}
		});
		btnNewButton.setBounds(365, 172, 189, 31);
		frame.getContentPane().add(btnNewButton);

		JButton btnLihatDataPengunjung = new JButton("Lihat Data Pengunjung");
		btnLihatDataPengunjung.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				datapengunjung window = new datapengunjung(); 
				window.getFrame().setVisible(true);

				if (frame != null) {
					frame.dispose();
				}
			}
		});
		btnLihatDataPengunjung.setBounds(365, 237, 189, 31);
		frame.getContentPane().add(btnLihatDataPengunjung);

		JButton btnLihatData = new JButton("Lihat Data Users");
		btnLihatData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				datausers window = new datausers(); 
				window.getFrame().setVisible(true);

				if (frame != null) {
					frame.dispose();
				}
			}
		});
		btnLihatData.setBounds(365, 304, 189, 31);
		frame.getContentPane().add(btnLihatData);

		JButton btnNewButton_1 = new JButton("Logout");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(
						null,
						"Yakin ingin logout?",
						"Konfirmasi Logout",
						JOptionPane.YES_NO_OPTION
						);

				if (confirm == JOptionPane.YES_OPTION) {
					frame.dispose();
					login window = new login(); 
					window.getFrame().setVisible(true);
				}
			}
		});
		btnNewButton_1.setBounds(746, 10, 85, 21);
		frame.getContentPane().add(btnNewButton_1);
		
		JLabel lblNewLabel_1_2_1 = new JLabel("Member");
		lblNewLabel_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_2_1.setBounds(278, 370, 77, 25);
		frame.getContentPane().add(lblNewLabel_1_2_1);
		
		JButton btnLihatDataMember = new JButton("Lihat Data Member");
		btnLihatDataMember.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				datamember window = new datamember(); 
				window.getFrame().setVisible(true);

				if (frame != null) {
					frame.dispose();
				}
			}
		});
		btnLihatDataMember.setBounds(365, 371, 189, 31);
		frame.getContentPane().add(btnLihatDataMember);
		
		JLabel lblNewLabel_1_2_1_1 = new JLabel("Peminjaman");
		lblNewLabel_1_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_2_1_1.setBounds(240, 436, 115, 25);
		bgPanel.add(lblNewLabel_1_2_1_1);
		
		JButton btnLihatDataPeminjaman = new JButton("Lihat Data Peminjaman");
		btnLihatDataPeminjaman.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				datapinjam window = new datapinjam(); 
				window.getFrame().setVisible(true);

				if (frame != null) {
					frame.dispose();
				}
			}
		});
		btnLihatDataPeminjaman.setBounds(365, 437, 189, 31);
		bgPanel.add(btnLihatDataPeminjaman);

	}
}
