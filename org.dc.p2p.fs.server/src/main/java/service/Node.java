package service;

import util.Service;

public class Node {

    private String name;
    private String bsServerIP="127.0.0.1";
    private int bsServerPort=55555;
    private String clientPort;
    private Service service =new Service();

    public void registerNode() throws Exception {
        Neighbour[] neighbours = null;
        String regMessage ="0036 REG 129.82.123.45 5004 1234abcdk";
        service.sendToBS(regMessage,bsServerIP,bsServerPort);
    }

    public void unRegisterNode() throws Exception {
        Neighbour[] neighbours = null;
        String regMessage ="0036 REG 129.82.123.45 5001 1234abcd";
        service.sendToBS(regMessage,bsServerIP,bsServerPort);
    }
}
