/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.torishonok.ollivander;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author vikus
 */
public class Supply extends JFrame {
    private static final String URL = "jdbc:postgresql://localhost:5432/your_database"; 
    private static final String USER = "your_username"; 
    private static final String PASSWORD = "your_password"; 

    private JTable supplyTable;
    private DefaultTableModel tableModel;
    private JTextField componentTypeField;
    private JTextField componentNameField;
    private JTextField quantityField;
    private JTextField supplierField;

    public Supply() {
        setTitle("Supply Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        
        tableModel = new DefaultTableModel(new String[]{"ID", "Supply Date", "Component Type", "Component Name", "Quantity", "Supplier"}, 0);
        supplyTable = new JTable(tableModel);
        loadSupplies();

       
        JScrollPane scrollPane = new JScrollPane(supplyTable);
        add(scrollPane, BorderLayout.CENTER);

        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));

        inputPanel.add(new JLabel("Component Type:"));
        componentTypeField = new JTextField();
        inputPanel.add(componentTypeField);

        inputPanel.add(new JLabel("Component Name:"));
        componentNameField = new JTextField();
        inputPanel.add(componentNameField);

        inputPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        inputPanel.add(quantityField);

        inputPanel.add(new JLabel("Supplier:"));
        supplierField = new JTextField();
        inputPanel.add(supplierField);

        JButton addButton = new JButton("Add Supply");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSupply();
            }
        });
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.SOUTH);
    }

    private void loadSupplies() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             java.sql.Statement stmt = connection.createStatement();
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
        }
    }

    private void addSupply() {
        String componentType = componentTypeField.getText();
        String componentName = componentNameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        String supplier = supplierField.getText();

        String insertSupply = "INSERT INTO Supplies (supply_date, component_type, component_name, quantity, supplier) VALUES (CURRENT_DATE, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(insertSupply)) {

            pstmt.setString(1, componentType);
            pstmt.setString(2, componentName);
            pstmt.setInt(3, quantity);
            pstmt.setString(4, supplier);
            pstmt.executeUpdate();

            
            tableModel.addRow(new Object[]{null, new Date(System.currentTimeMillis()), componentType, componentName, quantity, supplier});
            clearInputFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void clearInputFields() {
        componentTypeField.setText("");
        componentNameField.setText("");
        quantityField.setText("");
        supplierField.setText("");
    }
}

