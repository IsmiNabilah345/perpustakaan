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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class addmember {

	private JFrame frame;
	private JTextField textnama;
	private JTextField textkode;
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
					addmember window = new addmember();
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
	public addmember() {
		initialize();
	}

	private Connection con = dbConnect.getConnection();
	
	private String generateKodeMember() {
	    try {
	        pst = con.prepareStatement("SELECT MAX(kode_member) FROM member");
	        ResultSet rs = pst.executeQuery();

	        int nomorUrut = 1;
	        if (rs.next() && rs.getString(1) != null) {
	            String kodeTerakhir = rs.getString(1);
	            String nomorStr = kodeTerakhir.substring(3);
	            nomorUrut = Integer.parseInt(nomorStr) + 1;
	        }

	        String tahun = String.valueOf(java.time.Year.now().getValue()).substring(2);
	        return "M" + tahun + String.format("%03d", nomorUrut);

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return "M25001";
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

		JLabel lblNewLabel = new JLabel("Data Member Perpustakaan");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblNewLabel.setBounds(155, 43, 551, 57);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Nama Lengkap :");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(48, 163, 151, 41);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Kode Member   :");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_1.setBounds(48, 234, 151, 41);
		frame.getContentPane().add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_2 = new JLabel("Alamat             :");
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

		textalamat = new JTextField();
		textalamat.setColumns(10);
		textalamat.setBounds(237, 398, 179, 34);
		frame.getContentPane().add(textalamat);

		textstatus = new JTextField();
		textstatus.setColumns(10);
		textstatus.setBounds(237, 309, 179, 34);
		frame.getContentPane().add(textstatus);

		JButton btnNewButton = new JButton("Kirim");
		btnNewButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String nama_member = textnama.getText().trim(); 
		        String kode_member = generateKodeMember(); // generate otomatis
		        String alamat = textalamat.getText().trim(); 
		        String status = textstatus.getText().trim(); 

		        if (nama_member.isEmpty() || alamat.isEmpty() || status.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Warning", JOptionPane.WARNING_MESSAGE);
		            return;
		        }

		        try {
		            if (con != null) {
		                pst = con.prepareStatement("INSERT INTO member(nama_member, kode_member, alamat, status) VALUES(?,?,?,?)");
		                pst.setString(1, nama_member);
		                pst.setString(2, kode_member);
		                pst.setString(3, alamat);
		                pst.setString(4, status);
		                pst.executeUpdate();

		                JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan dengan kode: " + kode_member);

		                datamember window = new datamember();
		                window.getFrame().setVisible(true);
		                frame.dispose();
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
				datamember window = new datamember();
				window.getFrame().setVisible(true);
				
				if (frame != null) {
					frame.dispose();
				}
			}
		});
		btnNewButton_1.setBounds(10, 10, 103, 34);
		frame.getContentPane().add(btnNewButton_1);
		
		JLabel lblNewLabel_1_2_1 = new JLabel("Status              :");
		lblNewLabel_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_2_1.setBounds(48, 390, 151, 41);
		frame.getContentPane().add(lblNewLabel_1_2_1);
		
		textkode = new JTextField();
		textkode.setColumns(10);
		textkode.setBounds(237, 242, 179, 34);
		frame.getContentPane().add(textkode);
		
		textkode.setText(generateKodeMember());
	}
}
