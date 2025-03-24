/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.inlong.sort.base.metric;

import org.apache.inlong.audit.AuditReporterImpl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.flink.table.data.RowData;
import org.apache.flink.types.RowKind;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.inlong.audit.consts.ConfigConstants.DEFAULT_AUDIT_TAG;
import static org.apache.inlong.common.constant.Constants.DEFAULT_AUDIT_VERSION;
import static org.apache.inlong.sort.base.Constants.GROUP_ID;
import static org.apache.inlong.sort.base.Constants.STREAM_ID;
import static org.apache.inlong.sort.base.util.CalculateObjectSizeUtils.getDataSize;

@Slf4j
public class CdcExactlyMetric implements Serializable, SourceMetricsReporter {

    private final Map<String, String> labels;
    private final Map<RowKind, Integer> auditKeyMap;
    private final String groupId;
    private final String streamId;

    private AuditReporterImpl auditReporter;
    private Long currentCheckpointId = 0L;
    private Long lastCheckpointId = 0L;

    public CdcExactlyMetric(MetricOption option) {
        this.labels = option.getLabels();
        this.groupId = labels.get(GROUP_ID);
        this.streamId = labels.get(STREAM_ID);
        this.auditKeyMap = new HashMap<>();

        if (option.getIpPorts().isPresent()) {
            auditReporter = new AuditReporterImpl();
            auditReporter.setAutoFlush(false);
            auditReporter.setAuditProxy(option.getIpPortSet());
            List<Integer> auditKeys = option.getInlongAuditKeys();

            if (CollectionUtils.isEmpty(auditKeys)) {
                log.warn("inlong audit keys is empty");
            } else if (auditKeys.size() == 1) {
                auditKeyMap.put(RowKind.INSERT, auditKeys.get(0));
                log.warn("only the insert audit key is set, the update and delete audit will be ignored");
            } else if (auditKeys.size() == 4) {
                auditKeyMap.put(RowKind.INSERT, auditKeys.get(0));
                auditKeyMap.put(RowKind.UPDATE_BEFORE, auditKeys.get(1));
                auditKeyMap.put(RowKind.UPDATE_AFTER, auditKeys.get(2));
                auditKeyMap.put(RowKind.DELETE, auditKeys.get(3));
            } else {
                throw new IllegalArgumentException("audit key size must be 1 or 4");
            }
        }
        log.info("CdcExactlyMetric init, groupId: {}, streamId: {}, audit key: {}", groupId, streamId, auditKeyMap);
    }

    @Override
    public void outputMetricsWithEstimate(Object data, long dataTime) {
        long size = getDataSize(data);
        if (data instanceof RowData) {
            RowData rowData = (RowData) data;
            RowKind rowKind = rowData.getRowKind();
            int key = auditKeyMap.get(rowKind);
            outputMetrics(1, size, dataTime, key);
        } else {
            outputMetrics(1, size, dataTime, auditKeyMap.get(RowKind.INSERT));
        }
    }

    public void outputMetrics(long rowCountSize, long rowDataSize, long dataTime, int key) {
        if (auditReporter != null) {
            auditReporter.add(
                    this.currentCheckpointId,
                    key,
                    DEFAULT_AUDIT_TAG,
                    groupId,
                    streamId,
                    dataTime,
                    rowCountSize,
                    rowDataSize,
                    DEFAULT_AUDIT_VERSION);
        }
    }

    public void updateLastCheckpointId(Long checkpointId) {
        lastCheckpointId = checkpointId;
    }

    public void updateCurrentCheckpointId(Long checkpointId) {
        currentCheckpointId = checkpointId;
    }

    public void flushAudit() {
        if (auditReporter != null) {
            auditReporter.flush(lastCheckpointId);
        }
    }
}
