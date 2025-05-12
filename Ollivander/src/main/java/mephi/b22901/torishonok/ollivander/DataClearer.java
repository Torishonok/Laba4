/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.torishonok.ollivander;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author vikus
 */
public class DataClearer {
    private static final String URL = "jdbc:postgresql://aws-0-eu-north-1.pooler.supabase.com:5432/postgres";
    private static final String USER = "postgres.dcafcvlyrhpkrxvndgnn";
    private static final String PASSWORD = "Vikushasa&30";

    public void clearDatabase() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            
            statement.executeUpdate("DELETE FROM wandmaterials;");
            statement.executeUpdate("DELETE FROM wands;");
            statement.executeUpdate("DELETE FROM supplies;");
            statement.executeUpdate("DELETE FROM customers;");

            JOptionPane.showMessageDialog(null, "Все данные успешно очищены!", "Успех", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при очистке данных: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}

