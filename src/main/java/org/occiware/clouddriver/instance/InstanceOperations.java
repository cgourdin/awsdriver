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

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.model.*;
import org.apache.log4j.Logger;
import org.occiware.clouddriver.IAM.IamInstanceProfileDO;
import org.occiware.clouddriver.client.AwsEC2Client;
import org.occiware.clouddriver.IAM.GroupIdentifierDO;
import org.occiware.clouddriver.network.IpAddressDO;
import org.occiware.clouddriver.network.NetAssociationDO;
import org.occiware.clouddriver.network.NetworkInterfaceDO;
import org.occiware.clouddriver.storage.InstanceVolumeDO;
import org.occiware.clouddriver.tags.TagDO;
import org.occiware.clouddriver.tags.TagsOperation;

import java.util.*;

/**
 * All instances direct operations.
 * Created by Christophe Gourdin on 10/12/2016.
 */
public class InstanceOperations {

    public static final Logger logger = Logger.getLogger(InstanceOperations.class);

    private AwsEC2Client ec2Client;

    public InstanceOperations(AwsEC2Client ec2Client) {
        this.ec2Client = ec2Client;
    }


    /**
     * Create one ec2 instance with data instance object.
     * @param instance
     * @throws InstanceOperationException
     */
    public void createInstance(InstanceDO instance) throws InstanceOperationException {


        // Check instance data object before creation.
        checkInstanceObjCreation(instance);
        boolean hasPlacement = false;
        String keyPairName = instance.getKeyPairName();
        String imageId = instance.getImage().getImageId();
        String instanceType = instance.getInstanceType();
        Boolean monitoring = instance.isMonitoring();
        String region = instance.getRegionId();
        String zone = instance.getZoneId();
        List<GroupIdentifierDO> securityGroups = instance.getSecurityGroups();
        String name = instance.getName();
        String userData = instance.getUserData();

        RunInstancesRequest rRequest = new RunInstancesRequest(imageId, 1,1);

        rRequest.setInstanceType(instanceType);
        rRequest.setMonitoring(monitoring);
        if (keyPairName != null) {
            rRequest.setKeyName(keyPairName);
        }
        if (userData != null) {
            rRequest.setUserData(userData);
        }

        Placement placement = new Placement();
        PlacementDO placementDO = instance.getPlacement();

        if (placementDO != null && placementDO.getAvailabilityZone() != null) {
            placement.setAvailabilityZone(placementDO.getAvailabilityZone());
            hasPlacement = true;
        } else {
            if (zone != null) {
                if (!zone.trim().isEmpty()) {
                    placement.setAvailabilityZone(region + zone);
                    hasPlacement = true;
                }
            }
        }
        if (placementDO != null) {
            String groupName = placementDO.getGroupName();
            String tenancy = placementDO.getTenancy();
            if (groupName != null) {
                placement.setGroupName(groupName);
                hasPlacement = true;
            }
            if (tenancy != null) {
                placement.setTenancy(tenancy);
                hasPlacement = true;
            }
        }
        if (hasPlacement) {
            rRequest.setPlacement(placement);
        }

        if (securityGroups != null && !securityGroups.isEmpty()) {
            List<String> securityGroupNames = new ArrayList<>();
            for (GroupIdentifierDO secGroup : securityGroups) {
                securityGroupNames.add(secGroup.getGroupName());
            }

            rRequest.setSecurityGroups(securityGroupNames);
        }
        RunInstancesResult runInstancesResult;
        try {
            runInstancesResult = ec2Client.getClientInstance().runInstances(rRequest);
        } catch (AmazonServiceException ase) {
            logger.error("Exception thrown from aws : " + ase.getErrorCode() + " --> " + ase.getErrorMessage());
            throw new InstanceOperationException(ase);
        } catch (AmazonClientException ace) {
            logger.error("Exception thrown from aws : " + ace.getMessage());
            throw new InstanceOperationException(ace);
        } finally {
            ec2Client.getClientInstance().shutdown();
        }

        List<Instance> instancesReturn = runInstancesResult.getReservation().getInstances();

        String instanceProviderId = null;
        int length;
        for (Instance instanceAws : instancesReturn) {
            instanceProviderId = instanceAws.getInstanceId();
            instance.setInstanceId(instanceProviderId);
            length = instanceAws.getPlacement().getAvailabilityZone().length();
            instance.setZoneId(instanceAws.getPlacement().getAvailabilityZone().substring(length - 1, length));
        }

        if (name != null && instanceProviderId != null) {
            TagsOperation tagOperation = new TagsOperation(ec2Client);
            tagOperation.createTag(instanceProviderId, "Name", name);
        }

        ec2Client.getClientInstance().shutdown();
    }

    /**
     * Describe all instances in regionZones defined in parameter.
     * @param availabilityZones the regionId + zoneId in a list of String.
     * @return a list of instances data object.
     * @throws InstanceOperationException
     */
    public List<InstanceDO> describeInstancesForAvailabilityZone(final List<String> availabilityZones) throws InstanceOperationException {
        Filter filter = new Filter("availability-zone", availabilityZones);
        return describeInstanceWithFilters(filter);
    }


    public InstanceDO retrieveInstanceForProviderId(final String instanceId) throws InstanceOperationException {
        InstanceDO instanceDO = null;
        // Search for this instance




        return instanceDO;
    }

    /**
     * Describe instances with specific filters.
     * @param filters :
     * affinity - The affinity setting for an instance running on a Dedicated Host (default | host).
    architecture - The instance architecture (i386 | x86_64).
    association.public-ip - The address of the Elastic IP address (IPv4) bound to the network interface.
    association.ip-owner-id - The owner of the Elastic IP address (IPv4) associated with the network interface.
    association.allocation-id - The allocation ID returned when you allocated the Elastic IP address (IPv4) for your network interface.
    association.association-id - The association ID returned when the network interface was associated with an IPv4 address.
    availability-zone - The Availability Zone of the instance.
    block-device-mapping.attach-time - The attach time for an EBS volume mapped to the instance, for example, 2010-09-15T17:15:20.000Z.
    block-device-mapping.delete-on-termination - A Boolean that indicates whether the EBS volume is deleted on instance termination.
    block-device-mapping.device-name - The device name for the EBS volume (for example, /dev/sdh or xvdh).
    block-device-mapping.status - The status for the EBS volume (attaching | attached | detaching | detached).
    block-device-mapping.volume-id - The volume ID of the EBS volume.
    client-token - The idempotency token you provided when you launched the instance.
    dns-name - The public DNS name of the instance.
    group-id - The ID of the security group for the instance. EC2-Classic only.
    group-name - The name of the security group for the instance. EC2-Classic only.
    host-id - The ID of the Dedicated Host on which the instance is running, if applicable.
    hypervisor - The hypervisor type of the instance (ovm | xen).
    iam-instance-profile.arn - The instance profile associated with the instance. Specified as an ARN.
    image-id - The ID of the image used to launch the instance.
    instance-id - The ID of the instance.
    instance-lifecycle - Indicates whether this is a Spot Instance or a Scheduled Instance ( spot | scheduled).
    instance-state-code - The state of the instance, as a 16-bit unsigned integer. The high byte is an opaque internal value and should be ignored. The low byte is set based on the state represented. The valid values are: 0 (pending), 16 (running), 32 (shutting-down), 48 (terminated), 64 (stopping), and 80 (stopped).
    instance-state-name - The state of the instance (pending | running | shutting-down | terminated | stopping | stopped).
    instance-type - The type of instance (for example, t2.micro).
    instance.group-id - The ID of the security group for the instance.
    instance.group-name - The name of the security group for the instance.
    ip-address - The public IPv4 address of the instance.
    kernel-id - The kernel ID.
    key-name - The name of the key pair used when the instance was launched.
    launch-index - When launching multiple instances, this is the index for the instance in the launch group (for example, 0, 1, 2, and so on).
    launch-time - The time when the instance was launched.
    monitoring-state - Indicates whether detailed monitoring is enabled (disabled | enabled).
    network-interface.addresses.private-ip-address - The private IPv4 address associated with the network interface.
    network-interface.addresses.primary - Specifies whether the IPv4 address of the network interface is the primary private IPv4 address.
    network-interface.addresses.association.public-ip - The ID of the association of an Elastic IP address (IPv4) with a network interface.
    network-interface.addresses.association.ip-owner-id - The owner ID of the private IPv4 address associated with the network interface.
    network-interface.attachment.attachment-id - The ID of the interface attachment.
    network-interface.attachment.instance-id - The ID of the instance to which the network interface is attached.
    network-interface.attachment.instance-owner-id - The owner ID of the instance to which the network interface is attached.
    network-interface.attachment.device-index - The device index to which the network interface is attached.
    network-interface.attachment.status - The status of the attachment (attaching | attached | detaching | detached).
    network-interface.attachment.attach-time - The time that the network interface was attached to an instance.
    network-interface.attachment.delete-on-termination - Specifies whether the attachment is deleted when an instance is terminated.
    network-interface.availability-zone - The Availability Zone for the network interface.
    network-interface.description - The description of the network interface.
    network-interface.group-id - The ID of a security group associated with the network interface.
    network-interface.group-name - The name of a security group associated with the network interface.
    network-interface.ipv6-addresses.ipv6-address - The IPv6 address associated with the network interface.
    network-interface.mac-address - The MAC address of the network interface.
    network-interface.network-interface-id - The ID of the network interface.
    network-interface.owner-id - The ID of the owner of the network interface.
    network-interface.private-dns-name - The private DNS name of the network interface.
    network-interface.requester-id - The requester ID for the network interface.
    network-interface.requester-managed - Indicates whether the network interface is being managed by AWS.
    network-interface.status - The status of the network interface (available) | in-use).
    network-interface.source-dest-check - Whether the network interface performs source/destination checking. A value of true means checking is enabled, and false means checking is disabled. The value must be false for the network interface to perform network address translation (NAT) in your VPC.
    network-interface.subnet-id - The ID of the subnet for the network interface.
    network-interface.vpc-id - The ID of the VPC for the network interface.
    owner-id - The AWS account ID of the instance owner.
    placement-group-name - The name of the placement group for the instance.
    platform - The platform. Use windows if you have Windows instances; otherwise, leave blank.
    private-dns-name - The private IPv4 DNS name of the instance.
    private-ip-address - The private IPv4 address of the instance.
    product-code - The product code associated with the AMI used to launch the instance.
    product-code.type - The type of product code (devpay | marketplace).
    ramdisk-id - The RAM disk ID.
    reason - The reason for the current state of the instance (for example, shows "User Initiated [date]" when you stop or terminate the instance). Similar to the state-reason-code filter.
    requester-id - The ID of the entity that launched the instance on your behalf (for example, AWS Management Console, Auto Scaling, and so on).
    reservation-id - The ID of the instance's reservation. A reservation ID is created any time you launch an instance. A reservation ID has a one-to-one relationship with an instance launch request, but can be associated with more than one instance if you launch multiple instances using the same launch request. For example, if you launch one instance, you'll get one reservation ID. If you launch ten instances using the same launch request, you'll also get one reservation ID.
    root-device-name - The name of the root device for the instance (for example, /dev/sda1 or /dev/xvda).
    root-device-type - The type of root device that the instance uses (ebs | instance-store).
    source-dest-check - Indicates whether the instance performs source/destination checking. A value of true means that checking is enabled, and false means checking is disabled. The value must be false for the instance to perform network address translation (NAT) in your VPC.
    spot-instance-request-id - The ID of the Spot instance request.
    state-reason-code - The reason code for the state change.
    state-reason-message - A message that describes the state change.
    subnet-id - The ID of the subnet for the instance.
    tag:key=value - The key/value combination of a tag assigned to the resource, where tag:key is the tag's key.
    tag-key - The key of a tag assigned to the resource. This filter is independent of the tag-value filter. For example, if you use both the filter "tag-key=Purpose" and the filter "tag-value=X", you get any resources assigned both the tag key Purpose (regardless of what the tag's value is), and the tag value X (regardless of what the tag's key is). If you want to list only resources where Purpose is X, see the tag:key=value filter.
    tag-value - The value of a tag assigned to the resource. This filter is independent of the tag-key filter.
    tenancy - The tenancy of an instance (dedicated | default | host).
    virtualization-type - The virtualization type of the instance (paravirtual | hvm).
    vpc-id - The ID of the VPC that the instance is running in.
     * @return a list of ec2 (may be spot, classic ec2 and reserved instances).
     */
    public List<InstanceDO> describeInstanceWithFilters(Filter... filters) throws InstanceOperationException {
        List<InstanceDO> instances = new ArrayList<>();
        List<Instance> instancesAWS = new LinkedList<Instance>();
        DescribeInstancesRequest descRequest = new DescribeInstancesRequest().withFilters(filters);

        try {
            DescribeInstancesResult result = ec2Client.getClientInstance().describeInstances(descRequest);
            List<Reservation> reservations = result.getReservations();
            for (Reservation reservation : reservations) {
                instancesAWS.addAll(reservation.getInstances());
            }
            if (!instancesAWS.isEmpty()) {
                instances = buildInstanceDataFromInstanceAWS(instancesAWS);
            }


        } catch (AmazonServiceException ase) {
            logger.error("Exception thrown from aws : " + ase.getErrorCode() + " --> " + ase.getErrorMessage());
            throw new InstanceOperationException(ase);
        } catch (AmazonClientException ace) {
            logger.error("Exception thrown from aws : " + ace.getMessage());
            throw new InstanceOperationException(ace);
        } finally {
            ec2Client.getClientInstance().shutdown();
        }

        return instances;

    }

    private List<InstanceDO> buildInstanceDataFromInstanceAWS(List<Instance> instancesAWS) {

        List<InstanceDO> instances = new ArrayList<>();
        for (Instance instance : instancesAWS) {
            InstanceDO instanceDO = new InstanceDO();
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

            Placement placement = instance.getPlacement();
            if (placement != null) {
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

                instanceDO.setPlacement(placementDO);
            }

            // Ebs volumes attached on instance.
            if (instance.getBlockDeviceMappings() != null && !instance.getBlockDeviceMappings().isEmpty()) {
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
                instanceDO.setVolumes(instanceVolumeDOs);
            }

            if (instance.getIamInstanceProfile() != null) {
                IamInstanceProfile profile = instance.getIamInstanceProfile();
                IamInstanceProfileDO profileDO = new IamInstanceProfileDO();
                profileDO.setArn(profile.getArn());
                profileDO.setProfileId(profile.getId());
                instanceDO.setIamInstanceProfile(profileDO);
            }

            if (instance.getMonitoring() != null) {
                Monitoring monitoring = instance.getMonitoring();
                instanceDO.setMonitoringState(monitoring.getState());
            }

            // Network part.
            if (instance.getNetworkInterfaces() != null && !instance.getNetworkInterfaces().isEmpty()) {
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
                        GroupIdentifierDO grpDO;
                        List<GroupIdentifierDO> grpDOs = new ArrayList<>();
                        for (GroupIdentifier grpId : groupIdentifiers) {
                            grpDO = new GroupIdentifierDO();
                            grpDO.setGroupId(grpId.getGroupId());
                            grpDO.setGroupName(grpId.getGroupName());
                            grpDOs.add(grpDO);
                        }
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
                instanceDO.setNetworkAdapters(networkInterfaceDOs);
            }

            List<ProductCode> productCodes = instance.getProductCodes();
            if (productCodes != null && !productCodes.isEmpty()) {
                List<ProductCodeDO> productCodeDOs = new ArrayList<>();
                ProductCodeDO productCodeDO;
                for (ProductCode productCode : productCodes) {
                    productCodeDO = new ProductCodeDO();
                    productCodeDO.setProductCodeId(productCode.getProductCodeId());
                    productCodeDO.setProductCodeType(productCode.getProductCodeType());
                    productCodeDOs.add(productCodeDO);
                }
                instanceDO.setProductCodes(productCodeDOs);
            }

            List<GroupIdentifier> groups = instance.getSecurityGroups();
            if (groups != null && !groups.isEmpty()) {
                GroupIdentifierDO groupIdentifierDO;
                List<GroupIdentifierDO> groupIdentifierDOs = new ArrayList<>();
                for (GroupIdentifier group : groups) {
                    groupIdentifierDO = new GroupIdentifierDO();
                    groupIdentifierDO.setGroupId(group.getGroupId());
                    groupIdentifierDO.setGroupName(group.getGroupName());
                    groupIdentifierDOs.add(groupIdentifierDO);
                }
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
                TagDO tagDO;
                List<TagDO> tagDOs = new ArrayList<>();
                for (Tag tag : tags) {
                    tagDO = new TagDO(tag.getKey(), tag.getValue());
                    tagDOs.add(tagDO);
                }
                instanceDO.setTags(tagDOs);
            }


        }



        return instances;



    }


    public InstanceDO retrieveInstanceByName(final String name) throws InstanceOperationException {
        InstanceDO instance = null;


        return instance;
    }


    public void startInstance() throws InstanceOperationException {

    }
    public void stopInstance() throws InstanceOperationException {

    }
    public void pauseInstance() throws InstanceOperationException {

    }
    public void resumeInstance() throws InstanceOperationException {

    }

    public void terminateInstance() throws InstanceOperationException {

    }

    public void migrateInstanceToAnotherRegionZone() throws InstanceOperationException {

    }


    /**
     * Check the instance datas before creating the ec2 instance.
     * @param instance
     * @throws InstanceOperationException
     */
    private void checkInstanceObjCreation(final InstanceDO instance) throws InstanceOperationException {

        if (instance == null) {
            throw new InstanceOperationException("Instance ec2 is not defined");
        }
        if (instance.getRegionId() == null) {
            throw new InstanceOperationException("Instance region is not defined");
        }
        if (instance.getInstanceType() == null) {
            throw new InstanceOperationException("Instance type is not defined");
        }
        try {
            // Check if instance type is a valid instance type.
            InstanceType.valueOf(instance.getInstanceType());
        } catch (IllegalArgumentException ex) {
            throw new InstanceOperationException("Instance type : " + instance.getInstanceType() + " is not an ec2 instance type, for example, m1.medium is an ec2 instance type.");
        }


        if (instance.getPlacement() != null) {

            if (instance.getPlacement().getAvailabilityZone() != null) {
                // TODO : Check placement availability zone (region + zone must exist on EC2).


            }

            if (instance.getPlacement().getTenancy() != null) {
                try {
                    Tenancy.valueOf(instance.getPlacement().getTenancy());
                } catch (IllegalArgumentException ex) {
                    throw new InstanceOperationException("Cannot apply tenancy value : " + instance.getPlacement().getTenancy() + ", tenancy doesn't exist on ec2.");
                }
            }



        }






    }

}
