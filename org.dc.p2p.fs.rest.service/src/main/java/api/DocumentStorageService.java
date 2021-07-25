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

import static client.api.downloadUtil.printMD5ofFile;
import static java.nio.file.Files.readAllBytes;

@Service
public class DocumentStorageService {

    private static final Logger log = LoggerFactory.getLogger(DocumentStorageService.class);

    public Resource getFileAsResource(String name) throws MalformedURLException {
        File file = new File(ServerConfigurations.props.getProperty("file.tempDir") + "/" + name);
        RandomAccessFile rafile;
        try {
            rafile = new RandomAccessFile(file, "rw");
            byte[] array = new byte[70]; // length is bounded by 70
            new Random().nextBytes(array);
            rafile.writeBytes(new String(array, StandardCharsets.UTF_8));
            long randomNum = ThreadLocalRandom.current().nextInt(1, 11);
            rafile.setLength(randomNum*1048576);
            log.info("File size of the \"" + name + "\" file is " + randomNum + " MB");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Path DirectoryPath = Paths.get(ServerConfigurations.props.getProperty("file.tempDir")).toAbsolutePath().normalize();
        printMD5ofFile(DirectoryPath.resolve(name), name);
        return new UrlResource( DirectoryPath.resolve(name).toUri());
    }
}
