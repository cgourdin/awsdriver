package org.occiware.clouddriver.IAM;

/**
 * Created by christophe on 16/12/2016.
 */
public class IamInstanceProfileDO {
    private String arn;
    private String profileId;

    public String getArn() {
        return arn;
    }

    public void setArn(String arn) {
        this.arn = arn;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}
