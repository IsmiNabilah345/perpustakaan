package perpustakaan;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Label;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Perpustakaan {

	private JFrame frmWelcome;
	private JFrame frame;

	public JFrame getFrame() {
		return frmWelcome;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Perpustakaan window = new Perpustakaan();
					window.frmWelcome.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Perpustakaan() {
		initialize();
		Connect();
	}

	Connection con;
	PreparedStatement pst;
	private JTextField textName;
	private JTextField textkode;
	private JTextField textName2;
	private JTextField textTanggal;


	public void Connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/perpustakaan", "root", "");
			System.out.println("Koneksi berhasil!");
		} catch (ClassNotFoundException ex) {
			System.out.println("Driver tidak ditemukan: " + ex.getMessage());
		} catch (SQLException ex) {
			System.out.println("Koneksi gagal: " + ex.getMessage());
		}
	}


	private String generateKodeBuku() {
	    if (con == null) {
	        Connect();
	    }

	    try {
	        String tahun = String.valueOf(LocalDate.now().getYear()).substring(2); // "25"

	        pst = con.prepareStatement("SELECT MAX(CAST(SUBSTRING(kode_buku, 4) AS UNSIGNED)) FROM buku WHERE kode_buku LIKE ?");
	        pst.setString(1, "B" + tahun + "%");
	        ResultSet rs = pst.executeQuery();

	        int urutan = 1;
	        if (rs.next() && rs.getString(1) != null) {
	            urutan = rs.getInt(1) + 1;
	        }

	        return String.format("B%s%03d", tahun, urutan);

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return "B" + String.valueOf(LocalDate.now().getYear()).substring(2) + "000";
	}



	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWelcome = new JFrame();
		frmWelcome.getContentPane().setForeground(new Color(255, 255, 255));
		frmWelcome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWelcome.setBounds(100, 100, 860, 629);
		frmWelcome.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(UIManager.getBorder("InternalFrame.border"));
		panel.setBounds(21, 28, 800, 540);
		frmWelcome.getContentPane().add(panel);
		panel.setLayout(null);

		Label label = new Label("Data Buku");
		label.setFont(new Font("Century Gothic", Font.BOLD, 20));
		label.setBounds(347, 30, 135, 39);
		panel.add(label);

		Label label_1 = new Label("Nama Buku      :");
		label_1.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.PLAIN, 14));
		label_1.setBounds(48, 133, 151, 21);
		panel.add(label_1);

		Label label_2 = new Label("Kode Buku       :");
		label_2.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.PLAIN, 14));
		label_2.setBounds(48, 199, 135, 21);
		panel.add(label_2);

		Label label_3 = new Label("Nama Penulis  :");
		label_3.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.PLAIN, 14));
		label_3.setBounds(48, 266, 141, 27);
		panel.add(label_3);

		JButton btnNewButton = new JButton("Kirim");
		btnNewButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        Connect();

		        String nama_buku = textName.getText();
		        String kode_buku = textkode.getText().trim();
		        String nama_penulis = textName2.getText();
		        Date tgl_terbit;

		        if (kode_buku.isEmpty()) {
		            kode_buku = generateKodeBuku();
		            textkode.setText(kode_buku);
		        }

		        try {
		            java.util.Date utilDate = new SimpleDateFormat("dd-MM-yyyy").parse(textTanggal.getText());
		            tgl_terbit = new java.sql.Date(utilDate.getTime());
		        } catch (ParseException a) {
		            JOptionPane.showMessageDialog(null, "Format tanggal salah! Gunakan yyyy-MM-dd", "Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        try {
		            if (con != null) {
		                pst = con.prepareStatement("INSERT INTO buku(kode_buku, nama_buku, nama_penulis, tgl_terbit) VALUES(?,?,?,?)");
		                pst.setString(1, kode_buku);
		                pst.setString(2, nama_buku);
		                pst.setString(3, nama_penulis);
		                pst.setDate(4, tgl_terbit);
		                pst.executeUpdate();

		                JOptionPane.showMessageDialog(null, "Buku berhasil ditambahkan");

		                databuku window = new databuku();
		                window.getFrame().setVisible(true);
		                if (frmWelcome != null) {
		                    frmWelcome.dispose();
		                }

		            } else {
		                JOptionPane.showMessageDialog(null, "Koneksi ke database gagal!", "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        } catch (SQLException el) {
		            el.printStackTrace();
		        }
		    }
		});

		btnNewButton.setBounds(48, 468, 108, 33);
		panel.add(btnNewButton);

		textName = new JTextField();
		textName.setBounds(205, 133, 201, 27);
		panel.add(textName);
		textName.setColumns(10);

		textkode = new JTextField();
		textkode.setColumns(10);
		textkode.setBounds(205, 199, 201, 27);
		panel.add(textkode);
		textkode.setText(String.valueOf(generateKodeBuku())); // Panggil sebelum textkode dibuat!

		textName2 = new JTextField();
		textName2.setColumns(10);
		textName2.setBounds(205, 266, 201, 27);
		panel.add(textName2);

		JButton btnNewButton_1 = new JButton("Kembali");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				databuku window = new databuku();
				window.getFrame().setVisible(true);

				if (frame != null) {
					frame.dispose();
				}
			}
		});
		btnNewButton_1.setBounds(20, 20, 100, 30);
		panel.add(btnNewButton_1);

		Label label_3_1 = new Label("Tanggal Terbit :");
		label_3_1.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.PLAIN, 14));
		label_3_1.setBounds(48, 337, 141, 27);
		panel.add(label_3_1);

		textTanggal = new JTextField();
		textTanggal.setColumns(10);
		textTanggal.setBounds(205, 337, 201, 27);
		panel.add(textTanggal);
	}
}
