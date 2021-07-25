package conf;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author janaka
 */
public class ServerConfigurations {
    public static Properties prop = new Properties();
    public static final Properties props = new Properties();

    public ServerConfigurations() {
        try {
            String propFileLocation = System.getProperty("propFileLocation");
            props.load(new FileInputStream(propFileLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServerIP(){
        return getStringProperty("SERVER_IP");
    }

    public int getServerPort(){
        return getIntegerProperty("SERVER_PORT");
    }

    public String getBSIP(){
        return getStringProperty("BS_IP");
    }

    public int getBSPort(){
        return getIntegerProperty("BS_PORT");
    }

    public String getFilesStorage(){
        return getStringProperty("FILE_STORAGE");
    }

    private int getIntegerProperty(String propertyName){
        try{
            prop.load(new FileInputStream("org.dc.p2p.fs.ui/src/main/resources/config.properties"));
            return Integer.valueOf(prop.getProperty(propertyName));
        }catch(ClassCastException ex){
            //System.out.println(ex.getMessage());
            return -1;
        }catch(IOException ex){
            //System.out.println(ex.getMessage());
            return -1;
        }catch (NumberFormatException ex){
            return -1;
        }
    }

    public String getStringProperty(String propertyName){
        try{
            prop.load(new FileInputStream("org.dc.p2p.fs.ui/src/main/resources/config.properties"));
            return prop.getProperty(propertyName);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
