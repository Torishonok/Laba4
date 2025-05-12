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
public class AllParchesHistory extends JDialog {
    private JTextArea purchasesTextArea;
    private JButton closeButton;

    private static final String URL = "jdbc:postgresql://aws-0-eu-north-1.pooler.supabase.com:5432/postgres";
    private static final String USER = "postgres.dcafcvlyrhpkrxvndgnn";
    private static final String PASSWORD = "Vikushasa&30";

    public AllParchesHistory(JFrame parent) {
        super(parent, "История всех покупок", true);
        setLayout(new BorderLayout());

        purchasesTextArea = new JTextArea();
        purchasesTextArea.setEditable(false);
        loadAllPurchases();

        closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> dispose());

        add(new JScrollPane(purchasesTextArea), BorderLayout.CENTER);
        add(closeButton, BorderLayout.SOUTH);

        setSize(400, 300);
        setLocationRelativeTo(parent);
    }

    private void loadAllPurchases() {
        String query = "SELECT p.id, c.name AS customer_name, w.woodtype, w.coretype, p.purchase_date " +
                       "FROM purchases p " +
                       "JOIN Customers c ON p.customer_id = c.id " +
                       "JOIN Wands w ON p.wand_id = w.id";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            StringBuilder purchasesBuilder = new StringBuilder();
            purchasesBuilder.append("История всех покупок:\n\n");

            while (resultSet.next()) {
                int purchaseId = resultSet.getInt("id");
                String customerName = resultSet.getString("customer_name");
                String woodType = resultSet.getString("woodtype");
                String coreType = resultSet.getString("coretype");
                Date purchaseDate = resultSet.getDate("purchase_date");

                purchasesBuilder.append("ID покупки: ").append(purchaseId)
                                .append(", Клиент: ").append(customerName)
                                .append(", Дерево: ").append(woodType)
                                .append(", Сердцевина: ").append(coreType)
                                .append(", Дата покупки: ").append(purchaseDate)
                                .append("\n");
            }

            purchasesTextArea.setText(purchasesBuilder.toString());
            if (purchasesBuilder.length() == 0) {
                purchasesTextArea.setText("Нет записей о покупках.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при загрузке истории покупок: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}