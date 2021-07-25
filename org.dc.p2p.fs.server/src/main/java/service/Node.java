package service;

import util.Service;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private String name;
    private String bsServerIP = "127.0.0.1";
    private int bsServerPort = 55555;
    private Service service = new Service();
    private final List<Neighbour> neighboursList = new ArrayList<>();

    public void registerNode() throws Exception {

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
}
