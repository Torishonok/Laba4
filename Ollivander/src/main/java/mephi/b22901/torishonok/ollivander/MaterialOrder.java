/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.torishonok.ollivander;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author vikus
 */
public class MaterialOrder {
    private SupplyGenerator supplyGenerator = new SupplyGenerator();

    public void orderMaterials() {
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        JLabel woodLabel = new JLabel("Количество древесины (10-15):");
        JTextField woodField = new JTextField();
        JLabel coreLabel = new JLabel("Количество сердцевин (10-15):");
        JTextField coreField = new JTextField();

        panel.add(woodLabel);
        panel.add(woodField);
        panel.add(coreLabel);
        panel.add(coreField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Заказ материалов", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int woodQuantity = Integer.parseInt(woodField.getText());
                int coreQuantity = Integer.parseInt(coreField.getText());
                if (woodQuantity < 10 || woodQuantity > 15 || coreQuantity < 10 || coreQuantity > 15) {
                    JOptionPane.showMessageDialog(null, "Количество должно быть от 10 до 15.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                
                String receipt = String.format("Ваш заказ:\nДревесина: %d штук\nСердцевины: %d штук", woodQuantity, coreQuantity);
                JOptionPane.showMessageDialog(null, receipt, "Чек", JOptionPane.INFORMATION_MESSAGE);
                

                
                addToSupplies(woodQuantity, coreQuantity);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Пожалуйста, введите корректные числа.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addToSupplies(int woodQuantity, int coreQuantity) {
        try (Connection connection = DriverManager.getConnection(SupplyGenerator.URL, SupplyGenerator.USER, SupplyGenerator.PASSWORD)) {
           
            for (int i = 0; i < woodQuantity; i++) {
                supplyGenerator.generateNewSupply(connection);
            }

           
            for (int i = 0; i < coreQuantity; i++) {
                supplyGenerator.generateNewSupply(connection);
            }

            JOptionPane.showMessageDialog(null, "Заказ успешно добавлен в таблицу Supplies.", "Успех", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при добавлении данных: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}

