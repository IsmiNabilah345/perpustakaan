package perpustakaan;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class databuku {

	public JFrame getFrame() {
		return frame;
	}

	private JFrame frame;
	private JTable table;
	private DefaultTableModel model;
	private Connection con;
	private PreparedStatement pst;
	private JLabel label;
	private JLabel label_1;
	private final JLabel label_2 = new JLabel("New label");

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					databuku window = new databuku();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public databuku() {
		initialize();
		connectDB();
		loadData();
	}

	private void initialize() {
		frame = new JFrame("Data Buku");
		frame.setBounds(100, 100, 857, 629);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Tabel
		model = new DefaultTableModel();
		table = new JTable(model);
		model.setColumnIdentifiers(new Object[]{"Kode Buku", "Nama Buku", "Nama Penulis", "Tgl Terbit", "Edit", "Delete"});

		// Tambahkan tombol Edit & Delete di setiap baris
		table.getColumn("Edit").setCellRenderer(new ButtonRenderer());
		table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));

		table.getColumn("Delete").setCellRenderer(new ButtonRenderer());
		table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete"));
		frame.getContentPane().setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane(table);
		scrollPane_1.setBounds(0, 0, 0, 0);
		frame.getContentPane().add(scrollPane_1);

		frame = new JFrame("Data Buku");
		frame.setBounds(100, 100, 857, 629);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel titleLabel = new JLabel("Data Buku di Perpustakaan");
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		titleLabel.setBounds(250, 20, 400, 30);
		frame.getContentPane().add(titleLabel);

		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(700, 20, 120, 30);
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Perpustakaan window = new Perpustakaan();  
				window.getFrame().setVisible(true);
				frame.dispose();
			}
		});
		frame.getContentPane().add(btnCreate);

		JButton btnKembali = new JButton("Kembali");
		btnKembali.setBounds(20, 20, 100, 30);
		btnKembali.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dashboard window = new dashboard(); 
				window.getFrame().setVisible(true);
				frame.dispose();
			}
		});
		frame.getContentPane().add(btnKembali);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 70, 800, 500);
		frame.getContentPane().add(scrollPane);

	}

	private void connectDB() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/perpustakaan", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadData() {
		try {
			pst = con.prepareStatement("SELECT * FROM buku");
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				model.addRow(new Object[]{
						rs.getString("kode_buku"),
						rs.getString("nama_buku"),
						rs.getString("nama_penulis"),
						rs.getDate("tgl_terbit"),
						"Edit",
						"Delete"
				});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	class ButtonRenderer extends JButton implements TableCellRenderer {
		public ButtonRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}

	class ButtonEditor extends DefaultCellEditor {
		private JButton button;
		private String action;

		public ButtonEditor(JCheckBox checkBox, String action) {
			super(checkBox);
			this.action = action;
			button = new JButton(action);
			button.setOpaque(true);

			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int row = table.getSelectedRow();
					if (action.equals("Edit")) {
						editData(row);
					} else {
						deleteData(row);
					}
				}
			});
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			button.setText((value == null) ? "" : value.toString());
			return button;
		}

		private void editData(int row) {
			try {
				Object kodeBukuObj = table.getValueAt(row, 0);
				int kode_buku = Integer.parseInt(kodeBukuObj.toString());

				int id_buku = getIdFromDatabase(kode_buku);
				if (id_buku == 0) {
					JOptionPane.showMessageDialog(null, "Error: ID Buku tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				String nama_buku = JOptionPane.showInputDialog("Masukkan nama baru:", table.getValueAt(row, 1));
				String nama_penulis = JOptionPane.showInputDialog("Masukkan nama penulis baru:", table.getValueAt(row, 2));
				String tgl_terbit = JOptionPane.showInputDialog("Masukkan tanggal terbit baru (yyyy-MM-dd):", table.getValueAt(row, 3));

				if (nama_buku != null && nama_penulis != null && tgl_terbit != null) {
					pst = con.prepareStatement("UPDATE buku SET nama_buku = ?, nama_penulis = ?, tgl_terbit = ? WHERE id_buku = ?");
					pst.setString(1, nama_buku);
					pst.setString(2, nama_penulis);

					if (tgl_terbit.matches("\\d{4}-\\d{2}-\\d{2}")) {
						pst.setDate(3, java.sql.Date.valueOf(tgl_terbit));
					} else {
						JOptionPane.showMessageDialog(null, "Format tanggal salah! Harus yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}

					pst.setInt(4, id_buku);

					int rowsAffected = pst.executeUpdate();
					if (rowsAffected > 0) {
						JOptionPane.showMessageDialog(null, "Data buku berhasil diperbarui!");

						table.setValueAt(nama_buku, row, 1);
						table.setValueAt(nama_penulis, row, 2);
						table.setValueAt(tgl_terbit, row, 3);
					} else {
						JOptionPane.showMessageDialog(null, "Gagal memperbarui data!", "Error", JOptionPane.ERROR_MESSAGE);
					}

					pst.close();
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Error: Kode Buku bukan angka valid!", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		private int getIdFromDatabase(int kode_buku) {
			try {
				pst = con.prepareStatement("SELECT id_buku FROM buku WHERE kode_buku = ?");
				pst.setInt(1, kode_buku);
				ResultSet rs = pst.executeQuery();

				if (rs.next()) {
					return rs.getInt("id_buku");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return 0; 
		}

		private void deleteData(int row) {

			int kode_buku = Integer.parseInt(table.getValueAt(row, 0).toString());
			int id_buku = getIdFromDatabase(kode_buku);

			if (id_buku == 0) {
				JOptionPane.showMessageDialog(null, "Error: ID Buku tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String nama_buku = table.getValueAt(row, 1).toString();

			int confirm = JOptionPane.showConfirmDialog(null,
					"Apakah Anda yakin ingin menghapus buku '" + nama_buku + "'?",
					"Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				try {
					pst = con.prepareStatement("DELETE FROM buku WHERE id_buku = ?");
					pst.setInt(1, id_buku);
					int rowsAffected = pst.executeUpdate();

					if (rowsAffected > 0) {
						JOptionPane.showMessageDialog(null, "Buku berhasil dihapus dari database!");
						model.removeRow(row);
					} else {
						JOptionPane.showMessageDialog(null, "Gagal menghapus buku! ID tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
					}

					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Penghapusan dibatalkan.");
			}
		}


	}
}