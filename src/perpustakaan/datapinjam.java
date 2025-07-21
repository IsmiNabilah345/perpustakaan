package perpustakaan;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.*;
import javax.swing.table.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;

public class datapinjam {

	public JFrame getFrame() {
		return frame;
	}

	private JFrame frame;
	private JTable table;
	private DefaultTableModel model;
	private Connection con;
	private PreparedStatement pst;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					datapinjam window = new datapinjam();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public datapinjam() {
		initialize();
		connectDB();
		loadData();
	}

	private void initialize() {
		frame = new JFrame("Data Peminjaman");
		frame.setBounds(100, 100, 857, 629);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel titleLabel = new JLabel("Data Peminjaman Perpustakaan");
		titleLabel.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 24));
		titleLabel.setBounds(158, 20, 400, 30);
		frame.getContentPane().add(titleLabel);

		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(583, 20, 120, 30);
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addpinjam window = new addpinjam();  
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

		model = new DefaultTableModel();
		table = new JTable(model);
		model.setColumnIdentifiers(new Object[]{"ID", "Nama Member", "Kode Member", "Nama Buku", "Status", "Tgl Pinjam", "Tgl Kembali", "Edit", "Delete"});

		table.getColumn("Edit").setCellRenderer(new ButtonRenderer());
		table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));

		table.getColumn("Delete").setCellRenderer(new ButtonRenderer());
		table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete"));

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 70, 800, 500);
		frame.getContentPane().add(scrollPane);

		JButton btnCetakReport = new JButton("Cetak Report");
		btnCetakReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cetakPDF();
			}
		});
		btnCetakReport.setBounds(713, 20, 120, 30);
		frame.getContentPane().add(btnCetakReport);
	}

	private void cetakPDF() {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Laporan_Peminjaman.pdf"));
			document.open();

			PdfContentByte canvas = writer.getDirectContentUnder();
			Font watermarkFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 60, Font.NORMAL, new BaseColor(200, 200, 200, 50));
			Phrase watermark = new Phrase("Perpustakaan Cimahi", watermarkFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, 300, 400, 45);

			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
			Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

			Paragraph title = new Paragraph("LAPORAN PEMINJAMAN BUKU\n", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID")));
			Paragraph tanggalCetak = new Paragraph("Dicetak pada: " + todayStr + "\n\n", bodyFont);
			tanggalCetak.setAlignment(Element.ALIGN_RIGHT);
			document.add(tanggalCetak);

			LineSeparator garis = new LineSeparator();
			document.add(garis);
			document.add(new Paragraph("\n"));

			Paragraph spacer = new Paragraph();
			spacer.setSpacingBefore(20f);
			document.add(spacer);

			PdfPTable table = new PdfPTable(7);
			table.setWidthPercentage(100);
			table.setWidths(new float[]{1f, 3f, 2.5f, 3f, 2.5f, 2.5f, 2.5f});

			String[] headers = {"No", "Nama Member", "Kode Member", "Judul Buku", "Status", "Tgl Pinjam", "Tgl Kembali"};
			for (String h : headers) {
				PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
				cell.setBackgroundColor(new BaseColor(240, 240, 240));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(5);
				table.addCell(cell);
			}

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/perpustakaan", "root", "");
			PreparedStatement pst = con.prepareStatement(
					"SELECT m.nama_member, m.kode_member, b.nama_buku, p.status, p.tgl_pinjam, p.tgl_kembali " +
							"FROM pinjam p " +
							"JOIN member m ON p.id_member = m.id_member " +
							"JOIN buku b ON p.id_buku = b.id_buku"
					);
			ResultSet rs = pst.executeQuery();

			int no = 1;
			while (rs.next()) {
				table.addCell(new Phrase(String.valueOf(no), bodyFont)); 
				table.addCell(new Phrase(rs.getString("nama_member"), bodyFont));
				table.addCell(new Phrase(rs.getString("kode_member"), bodyFont));
				table.addCell(new Phrase(rs.getString("nama_buku"), bodyFont));
				table.addCell(new Phrase(rs.getString("status"), bodyFont));

				LocalDate tglPinjam = rs.getDate("tgl_pinjam") != null ? rs.getDate("tgl_pinjam").toLocalDate() : null;
				String pinjamStr = tglPinjam != null ? tglPinjam.format(formatter) : "-";
				table.addCell(new Phrase(pinjamStr, bodyFont));

				LocalDate tglKembali = rs.getDate("tgl_kembali") != null ? rs.getDate("tgl_kembali").toLocalDate() : null;
				String kembaliStr = tglKembali != null ? tglKembali.format(formatter) : "-";
				table.addCell(new Phrase(kembaliStr, bodyFont));
				no++;
			}

			document.add(table);
			document.close();

			JOptionPane.showMessageDialog(null, "📄 Laporan berhasil dicetak ke Laporan_Peminjaman.pdf");

			File pdfFile = new File("Laporan_Peminjaman.pdf");
			if (Desktop.isDesktopSupported() && pdfFile.exists()) {
				Desktop.getDesktop().open(pdfFile);
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "❌ Gagal mencetak laporan: " + e.getMessage());
		}
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
			pst = con.prepareStatement(
					"SELECT p.id_pinjam, m.nama_member, m.kode_member, b.nama_buku, p.status, p.tgl_pinjam, p.tgl_kembali " +
							"FROM pinjam p " +
							"JOIN member m ON p.id_member = m.id_member " +
							"JOIN buku b ON p.id_buku = b.id_buku"
					);
			ResultSet rs = pst.executeQuery();

			model.setRowCount(0); 

			while (rs.next()) {
				model.addRow(new Object[]{
						rs.getInt("id_pinjam"),
						rs.getString("nama_member"),
						rs.getString("kode_member"),
						rs.getString("nama_buku"),
						rs.getString("status"),
						rs.getString("tgl_pinjam"),
						rs.getString("tgl_kembali"),
						"Edit",
						"Delete"
				});
			}

			TableColumnModel tcm = table.getColumnModel();
			tcm.getColumn(0).setMinWidth(0);
			tcm.getColumn(0).setMaxWidth(0);
			tcm.getColumn(0).setWidth(0);

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
				int id_pinjam = Integer.parseInt(table.getValueAt(row, 0).toString());
				String nama_member = table.getValueAt(row, 1).toString();
				String kode_member = table.getValueAt(row, 2).toString();
				String nama_buku = table.getValueAt(row, 3).toString();
				String status_lama = table.getValueAt(row, 4).toString();

				String[] pilihan = {"Belum Dikembalikan", "Sudah Dikembalikan"};
				String status_baru = (String) JOptionPane.showInputDialog(
						null,
						"Ubah Status Peminjaman:",
						"Edit Status",
						JOptionPane.QUESTION_MESSAGE,
						null,
						pilihan,
						status_lama
						);

				if (status_baru == null || status_baru.equals(status_lama)) {
					return; 
				}

				PreparedStatement update;
				if (status_baru.equals("Sudah Dikembalikan")) {
					update = con.prepareStatement("UPDATE pinjam SET status = ?, tgl_kembali = ? WHERE id_pinjam = ?");
					update.setString(1, status_baru);
					update.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
					update.setInt(3, id_pinjam);
				} else {
					update = con.prepareStatement("UPDATE pinjam SET status = ? WHERE id_pinjam = ?");
					update.setString(1, status_baru);
					update.setInt(2, id_pinjam);
				}
				update.executeUpdate();

				JOptionPane.showMessageDialog(null, "✅ Status berhasil diupdate!");
				model.setRowCount(0);
				loadData();

			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "❌ Gagal update data.");
			}
		}


		private void deleteData(int row) {
			try {
				int id_pinjam = Integer.parseInt(table.getValueAt(row, 0).toString());
				String nama = table.getValueAt(row, 1).toString();

				int confirm = JOptionPane.showConfirmDialog(
						null,
						"Yakin ingin menghapus data pinjaman \"" + nama + "\"?",
						"Konfirmasi Hapus",
						JOptionPane.YES_NO_OPTION
						);
				if (confirm != JOptionPane.YES_OPTION) return;

				PreparedStatement delete = con.prepareStatement("DELETE FROM pinjam WHERE id_pinjam = ?");
				delete.setInt(1, id_pinjam);
				delete.executeUpdate();

				JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
				model.setRowCount(0);
				loadData();

			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus data.");
			}
		}
	}
}
