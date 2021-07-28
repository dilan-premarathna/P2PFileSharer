package util;

import service.Neighbour;
import service.Node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

public class MessageProcessor implements Runnable {

    private Node node;
    private boolean stopListner = false;
    private List<Neighbour> connectedNeighbourList;
    private final String nodeIP;
    private final int serverPort;
    private final Service service = new Service();

    public MessageProcessor(Node node, String nodeIP, int port){
        this.node = node;
        this.connectedNeighbourList = node.getConnectedNeighboursList();
        this.nodeIP = nodeIP;
        this.serverPort = port;
    }

    @Override
    public void run() {

        try {

            while (!stopListner) {

                DatagramSocket socket = new DatagramSocket(serverPort);

                byte[] b1 = new byte[2048];

                DatagramPacket packet = new DatagramPacket(b1, b1.length);
                socket.receive(packet);
                String msg = new String(packet.getData(),0,packet.getLength());
                String res = this.processMessage(msg);
                byte[] b2 = res.getBytes();

                DatagramPacket dp1 = new DatagramPacket(b2, b2.length, packet.getAddress(), packet.getPort());
                socket.send(dp1);
                socket.close();
                Thread.sleep(10);
            }
        } catch (InterruptedException | IOException e) {
            System.err.println(e);
        }
    }

    public String processMessage(String message) throws IOException {

        String[] mes = message.split(" ");
        int port;
        String ip;
        String responseMsg="";

        System.out.println("New Message: " + message);

        switch (mes[1]) {
            case "JOIN":

                    ip = mes[2];
                    port = Integer.parseInt(mes[3]);
                    System.out.println("New JOIN message");

                    Neighbour neighbour = new Neighbour(ip,port);
                    boolean neighbourExist = false;
                    String joinResMessage="";
                    for (Neighbour neb : connectedNeighbourList) {
                        if (neb.getIp().equals(ip) && neb.getPort() == port) {
                            neighbourExist = true;
                            break;
                        }
                    }
                    if (!neighbourExist){
                        connectedNeighbourList.add(neighbour);
                        joinResMessage = "JOINOK " + "0";}
                    else {
                        System.out.println("##### neighbour exist ######");
                        joinResMessage = "JOINOK " + "9999";
                    }


                    joinResMessage = String.format("%04d", joinResMessage.length() + 5) + " " + joinResMessage;
                    responseMsg = joinResMessage;
                    System.out.println("res:" + joinResMessage);
                break;

            case "LEAVE":
                    ip = mes[2];
                    port = Integer.parseInt(mes[3]);
                    connectedNeighbourList.removeIf(neigh -> (neigh.getIp().equals(ip) && neigh.getPort()==port));
                    String leaveResMessage = "LEAVEOK " + "0";
                    leaveResMessage = String.format("%04d", leaveResMessage.length() + 5) + " " + leaveResMessage;
                    responseMsg = leaveResMessage;
                break;
            case "SER":
                String str = node.isFilePresent(mes[4]);
                if (str.length() > 0) {
                    String msg = " SEROK " + nodeIP + " " + node.getRestServicePort() + " " + str + " " + "1";
                    int length = msg.length() + 5;
                    msg = String.format("%04d", length) + msg;
                    service.send(msg, mes[2], Integer.parseInt(mes[3]));
                    //send
                } else {
                    mes[5] = String.valueOf(Integer.parseInt(mes[5]) - 1);
                    if (!mes[5].equals("0")) {
                        String joined = String.join(" ", mes);
                        for (Neighbour neighbour_ : connectedNeighbourList) {
                            service.send(joined, neighbour_.getIp(), neighbour_.getPort());
                        }
                    }
                }
                break;
            case "SEROK":
                if(Integer.parseInt(mes[2]) > 0) {
                    node.addToResultObjList(node.setResultObj(mes[3], Integer.parseInt(mes[4]), Arrays.copyOfRange(mes, 6, mes.length)));
                }
                break;

            case "HEALTH":
                String healthRes = "HEALTHOK";
                String healthMessage = String.format("%04d", healthRes.length() + 5) + " " + healthRes;
                responseMsg = healthMessage;
                break;
            default:
                System.err.println("Error: " + message);
                break;
        }
        return responseMsg;

    }

}
