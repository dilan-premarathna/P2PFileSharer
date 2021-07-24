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
        DatagramPacket dp1 = new DatagramPacket(b1, b1.length);
        socket.receive(dp1);

        String str = new String(dp1.getData());
        System.out.println("result is " + str);
        socket.close();
        return str;

    }
}