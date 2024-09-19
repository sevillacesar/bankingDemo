package ec.com.banking.neo.account.mov.responses;

/**
 * @author cesarsevilla
 * @project bankingDemoJava
 */

public class BaseResponse {
    private String message;

    public BaseResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
