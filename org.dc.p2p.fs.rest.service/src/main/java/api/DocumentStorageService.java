package api;

import conf.ServerConfigurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import static java.nio.file.Files.readAllBytes;

@Service
public class DocumentStorageService {

    private static final Logger log = LoggerFactory.getLogger(DocumentStorageService.class);

    public Resource getFileAsResource(String name) throws MalformedURLException {
        File file = new File(ServerConfigurations.props.getProperty("fileDownload.tempDir") + "/" + name);
        RandomAccessFile rafile;
        try {
            rafile = new RandomAccessFile(file, "rw");
            byte[] array = new byte[70]; // length is bounded by 70
            new Random().nextBytes(array);
            rafile.writeBytes(new String(array, StandardCharsets.UTF_8));
            long randomNum = ThreadLocalRandom.current().nextInt(1, 11);
            rafile.setLength(randomNum*1048576);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Path DirectoryPath = Paths.get(ServerConfigurations.props.getProperty("fileDownload.tempDir")).toAbsolutePath().normalize();
        printMD5ofFile(DirectoryPath.resolve(name), name);
        return new UrlResource( DirectoryPath.resolve(name).toUri());

    }

    private static void printMD5ofFile(Path filePath, String name) {
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
        log.info("MD5sum of the file " + name + " is " + myChecksum);
    }
}
