package util;

public class Query {
    private String initNodeIP;
    private int initNodePort;
    private long msgID;
    private String fName;
    private int hCount;

    public Query(String initNodeIP, int initNodePort, String fName, int hCount){
        this.initNodeIP = initNodeIP;
        this.initNodePort = initNodePort;
        this.fName = fName;
        this.hCount = hCount;
    }

    public String getInitNodeIP() {
        return initNodeIP;
    }

    public String getfName() {
        return fName;
    }

    public int gethCount() {
        return hCount;
    }

    public void updatehCount() {
        hCount = hCount-1;
    }

    public int getInitNodePort() {
        return initNodePort;
    }

    public String getMsgString() {
        String msg = " SER " + initNodeIP + " " + initNodePort + " " + fName + " " + gethCount();
        int length = msg.length()+5;
        return String.format("%04d", length) + msg;
    }
}

