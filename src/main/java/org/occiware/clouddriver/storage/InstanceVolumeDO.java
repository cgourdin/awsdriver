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
package org.occiware.clouddriver.storage;

import java.util.Date;

/**
 * Created by christophe on 16/12/2016.
 * Ebs volume on instance.
 */
public class InstanceVolumeDO {

    private Date attachTime;
    private boolean deleteOnTermination;
    private String status;
    private String deviceName;
    private String volumeId;

    public Date getAttachTime() {
        return attachTime;
    }

    public void setAttachTime(Date attachTime) {
        this.attachTime = attachTime;
    }

    public boolean isDeleteOnTermination() {
        return deleteOnTermination;
    }

    public void setDeleteOnTermination(boolean deleteOnTermination) {
        this.deleteOnTermination = deleteOnTermination;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
