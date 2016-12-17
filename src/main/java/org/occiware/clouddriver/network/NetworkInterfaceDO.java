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
package org.occiware.clouddriver.network;

import org.occiware.clouddriver.IAM.GroupIdentifierDO;

import java.util.Date;
import java.util.List;

/**
 * Created by christophe on 10/12/2016.
 */
public class NetworkInterfaceDO {

    private String description;
    private String macAddress;
    private String networkInterfaceId;
    private String ownerId;
    private String privateDnsName;
    private String privateIpAddress;
    private boolean sourceDestCheck;
    private String status;
    private String subnetId;
    private String vpcId;

    private List<GroupIdentifierDO> groupIdentifierDOs;
    private List<String> ipv6Addresses;
    private List<IpAddressDO> ipAddresses;
    private List<GroupIdentifierDO> securityGroups;
    private NetAssociationDO netAssociation;

    // Attachment part.
    private String attachmentId;
    private Date attachTime;
    private boolean deleteOnTermination;
    private Integer deviceIndex;
    private String attachmentStatus;

    public List<GroupIdentifierDO> getGroupIdentifierDOs() {
        return groupIdentifierDOs;
    }

    public void setGroupIdentifierDOs(List<GroupIdentifierDO> groupIdentifierDOs) {
        this.groupIdentifierDOs = groupIdentifierDOs;
    }

    public List<String> getIpv6Addresses() {
        return ipv6Addresses;
    }

    public void setIpv6Addresses(List<String> ipv6Addresses) {
        this.ipv6Addresses = ipv6Addresses;
    }

    public List<IpAddressDO> getIpAddresses() {
        return ipAddresses;
    }

    public void setIpAddresses(List<IpAddressDO> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

    public NetAssociationDO getNetAssociation() {
        return netAssociation;
    }

    public void setNetAssociation(NetAssociationDO netAssociation) {
        this.netAssociation = netAssociation;
    }

    public List<GroupIdentifierDO> getSecurityGroups() {
        return securityGroups;
    }

    public void setSecurityGroups(List<GroupIdentifierDO> securityGroups) {
        this.securityGroups = securityGroups;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getNetworkInterfaceId() {
        return networkInterfaceId;
    }

    public void setNetworkInterfaceId(String networkInterfaceId) {
        this.networkInterfaceId = networkInterfaceId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPrivateDnsName() {
        return privateDnsName;
    }

    public void setPrivateDnsName(String privateDnsName) {
        this.privateDnsName = privateDnsName;
    }

    public String getPrivateIpAddress() {
        return privateIpAddress;
    }

    public void setPrivateIpAddress(String privateIpAddress) {
        this.privateIpAddress = privateIpAddress;
    }

    public boolean isSourceDestCheck() {
        return sourceDestCheck;
    }

    public void setSourceDestCheck(boolean sourceDestCheck) {
        this.sourceDestCheck = sourceDestCheck;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

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

    public String getAttachmentStatus() {
        return attachmentStatus;
    }

    public void setAttachmentStatus(String attachmentStatus) {
        this.attachmentStatus = attachmentStatus;
    }

    public Integer getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Integer deviceIndex) {
        this.deviceIndex = deviceIndex;
    }
}
