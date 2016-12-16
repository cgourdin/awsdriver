package org.occiware.clouddriver.client;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import org.apache.log4j.Logger;

/**
 * Created by christophe on 10/12/2016.
 */
public class AwsS3Client {

    public static final Logger logger = Logger.getLogger(AwsS3Client.class);

    private AmazonS3Client s3Client = null;

    public AwsS3Client(final String accessKey, final String secretKey) {
        ClientConfiguration config = new ClientConfiguration();
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Client = new AmazonS3Client(credentials, config);
    }

    public boolean checkClientConnection() {
        boolean result = false;

        if (s3Client == null) {
            return result;
        }
       // TODO : execute client list files on root directory.


        return result;
    }

    public AmazonS3Client getClient() {

        return s3Client;
    }

}
