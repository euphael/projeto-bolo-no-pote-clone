package cake.backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckerController {
    @GetMapping
    public ResponseEntity<String> get() {
        return new ResponseEntity<>("successfully", HttpStatus.OK);
    }
}
