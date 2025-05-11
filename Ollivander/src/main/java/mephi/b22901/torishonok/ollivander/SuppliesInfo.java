/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.torishonok.ollivander;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author vikus
 */
public class SuppliesInfo extends JFrame {
    private static final String URL = "jdbc:postgresql://localhost:5432/Ollivander"; 
    private static final String USER = "postgres"; 
    private static final String PASSWORD = "12345"; 

    public SuppliesInfo() {
        setTitle("Информация о складе");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Supply Date", "Component Type", "Component Name", "Quantity", "Supplier"}, 0);
        JTable suppliesTable = new JTable(tableModel);
        loadSupplies(tableModel);

       
        JScrollPane scrollPane = new JScrollPane(suppliesTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadSupplies(DefaultTableModel tableModel) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Supplies")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                Date supplyDate = rs.getDate("supply_date");
                String componentType = rs.getString("component_type");
                String componentName = rs.getString("component_name");
                int quantity = rs.getInt("quantity");
                String supplier = rs.getString("supplier");

                tableModel.addRow(new Object[]{id, supplyDate, componentType, componentName, quantity, supplier});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при загрузке данных: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
