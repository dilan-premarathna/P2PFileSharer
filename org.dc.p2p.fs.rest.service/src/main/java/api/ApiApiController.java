package api;

import model.HealthCheckResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
public class ApiApiController implements ApiApi {

    private static final Logger log = LoggerFactory.getLogger(ApiApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ApiApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<HealthCheckResponseDTO> apiHealthcheckGet() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            HealthCheckResponseDTO response = new HealthCheckResponseDTO();
            response.status("Server Up");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        log.error("The accept message type should be application/json.");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
