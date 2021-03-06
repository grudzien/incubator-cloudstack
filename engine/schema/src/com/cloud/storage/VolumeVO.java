// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.cloud.storage;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.cloud.storage.Storage.StoragePoolType;
import com.cloud.utils.NumbersUtil;
import com.cloud.utils.db.GenericDao;

@Entity
@Table(name = "volumes")
public class VolumeVO implements Volume {
    @Id
    @TableGenerator(name = "volume_sq", table = "sequence", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "volume_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    long id;

    @Column(name = "name")
    String name;

    @Column(name = "pool_id")
    Long poolId;
    
    @Column(name = "last_pool_id")
    Long lastPoolId;
    
    @Column(name = "account_id")
    long accountId;

    @Column(name = "domain_id")
    long domainId;

    @Column(name = "instance_id")
    Long instanceId = null;

    @Column(name = "device_id")
    Long deviceId = null;

    @Column(name = "size")
    Long size;

    @Column(name = "folder")
    String folder;

    @Column(name = "path")
    String path;

    @Column(name = "pod_id")
    Long podId;

    @Column(name = "created")
    Date created;

    @Column(name = "attached")
    @Temporal(value = TemporalType.TIMESTAMP)
    Date attached;

    @Column(name = "data_center_id")
    long dataCenterId;

    @Column(name = "host_ip")
    String hostip;

    @Column(name = "disk_offering_id")
    long diskOfferingId;

    @Column(name = "template_id")
    Long templateId;

    @Column(name = "first_snapshot_backup_uuid")
    String firstSnapshotBackupUuid;

    @Column(name = "volume_type")
    @Enumerated(EnumType.STRING)
    Type volumeType = Volume.Type.UNKNOWN;

    @Column(name = "pool_type")
    @Enumerated(EnumType.STRING)
    StoragePoolType poolType;

    @Column(name = GenericDao.REMOVED_COLUMN)
    Date removed;

    @Column(name = "updated")
    @Temporal(value = TemporalType.TIMESTAMP)
    Date updated;
    
    @Column(name="update_count", updatable = true, nullable=false)
    protected long updatedCount;	// This field should be updated everytime the state is updated.  There's no set method in the vo object because it is done with in the dao code.

    @Column(name = "recreatable")
    boolean recreatable;

    @Column(name = "state")
    @Enumerated(value = EnumType.STRING)
    private State state;

    @Column(name = "chain_info")
    String chainInfo;

    @Column(name = "uuid")
    String uuid;

    @Column(name="display_volume", updatable=true, nullable=false)
    protected boolean displayVolume;

    @Transient
    // @Column(name="reservation")
    String reservationId;
    
    // Real Constructor
    public VolumeVO(Type type, String name, long dcId, long domainId, long accountId, long diskOfferingId, long size) {
        this.volumeType = type;
        this.name = name;
        this.dataCenterId = dcId;
        this.accountId = accountId;
        this.domainId = domainId;
        this.size = size;
        this.diskOfferingId = diskOfferingId;
        this.state = State.Allocated;
        this.uuid = UUID.randomUUID().toString();
    }

    public VolumeVO(String name, long dcId, long podId, long accountId, long domainId, Long instanceId, String folder, String path, long size, Volume.Type vType) {
        this.name = name;
        this.accountId = accountId;
        this.domainId = domainId;
        this.instanceId = instanceId;
        this.folder = folder;
        this.path = path;
        this.size = size;
        this.podId = podId;
        this.dataCenterId = dcId;
        this.volumeType = vType;
        this.state = Volume.State.Allocated;
        this.recreatable = false;
        this.uuid = UUID.randomUUID().toString();
    }

    // Copy Constructor
    public VolumeVO(Volume that) {
        this(that.getName(), that.getDataCenterId(), that.getPodId(), that.getAccountId(), that.getDomainId(), that.getInstanceId(), that.getFolder(), that.getPath(), that.getSize(), that
                .getVolumeType());
        this.recreatable = that.isRecreatable();
        this.state = that.getState();
        this.size = that.getSize();
        this.diskOfferingId = that.getDiskOfferingId();
        this.poolId = that.getPoolId();
        this.attached = that.getAttached();
        this.chainInfo = that.getChainInfo();
        this.templateId = that.getTemplateId();
        this.deviceId = that.getDeviceId();
        this.uuid = UUID.randomUUID().toString();
    }

    @Override
    public long getUpdatedCount() {
    	return this.updatedCount;
    }
    
    @Override
    public void incrUpdatedCount() {
    	this.updatedCount++;
    }
    
    public void decrUpdatedCount() {
    	this.updatedCount--;
    }
    
    @Override
    public boolean isRecreatable() {
        return recreatable;
    }

    public void setRecreatable(boolean recreatable) {
        this.recreatable = recreatable;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Long getPodId() {
        return podId;
    }

    @Override
    public long getDataCenterId() {
        return dataCenterId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getAccountId() {
        return accountId;
    }

    public void setPoolType(StoragePoolType poolType) {
        this.poolType = poolType;
    }

    public StoragePoolType getPoolType() {
        return poolType;
    }

    @Override
    public long getDomainId() {
        return domainId;
    }

    @Override
    public String getFolder() {
        return folder;
    }

    @Override
    public String getPath() {
        return path;
    }

    protected VolumeVO() {
    }

    @Override
    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public Long getInstanceId() {
        return instanceId;
    }

    @Override
    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public Type getVolumeType() {
        return volumeType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public void setDomainId(long domainId) {
        this.domainId = domainId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHostIp() {
        return hostip;
    }

    public void setHostIp(String hostip) {
        this.hostip = hostip;
    }

    public void setPodId(Long podId) {
        this.podId = podId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public void setVolumeType(Type type) {
        volumeType = type;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    public Date getRemoved() {
        return removed;
    }

    public void setRemoved(Date removed) {
        this.removed = removed;
    }

    @Override
    public long getDiskOfferingId() {
        return diskOfferingId;
    }

    public void setDiskOfferingId(long diskOfferingId) {
        this.diskOfferingId = diskOfferingId;
    }

    @Override
    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getFirstSnapshotBackupUuid() {
        return firstSnapshotBackupUuid;
    }

    public void setFirstSnapshotBackupUuid(String firstSnapshotBackupUuid) {
        this.firstSnapshotBackupUuid = firstSnapshotBackupUuid;
    }

    @Override
    public Long getPoolId() {
        return poolId;
    }

    public void setPoolId(Long poolId) {
        this.poolId = poolId;
    }

    @Override
    public Date getUpdated() {
        return updated;
    }

    @Override
    public State getState() {
        return state;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return new StringBuilder("Vol[").append(id).append("|vm=").append(instanceId).append("|").append(volumeType).append("]").toString();
    }

    @Override
    public Date getAttached() {
        return this.attached;
    }

    public void setAttached(Date attached) {
        this.attached = attached;
    }

    @Override
    public String getChainInfo() {
        return this.chainInfo;
    }

    public void setChainInfo(String chainInfo) {
        this.chainInfo = chainInfo;
    }
    
    public Long getLastPoolId() {
    	return this.lastPoolId;
    }
    
    public void setLastPoolId(Long poolId) {
    	this.lastPoolId = poolId;
    }

    @Override
    public int hashCode() {
        return NumbersUtil.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VolumeVO) {
            return id == ((VolumeVO) obj).id;
        } else {
            return false;
        }
    }
    
    @Override
    public String getReservationId() {
    	return this.reservationId;
    }
    
    @Override
    public void setReservationId(String reserv) {
    	this.reservationId = reserv;
    }
    
    @Override
    public String getUuid() {
    	return this.uuid;
    }
    
    public void setUuid(String uuid) {
    	this.uuid = uuid;
    }


    public boolean isDisplayVolume() {
        return displayVolume;
    }

    public void setDisplayVolume(boolean displayVolume) {
        this.displayVolume = displayVolume;
    }
}
