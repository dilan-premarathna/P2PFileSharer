package conf;


import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * @author janaka
 */
public class ServerConfigurations {
    public static Properties prop = new Properties();
    public static final Properties props = new Properties();
    public static final List<String> randomNameList = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(ServerConfigurations.class);

    public ServerConfigurations() {
        try {
            String propFileLocation = System.getProperty("propFileLocation");
            props.load(new FileInputStream(propFileLocation));
            fileListInitializer();
            logger.info("Random File names for the App instance is " + randomNameList.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fileListInitializer() {
            String fileListPath = props.getProperty("fileNameList.location");
        Charset charset = StandardCharsets.ISO_8859_1;
        try {
            List<String> result = Files.readAllLines(Paths.get(fileListPath), charset);
            int size = result.size();
            for (int i=0;i<3;i++) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, size);
                randomNameList.add(result.get(randomNum));
                result.remove(randomNum);
                size--;
            }
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
