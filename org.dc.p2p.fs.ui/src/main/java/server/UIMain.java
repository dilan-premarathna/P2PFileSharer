package server;

import conf.ServerConfigurations;
import gui.ServerHome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HelthcheckService;
import service.Node;

import javax.swing.*;
import java.util.List;

/**
 * @author janaka
 */

public class UIMain {

    private static final Logger log = LoggerFactory.getLogger(UIMain.class);

    public UIMain(ServerConfigurations configs) throws Exception {

        log.info("Starting the server ...");
        log.info("Server IP = "+ configs.getServerIP());
        log.info("Server Port = " + configs.getServerPort());
        log.info("BS IP = " + configs.getBSIP());
        log.info("BS Port = " + configs.getBSPort());

        Node node = new Node(configs.getServerIP(), configs.getServerPort(), configs.getServerName(), configs.getBSIP(),
                configs.getBSPort(), configs.getSocketTimeout(), configs.getRetryLimit(), configs.getRestServicePort());
        node.registerNode();

        // Initialize health check API
        HelthcheckService service = new HelthcheckService(node);
        service.scheduleTask(Node.neighboursList);

        List<String> fList = configs.getRandomNameList();
        log.info(fList.toString());
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