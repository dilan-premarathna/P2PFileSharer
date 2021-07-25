package util;

import service.Neighbour;
import service.Node;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ResponseProcessor implements Runnable {

    private Node node;
    private List<String> message_list;
    private BlockingQueue<String> messageQueue;
    private final int MAX_QUEUE_SIZE = 100;
    private boolean stopListner = false;
    private Service service;

    @Override
    public void run() {

        try {
            String msg;

            while (!stopListner) {
                msg = messageQueue.take();
                this.processMessage(msg);
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    public void processMessage(String message) {

        /*
         BS_Register
         ===========
         length REG IP_address port_no username
         length REGOK no_nodes IP_1 port_1 IP_2 port_2

         BS_Unregister
         =============
         length UNREG IP_address port_no username
         length UNROK value

         Distributed System
         ==================
         Join
         length JOIN IP_address port_no timestamp
         length JOINOK value IP_address port_no timestamp

         Leave
         length LEAVE IP_address port_no timestamp
         length LEAVEOK value timestamp

         Search
         length SER IP port forwarding_IP forwarding_port file_name hops timestamp
         length SEROK no_files IP port forwarding_IP forwarding_port hops searchedFile filename1 filename2 ... ... timestamp

         Requist Neighbours
         length NEREQ IP port count timestamp
         length NERRES IP port value IP_1 port_1 IP_2 port_2 ... ... timestamp

         IsAlive
         length ISALIVE IP port timestamp
         length ALIVE IP port timestamp
         */
        String[] mes = message.split(" ");
        int port;
        String ip;

        System.out.println("New Message: " + message);

        switch (mes[1]) {
            case "JOIN":
                if (!addMessage(message)) {
                    ip = mes[2];
                    port = Integer.parseInt(mes[3]);
                    System.out.println("New JOIN message");
                    node.addNeighbours(new Neighbour(ip, port));

                    String resMsg = (new Message(MessageType.JOINOK,
                            0,
                            node.getIp(),
                            node.getPort())).getMessage();
                    System.out.println("res:" + resMsg);
                    sendMessage(resMsg, ip, port);
                }
                break;

            case "LEAVE":
                if (!addMessage(message)) {
                    ip = mes[2];
                    port = Integer.parseInt(mes[3]);
                    node.removeNeighbour(new Neighbour(ip, port));
                }
                break;
            default:
                System.err.println("Error: " + message);
                break;
        }

    }

    public boolean addMessage(String message) {

        if (message_list.stream().anyMatch((str) -> (str.equals(message)))) {
            return true;
        } else {
            message_list.add(message);
            return false;
        }
    }
}
