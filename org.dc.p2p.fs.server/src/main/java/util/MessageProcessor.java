package util;

import service.Neighbour;
import service.Node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class MessageProcessor implements Runnable {

    private Node node;
    private boolean stopListner = false;
    private List<Neighbour> neighbourList;
    private final int serverPort;

    public MessageProcessor(Node node, int port){
        this.node = node;
        this.neighbourList = node.getNeighboursList();
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

    public String processMessage(String message) {

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
                    for (Neighbour neb : neighbourList) {
                        if (neb.getIp().equals(ip) && neb.getPort() == port) {
                            neighbourExist = true;
                            break;
                        }
                    }
                    if (!neighbourExist){
                        neighbourList.add(neighbour);}
                    else {
                        System.out.println("##### neighbour exist ######");
                    }

                    String joinResMessage = "JOINOK " + "0";
                    joinResMessage = String.format("%04d", joinResMessage.length() + 5) + " " + joinResMessage;
                    responseMsg = joinResMessage;
                    System.out.println("res:" + joinResMessage);
                break;

            case "LEAVE":
                    ip = mes[2];
                    port = Integer.parseInt(mes[3]);
                    neighbourList.removeIf(neigh -> (neigh.getIp().equals(ip) && neigh.getPort()==port));
                    String leaveResMessage = "LEAVEOK " + "0";
                    leaveResMessage = String.format("%04d", leaveResMessage.length() + 5) + " " + leaveResMessage;
                    responseMsg = leaveResMessage;
                break;

            default:
                System.err.println("Error: " + message);
                break;
        }
        return responseMsg;

    }

}
