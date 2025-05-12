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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author vikus
 */
public class WandCreator {
    private static final String URL = "jdbc:postgresql://localhost:5432/Ollivander"; 
    private static final String USER = "postgres"; 
    private static final String PASSWORD = "12345"; 

    private static final String[] CORES = {
        "Волос Вейлы", "Сердечная жила дракона", "Шерсть единорога", 
        "Перо феникса", "Рог рогатого змея", "Ус тролля", 
        "Крыло феи", "Рог васелиска", "Волос африканской русалки", 
        "Шерсть вампуса"
    };

    private static final String[] WOODS = {
        "Липа", "Ель", "Акация", "Английский дуб", "Боярышник", 
        "Бузина", "Красное дерево", "Ясень", "Кедр", "Чёрный орешник"
    };

   public void createWand() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        
        List<String> cores = getAvailableComponents(connection, CORES, true); 
        List<String> woods = getAvailableWoods(connection); 

        if (cores.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Нет доступных сердцевин на складе.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (woods.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Нет доступной древесины на складе.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedCore = (String) JOptionPane.showInputDialog(null, "Выберите сердцевину:", "Выбор сердцевины", JOptionPane.QUESTION_MESSAGE, null, cores.toArray(), cores.get(0));
        String selectedWood = (String) JOptionPane.showInputDialog(null, "Выберите древесину:", "Выбор древесины", JOptionPane.QUESTION_MESSAGE, null, woods.toArray(), woods.get(0));

        if (selectedCore != null && selectedWood != null) {
            addWandToDatabase(connection, selectedCore, selectedWood);
            removeUsedMaterials(connection, selectedCore, selectedWood); // Удаляем материалы только после успешного добавления палочки
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Ошибка при работе с базой данных: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}

    private List<String> getAvailableComponents(Connection connection, String[] componentTypes, boolean isCore) throws SQLException {
        List<String> availableComponents = new ArrayList<>();
        String query = "SELECT \"component_name\" FROM Supplies WHERE \"component_name\" IN (";

        
        for (int i = 0; i < componentTypes.length; i++) {
            query += "?";
            if (i < componentTypes.length - 1) {
                query += ", ";
            }
        }
        query += ") AND \"quantity\" > 0"; 

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            for (int i = 0; i < componentTypes.length; i++) {
                pstmt.setString(i + 1, componentTypes[i]);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                availableComponents.add(rs.getString("component_name")); 
            }
        }

        
        //System.out.println("Доступные компоненты: " + availableComponents);
        
        return availableComponents;
    }
    private List<String> getAvailableWoods(Connection connection) throws SQLException {
        List<String> availableWoods = new ArrayList<>();
        String query = "SELECT \"component_type\" FROM Supplies WHERE \"component_type\" IN (";

        
        for (int i = 0; i < WOODS.length; i++) {
            query += "?";
            if (i < WOODS.length - 1) {
                query += ", ";
            }
        }
        query += ") AND \"quantity\" > 0"; 
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            for (int i = 0; i < WOODS.length; i++) {
                pstmt.setString(i + 1, WOODS[i]);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                availableWoods.add(rs.getString("component_type")); 
            }
        }

        
        //System.out.println("Доступные древесины: " + availableWoods);
        
        return availableWoods;
    }

    private void addWandToDatabase(Connection connection, String core, String wood) throws SQLException {
        String insertWand = "INSERT INTO Wands (woodtype, coretype, length, price, status, manufacturedat, buyerid, soldat, available_quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertWand)) {
            pstmt.setString(1, wood); 
            pstmt.setString(2, core); 
            pstmt.setDouble(3, 11.00); 
            pstmt.setDouble(4, 40.50); 
            pstmt.setString(5, "available"); 
            pstmt.setDate(6, Date.valueOf("2024-06-10")); 
            pstmt.setObject(7, null); 
            pstmt.setObject(8, null); 
            pstmt.setInt(9, 1); 

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Палочка успешно создана!", "Успех", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void removeUsedMaterials(Connection connection, String core, String wood) throws SQLException {
    
    String checkSupply = "SELECT \"component_name\", \"quantity\" FROM Supplies WHERE \"component_name\" = ? OR \"component_name\" = ?";
    try (PreparedStatement checkStmt = connection.prepareStatement(checkSupply)) {
        checkStmt.setString(1, core);
        checkStmt.setString(2, wood);
        ResultSet rs = checkStmt.executeQuery();

        
        Map<String, Integer> quantities = new HashMap<>();
        while (rs.next()) {
            quantities.put(rs.getString("component_name"), rs.getInt("quantity"));
        }

        
        for (String component : quantities.keySet()) {
            if (quantities.get(component) <= 0) {
                JOptionPane.showMessageDialog(null, "Недостаточно материалов на складе для создания палочки: " + component, "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    
    String updateSupply = "UPDATE Supplies SET \"quantity\" = \"quantity\" - 1 WHERE \"component_name\" = ? OR \"component_name\" = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(updateSupply)) {
        pstmt.setString(1, core);
        pstmt.setString(2, wood);
        pstmt.executeUpdate();
    }

    
    String deleteSupply = "DELETE FROM Supplies WHERE \"component_name\" = ? AND \"quantity\" = 0";
    try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSupply)) {
        deleteStmt.setString(1, core);
        deleteStmt.executeUpdate();
    }

    try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSupply)) {
        deleteStmt.setString(1, wood);
        deleteStmt.executeUpdate();
    }
}
}