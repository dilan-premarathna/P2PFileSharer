package service;

import util.Communicator;
import util.Query;
import util.Service;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Arrays;

public class Node {

    private String name;
    private String bsServerIP = "127.0.0.1";
    private int bsServerPort = 55555;
    private Service service = new Service();
    private final List<Neighbour> neighboursList = new ArrayList<>();
    private String ip;
    private String resultIP;
    private int resultPort;
    private int clientPort;
    private ArrayList<String> fileList = new ArrayList<>();
    private String[] resultList;
    private ArrayList<Neighbour> neighbourArrayList = new ArrayList<>();
    private Communicator communicator = new Communicator();
    private Query query;

    public void registerNode(String ip, int port, String bsServerIP, int bsServerPort) throws Exception {
        this.ip = ip;
        this.clientPort = port;
        this.bsServerIP = bsServerIP;
        this.bsServerPort = bsServerPort;
        String regMessage = "0036 REG 127.0.0.1 5000 client1";
        String bsResponse = service.sendToBS(regMessage, bsServerIP, bsServerPort);
        Neighbour[] neighbours = processBSResponse(bsResponse.trim());
        if (neighbours.length != 0) {
            processNeighbour(neighbours);
        }
        System.out.println(neighbours);
    }

    public void unRegisterNode() throws Exception {

        Neighbour[] neighbours = null;
        String regMessage = "0036 REG 129.82.123.45 5001 1234abcd";
        service.sendToBS(regMessage, bsServerIP, bsServerPort);
    }

    public Neighbour[] processBSResponse(String message)
            throws Exception {

        String[] mes = message.split(" ");
        System.out.println(message);
        System.out.println("processBSResponse: " + mes[2]);
        int noNodes = Integer.parseInt(mes[2]);

        if (0 <= noNodes && noNodes <= 2) {
            Neighbour[] neighbour = new Neighbour[noNodes];
            for (int n = 0; n < noNodes; n++) {
                neighbour[n] = new Neighbour(mes[(n * 2) + 3],
                        Integer.parseInt(mes[(n * 2) + 4]));
            }
            return neighbour;
        } else {
            switch (noNodes) {
                case 9996:
                    throw new Exception("failed, can’t register." + " BS full");
                case 9997:
                    throw new Exception("failed, registered" + " to another user, try a different IP and port");
                case 9998:
                    throw new Exception("failed, already registered" + " to you, unregister first");
                case 9999:
                    throw new Exception("failed, there is some error" + " in the command");
            }
        }
        return null;
    }

    public void processNeighbour(Neighbour[] neighbours) throws Exception {

        for (Neighbour neighbour : neighbours) {
            neighbour.NeighbourConnect(neighboursList);
        }
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
