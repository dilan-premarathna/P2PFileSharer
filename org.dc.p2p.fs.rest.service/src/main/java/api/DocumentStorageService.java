package api;

import conf.ServerConfigurations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DocumentStorageService {

    @Value("${fileDownload.tempDir}")
    private String tempDirectory;

    public Resource getFileAsResource(String name) throws MalformedURLException {
        File file = new File(tempDirectory + "/" + name);
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
        Path filePath = Paths.get(tempDirectory).toAbsolutePath().normalize();
        Path targetLocation = filePath.resolve(name);
        return new UrlResource(targetLocation.toUri());

    }
}
