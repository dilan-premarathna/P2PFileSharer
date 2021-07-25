package util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class Service {

    public String sendToBS(String message, String serverIP, int serverPort, int soTimeout) throws Exception {

        InetAddress ia = InetAddress.getByName(serverIP);
        byte[] messageBytes = message.getBytes();
        DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, ia, serverPort);

        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(soTimeout);
        socket.send(packet);

        byte[] b1 = new byte[2048];
        DatagramPacket dpResponse = new DatagramPacket(b1, b1.length);

        try {
            socket.receive(dpResponse);
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout reached while receiving data from Boostrap server " + e);
            socket.close();
            return null;
        }
        String bsResponse = new String(dpResponse.getData());
        System.out.println("result is " + bsResponse);
        socket.close();
        return bsResponse.trim();

    }
}