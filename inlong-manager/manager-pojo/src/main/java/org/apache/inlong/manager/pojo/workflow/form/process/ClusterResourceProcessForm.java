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

package org.apache.inlong.manager.pojo.workflow.form.process;

import org.apache.inlong.manager.pojo.group.GroupFullInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Form of create inlong cluster resource
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ClusterResourceProcessForm extends BaseProcessForm {

    public static final String FORM_NAME = "ClusterResourceProcessForm";

    private String inlongClusterTag;

    private List<GroupFullInfo> groupFullInfoList;

    /**
     * Get cluster resource process form info.
     */
    public static ClusterResourceProcessForm getProcessForm(String inlongClusterTag,
            List<GroupFullInfo> groupFullInfoList) {
        ClusterResourceProcessForm processForm = new ClusterResourceProcessForm();
        processForm.setInlongClusterTag(inlongClusterTag);
        processForm.setGroupFullInfoList(groupFullInfoList);
        return processForm;
    }

    @Override
    public String getFormName() {
        return FORM_NAME;
    }

}
