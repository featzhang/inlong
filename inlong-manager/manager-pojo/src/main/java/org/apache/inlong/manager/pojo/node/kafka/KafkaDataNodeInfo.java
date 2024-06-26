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

package org.apache.inlong.manager.pojo.node.kafka;

import org.apache.inlong.manager.common.consts.DataNodeType;
import org.apache.inlong.manager.common.util.CommonBeanUtils;
import org.apache.inlong.manager.common.util.JsonTypeDefine;
import org.apache.inlong.manager.pojo.node.DataNodeInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Kafka data node info
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonTypeDefine(value = DataNodeType.KAFKA)
@ApiModel("Kafka data node info")
public class KafkaDataNodeInfo extends DataNodeInfo {

    @ApiModelProperty("kafka bootstrapServers")
    private String bootstrapServers;

    @ApiModelProperty("kafka client id")
    private String clientId;

    @ApiModelProperty(value = "kafka produce confirmation mechanism")
    private String ack;

    @ApiModelProperty("audit set name")
    private String auditSetName;

    public KafkaDataNodeInfo() {
        this.setType(DataNodeType.KAFKA);
    }

    @Override
    public KafkaDataNodeRequest genRequest() {
        return CommonBeanUtils.copyProperties(this, KafkaDataNodeRequest::new);
    }
}
