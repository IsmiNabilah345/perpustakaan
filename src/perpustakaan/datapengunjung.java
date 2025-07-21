package perpustakaan;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileOutputStream;
import com.itextpdf.text.Font;

import java.awt.Desktop;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class datapengunjung {

	public JFrame getFrame() {
		return frame_1;
	}

	private JFrame frame;
	private JFrame frame_1;
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
					datapengunjung window = new datapengunjung();
					window.frame_1.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public datapengunjung() {
		initialize();
		connectDB();
		loadData();
	}

	private void initialize() {
		frame = new JFrame("Data Pengunjung");
		frame.setBounds(100, 100, 857, 629);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		model = new DefaultTableModel();
		table = new JTable(model);
		model.setColumnIdentifiers(new Object[]{"ID", "Nama Pengunjung", "Alamat", "Status", "Keterangan Pengunjung", "Edit", "Delete"});

		table.getColumn("Edit").setCellRenderer(new ButtonRenderer());
		table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));

		table.getColumn("Delete").setCellRenderer(new ButtonRenderer());
		table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete"));
		frame.getContentPane().setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane(table);
		scrollPane_1.setBounds(0, 0, 0, 0);
		frame.getContentPane().add(scrollPane_1);

		frame_1 = new JFrame("Data Pengunjung");
		frame_1.setBounds(100, 100, 857, 629);
		frame_1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame_1.getContentPane().setLayout(null);

		JLabel titleLabel = new JLabel("Data Pengunjung Perpustakaan");
		titleLabel.setFont(new java.awt.Font("Tahoma", Font.BOLD, 24));
		titleLabel.setBounds(147, 20, 400, 30);
		frame_1.getContentPane().add(titleLabel);

		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(557, 20, 120, 30);
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addpengunjung window = new addpengunjung();  
				window.getFrame().setVisible(true);
				frame.dispose();
			}
		});
		frame_1.getContentPane().add(btnCreate);

		JButton btnKembali = new JButton("Kembali");
		btnKembali.setBounds(20, 20, 100, 30);
		btnKembali.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dashboard window = new dashboard(); 
				window.getFrame().setVisible(true);
				frame.dispose();
			}
		});
		frame_1.getContentPane().add(btnKembali);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 70, 800, 500);
		frame_1.getContentPane().add(scrollPane);

		JButton btnCekReport = new JButton("Cek Report");
		btnCekReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cetakPDF();
			}
		});
		btnCekReport.setBounds(700, 20, 120, 30);
		frame_1.getContentPane().add(btnCekReport);

	}

	private void cetakPDF() {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Laporan_Pengunjung.pdf"));
			document.open();

			PdfContentByte canvas = writer.getDirectContentUnder();
			Font watermarkFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 60, Font.NORMAL, new BaseColor(200, 200, 200, 60)); // abu transparan

			Phrase watermark = new Phrase("Perpustakaan Cimahi", watermarkFont);

			ColumnText.showTextAligned(
					canvas,
					Element.ALIGN_CENTER,
					watermark,
					300, 400,
					45       
					);

			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
			Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

			Paragraph title = new Paragraph("LAPORAN DATA PENGUNJUNG PERPUSTAKAAN\n", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			DateTimeFormatter todayFormat = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
			String todayStr = LocalDate.now().format(todayFormat);
			Paragraph tanggalCetak = new Paragraph("Dicetak pada: " + todayStr + "\n\n", bodyFont);
			tanggalCetak.setAlignment(Element.ALIGN_RIGHT);
			document.add(tanggalCetak);

			LineSeparator garis = new LineSeparator();
			document.add(garis);
			document.add(new Paragraph("\n"));

			Paragraph spacer = new Paragraph();
			spacer.setSpacingBefore(20f);
			document.add(spacer);

			PdfPTable table = new PdfPTable(6);
			table.setWidthPercentage(100);
			table.setWidths(new float[]{2f, 2f, 3f, 2f, 3f, 2f});

			String[] headers = {"No", "Nama", "Alamat", "Status", "Keperluan", "Tanggal Kunjungan"};
			for (String h : headers) {
				PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(new BaseColor(240, 240, 240));
				cell.setPadding(5);
				table.addCell(cell);
			}

			Connection con = dbConnect.getConnection();
			PreparedStatement pst = con.prepareStatement("SELECT nama_pgj, alamat, status, ket_pgj, tgl_pgj FROM pengunjung");
			ResultSet rs = pst.executeQuery();

			int no = 1;
			while (rs.next()) {
				String rawDate = rs.getString("tgl_pgj");
				LocalDate date = LocalDate.parse(rawDate);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));

				table.addCell(new Phrase(String.valueOf(no), bodyFont)); 
				table.addCell(new Phrase(rs.getString("nama_pgj"), bodyFont));
				table.addCell(new Phrase(rs.getString("alamat"), bodyFont));
				table.addCell(new Phrase(rs.getString("status"), bodyFont));
				table.addCell(new Phrase(rs.getString("ket_pgj"), bodyFont));
				table.addCell(new Phrase(date.format(formatter), bodyFont));
				no++;
			}

			document.add(table);
			document.close();

			JOptionPane.showMessageDialog(null, "Laporan berhasil dicetak ke Laporan_Pengunjung.pdf");
			try {
				if (Desktop.isDesktopSupported()) {
					File pdfFile = new File("Laporan_Pengunjung.pdf");
					if (pdfFile.exists()) {
						Desktop.getDesktop().open(pdfFile);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Gagal membuka file PDF: " + ex.getMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Gagal mencetak laporan: " + e.getMessage());
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
			pst = con.prepareStatement("SELECT * FROM pengunjung");
			ResultSet rs = pst.executeQuery();

			model.setRowCount(0); 

			while (rs.next()) {
				model.addRow(new Object[]{
						rs.getInt("id_pgj"), 
						rs.getString("nama_pgj"),
						rs.getString("alamat"),
						rs.getString("status"),
						rs.getString("ket_pgj"),
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
				int id_pgj = Integer.parseInt(table.getValueAt(row, 0).toString()); 
				String nama = table.getValueAt(row, 1).toString();                  
				String alamat = table.getValueAt(row, 2).toString();            
				String status = table.getValueAt(row, 3).toString();          
				String keterangan = table.getValueAt(row, 4).toString();         

				String newNama = JOptionPane.showInputDialog(null, "Edit Nama", nama);
				String newAlamat = JOptionPane.showInputDialog(null, "Edit Alamat", alamat);
				String newStatus = JOptionPane.showInputDialog(null, "Edit Status", status);
				String newKeterangan = JOptionPane.showInputDialog(null, "Edit Keterangan", keterangan);

				PreparedStatement update = con.prepareStatement(
						"UPDATE pengunjung SET nama_pgj = ?, alamat = ?, status = ?, ket_pgj = ? WHERE id_pgj = ?"
						);
				update.setString(1, newNama);
				update.setString(2, newAlamat);
				update.setString(3, newStatus);
				update.setString(4, newKeterangan);
				update.setInt(5, id_pgj);
				update.executeUpdate();

				JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");

				model.setRowCount(0);
				loadData();

			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat edit data.");
			}

		}

		private void deleteData(int row) {
			try {
				int id_pgj = Integer.parseInt(table.getValueAt(row, 0).toString());

				String nama = table.getValueAt(row, 1).toString();

				int confirm = JOptionPane.showConfirmDialog(
						null,
						"Yakin ingin menghapus data pengunjung \"" + nama + "\"?",
						"Konfirmasi Hapus",
						JOptionPane.YES_NO_OPTION
						);
				if (confirm != JOptionPane.YES_OPTION) {
					return;
				}

				PreparedStatement delete = con.prepareStatement("DELETE FROM pengunjung WHERE id_pgj = ?");
				delete.setInt(1, id_pgj);
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