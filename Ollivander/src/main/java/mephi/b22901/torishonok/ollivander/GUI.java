/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.torishonok.ollivander;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author vikus
 */
public class GUI extends JFrame {
    
    private DataClearer dataClearer;
    
    public GUI() {
        dataClearer = new DataClearer();
        setTitle("Warehouse Manager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1)); // 7 кнопок в одном столбце

        // Создание кнопок
        JButton infoButton = new JButton("Информация о складе");
        JButton createWandButton = new JButton("Создать новую палочку");
        JButton sellWandButton = new JButton("Продать палочку");
        JButton orderMaterialsButton = new JButton("Заказать материалы");
        JButton clientBaseButton = new JButton("Клиентская база");
        JButton purchaseHistoryButton = new JButton("История покупок");
        JButton clearDataButton = new JButton("Очистить данные");

        // Добавление слушателей для кнопок (пока пустые)
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SuppliesInfo suppliesInfo = new SuppliesInfo();
                suppliesInfo.setVisible(true);
            }
        });

        createWandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WandCreator wandCreator = new WandCreator();
                wandCreator.createWand();
            }
        });

        sellWandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Логика для продажи палочки
                JOptionPane.showMessageDialog(null, "Продажа палочки еще не реализована.");
            }
        });

        orderMaterialsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MaterialOrder materialOrder = new MaterialOrder();
                materialOrder.orderMaterials();
            }
        });

        clientBaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerForm customerForm = new CustomerForm(GUI.this);
                customerForm.setVisible(true); // Открытие формы для ввода данных о клиенте
            }
        });

        purchaseHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Логика для истории покупок
                JOptionPane.showMessageDialog(null, "История покупок еще не реализована.");
            }
        });
        
        
        
        JFrame frame = new JFrame("Управление складом");
        frame.setLayout(new FlowLayout());
        clearDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(GUI.this,
                        "Внимание! Все данные будут утеряны. Вы уверены, что хотите продолжить?",
                        "Подтверждение очистки",
                        JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    dataClearer.clearDatabase(); // Вызов метода очистки данных
                }
            }
        });

        // Добавление кнопок на панель
        add(infoButton);
        add(createWandButton);
        add(sellWandButton);
        add(orderMaterialsButton);
        add(clientBaseButton);
        add(purchaseHistoryButton);
        add(clearDataButton);
    }
}
