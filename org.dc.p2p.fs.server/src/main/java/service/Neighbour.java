package service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

class Neighbour {
    private String ip;
    private int port;

    public Neighbour(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public String getIp(){
        return this.ip;
    }

    public int getPort(){
        return this.port;
    }

    public boolean NeighbourConnect(List<Neighbour> neighbourList)  throws Exception {

        String joinMessage ="0027 JOIN 64.12.123.190 432";
        InetAddress ia = InetAddress.getByName(getIp());
        byte[] messageBytes = joinMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, ia, getPort());

        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);

        byte[] b1 = new byte[2048];
        DatagramPacket dpResponse = new DatagramPacket(b1, b1.length);
        socket.receive(dpResponse);

        String nodeResponse = new String(dpResponse.getData());
        System.out.println("result is " + nodeResponse);
        socket.close();
        return processNodeResponse(nodeResponse.trim(),neighbourList);
    }

    public boolean processNodeResponse(String message, List<Neighbour> neighbourList)
            throws Exception {

        String[] mes = message.split(" ");
        System.out.println(message);
        System.out.println("processNodeResponse: " + mes[2]);


        if (mes[1].equals("JOINOK")) {
            if (mes[2].equals("0")) {
                for (Neighbour neb:neighbourList) {
                    if(neb.getIp() == this.getIp() && neb.getPort() == this.getPort())
                        return false;
                }
                neighbourList.add(this);

            } else if (mes[2].equals("9999")) {
                throw new Exception("failed, can’t register." + " initial join failed");
            }
        }
        return true;
    }


}