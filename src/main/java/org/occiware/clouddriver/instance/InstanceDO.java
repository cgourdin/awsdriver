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
package org.occiware.clouddriver.instance;

import org.occiware.clouddriver.IAM.AclDO;
import org.occiware.clouddriver.IAM.IamInstanceProfileDO;
import org.occiware.clouddriver.IAM.SecurityGroupDO;
import org.occiware.clouddriver.image.ImageDO;
import org.occiware.clouddriver.image.SnapshotDO;
import org.occiware.clouddriver.network.*;
import org.occiware.clouddriver.storage.InstanceVolumeDO;
import org.occiware.clouddriver.storage.VolumeDO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Instance data object, this represents an ec2 compute.
 * Created by Christophe Gourdin on 10/12/2016.
 */
public class InstanceDO implements Serializable {

    private String instanceId;
    private String imageId;
    private Integer amiLaunchIndex;
    private boolean enaSupport;
    private String instanceType;
    private String system;
    private ImageDO image;
    private String regionId;
    private String zoneId;
    private boolean multiZone;
    private boolean optimizedEbsIO;
    private String name;
    private String instanceState;
    private String description;
    private boolean publicInstance;
    private List<IpAddressDO> ipAddresses;
    private List<NetworkInterfaceDO> networkAdapters;
    private List<LoadBalancerDO> loadBalancers;
    private List<InstanceVolumeDO> volumes;
    private List<SnapshotDO> snapshots;
    private List<SecurityGroupDO> securityGroups;
    private List<AclDO> acls;
    private String instanceClusteredGroupName;
    private String rootDeviceType;
    private String architecture;
    private PlacementDO placement;
    private String hypervisor;

    private String userData;
    private IamInstanceProfileDO iamInstanceProfile;
    private String monitoringState;
    private String kernelId;
    private String instanceLifeCycle;
    private String platform;
    private String privateDnsName;
    private String privateIpAddress;
    private String publicDnsName;
    private String publicIpAddress;
    private boolean sourceDestCheck;
    private Date launchTime;
    private String ramDiskId;
    private String rootDeviceName;
    private String spotInstanceRequestId;
    private String sriovNetSuppport;
    private String stateTransitionReason;
    private String subnetId;
    private String virtualizationType;
    private String vpcId;


    /**
     * Instance monitoring activation on AWS Monitoring (cloudwatch).
     */
    private boolean monitoring = false;

    /**
     * Keypair name to use if aws based keypair.
     */
    private String keyPairName;


    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public ImageDO getImage() {
        return image;
    }

    public void setImage(ImageDO image) {
        this.image = image;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public boolean isMultiZone() {
        return multiZone;
    }

    public void setMultiZone(boolean multiZone) {
        this.multiZone = multiZone;
    }

    public boolean isOptimizedEbsIO() {
        return optimizedEbsIO;
    }

    public void setOptimizedEbsIO(boolean optimizedEbsIO) {
        this.optimizedEbsIO = optimizedEbsIO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstanceState() {
        return instanceState;
    }

    public void setInstanceState(String instanceState) {
        this.instanceState = instanceState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublicInstance() {
        return publicInstance;
    }

    public void setPublicInstance(boolean publicInstance) {
        this.publicInstance = publicInstance;
    }

    public List<IpAddressDO> getIpAddresses() {
        return ipAddresses;
    }

    public void setIpAddresses(List<IpAddressDO> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

    public List<NetworkInterfaceDO> getNetworkAdapters() {
        return networkAdapters;
    }

    public void setNetworkAdapters(List<NetworkInterfaceDO> networkAdapters) {
        this.networkAdapters = networkAdapters;
    }

    public List<LoadBalancerDO> getLoadBalancers() {
        return loadBalancers;
    }

    public void setLoadBalancers(List<LoadBalancerDO> loadBalancers) {
        this.loadBalancers = loadBalancers;
    }

    public List<InstanceVolumeDO> getVolumes() {
        if (volumes == null) {
            volumes = new ArrayList<>();
        }
        return volumes;
    }

    public void setVolumes(List<InstanceVolumeDO> volumes) {
        this.volumes = volumes;
    }

    public List<SnapshotDO> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<SnapshotDO> snapshots) {
        this.snapshots = snapshots;
    }

    public List<AclDO> getAcls() {
        return acls;
    }

    public void setAcls(List<AclDO> acls) {
        this.acls = acls;
    }

    public String getInstanceClusteredGroupName() {
        return instanceClusteredGroupName;
    }

    public void setInstanceClusteredGroupName(String instanceClusteredGroupName) {
        this.instanceClusteredGroupName = instanceClusteredGroupName;
    }

    public String getRootDeviceType() {
        return rootDeviceType;
    }

    public void setRootDeviceType(String rootDeviceType) {
        this.rootDeviceType = rootDeviceType;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        if (architecture != null && architecture.equals("i386")) {
            architecture = "x86";
        } else {
            architecture = "x64";
        }
        this.architecture = architecture;
    }

    public String getKeyPairName() {
        return keyPairName;
    }

    public void setKeyPairName(String keyPairName) {
        this.keyPairName = keyPairName;
    }

    public List<SecurityGroupDO> getSecurityGroups() {
        return securityGroups;
    }

    public void setSecurityGroups(List<SecurityGroupDO> securityGroups) {
        this.securityGroups = securityGroups;
    }

    public boolean isMonitoring() {
        return monitoring;
    }

    public void setMonitoring(boolean monitoring) {
        this.monitoring = monitoring;
    }

    public PlacementDO getPlacement() {
        return placement;
    }

    public void setPlacement(PlacementDO placement) {
        this.placement = placement;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public Integer getAmiLaunchIndex() {
        return amiLaunchIndex;
    }

    public void setAmiLaunchIndex(Integer amiLaunchIndex) {
        this.amiLaunchIndex = amiLaunchIndex;
    }

    public boolean isEnaSupport() {
        return enaSupport;
    }

    public void setEnaSupport(boolean enaSupport) {
        this.enaSupport = enaSupport;
    }

    public String getHypervisor() {
        return hypervisor;
    }

    public void setHypervisor(String hypervisor) {
        this.hypervisor = hypervisor;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public IamInstanceProfileDO getIamInstanceProfile() {
        return iamInstanceProfile;
    }

    public void setIamInstanceProfile(IamInstanceProfileDO iamInstanceProfile) {
        this.iamInstanceProfile = iamInstanceProfile;
    }

    public String getMonitoringState() {
        return monitoringState;
    }

    public void setMonitoringState(String monitoringState) {
        this.monitoringState = monitoringState;
    }


    public String getKernelId() {
        return kernelId;
    }

    public void setKernelId(String kernelId) {
        this.kernelId = kernelId;
    }

    public String getInstanceLifeCycle() {
        return instanceLifeCycle;
    }

    public void setInstanceLifeCycle(String instanceLifeCycle) {
        this.instanceLifeCycle = instanceLifeCycle;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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

    public String getPublicDnsName() {
        return publicDnsName;
    }

    public void setPublicDnsName(String publicDnsName) {
        this.publicDnsName = publicDnsName;
    }

    public String getPublicIpAddress() {
        return publicIpAddress;
    }

    public void setPublicIpAddress(String publicIpAddress) {
        this.publicIpAddress = publicIpAddress;
    }

    public boolean isSourceDestCheck() {
        return sourceDestCheck;
    }

    public void setSourceDestCheck(boolean sourceDestCheck) {
        this.sourceDestCheck = sourceDestCheck;
    }

    public Date getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Date launchTime) {
        this.launchTime = launchTime;
    }

    public String getRamDiskId() {
        return ramDiskId;
    }

    public void setRamDiskId(String ramDiskId) {
        this.ramDiskId = ramDiskId;
    }

    public String getRootDeviceName() {
        return rootDeviceName;
    }

    public void setRootDeviceName(String rootDeviceName) {
        this.rootDeviceName = rootDeviceName;
    }

    public String getSpotInstanceRequestId() {
        return spotInstanceRequestId;
    }

    public void setSpotInstanceRequestId(String spotInstanceRequestId) {
        this.spotInstanceRequestId = spotInstanceRequestId;
    }

    public String getSriovNetSuppport() {
        return sriovNetSuppport;
    }

    public void setSriovNetSuppport(String sriovNetSuppport) {
        this.sriovNetSuppport = sriovNetSuppport;
    }

    public String getStateTransitionReason() {
        return stateTransitionReason;
    }

    public void setStateTransitionReason(String stateTransitionReason) {
        this.stateTransitionReason = stateTransitionReason;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public String getVirtualizationType() {
        return virtualizationType;
    }

    public void setVirtualizationType(String virtualizationType) {
        this.virtualizationType = virtualizationType;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }
}
