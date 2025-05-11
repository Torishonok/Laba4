/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.torishonok.ollivander;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author vikus
 */
public class WandCreator {
    private static final String URL = "jdbc:postgresql://localhost:5432/Ollivander"; // Замените на ваш URL
    private static final String USER = "postgres"; // Замените на ваше имя пользователя
    private static final String PASSWORD = "12345"; // Замените на ваш пароль

    public void createWand() {
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        // Получение уникальных сердцевин и древесины
        List<String> cores = getUniqueComponents(connection, "core"); // Изменено на "core"
        List<String> woods = getUniqueComponents(connection, "wood"); // Изменено на "wood"

        // Проверка наличия материалов
        if (cores.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Нет доступных сердцевин на складе.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (woods.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Нет доступной древесины на складе.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Создание диалогового окна для выбора сердцевины и древесины
        String selectedCore = (String) JOptionPane.showInputDialog(null, "Выберите сердцевину:", "Выбор сердцевины", JOptionPane.QUESTION_MESSAGE, null, cores.toArray(), cores.get(0));
        String selectedWood = (String) JOptionPane.showInputDialog(null, "Выберите древесину:", "Выбор древесины", JOptionPane.QUESTION_MESSAGE, null, woods.toArray(), woods.get(0));

        if (selectedCore != null && selectedWood != null) {
            // Добавление палочки в таблицу wands
            addWandToDatabase(connection, selectedCore, selectedWood);
            // Удаление использованных материалов из таблицы Supplies
            removeUsedMaterials(connection, selectedCore, selectedWood);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Ошибка при работе с базой данных: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}

    private List<String> getUniqueComponents(Connection connection, String componentType) throws SQLException {
    List<String> components = new ArrayList<>();
    String query = "SELECT DISTINCT component_name FROM Supplies WHERE component_type = ?";

    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
        pstmt.setString(1, componentType);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            components.add(rs.getString("component_name"));
        }
    }
    
    // Отладочное сообщение
    System.out.println("Уникальные компоненты для " + componentType + ": " + components);
    
    return components;
}

    private void addWandToDatabase(Connection connection, String core, String wood) throws SQLException {
    String insertWand = "INSERT INTO Wands (woodtype, coretype, length, price, status, manufacturedat, buyerid, soldat, available_quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement pstmt = connection.prepareStatement(insertWand)) {
        pstmt.setString(1, wood); // woodtype
        pstmt.setString(2, core); // coretype
        pstmt.setDouble(3, 11.00); // length (пример, вы можете изменить это значение)
        pstmt.setDouble(4, 40.50); // price (пример, вы можете изменить это значение)
        pstmt.setString(5, "available"); // status (пример, вы можете изменить это значение)
        pstmt.setDate(6, Date.valueOf("2024-06-10")); // manufacturedat (пример, вы можете изменить это значение)
        pstmt.setObject(7, null); // buyerid (пример, вы можете изменить это значение)
        pstmt.setObject(8, null); // soldat (пример, вы можете изменить это значение)
        pstmt.setInt(9, 1); // available_quantity (пример, вы можете изменить это значение)

        pstmt.executeUpdate();
        JOptionPane.showMessageDialog(null, "Палочка успешно создана!", "Успех", JOptionPane.INFORMATION_MESSAGE);
    }
}

    private void removeUsedMaterials(Connection connection, String core, String wood) throws SQLException {
        String deleteSupply = "DELETE FROM Supplies WHERE component_name = ? OR component_name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(deleteSupply)) {
            pstmt.setString(1, core);
            pstmt.setString(2, wood);
            pstmt.executeUpdate();
        }
    }
}
