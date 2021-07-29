package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class Service {

    private static final Logger log = LoggerFactory.getLogger(Service.class);

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
            log.error("Timeout reached while receiving data from Boostrap server ");
            socket.close();
            return null;
        }
        String bsResponse = new String(dpResponse.getData());
        log.info("result is " + bsResponse);
        socket.close();
        return bsResponse.trim();

    }

    public void send(String message, String ip, int port) throws IOException {

        log.info(message);
        DatagramSocket socket;

        socket = new DatagramSocket();

        InetAddress IPAddress = InetAddress.getByName(ip);
        byte[] toSend  = message.getBytes();
        DatagramPacket packet =new DatagramPacket(toSend, toSend.length, IPAddress, port);
        socket.send(packet);
        socket.close();

    }

    public String receive(int port) throws IOException{

        DatagramSocket socket_re;
        socket_re = new DatagramSocket(port);

        byte[] buf = new byte[65536];
        DatagramPacket incoming = new DatagramPacket(buf, buf.length);
        socket_re.receive(incoming);

        String data = new String(incoming.getData(), 0, incoming.getLength());

        return data;
    }
}
