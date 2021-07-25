package util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Service {

    public String sendToBS(String message, String serverIP, int serverPort) throws Exception {

        InetAddress ia = InetAddress.getByName(serverIP);
        byte[] messageBytes = message.getBytes();
        DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, ia, serverPort);

        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);

        byte[] b1 = new byte[2048];
        DatagramPacket dpResponse = new DatagramPacket(b1, b1.length);
        socket.receive(dpResponse);

        String bsResponse = new String(dpResponse.getData());
        System.out.println("result is " + bsResponse);
        socket.close();
        return bsResponse.trim();

    }
}