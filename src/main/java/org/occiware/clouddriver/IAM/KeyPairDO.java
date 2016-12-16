package org.occiware.clouddriver.IAM;

/**
 * Created by christophe on 10/12/2016.
 */
public class KeyPairDO {

    private String privateKey;
    private String publicKey;

    private String fingerPrintPrivateKey;
    private String fingerPrintPublicKey;
    private String keyPairName;

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getFingerPrintPrivateKey() {
        return fingerPrintPrivateKey;
    }

    public void setFingerPrintPrivateKey(String fingerPrintPrivateKey) {
        this.fingerPrintPrivateKey = fingerPrintPrivateKey;
    }

    public String getKeyPairName() {
        return keyPairName;
    }

    public void setKeyPairName(String keyPairName) {
        this.keyPairName = keyPairName;
    }

    public String getFingerPrintPublicKey() {
        return fingerPrintPublicKey;
    }

    public void setFingerPrintPublicKey(String fingerPrintPublicKey) {
        this.fingerPrintPublicKey = fingerPrintPublicKey;
    }
}
