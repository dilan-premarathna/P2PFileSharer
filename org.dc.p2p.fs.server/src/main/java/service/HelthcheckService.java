package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HelthcheckService {

    private static final Logger log = LoggerFactory.getLogger(HelthcheckService.class);
    public static ScheduledExecutorService executor;

    public HelthcheckService(){
        log.info("Initializing HealthCheck API...");
        executor = Executors.newScheduledThreadPool(2);
    }

    public void scheduleTask(List<Neighbour> neighbourList) {
        Runnable neighbour1 = () -> {
            try {
                if (!neighbourList.isEmpty() && neighbourList.get(0) != null) {
                    checkServerHealth(neighbourList.get(0));
                } else {
                    log.info("No neighbour nodes found to start HealthCheck API.");
                }
            } catch (Exception e) {
//                log.error("Health Check failed for the neighbour " +
//                        neighbourList.get(0).getIp() + ":" + neighbourList.get(0).getPort(),e);
                // unreg neighbour
            }
        };
        Runnable neighbour2 = () -> {
            try {
                if (!neighbourList.isEmpty() && neighbourList.size()>1 && neighbourList.get(1) != null) {
                    checkServerHealth(neighbourList.get(1));
                } else {
                    log.info("No 2nd neighbour node found to start HealthCheck API.");
                }
            } catch (Exception e) {
//                log.error("Health Check failed for the neighbour " +
//                        neighbourList.get(1).getIp() + ":" + neighbourList.get(1).getPort(),e);
                // unreg neighbour
            }
        };

        executor.scheduleAtFixedRate(neighbour1, 5, 5, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(neighbour2, 5, 5, TimeUnit.SECONDS);

    }

    private void checkServerHealth(Neighbour neighbour) {

    }
}
