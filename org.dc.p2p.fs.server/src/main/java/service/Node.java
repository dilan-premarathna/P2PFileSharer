package service;

import util.Query;
import util.Result;
import util.Service;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Arrays;

public class Node {

    private final int serverPort;
    private int resultPort;
    private final int bsServerPort;
    private final int soTimeout;
    private int retryCount;
    private final int retryLimit;
    private final String serverName;
    private final String serverIP;
    private String resultIP;
    private final String bsServerIP;
    private String[] resultList;
    private Query query;
    private final Service service = new Service();
    private List<String> fileList;
    public static List<Neighbour> neighboursList = new ArrayList<>();
    private final Result result = new Result();

    public Node(String ip, int port, String serverName, String bsServerIP, int bsServerPort, int soTimeout, int retryLimit) {

        this.serverIP = ip;
        this.serverName = serverName;
        this.serverPort = port;
        this.bsServerIP = bsServerIP;
        this.bsServerPort = bsServerPort;
        this.soTimeout = soTimeout;
        this.retryLimit = retryLimit;
    }

    public void registerNode() throws Exception {

        Neighbour[] neighbours = new Neighbour[0];
        String regMessage = "REG " + this.serverIP + " " + this.serverPort + " " + serverName;
        regMessage = String.format("%04d", regMessage.length() + 5) + " " + regMessage;
        System.out.println(regMessage);
        String bsResponse = service.sendToBS(regMessage, bsServerIP, bsServerPort, soTimeout);
        if (bsResponse != null) {
            neighbours = processBSResponse(bsResponse);
        } else {
            System.out.println("Error occurred while connecting to Boostrap Server");
        }
        if (neighbours.length != 0) {
            boolean neighbourConnectStatus = processNeighbour(neighbours);
            if (!neighbourConnectStatus) {
                unRegisterNode();
            } else {
                retryCount = 0;
            }
        }

        System.out.println(neighbours.toString());
    }

    public void unRegisterNode() throws Exception {

        String unRegMessage = "UNREG " + serverIP + " " + serverPort + " " + serverName;
        unRegMessage = String.format("%04d", unRegMessage.length() + 5) + " " + unRegMessage;
        String bsResponse = service.sendToBS(unRegMessage, bsServerIP, bsServerPort, soTimeout);
        System.out.println(bsResponse);
        retryCount += 1;
        if (retryCount <= retryLimit) {
            System.out.println(retryCount + " Retry to register node");
            registerNode();
        } else {
            System.out.println("Retry Limit reached");
        }
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

    public boolean processNeighbour(Neighbour[] neighbours) throws Exception {

        boolean connectSuccess = false;
        for (Neighbour neighbour : neighbours) {
            connectSuccess = neighbour.NeighbourConnect(neighboursList, serverIP, serverPort, soTimeout);
            if (!connectSuccess)
                return false;
        }
        return connectSuccess;
    }

    private String isFilePresent(String fName) {

        StringBuilder fileStr = new StringBuilder();
        for (String element : fileList
        ) {
            if (element.toLowerCase().contains(fName.toLowerCase())) fileStr.append(element).append("#");
        }
        return fileStr.toString();
    }

    public void searchFiles(String fName) throws IOException {

        String str = isFilePresent(fName);
        if (str.length() > 0) {
            resultList = str.split("#");

        } else {
            query = new Query(this.serverIP, this.serverPort, fName, 5);
            System.out.println(query.getMsgString());

            for (Neighbour neighbour : neighboursList) {
                service.send(query.getMsgString(), neighbour.getIp(), neighbour.getPort());
            }
        }
        System.out.println("Searching done!");
    }

    private void decodeAndAct(String recQuery) throws IOException {

        String[] msgList = recQuery.split(" ");

        if (msgList[1].equals("SER")) {
            String str = isFilePresent(msgList[4]);
            if (str.length() > 0) {
                String msg = " SEROK " + serverIP + " " + serverPort + " " + str + " " + "1";
                int length = msg.length() + 5;
                msg = String.format("%04d", length) + msg;
                service.send(msg, msgList[2], Integer.parseInt(msgList[3]));
                //send
            } else {
                msgList[5] = String.valueOf(Integer.parseInt(msgList[5]) - 1);
                if (!msgList[5].equals("0")) {
                    String joined = String.join(" ", msgList);
                    for (Neighbour neighbour : neighboursList) {
                        service.send(joined, neighbour.getIp(), neighbour.getPort());
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

        String recQuery = service.receive(serverPort);
        decodeAndAct(recQuery);
    }

    public Result getResultList() {

        result.setResult(resultIP, resultPort, resultList);

        return result;
    }

    public String getResultIP() {

        return resultIP;
    }

    public int getResultPort() {

        return resultPort;
    }

    public void setFileList(List<String> fList) {

        fileList = fList;
    }

    public List<Neighbour> getNeighboursList() {

        return neighboursList;
    }
}
