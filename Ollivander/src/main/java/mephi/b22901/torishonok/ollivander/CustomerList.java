/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.torishonok.ollivander;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author vikus
 */
public class CustomerList extends JDialog {
     private JList<String> customerJList;
    private DefaultListModel<String> listModel;
    private JButton viewHistoryButton;
    private JButton cancelButton;

    private static final String URL = "jdbc:postgresql://aws-0-eu-north-1.pooler.supabase.com:5432/postgres";
    private static final String USER = "postgres.dcafcvlyrhpkrxvndgnn";
    private static final String PASSWORD = "Vikushasa&30";

    public CustomerList(JFrame parent) {
        super(parent, "Список клиентов", true);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        customerJList = new JList<>(listModel);
        loadCustomers();

        viewHistoryButton = new JButton("Просмотреть историю покупок");
        cancelButton = new JButton("Отмена");

        viewHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCustomer = customerJList.getSelectedValue();
                if (selectedCustomer != null) {
                    int customerId = Integer.parseInt(selectedCustomer.split(" - ")[0]); 
                    new PurchaseHistoryDialog(parent, customerId).setVisible(true); 
                } else {
                    JOptionPane.showMessageDialog(CustomerList.this, "Пожалуйста, выберите клиента.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });

        add(new JScrollPane(customerJList), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(300, 200);
        setLocationRelativeTo(parent);
    }

    private void loadCustomers() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT id, name FROM Customers")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                listModel.addElement(id + " - " + name); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при загрузке клиентов: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}