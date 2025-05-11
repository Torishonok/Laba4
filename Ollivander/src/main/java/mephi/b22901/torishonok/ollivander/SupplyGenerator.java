/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.torishonok.ollivander;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

/**
 *
 * @author vikus
 */
public class SupplyGenerator {
    static final String URL = "jdbc:postgresql://localhost:5432/Ollivander"; // Замените на ваш URL
    static final String USER = "postgres"; // Замените на ваше имя пользователя
    static final String PASSWORD = "12345"; // Замените на ваш пароль

    private static final String[] COMPONENT_TYPES = {"Липа", "Ель", "Акация", "Английский дуб", "Боярышник", "Бузина", "Красное дерево", "Ясень", "Кедр","Чёрный орешник" };
    private static final String[] COMPONENT_NAMES = {"Волос Вейлы", "Сердечная жила дракона", "Шерсть единорога", "Перо феникса", "Рог рогатого змея", "Ус тролля", "Крыло феи", "Рог васелиска","Волос африканской русалки", "Шерсть вампуса"};
    private static final String [] SUPPLIERS = {"Джимми Кеддел", "Геллерт", "Сефалопос"};

    public void generateSupplies(int numberOfSupplies) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Подключение к базе данных успешно!");

            for (int i = 0; i < numberOfSupplies; i++) {
                generateNewSupply(connection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateNewSupply(Connection connection) throws SQLException {
        String insertSupply = "INSERT INTO Supplies (supply_date, component_type, component_name, quantity, supplier) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSupply)) {
            pstmt.setDate(1, java.sql.Date.valueOf("2024-06-01")); // Установите дату поставки
            pstmt.setString(2, getRandomComponentType());
            pstmt.setString(3, getRandomComponentName());
            pstmt.setInt(4, getRandomQuantity());
            pstmt.setString(5, getRandomSupplier()); // Генерация имени поставщика

            pstmt.executeUpdate();
            //System.out.println("Новая поставка успешно добавлена.");
        }
    }

    private String getRandomComponentType() {
        Random random = new Random();
        return COMPONENT_TYPES[random.nextInt(COMPONENT_TYPES.length)];
    }

    private String getRandomComponentName() {
        Random random = new Random();
        return COMPONENT_NAMES[random.nextInt(COMPONENT_NAMES.length)];
    }

    private int getRandomQuantity() {
        Random random = new Random();
        return random.nextInt(10) + 1; 
    }

    private String getRandomSupplier() {
        Random random = new Random();
        return SUPPLIERS[random.nextInt(SUPPLIERS.length)];
    }
}