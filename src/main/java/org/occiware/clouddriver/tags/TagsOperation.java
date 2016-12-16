/**
 * Copyright (c) 2016 Christophe Gourdin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Contributors:
 * - Christophe Gourdin
 */
package org.occiware.clouddriver.tags;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.Tag;
import org.apache.log4j.Logger;
import org.occiware.clouddriver.client.AwsEC2Client;
import org.occiware.clouddriver.instance.InstanceOperationException;

/**
 * Define aws tags on resources, delete tags.
 * Created by christophe on 10/12/2016.
 */
public class TagsOperation {

    public static final Logger logger = Logger.getLogger(TagsOperation.class);

    private AwsEC2Client ec2Client;

    public TagsOperation(AwsEC2Client ec2Client) {
        this.ec2Client = ec2Client;
    }

    /**
     * Create a new tag on a resource.
     * @param resourceId
     * @param tagName
     * @param tagValue
     * @throws InstanceOperationException
     */
    public void createTag(final String resourceId, final String tagName, final String tagValue) throws InstanceOperationException {

        CreateTagsRequest createTagsRequest = new CreateTagsRequest();
        createTagsRequest.withResources(resourceId)
                .withTags(new Tag(tagName, tagValue));
        try {
            ec2Client.getClientInstance().createTags(createTagsRequest);
        } catch (AmazonServiceException ase) {
            logger.error("Exception thrown from aws : " + ase.getErrorCode() + " --> " + ase.getErrorMessage());
            ec2Client.getClientInstance().shutdown();
            throw new InstanceOperationException(ase);
        } catch (AmazonClientException ace) {
            logger.error("Exception thrown from aws : " + ace.getMessage());
            ec2Client.getClientInstance().shutdown();
            throw new InstanceOperationException(ace);
        }

    }

}
