package perpustakaan;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class datausers {

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
					datausers window = new datausers();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public datausers() {
		initialize();
		connectDB();
		loadData();
	}

	private void initialize() {
		frame = new JFrame("Data Users");
		frame.setBounds(100, 100, 857, 629);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		model = new DefaultTableModel();
		table = new JTable(model);
		model.setColumnIdentifiers(new Object[]{"ID", "Nama user", "Username", "Edit", "Delete"});

		table.getColumn("Edit").setCellRenderer(new ButtonRenderer());
		table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), "Edit"));

		table.getColumn("Delete").setCellRenderer(new ButtonRenderer());
		table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete"));
		frame.getContentPane().setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane(table);
		scrollPane_1.setBounds(0, 0, 0, 0);
		frame.getContentPane().add(scrollPane_1);

		frame = new JFrame("Data Users");
		frame.setBounds(100, 100, 857, 629);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel titleLabel = new JLabel("Data Users");
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		titleLabel.setBounds(250, 20, 400, 30);
		frame.getContentPane().add(titleLabel);

		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(700, 20, 120, 30);
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adduser window = new adduser();  
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
	        pst = con.prepareStatement("SELECT * FROM user");
	        ResultSet rs = pst.executeQuery();

	        model.setRowCount(0); 

	        while (rs.next()) {
	            model.addRow(new Object[]{
	                rs.getInt("id_user"), 
	                rs.getString("nama_user"),
	                rs.getString("username"),
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
			    int id_user = Integer.parseInt(table.getValueAt(row, 0).toString()); 
			    String nama = table.getValueAt(row, 1).toString();                  
			    String username = table.getValueAt(row, 2).toString();

			    String newNama = JOptionPane.showInputDialog(null, "Edit Nama", nama);
			    String newAlamat = JOptionPane.showInputDialog(null, "Edit Username", username);

			    PreparedStatement update = con.prepareStatement(
			        "UPDATE user SET nama_user = ?, username = ? WHERE id_user = ?"
			    );
			    update.setString(1, newNama);
			    update.setString(2, newAlamat);
			    update.setInt(3, id_user);
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
		        int id_user = Integer.parseInt(table.getValueAt(row, 0).toString());

		        String nama = table.getValueAt(row, 1).toString();

		        int confirm = JOptionPane.showConfirmDialog(
		            null,
		            "Yakin ingin menghapus data user \"" + nama + "\"?",
		            "Konfirmasi Hapus",
		            JOptionPane.YES_NO_OPTION
		        );
		        if (confirm != JOptionPane.YES_OPTION) {
		            return;
		        }

		        PreparedStatement delete = con.prepareStatement("DELETE FROM user WHERE id_user = ?");
		        delete.setInt(1, id_user);
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