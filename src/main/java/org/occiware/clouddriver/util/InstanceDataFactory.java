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
package org.occiware.clouddriver.util;

import com.amazonaws.services.ec2.model.*;
import org.occiware.clouddriver.IAM.GroupIdentifierDO;
import org.occiware.clouddriver.IAM.IamInstanceProfileDO;
import org.occiware.clouddriver.instance.InstanceDO;
import org.occiware.clouddriver.instance.PlacementDO;
import org.occiware.clouddriver.instance.ProductCodeDO;
import org.occiware.clouddriver.network.IpAddressDO;
import org.occiware.clouddriver.network.NetAssociationDO;
import org.occiware.clouddriver.network.NetworkInterfaceDO;
import org.occiware.clouddriver.storage.InstanceVolumeDO;
import org.occiware.clouddriver.tags.TagDO;

import java.util.ArrayList;
import java.util.List;

/**
 * Static methods to build instance data object from AWS XML Model.
 * Created by christophe on 17/12/2016.
 */
public class InstanceDataFactory {


    /**
     *
     * @param instancesAWS
     * @return
     */
    public static List<InstanceDO> buildInstancesDatasFromInstancesModels(List<Instance> instancesAWS) {
        if (instancesAWS == null || instancesAWS.isEmpty()) {
            return new ArrayList<>();
        }

        List<InstanceDO> instances = new ArrayList<>();
        for (Instance instance : instancesAWS) {
            InstanceDO instanceDO = buildInstanceDataFromModel(instance);
            instances.add(instanceDO);
        }
        return instances;
    }

    public static InstanceDO buildInstanceDataFromModel(Instance instance) {
        InstanceDO instanceDO = new InstanceDO();
        buildBasicInstanceData(instance, instanceDO);

        Placement placement = instance.getPlacement();
        if (placement != null) {
            PlacementDO placementDO = buildPlacementDO(instanceDO, placement);
            instanceDO.setPlacement(placementDO);
        }

        // Ebs volumes attached on instance.
        if (instance.getBlockDeviceMappings() != null && !instance.getBlockDeviceMappings().isEmpty()) {
            List<InstanceVolumeDO> instanceVolumeDOs = BuildInstanceVolumeDOs(instance);
            instanceDO.setVolumes(instanceVolumeDOs);
        }

        if (instance.getIamInstanceProfile() != null) {
            IamInstanceProfileDO profileDO = buildIamInstanceProfileDO(instance);
            instanceDO.setIamInstanceProfile(profileDO);
        }

        if (instance.getMonitoring() != null) {
            Monitoring monitoring = instance.getMonitoring();
            instanceDO.setMonitoringState(monitoring.getState());
        }

        // Network part.
        if (instance.getNetworkInterfaces() != null && !instance.getNetworkInterfaces().isEmpty()) {
            List<NetworkInterfaceDO> networkInterfaceDOs = buildNetworkInterfacesDatas(instance);
            instanceDO.setNetworkAdapters(networkInterfaceDOs);
        }

        List<ProductCode> productCodes = instance.getProductCodes();
        if (productCodes != null && !productCodes.isEmpty()) {
            List<ProductCodeDO> productCodeDOs = buildProductCodesDatas(productCodes);
            instanceDO.setProductCodes(productCodeDOs);
        }

        List<GroupIdentifier> groups = instance.getSecurityGroups();
        if (groups != null && !groups.isEmpty()) {
            List<GroupIdentifierDO> groupIdentifierDOs = buildSecurityGroupsDatas(groups);
            instanceDO.setSecurityGroups(groupIdentifierDOs);
        }

        InstanceState state = instance.getState();
        if (state != null) {
            instanceDO.setInstanceState(state.getName());
            instanceDO.setInstanceStateCode(state.getCode());
            StateReason stateReason = instance.getStateReason();
            if (stateReason != null) {
                instanceDO.setInstanceStateReasonMessage(stateReason.getMessage());
                instanceDO.setInstanceStateReasonCode(stateReason.getCode());
            }
        }

        List<Tag> tags = instance.getTags();
        if (tags != null && !tags.isEmpty()) {
            List<TagDO> tagDOs = buildTagsDatas(tags);
            instanceDO.setTags(tagDOs);
        }
        return instanceDO;
    }

    /**
     *
     * @param tags
     * @return
     */
    private static List<TagDO> buildTagsDatas(List<Tag> tags) {
        TagDO tagDO;
        List<TagDO> tagDOs = new ArrayList<>();
        for (Tag tag : tags) {
            tagDO = new TagDO(tag.getKey(), tag.getValue());
            tagDOs.add(tagDO);
        }
        return tagDOs;
    }

    /**
     *
     * @param groups
     * @return
     */
    private static List<GroupIdentifierDO> buildSecurityGroupsDatas(List<GroupIdentifier> groups) {
        GroupIdentifierDO groupIdentifierDO;
        List<GroupIdentifierDO> groupIdentifierDOs = new ArrayList<>();
        for (GroupIdentifier group : groups) {
            groupIdentifierDO = new GroupIdentifierDO();
            groupIdentifierDO.setGroupId(group.getGroupId());
            groupIdentifierDO.setGroupName(group.getGroupName());
            groupIdentifierDOs.add(groupIdentifierDO);
        }
        return groupIdentifierDOs;
    }

    /**
     *
     * @param productCodes
     * @return
     */
    private static List<ProductCodeDO> buildProductCodesDatas(List<ProductCode> productCodes) {
        List<ProductCodeDO> productCodeDOs = new ArrayList<>();
        ProductCodeDO productCodeDO;
        for (ProductCode productCode : productCodes) {
            productCodeDO = new ProductCodeDO();
            productCodeDO.setProductCodeId(productCode.getProductCodeId());
            productCodeDO.setProductCodeType(productCode.getProductCodeType());
            productCodeDOs.add(productCodeDO);
        }
        return productCodeDOs;
    }

    /**
     *
     * @param instance
     * @return
     */
    private static List<NetworkInterfaceDO> buildNetworkInterfacesDatas(Instance instance) {
        List<InstanceNetworkInterface> netInts = instance.getNetworkInterfaces();
        InstanceNetworkInterfaceAssociation netIntAsso;
        InstanceNetworkInterfaceAttachment netIntAttach;

        List<GroupIdentifier> groupIdentifiers;
        List<NetworkInterfaceDO> networkInterfaceDOs = new ArrayList<>();
        NetworkInterfaceDO netDO;
        List<InstancePrivateIpAddress> ipAddresses;
        List<InstanceIpv6Address> ipv6Addresses;
        for (InstanceNetworkInterface netInt : netInts) {
            netDO = new NetworkInterfaceDO();
            netDO.setDescription(netInt.getDescription());
            netDO.setMacAddress(netInt.getMacAddress());
            netDO.setNetworkInterfaceId(netInt.getNetworkInterfaceId());
            netDO.setOwnerId(netInt.getOwnerId());
            netDO.setPrivateDnsName(netInt.getPrivateDnsName());
            netDO.setPrivateIpAddress(netInt.getPrivateIpAddress());
            netDO.setSourceDestCheck(netInt.getSourceDestCheck());
            netDO.setStatus(netInt.getStatus());
            netDO.setSubnetId(netInt.getSubnetId());
            netDO.setVpcId(netInt.getVpcId());

            netIntAsso = netInt.getAssociation();
            if (netIntAsso != null) {
                NetAssociationDO associationDO = new NetAssociationDO();
                associationDO.setIpOwnerId(netIntAsso.getIpOwnerId());
                associationDO.setPublicDnsName(netIntAsso.getPublicDnsName());
                associationDO.setPublicIp(netIntAsso.getPublicIp());
                netDO.setNetAssociation(associationDO);
            }

            netIntAttach = netInt.getAttachment();
            if (netIntAttach != null) {
                netDO.setAttachmentId(netIntAttach.getAttachmentId());
                netDO.setAttachTime(netIntAttach.getAttachTime());
                netDO.setDeleteOnTermination(netIntAttach.getDeleteOnTermination());
                netDO.setDeviceIndex(netIntAttach.getDeviceIndex());
                netDO.setAttachmentStatus(netIntAttach.getStatus());
            }

            groupIdentifiers = netInt.getGroups();
            if (groupIdentifiers != null && !groupIdentifiers.isEmpty()) {
                List<GroupIdentifierDO> grpDOs = buildSecurityGroupsDatas(groupIdentifiers);
                netDO.setSecurityGroups(grpDOs);
            }

            ipv6Addresses = netInt.getIpv6Addresses();
            if (ipv6Addresses != null && !ipv6Addresses.isEmpty()) {
                List<String> ipv6AddressesStr = new ArrayList<>();
                for (InstanceIpv6Address ipv6Address : ipv6Addresses) {
                    ipv6AddressesStr.add(ipv6Address.getIpv6Address());
                }
                netDO.setIpv6Addresses(ipv6AddressesStr);
            }

            ipAddresses = netInt.getPrivateIpAddresses();
            if (ipAddresses != null && !ipAddresses.isEmpty()) {
                List<IpAddressDO> ipAddressDOs = new ArrayList<>();
                IpAddressDO addressDO;
                InstanceNetworkInterfaceAssociation netAsso;
                for (InstancePrivateIpAddress ipAddress : ipAddresses) {
                    addressDO = new IpAddressDO();
                    addressDO.setPrimary(ipAddress.isPrimary());
                    addressDO.setPrivateDnsName(ipAddress.getPrivateDnsName());
                    addressDO.setPrivateIpAddress(ipAddress.getPrivateIpAddress());
                    netAsso = ipAddress.getAssociation();
                    if (netAsso != null) {
                        NetAssociationDO associationDO = new NetAssociationDO();
                        associationDO.setIpOwnerId(netAsso.getIpOwnerId());
                        associationDO.setPublicDnsName(netAsso.getPublicDnsName());
                        associationDO.setPublicIp(netAsso.getPublicIp());
                        addressDO.setNetAssociation(associationDO);
                    }
                    ipAddressDOs.add(addressDO);
                }
                netDO.setIpAddresses(ipAddressDOs);
            }

            networkInterfaceDOs.add(netDO);
        }
        return networkInterfaceDOs;
    }

    /**
     *
     * @param instance
     * @return
     */
    private static IamInstanceProfileDO buildIamInstanceProfileDO(Instance instance) {
        IamInstanceProfile profile = instance.getIamInstanceProfile();
        IamInstanceProfileDO profileDO = new IamInstanceProfileDO();
        profileDO.setArn(profile.getArn());
        profileDO.setProfileId(profile.getId());
        return profileDO;
    }

    /**
     *
     * @param instance
     * @return
     */
    private static List<InstanceVolumeDO> BuildInstanceVolumeDOs(Instance instance) {
        List<InstanceBlockDeviceMapping> blockDeviceMappings = instance.getBlockDeviceMappings();
        String deviceName;
        InstanceVolumeDO instVolumeDO;
        EbsInstanceBlockDevice ebs;
        List<InstanceVolumeDO> instanceVolumeDOs = new ArrayList<>();
        for (InstanceBlockDeviceMapping blockDeviceMapping : blockDeviceMappings) {
            deviceName = blockDeviceMapping.getDeviceName();
            ebs = blockDeviceMapping.getEbs();

            if (ebs != null) {
                instVolumeDO = new InstanceVolumeDO();
                instVolumeDO.setAttachTime(ebs.getAttachTime());
                instVolumeDO.setDeleteOnTermination(ebs.getDeleteOnTermination());
                instVolumeDO.setStatus(ebs.getStatus());
                instVolumeDO.setVolumeId(ebs.getVolumeId());
                instVolumeDO.setDeviceName(deviceName);
                instanceVolumeDOs.add(instVolumeDO);
            }

        }
        return instanceVolumeDOs;
    }

    /**
     *
     * @param instance
     * @param instanceDO
     */
    private static void buildBasicInstanceData(Instance instance, InstanceDO instanceDO) {
        instanceDO.setInstanceId(instance.getInstanceId());
        instanceDO.setImageId(instance.getImageId());
        instanceDO.setAmiLaunchIndex(instance.getAmiLaunchIndex());
        instanceDO.setArchitecture(instance.getArchitecture());
        instanceDO.setOptimizedEbsIO(instance.getEbsOptimized());
        instanceDO.setEnaSupport(instance.getEnaSupport());
        instanceDO.setHypervisor(instance.getHypervisor());
        instanceDO.setInstanceType(instance.getInstanceType());
        instanceDO.setKernelId(instance.getKernelId());
        instanceDO.setInstanceLifeCycle(instance.getInstanceLifecycle());
        instanceDO.setPlatform(instance.getPlatform());
        instanceDO.setPrivateDnsName(instance.getPrivateDnsName());
        instanceDO.setPrivateIpAddress(instance.getPrivateIpAddress());
        instanceDO.setPublicDnsName(instance.getPublicDnsName());
        instanceDO.setPublicIpAddress(instance.getPublicIpAddress());
        instanceDO.setSourceDestCheck(instance.getSourceDestCheck());
        instanceDO.setKeyPairName(instance.getKeyName());
        instanceDO.setLaunchTime(instance.getLaunchTime());
        instanceDO.setRamDiskId(instance.getRamdiskId());
        instanceDO.setRootDeviceName(instance.getRootDeviceName());
        instanceDO.setRootDeviceType(instance.getRootDeviceType());
        instanceDO.setSpotInstanceRequestId(instance.getSpotInstanceRequestId());
        instanceDO.setSriovNetSuppport(instance.getSriovNetSupport());
        instanceDO.setStateTransitionReason(instance.getStateTransitionReason());
        instanceDO.setSubnetId(instance.getSubnetId());
        instanceDO.setVirtualizationType(instance.getVirtualizationType());
        instanceDO.setVpcId(instance.getVpcId());
    }

    /**
     *
     * @param instanceDO
     * @param placement
     * @return
     */
    private static PlacementDO buildPlacementDO(InstanceDO instanceDO, Placement placement) {
        PlacementDO placementDO = new PlacementDO();
        placementDO.setAffinity(placement.getAffinity());
        String availabilityZone = placement.getAvailabilityZone();
        String region;
        String zone;
        if (availabilityZone != null) {
            placementDO.setAvailabilityZone(availabilityZone);
            region = availabilityZone.substring(0, availabilityZone.length() - 1);
            zone = availabilityZone.substring(availabilityZone.length() - 1, 1);

            instanceDO.setZoneId(zone);
            instanceDO.setRegionId(region);
        }

        placementDO.setGroupName(placement.getGroupName());

        placementDO.setHostId(placement.getHostId());
        placementDO.setTenancy(placement.getTenancy());
        return placementDO;
    }


}
