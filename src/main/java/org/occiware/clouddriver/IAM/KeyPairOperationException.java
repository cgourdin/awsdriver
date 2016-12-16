package org.occiware.clouddriver.IAM;

/**
 * Created by christophe on 11/12/2016.
 */
public class KeyPairOperationException extends Exception {


    public KeyPairOperationException() {
    }

    public KeyPairOperationException(String message) {
        super(message);
    }

    public KeyPairOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyPairOperationException(Throwable cause) {
        super(cause);
    }
}
