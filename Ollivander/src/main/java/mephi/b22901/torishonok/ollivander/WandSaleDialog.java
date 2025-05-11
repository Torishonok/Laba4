/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.torishonok.ollivander;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author vikus
 */
public class WandSaleDialog extends JDialog {
    private JList<String> wandJList;
    private DefaultListModel<String> wandListModel;
    private JTextField customerNameField;
    private JTextField contactInfoField;
    private JButton sellButton;
    private JButton cancelButton;

    private static final String URL = "jdbc:postgresql://localhost:5432/Ollivander";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";

    public WandSaleDialog(JFrame parent) {
        super(parent, "Продажа палочки", true);
        setLayout(new BorderLayout());

        wandListModel = new DefaultListModel<>();
        wandJList = new JList<>(wandListModel);
        loadWands();

        wandJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedWand = wandJList.getSelectedValue();
                    if (selectedWand != null) {
                        
                        showCustomerInfoForm(selectedWand);
                    }
                }
            }
        });

        customerNameField = new JTextField(20);
        contactInfoField = new JTextField(20);
        sellButton = new JButton("Продать");
        cancelButton = new JButton("Отмена");

        sellButton.addActionListener(e -> sellWand());
        cancelButton.addActionListener(e -> dispose());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Имя клиента:"));
        inputPanel.add(customerNameField);
        inputPanel.add(new JLabel("Контактная информация:"));
        inputPanel.add(contactInfoField);
        inputPanel.add(sellButton);
        inputPanel.add(cancelButton);

        add(new JScrollPane(wandJList), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        setSize(400, 300);
        setLocationRelativeTo(parent);
    }

    private void loadWands() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT id, woodtype, coretype FROM Wands WHERE status = 'available'")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String woodType = resultSet.getString("woodtype");
                String coreType = resultSet.getString("coretype");
                wandListModel.addElement(id + " - " + woodType + " (" + coreType + ")"); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при загрузке палочек: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showCustomerInfoForm(String selectedWand) {
        
        customerNameField.setText("");
        contactInfoField.setText("");
    }

    private void sellWand() {
        String customerName = customerNameField.getText();
    String contactInfo = contactInfoField.getText();
    String selectedWand = wandJList.getSelectedValue();

    if (selectedWand == null) {
        JOptionPane.showMessageDialog(this, "Пожалуйста, выберите палочку.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        return;
    }
    int wandId = Integer.parseInt(selectedWand.split(" - ")[0]); 

    
    int customerId = getCustomerId(customerName, contactInfo);
    if (customerId == -1) {
        
        customerId = addNewCustomer(customerName, contactInfo);
    }

    
    recordSale(customerId, wandId);

    
    updateWandStatus(wandId, customerId); 

        JOptionPane.showMessageDialog(this, "Палочка успешно продана!", "Успех", JOptionPane.INFORMATION_MESSAGE);
        dispose(); 
    }

    private int getCustomerId(String name, String contactInfo) {
        
        String query = "SELECT id FROM Customers WHERE name = ? AND contact_info = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, contactInfo);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id"); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при проверке клиента: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        return -1; 
    }

    private int addNewCustomer(String name, String contactInfo) {
       
        String query = "INSERT INTO Customers (name, contact_info) VALUES (?, ?) RETURNING id";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, contactInfo);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id"); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при добавлении клиента: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        return -1; 
    }

    private void recordSale(int customerId, int wandId) {
        
        String query = "INSERT INTO purchases (customer_id, wand_id) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            statement.setInt(2, wandId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при записи продажи: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateWandStatus(int wandId, int customerId) {
    
    String query = "UPDATE Wands SET status = 'sold', buyerid = ?, soldat = CURRENT_DATE WHERE id = ?";
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, customerId); 
        statement.setInt(2, wandId);
        statement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Ошибка при обновлении статуса палочки: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}
}