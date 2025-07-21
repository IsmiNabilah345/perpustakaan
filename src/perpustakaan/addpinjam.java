package perpustakaan;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class addpinjam {

	public JFrame getFrame() {
		return frame;
	}

	private JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					addpinjam window = new addpinjam();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public addpinjam() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 858, 628);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Form Peminjaman Buku");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		lblNewLabel.setBounds(224, 57, 426, 49);
		frame.getContentPane().add(lblNewLabel);

		JButton btnNewButton_1 = new JButton("<kembali");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				datapinjam window = new datapinjam();
				window.getFrame().setVisible(true);
				frame.dispose();
			}
		});
		btnNewButton_1.setBounds(10, 10, 103, 34);
		frame.getContentPane().add(btnNewButton_1);

		JLabel lblPilihMember = new JLabel("Pilih Member:");
		lblPilihMember.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPilihMember.setBounds(48, 202, 143, 25);
		frame.getContentPane().add(lblPilihMember);

		JComboBox<relasimember> comboMember = new JComboBox<>();
		comboMember.setBounds(201, 202, 250, 25);
		frame.getContentPane().add(comboMember);
		loadMemberData(comboMember);

		JLabel lblPilihBuku = new JLabel("Pilih Buku:");
		lblPilihBuku.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPilihBuku.setBounds(48, 250, 143, 25);
		frame.getContentPane().add(lblPilihBuku);

		JComboBox<relasibuku> comboBuku = new JComboBox<>();
		comboBuku.setBounds(201, 250, 250, 25);
		frame.getContentPane().add(comboBuku);
		loadBukuData(comboBuku);

		JButton btnSimpan = new JButton("Simpan");
		btnSimpan.setBounds(201, 300, 100, 30);
		frame.getContentPane().add(btnSimpan);

		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				relasimember selectedMember = (relasimember) comboMember.getSelectedItem();
				relasibuku selectedBuku = (relasibuku) comboBuku.getSelectedItem();

				if (selectedMember == null || selectedBuku == null) {
					JOptionPane.showMessageDialog(frame, "Pilih member dan buku terlebih dahulu!");
					return;
				}

				try {
					Connection con = dbConnect.getConnection();

					PreparedStatement cek = con.prepareStatement(
							"SELECT * FROM pinjam WHERE id_buku = ? AND status = 'Belum Dikembalikan'");
					cek.setInt(1, selectedBuku.getId());
					ResultSet rs = cek.executeQuery();

					if (rs.next()) {
						JOptionPane.showMessageDialog(frame, "❌ Buku ini sedang dipinjam dan belum dikembalikan!");
						return;
					}

					PreparedStatement pst = con.prepareStatement(
							"INSERT INTO pinjam (id_member, id_buku, nama_member, kode_member, nama_buku, status, tgl_pinjam) VALUES (?, ?, ?, ?, ?, ?, ?)");
					pst.setInt(1, selectedMember.getId());
					pst.setInt(2, selectedBuku.getId());
					pst.setString(3, selectedMember.getNama());
					pst.setString(4, selectedMember.getKode());
					pst.setString(5, selectedBuku.getNama_buku());
					pst.setString(6, "Belum Dikembalikan");
					java.sql.Date tanggalPinjam = java.sql.Date.valueOf(java.time.LocalDate.now());
					pst.setDate(7, tanggalPinjam);

					pst.executeUpdate();
					JOptionPane.showMessageDialog(frame, "✅ Data peminjaman berhasil disimpan!");

					datapinjam window = new datapinjam();
					window.getFrame().setVisible(true);
					frame.dispose();

				} catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(frame, "❌ Gagal menyimpan data!");
				}
			}
		});
	}

	private void loadMemberData(JComboBox<relasimember> combo) {
		try {
			Connection con = dbConnect.getConnection();
			PreparedStatement pst = con
					.prepareStatement("SELECT id_member, nama_member, kode_member FROM member");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				relasimember m = new relasimember(
						rs.getInt("id_member"),
						rs.getString("nama_member"),
						rs.getString("kode_member"));
				combo.addItem(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadBukuData(JComboBox<relasibuku> combo) {
		try {
			Connection con = dbConnect.getConnection();
			PreparedStatement pst = con.prepareStatement("SELECT id_buku, nama_buku FROM buku");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				relasibuku b = new relasibuku(
						rs.getInt("id_buku"),
						rs.getString("nama_buku"));
				combo.addItem(b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
