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
import java.time.Year;
import java.time.LocalDate;

public class member {

	private JFrame frame;
	private JTextField textnama;
	private JTextField textalamat;
	private JTextField textstatus;

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
					member window = new member();
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
	public member() {
		initialize();
	}

	PreparedStatement pst;

	private String generateKodeMember(Connection con) {
	    try {
	        String tahun = String.valueOf(LocalDate.now().getYear()).substring(2);
	        PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM member WHERE kode_member LIKE ?");
	        pst.setString(1, "M" + tahun + "%");
	        java.sql.ResultSet rs = pst.executeQuery();
	        int urutan = 1;
	        if (rs.next()) {
	            urutan = rs.getInt(1) + 1;
	        }
	        return String.format("M%s%03d", tahun, urutan);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "M" + String.valueOf(LocalDate.now().getYear()).substring(2) + "000";
	    }
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 852, 628);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		BackgroundPanel bgPanel = new BackgroundPanel();
		bgPanel.setLayout(null);
		frame.setContentPane(bgPanel);

		JLabel lblNewLabel = new JLabel("Form Pendaftaran Member Perpustakaan");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblNewLabel.setBounds(116, 64, 629, 57);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Nama Lengkap :");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_1.setBounds(105, 189, 179, 41);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Alamat             :");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_1_1.setBounds(105, 265, 179, 41);
		frame.getContentPane().add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_2 = new JLabel("Status              :");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_1_2.setBounds(105, 346, 179, 41);
		frame.getContentPane().add(lblNewLabel_1_2);

		JLabel lblNewLabel_1_3 = new JLabel("");
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_3.setBounds(48, 390, 139, 41);
		frame.getContentPane().add(lblNewLabel_1_3);

		textnama = new JTextField();
		textnama.setBounds(298, 197, 179, 34);
		frame.getContentPane().add(textnama);
		textnama.setColumns(10);

		textalamat = new JTextField();
		textalamat.setColumns(10);
		textalamat.setBounds(298, 273, 179, 34);
		frame.getContentPane().add(textalamat);

		textstatus = new JTextField();
		textstatus.setColumns(10);
		textstatus.setBounds(298, 354, 179, 34);
		frame.getContentPane().add(textstatus);

		JButton btnNewButton = new JButton("Kirim");
		btnNewButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        Connection con = dbConnect.getConnection();

		        String nama_member = textnama.getText().trim(); 
		        String alamat = textalamat.getText().trim(); 
		        String status = textstatus.getText().trim(); 

		        if (nama_member.isEmpty() || alamat.isEmpty() || status.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Warning", JOptionPane.WARNING_MESSAGE);
		            return;
		        }

		        try {
		            if (con != null) {
		                String kode_member = generateKodeMember(con); // 💥 kode otomatis

		                pst = con.prepareStatement("INSERT INTO member(nama_member, alamat, status, kode_member) VALUES(?,?,?,?)");
		                pst.setString(1, nama_member);
		                pst.setString(2, alamat);
		                pst.setString(3, status);
		                pst.setString(4, kode_member); // ⬅ SIMPAN KODE

		                pst.executeUpdate();

		                JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan dengan kode: " + kode_member);

		                textnama.setText("");
		                textalamat.setText("");
		                textstatus.setText("");
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
				pengunjung window = new pengunjung();
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
