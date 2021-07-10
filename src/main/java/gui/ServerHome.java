package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author janaka
 */
public class ServerHome {
 public JPanel ServerMain;

 private JButton searchButton;
 private JTextField textField1;
 private JTextPane textPane1;
 private JTextPane textPane2;
 private JTextPane serverIPServerPortTextPane;

 public ServerHome() {


  searchButton.addActionListener(new ActionListener() {
   @Override
   public void actionPerformed(ActionEvent e) {
    System.out.println("Searching for the files");
    // Query flooding needs to be implemented
   }
  });
 }

}
