package util;

public class Result {

    private String ip;
    private int port;
    private String[] fileList;

    public void setResult(String ip, int port, String[] fileList) {
        this.ip = ip;
        this.port = port;
        this.fileList = fileList;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String[] getFileList() {
        return fileList;
    }

}
