package service;

import util.Communicator;
import util.Query;
import util.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Node {

    private String name;
    private String bsServerIP;
    private int bsServerPort;
    private String ip;
    private String resultIP;
    private int resultPort;
    private int clientPort;
    private Service service =new Service();
    private ArrayList<String> fileList = new ArrayList<>();
    private String[] resultList;
    private ArrayList<Neighbour> neighbourArrayList = new ArrayList<>();
    private Communicator communicator = new Communicator();
    private Query query;

    public void registerNode(String ip, int port, String bsServerIP, int bsServerPort) throws Exception {
        Neighbour[] neighbours = null;
        this.ip = ip;
        this.clientPort = port;
        this.bsServerIP = bsServerIP;
        this.bsServerPort = bsServerPort;
        String regMessage ="0036 REG" + " " + ip + " " + clientPort + " " + "1234abcdk";
        //String regMessage ="0036 REG 129.82.123.45 5004 1234abcdk";
       // service.sendToBS(regMessage,bsServerIP,bsServerPort);
    }

    public void unRegisterNode() throws Exception {
        Neighbour[] neighbours = null;
        String regMessage ="0036 REG 129.82.123.45 5001 1234abcd";
        service.sendToBS(regMessage,bsServerIP,bsServerPort);
    }

    private String isFilePresent(String fName) {
        StringBuilder fileStr = new StringBuilder();
        for (String element: fileList
             ) {
            if(element.contains(fName)) fileStr.append(element).append(" ");
            
        }
        return fileStr.toString();
    }

    public void searchFiles(String fName) throws IOException {

        if(isFilePresent(fName).length()>0) {
            resultList = fName.split(" ");

        } else {
            query = new Query(this.ip, this.clientPort, fName, 5);
            System.out.println(query.getMsgString());

            for (Neighbour neighbour : neighbourArrayList) {
                communicator.send(query.getMsgString(), neighbour.getIp(), neighbour.getPort(), 6000);
            }
        }
        System.out.println("Searching done!");
    }

    private void decodeAndAct(String recQuery) throws IOException {
        String[] msgList = recQuery.split(" ");

        if (msgList[1].equals("SER")) {
            String str = isFilePresent(msgList[4]);
            if (str.length() > 0) {
                String msg = " SEROK " + ip + " " + clientPort + " " + str + " " + "1";
                int length = msg.length();
                msg = String.format("%04d", length) + msg;
                communicator.send(msg, msgList[2], Integer.parseInt(msgList[3]), 6000);
                //send
            } else {
                msgList[5] = String.valueOf(Integer.parseInt(msgList[5]) - 1);
                if (!msgList[5].equals("0")) {
                    String joined = String.join(" ", msgList);
                    for (Neighbour neighbour : neighbourArrayList) {
                        communicator.send(joined, neighbour.getIp(), neighbour.getPort(), 6000);
                    }
                }
            }
        } else if (msgList[1].equals("SEROK") && Integer.parseInt(msgList[2]) > 0) {
            resultIP = msgList[3];
            resultPort = Integer.parseInt(msgList[4]);
            resultList = Arrays.copyOfRange(msgList, 6, msgList.length);
        }

    }

    public void receiveFromNeighbours() throws IOException {
        String recQuery = communicator.receive(clientPort);
        decodeAndAct(recQuery);
    }

    public String[] getResultList() {
        return resultList;
    }

    public String getResultIP() {
        return resultIP;
    }

    public int getResultPort() {
        return resultPort;
    }
}
