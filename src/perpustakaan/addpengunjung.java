package perpustakaan;

import java.awt.EventQueue;

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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class addpengunjung {

	private JFrame frame;
	private JTextField textname;
	private JTextField textalamat;
	private JTextField textstatus;
	private JTextField textket;
	
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
					addpengunjung window = new addpengunjung();
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
	public addpengunjung() {
		initialize();
		Connect();
	}

	Connection con;
	PreparedStatement pst;
	
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
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 863, 628);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(192, 192, 192));
		panel.setBounds(10, 10, 829, 571);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Tambah Data Pengunjung");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblNewLabel.setBounds(279, 35, 312, 78);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_2 = new JLabel("Nama Lengkap :");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_2.setBounds(66, 165, 157, 25);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("Alamat             :");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_2_1.setBounds(66, 229, 151, 25);
		panel.add(lblNewLabel_2_1);
		
		JLabel lblNewLabel_2_2 = new JLabel("Status              :");
		lblNewLabel_2_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_2_2.setBounds(66, 295, 151, 25);
		panel.add(lblNewLabel_2_2);
		
		JLabel lblNewLabel_2_3 = new JLabel("Keperluan        :");
		lblNewLabel_2_3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_2_3.setBounds(66, 358, 157, 25);
		panel.add(lblNewLabel_2_3);
		
		textname = new JTextField();
		textname.setBounds(233, 165, 234, 25);
		panel.add(textname);
		textname.setColumns(10);
		
		textalamat = new JTextField();
		textalamat.setColumns(10);
		textalamat.setBounds(233, 229, 234, 25);
		panel.add(textalamat);
		
		textstatus = new JTextField();
		textstatus.setColumns(10);
		textstatus.setBounds(233, 295, 234, 25);
		panel.add(textstatus);
		
		textket = new JTextField();
		textket.setColumns(10);
		textket.setBounds(233, 358, 234, 25);
		panel.add(textket);
		
		JButton btnNewButton_1 = new JButton("Kembali");
		btnNewButton_1.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        datapengunjung window = new datapengunjung();
		        window.getFrame().setVisible(true);

		        if (frame != null) {
		            frame.dispose();
		        }
		    }
		});
		btnNewButton_1.setBounds(20, 20, 100, 30);
		panel.add(btnNewButton_1);
		
		JButton btnNewButton = new JButton("Kirim");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        Connect();
		        
		        String nama_pgj, alamat, status, ket_pgj;

		        nama_pgj = textname.getText();
		        alamat = textalamat.getText();
		        status = textstatus.getText();
		        ket_pgj = textket.getText();
		        
		        try {
		            
		            if (con != null) {
		                pst = con.prepareStatement("INSERT INTO pengunjung(nama_pgj, alamat, status, ket_pgj) VALUES(?,?,?,?)");
		                pst.setString(1, nama_pgj);
		                pst.setString(2, alamat);
		                pst.setString(3, status);
		                pst.setString(4, ket_pgj);
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
		
	}
}
