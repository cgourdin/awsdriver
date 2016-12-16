package org.occiware.clouddriver.instance;

/**
 * Created by christophe on 10/12/2016.
 */
public class InstanceOperationException extends Exception {
    public InstanceOperationException() {
        super();
    }

    public InstanceOperationException(String message) {
        super(message);
    }

    public InstanceOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstanceOperationException(Throwable cause) {
        super(cause);
    }
}
