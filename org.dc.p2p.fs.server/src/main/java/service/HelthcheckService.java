package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HelthcheckService {

    private static final Logger log = LoggerFactory.getLogger(HelthcheckService.class);
    public static ScheduledExecutorService executor;
    private Node node;
    private String hltMsg = "HEALTH";

    public HelthcheckService(Node node){
        log.info("Initializing HealthCheck API...");
        this.node = node;
        executor = Executors.newScheduledThreadPool(2);
    }

    public void scheduleTask(List<Neighbour> neighbourList) {
        Runnable neighbour1 = () -> {
            try {
                if (!neighbourList.isEmpty() && neighbourList.get(0) != null) {
                    boolean neighbourHealthy = checkServerHealth(neighbourList.get(0));
                    if(!neighbourHealthy){
                        node.unRegisterNode();
                    }
                } else {
                    log.warn("No neighbour nodes found to start HealthCheck API.");
                }
            } catch (Exception e) {
                log.error("Health Check failed for the neighbour " +
                        neighbourList.get(0).getIp() + ":" + neighbourList.get(0).getPort(),e);
                try {
                    node.unRegisterNode();
                } catch (Exception exception) {
                    log.error("Could not unregister Node after Healthcheck failure.");
                }
            }
        };
        Runnable neighbour2 = () -> {
            try {
                if (!neighbourList.isEmpty() && neighbourList.size()>1 && neighbourList.get(1) != null) {
                    boolean neighbourHealthy = checkServerHealth(neighbourList.get(1));
                    if(!neighbourHealthy){
                        node.unRegisterNode();
                    }
                } else {
                    log.warn("No 2nd neighbour node found to start HealthCheck API.");
                }
            } catch (Exception e) {
                log.error("Health Check failed for the neighbour " +
                        neighbourList.get(1).getIp() + ":" + neighbourList.get(1).getPort(),e);
                try {
                    node.unRegisterNode();
                } catch (Exception exception) {
                    log.error("Could not unregister Node after Healthcheck failure.");
                }
            }
        };

        executor.scheduleAtFixedRate(neighbour1, 5, 30, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(neighbour2, 5, 30, TimeUnit.SECONDS);
    }

    private boolean checkServerHealth(Neighbour neighbour) throws Exception {

        log.info("Health check on "+neighbour.getIp()+" "+neighbour.getPort());
        InetAddress ia = InetAddress.getByName(neighbour.getIp());
        String healthMessage = String.format("%04d", hltMsg.length() + 5) + " " + hltMsg;
        byte[] messageBytes = healthMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, ia, neighbour.getPort());

        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(node.soTimeout);
        socket.send(packet);

        byte[] b1 = new byte[2048];
        DatagramPacket dpResponse = new DatagramPacket(b1, b1.length);

        try {
            socket.receive(dpResponse);
        } catch (SocketTimeoutException e) {
            log.error("Timeout reached for the Health Check API while receiving data from Boostrap server " , e);
            socket.close();
        }
        String bsResponse = new String(dpResponse.getData(),0,dpResponse.getLength());
        socket.close();
        return processHealth(bsResponse);
    }

    private boolean processHealth(String msg){
        String[] mes = msg.split(" ");
        return mes[1].equals("HEALTHOK");
    }
}
