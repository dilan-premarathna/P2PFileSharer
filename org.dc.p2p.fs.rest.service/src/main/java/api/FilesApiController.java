package api;

import conf.ServerConfigurations;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

@RestController
public class FilesApiController implements FilesApi {

    private static final Logger log = LoggerFactory.getLogger(FilesApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private DocumentStorageService docStorageService;

    @org.springframework.beans.factory.annotation.Autowired
    public FilesApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Resource> filesFileByNameGet(@NotNull @Parameter(in = ParameterIn.QUERY,
            description = "file name that need to be downloaded from the peer" ,required=true,
            schema=@Schema()) @Valid @RequestParam(value = "name", required = true) String name) {

        String accept = request.getHeader("Accept");
        if (accept != null && (accept.contains("multipart/form-data") || accept.contains("application/octet-stream"))) {
            try {
                Resource resource = null;
                String realFileName = "";
                if (name != null && !name.isEmpty()) {
                    try {
                        if (!ServerConfigurations.randomNameList.stream().anyMatch(name::equalsIgnoreCase)) {
                            log.warn("File name \"" + name + "\" not found in the system!.");
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                        }
                        realFileName =  getRealFileName(name);
                        resource = docStorageService.getFileAsResource(realFileName);
                    } catch (Exception e) {
                        log.error("Error while generating \"" + realFileName + " file.", e);
                    }
                    log.info("Downloading \"" + realFileName + "\" file by client " + request.getRemoteAddr());
                    return ResponseEntity.ok().contentType(MediaType.parseMediaType(accept))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
                }
                log.error("Missing required \"name\" query parameter.");
                return ResponseEntity.status(Response.SC_BAD_REQUEST).build();
            } catch (Exception e) {
                log.error("Couldn't build response for content type multipart/form-data or application/octet-stream", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        log.error("Bad accept header. Accept multipart/form-data or application/octet-stream only.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    private String getRealFileName(String name) {
        for (String  fileName : ServerConfigurations.randomNameList) {
            if (name.equalsIgnoreCase(fileName.toLowerCase())) {
                return fileName;
            }
        }
        log.warn("File name \"" + name +"\" not found in the Server.");
        return name;
    }
}
