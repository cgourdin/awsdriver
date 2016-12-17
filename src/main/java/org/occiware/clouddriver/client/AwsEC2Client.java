package org.occiware.clouddriver.client;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import org.apache.log4j.Logger;

/**
 * EC2 client.
 * Created by Christophe Gourdin on 10/12/2016.
 */
public class AwsEC2Client {

    public static final Logger logger = Logger.getLogger(AwsEC2Client.class);

    private AmazonEC2Client ec2Client = null;


    public AwsEC2Client(final String accessKey, final String secretKey) {
        ClientConfiguration config = new ClientConfiguration();
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        ec2Client = new AmazonEC2Client(credentials, config);

    }


    public boolean checkConnection() {
        boolean result = false;
        if (ec2Client == null) {
            return result;
        }

        // TODO : Make a test connection with a describe available regions.

        return result;
    }

    public AmazonEC2Client getClientInstance() {
        return this.ec2Client;
    }

    public void disconnectClient() {
        this.ec2Client.shutdown();
    }

}
