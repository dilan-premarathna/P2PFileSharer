package server;

/**
 * @author janaka
 */
public class Neighbour extends Host {
   
    private boolean isAlive = false;

    public Neighbour(String ip, int port) {
        super(ip, port);
    }

    /**
     * @return the isAlive
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * @param isAlive the isAlive to set
     */
    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

}
