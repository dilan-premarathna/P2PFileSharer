package util;

import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Communicator {

    public void send(String message, String ip, int port) throws IOException {

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