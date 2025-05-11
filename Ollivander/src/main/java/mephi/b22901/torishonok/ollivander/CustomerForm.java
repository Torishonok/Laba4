/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.torishonok.ollivander;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author vikus
 */
public class CustomerForm extends JDialog {
    private JTextField nameField;
    private JTextField contactInfoField;
    private JButton submitButton;
    private JButton cancelButton;

    private static final String URL = "jdbc:postgresql://localhost:5432/Ollivander";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";

    public CustomerForm(JFrame parent) {
        super(parent, "Добавить клиента", true);
        setLayout(new GridLayout(3, 2));

        
        add(new JLabel("Имя:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Контактная информация:"));
        contactInfoField = new JTextField();
        add(contactInfoField);

        
        submitButton = new JButton("Добавить");
        cancelButton = new JButton("Отмена");

        add(submitButton);
        add(cancelButton);

        
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });

        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });

        setSize(300, 150);
        setLocationRelativeTo(parent);
    }

    private void addCustomer() {
        String name = nameField.getText();
        String contactInfo = contactInfoField.getText();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Имя не может быть пустым.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Customers (name, contact_info) VALUES (?, ?)")) {

            statement.setString(1, name);
            statement.setString(2, contactInfo);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Клиент успешно добавлен!", "Успех", JOptionPane.INFORMATION_MESSAGE);
            dispose(); 
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при добавлении клиента: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
