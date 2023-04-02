/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import { DataWithBackend } from '@/plugins/DataWithBackend';
import { RenderRow } from '@/plugins/RenderRow';
import { RenderList } from '@/plugins/RenderList';
import { SortInfo } from '../common/SortInfo';

const { I18n } = DataWithBackend;
const { FieldDecorator } = RenderRow;

export default class FlinkSort extends SortInfo implements DataWithBackend, RenderRow, RenderList {
  @FieldDecorator({
    type: 'select',
    rules: [{ required: true }],
    props: {
      placeholder: '1.13',
      options: [
        {
          label: '1.13',
          value: '1.13',
        },
        {
          label: '1.15',
          value: '1.15',
        },
      ],
    },
  })
  @I18n('meta.Sort.Flink.Version')
  version: string;

  @FieldDecorator({
    type: 'select',
    rules: [{ required: true }],
    props: {
      placeholder: 'PreJob',
      options: [
        {
          label: 'PreJob',
          value: 'PreJob',
        },
        {
          label: 'PreSession',
          value: 'PreSession',
        },
        {
          label: 'Application',
          value: 'Application',
        },
      ],
    },
  })
  @I18n('meta.Sort.Flink.DeployMode')
  deployMode: string;

  @FieldDecorator({
    type: 'input',
    initialValue: '127.0.0.1',
    rules: [{ required: true }],
    props: {
      placeholder: '127.0.0.1',
    },
  })
  @I18n('meta.Sort.Flink.RestAddress')
  restAddress: string;

  @FieldDecorator({
    type: 'inputnumber',
    initialValue: '8081',
    rules: [{ required: true }],
    props: {
      placeholder: '8081',
    },
  })
  @I18n('meta.Sort.Flink.RestPort')
  restPort: number;

  @FieldDecorator({
    type: 'inputnumber',
    initialValue: '127.0.0.1',
    rules: [{ required: true }],
    props: {
      placeholder: '127.0.0.1',
    },
  })
  @I18n('meta.Sort.Flink.JobManagerPort')
  jobManagerPort: number;

  @FieldDecorator({
    type: 'inputnumber',
    initialValue: '1',
    rules: [{ required: true }],
    props: {
      placeholder: '1',
    },
  })
  @I18n('meta.Sort.Flink.Parallelism')
  parallelism: number;
}
