/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */



import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;



public class Mavenproject1 extends JFrame {

        private JTextField txtMSSV, txtHoTen, txtNamSinh;
        private JTable table;
        private DefaultTableModel tableModel;
        
    public Mavenproject1() {
            // Thiết lập cửa sổ
            setTitle("Quản Lý Sinh Viên");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            // Panel nhập liệu
            JPanel panelInput = new JPanel(new GridLayout(4, 2, 10, 10));
            panelInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            panelInput.add(new JLabel("MSSV:"));
            txtMSSV = new JTextField();
            panelInput.add(txtMSSV);

            panelInput.add(new JLabel("Họ Tên:"));
            txtHoTen = new JTextField();
            panelInput.add(txtHoTen);

            panelInput.add(new JLabel("Năm Sinh:"));
            txtNamSinh = new JTextField();
            panelInput.add(txtNamSinh);

            // Các nút
            JPanel panelButtons = new JPanel(new GridLayout(1, 3, 10, 10));
            JButton btnAdd = new JButton("Thêm");
            JButton btnUpdate = new JButton("Sửa");
            JButton btnDelete = new JButton("Xóa");
            panelButtons.add(btnAdd);
            panelButtons.add(btnUpdate);
            panelButtons.add(btnDelete);

            panelInput.add(panelButtons);

            add(panelInput, BorderLayout.NORTH);

            // Bảng hiển thị danh sách sinh viên
            tableModel = new DefaultTableModel(new String[]{"MSSV", "Họ Tên", "Năm Sinh"}, 0);
            table = new JTable(tableModel);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            add(new JScrollPane(table), BorderLayout.CENTER);

            // Thêm sự kiện cho các nút
            btnAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addStudent();
                }
            });

            btnUpdate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateStudent();
                }
            });

            btnDelete.addActionListener(new ActionListener() {
                @Override
public void actionPerformed(ActionEvent e) {
                    deleteStudent();
                }
            });

            
            // Nút Reset
JButton btnReset = new JButton("Reset");
panelButtons.add(btnReset);

btnReset.addActionListener(new ActionListener() { 
    @Override
    public void actionPerformed(ActionEvent e) {
        clearInputFields();
    }
});

            // Sự kiện chọn dòng trong bảng
            table.getSelectionModel().addListSelectionListener(event -> {
                if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    int selectedRow = table.getSelectedRow();
                    txtMSSV.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtHoTen.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtNamSinh.setText(tableModel.getValueAt(selectedRow, 2).toString());
                }
            });

            // Hiển thị danh sách sinh viên khi mở ứng dụng
            displayStudents();
        }

        private void addStudent() {
            int mssv = Integer.parseInt(txtMSSV.getText());
            String hoTen = txtHoTen.getText();
            int namSinh = Integer.parseInt(txtNamSinh.getText());

            String query = "INSERT INTO SinhVien (MSSV, HoTen, NamSinh) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, mssv);
                pstmt.setString(2, hoTen);
                pstmt.setInt(3, namSinh);
                pstmt.executeUpdate();

                displayStudents();
                clearInputFields();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }

        private void updateStudent() {
            int mssv = Integer.parseInt(txtMSSV.getText());
            String hoTen = txtHoTen.getText();
            int namSinh = Integer.parseInt(txtNamSinh.getText());

            String query = "UPDATE SinhVien SET HoTen = ?, NamSinh = ? WHERE MSSV = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, hoTen);
                pstmt.setInt(2, namSinh);
                pstmt.setInt(3, mssv);
                pstmt.executeUpdate();

                displayStudents();
                clearInputFields();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }

        private void deleteStudent() {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên để xóa.");
                return;
            }

            int mssv = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            String query = "DELETE FROM SinhVien WHERE MSSV = ?";
            try (Connection conn = DatabaseConnection.getConnection();
PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, mssv);
                pstmt.executeUpdate();

                displayStudents();
                clearInputFields();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
        
        
        
        private void displayStudents() {
            tableModel.setRowCount(0);
            String query = "SELECT * FROM SinhVien";
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    int mssv = rs.getInt("MSSV");
                    String hoTen = rs.getString("HoTen");
                    int namSinh = rs.getInt("NamSinh");
                    tableModel.addRow(new Object[]{mssv, hoTen, namSinh});
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }

        private void clearInputFields() {
            txtMSSV.setText("");
            txtHoTen.setText("");
            txtNamSinh.setText("");
        }

    
    public static void main(String[] args) {
    new Mavenproject1().setVisible(true);           
}
}

