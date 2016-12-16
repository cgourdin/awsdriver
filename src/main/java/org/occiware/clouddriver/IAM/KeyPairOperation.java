package org.occiware.clouddriver.IAM;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;
import org.apache.log4j.Logger;
import org.occiware.clouddriver.client.AwsEC2Client;
import org.occiware.clouddriver.instance.InstanceOperationException;

import java.util.HashMap;
import java.util.Map;

/**
 * Key pair operation management helper.
 * Created by christophe on 10/12/2016.
 */
public class KeyPairOperation {

    public static final Logger logger = Logger.getLogger(KeyPairOperation.class);

    private AwsEC2Client ec2Client;

    public KeyPairOperation(AwsEC2Client ec2Client) {
        this.ec2Client = ec2Client;
    }


    /**
     * Create a new Key Pair on AWS side.
     * @param keyPairName
     * @return
     * @throws KeyPairOperationException
     */
    public KeyPairDO createKeyPair(final String keyPairName) throws KeyPairOperationException {

        if (keyPairName == null) {
            throw new KeyPairOperationException("The 'keyPairName' must be provided for operation create KeyPair. Constraints: Accepts alphanumeric characters, spaces, dashes, and underscores.");
        }
        KeyPairDO keyPairDO = new KeyPairDO();


        try {
            CreateKeyPairResult result = ec2Client.getClientInstance().createKeyPair(new CreateKeyPairRequest(keyPairName));

            keyPairDO.setFingerPrintPrivateKey(result.getKeyPair().getKeyFingerprint());
            keyPairDO.setPrivateKey(result.getKeyPair().getKeyMaterial());
            keyPairDO.setKeyPairName(result.getKeyPair().getKeyName());
            ec2Client.getClientInstance().shutdown();

        } catch (AmazonServiceException ase) {
            logger.error("Exception thrown from aws : " + ase.getErrorCode() + " --> " + ase.getErrorMessage());
            throw new KeyPairOperationException(ase);
        } catch (AmazonClientException ace) {
            logger.error("Exception thrown from aws : " + ace.getMessage());
            throw new KeyPairOperationException(ace);
        } finally {
            ec2Client.getClientInstance().shutdown();
        }
        return keyPairDO;
    }

    /**
     * Import key pair to AWS.
     * @param keyPair a keypair data object with a public key and a name set..
     * @throws KeyPairOperationException Exception when aws exception when importing a new key pair.
     */
    public void importKeyPair(KeyPairDO keyPair) throws KeyPairOperationException {

        String keyPairName = keyPair.getKeyPairName();
        String encodedPublicKey = keyPair.getPublicKey();  // Base 64 encoded, DER

        if (keyPairName == null) {
            throw new KeyPairOperationException("The keyPair name must be provided for operation import KeyPair.");
        }
        if (encodedPublicKey == null) {
            throw new KeyPairOperationException("The keyPair public key encoded base 64, DER must be provided for operation importKeyPair.");
        }
        try {
            ImportKeyPairResult result = ec2Client.getClientInstance().importKeyPair(new ImportKeyPairRequest(keyPairName, encodedPublicKey));
            keyPair.setKeyPairName(result.getKeyName());
            keyPair.setFingerPrintPublicKey(result.getKeyFingerprint());
            ec2Client.getClientInstance().shutdown();
        } catch (AmazonServiceException ase) {
            logger.error("Exception thrown from aws : " + ase.getErrorCode() + " --> " + ase.getErrorMessage());
            throw new KeyPairOperationException(ase);
        } catch (AmazonClientException ace) {
            logger.error("Exception thrown from aws : " + ace.getMessage());
            throw new KeyPairOperationException(ace);
        } finally {
            ec2Client.getClientInstance().shutdown();
        }
    }

    /**
     * Delete a key pair from AWS EC2 Service.
     * @param keyPairName
     * @throws KeyPairOperationException
     */
    public void deleteKeyPair(final String keyPairName) throws KeyPairOperationException {

        if (keyPairName == null) {
            throw new KeyPairOperationException("The keyPair name must be provided for operation delete KeyPair.");
        }
        try {
            ec2Client.getClientInstance().deleteKeyPair(new DeleteKeyPairRequest(keyPairName));
        } catch (AmazonServiceException ase) {
            logger.error("Exception thrown from aws : " + ase.getErrorCode() + " --> " + ase.getErrorMessage());
            throw new KeyPairOperationException(ase);
        } catch (AmazonClientException ace) {
            logger.error("Exception thrown from aws : " + ace.getMessage());
            throw new KeyPairOperationException(ace);
        } finally {
            ec2Client.getClientInstance().shutdown();
        }


    }




}
