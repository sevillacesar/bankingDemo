package ec.com.banking.demo.account.client.errors;

import ec.com.banking.demo.account.client.responses.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author cesarsevilla
 * bankingDemoJava
 */

@Component
public class ClientError {

    private final Logger logger = Logger.getLogger(ClientError.class.getName());

    public ResponseEntity<?> handleBadRequest(Exception e) {
        logger.log(Level.WARNING, String.format("Bad request: %s ", e.getMessage()));
        return ResponseEntity.badRequest().body(new BaseResponse(e.getMessage()));
    }

    public ResponseEntity<?> handleNotFound(Exception e) {
        logger.log(Level.WARNING, String.format("Not found: %s ", e.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse(e.getMessage()));
    }

    public ResponseEntity<?> handleInternalError(Exception e) {
        logger.log(Level.SEVERE, String.format("Internal server error:  %s", e.getMessage()), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse("Internal server error"));
    }

    public Map<String, String> validationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(
                err -> errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage()));
        return errors;
    }
}
