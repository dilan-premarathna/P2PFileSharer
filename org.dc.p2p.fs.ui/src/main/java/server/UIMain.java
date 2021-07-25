package server;

import conf.ServerConfigurations;
import gui.ServerHome;
import service.Node;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author janaka
 */

public class UIMain {

    public UIMain(ServerConfigurations configs) throws Exception {

        System.out.println("Starting the server ...");
        System.out.println("Server IP = "+ configs.getServerIP());
        System.out.println("Server Port = " + configs.getServerPort());
        System.out.println("BS IP = " + configs.getBSIP());
        System.out.println("BS Port = " + configs.getBSPort());

        Node node = new Node(configs.getServerIP(), configs.getServerPort(), configs.getServerName(), configs.getBSIP(), configs.getBSPort());
        node.registerNode();


        List<String> fList = configs.getRandomNameList();
        System.out.println(fList);
        node.setFileList(fList);

        InitServerHomeUI(configs, node);

    }

    static void InitServerHomeUI(ServerConfigurations configs, Node node){
        JFrame frame = new JFrame("ServerHome");
        frame.setContentPane(new ServerHome(configs, node).ServerMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }

}