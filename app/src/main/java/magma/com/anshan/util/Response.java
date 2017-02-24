package magma.com.anshan.util;

/**
 * Created by Administrator on 2016/11/19 0019.
 */
import java.io.Serializable;

/**
 * Description:
 * Author: xy
 * Date: 2016/4/12 14:49
 */
public class Response implements Serializable {
    private static final long serialVersionUID = 8980216391057926016L;
    public int status;
    public String message;
    public Object response;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}