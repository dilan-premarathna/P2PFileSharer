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

    public String isConfigEnable(){
        return getStringProperty("ENABLE_CONFIG");
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


    private int getIntegerProperty(String propertyName){
        try{
            prop.load(new FileInputStream("src/main/resources/config.properties"));
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
            prop.load(new FileInputStream("src/main/resources/config.properties"));
            return prop.getProperty(propertyName);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

}
