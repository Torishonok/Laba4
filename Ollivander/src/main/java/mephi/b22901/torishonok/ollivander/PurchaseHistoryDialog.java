/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.torishonok.ollivander;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author vikus
 */
public class PurchaseHistoryDialog extends JDialog {
    private JTextArea historyTextArea;
    private JButton closeButton;

    private static final String URL = "jdbc:postgresql://localhost:5432/Ollivander";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";

    public PurchaseHistoryDialog(JFrame parent, int customerId) {
        super(parent, "Клиентская база", true);
        setLayout(new BorderLayout());

        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);
        //loadPurchaseHistory(customerId);

        closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> dispose());

        add(new JScrollPane(historyTextArea), BorderLayout.CENTER);
        add(closeButton, BorderLayout.SOUTH);

        setSize(400, 300);
        setLocationRelativeTo(parent);
    }

    
}