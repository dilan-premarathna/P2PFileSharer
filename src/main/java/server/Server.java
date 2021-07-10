package server;

import conf.ServerConfigurations;
import gui.ServerHome;

import javax.swing.*;
import java.io.IOException;

/**
 * @author janaka
 */

public class Server {

    public static void main(String[] args) throws IOException {

        System.out.printf("Starting the server ...");
        ServerConfigurations configs = new ServerConfigurations();
        System.out.println("Server IP = "+ configs.getServerIP());
        System.out.println("Server Port = " + configs.getServerPort());
        System.out.println("BS IP = " + configs.getBSIP());
        System.out.println("BS Port = " + configs.getBSPort());

        InitServerHomeUI();


    }

    static void InitServerHomeUI(){
        JFrame frame = new JFrame("ServerMain");
        frame.setContentPane(new ServerHome().ServerMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }

}