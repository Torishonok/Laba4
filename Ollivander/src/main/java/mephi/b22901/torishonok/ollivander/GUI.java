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
        setTitle("Ollivander shop");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1)); 

        
        JButton infoButton = new JButton("Информация о складе");
        JButton createWandButton = new JButton("Создать новую палочку");
        JButton sellWandButton = new JButton("Продать палочку");
        JButton orderMaterialsButton = new JButton("Заказать материалы");
        JButton clientBaseButton = new JButton("Клиентская база");
        JButton purchaseHistoryButton = new JButton("История покупок");
        JButton clearDataButton = new JButton("Очистить данные");

        
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
        WandSaleDialog wandSaleDialog = new WandSaleDialog(GUI.this);
        wandSaleDialog.setVisible(true); 
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
        CustomerList customerList = new CustomerList(GUI.this); 
        customerList.setVisible(true); 
    }
        });

        purchaseHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AllParchesHistory all = new AllParchesHistory(GUI.this);
                all.setVisible(true);
                
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
                    dataClearer.clearDatabase(); 
                }
            }
        });

       
        add(infoButton);
        add(createWandButton);
        add(sellWandButton);
        add(orderMaterialsButton);
        add(clientBaseButton);
        add(purchaseHistoryButton);
        add(clearDataButton);
    }
}
