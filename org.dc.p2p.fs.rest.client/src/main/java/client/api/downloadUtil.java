package client.api;

import client.handler.ApiException;
import conf.ServerConfigurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;

import static java.nio.file.Files.readAllBytes;

public class downloadUtil {
    private static final Logger log = LoggerFactory.getLogger(downloadUtil.class);

    public static void downloadFile(String ip, String port, String fileName) {
        FileDownloaderServiceApi apiInstance = new FileDownloaderServiceApi();
        try {
            File file =  apiInstance.filesFileByNameGet(fileName);
            Path downloadedFile = Paths.get(ServerConfigurations.props.getProperty("file.downloadDir")).
                    toAbsolutePath().normalize().resolve(fileName);
            Files.copy(file.toPath(), downloadedFile, StandardCopyOption.REPLACE_EXISTING);
            log.info("File \"" + fileName + "\" Finished Downloading.");
            printMD5ofFile(downloadedFile,fileName);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void printMD5ofFile(Path filePath, String name) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(readAllBytes(filePath));
        } catch (Exception e) {
            log.error("Error while calculating MD5sum of " + name + " file.");
            e.printStackTrace();
        }
        byte[] digest = md.digest();
        String myChecksum = DatatypeConverter.printHexBinary(digest).toUpperCase();
        log.info("MD5sum of the file \"" + name + "\" is " + myChecksum);
    }
}
