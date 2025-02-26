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
import { SourceInfo } from '../common/SourceInfo';
import i18n from '@/i18n';
import EditableTable from '@/ui/components/EditableTable';

const { I18n } = DataWithBackend;
const { FieldDecorator, SyncField, IngestionField } = RenderRow;
const { ColumnDecorator } = RenderList;

export default class PaimonSource
  extends SourceInfo
  implements DataWithBackend, RenderRow, RenderList
{
  @FieldDecorator({
    type: 'input',
    rules: [{ required: true }],
    props: values => ({
      disabled: [110, 130].includes(values?.status),
    }),
  })
  @ColumnDecorator()
  @SyncField()
  @IngestionField()
  @I18n('meta.Sources.Paimon.DbName')
  dbName: string;

  @FieldDecorator({
    type: 'input',
    rules: [{ required: true }],
    props: values => ({
      disabled: [110, 130].includes(values?.status),
    }),
  })
  @ColumnDecorator()
  @SyncField()
  @IngestionField()
  @I18n('meta.Sources.Paimon.TableName')
  tableName: string;

  @FieldDecorator({
    type: 'input',
    rules: [{ required: true }],
    props: values => ({
      disabled: [110, 130].includes(values?.status),
      placeholder: 'thrift://127.0.0.1:9083',
    }),
  })
  @ColumnDecorator()
  @SyncField()
  @IngestionField()
  @I18n('Catalog URI')
  catalogUri: string;

  @FieldDecorator({
    type: 'input',
    rules: [{ required: true }],
    props: values => ({
      disabled: [110, 130].includes(values?.status),
      placeholder: 'hdfs://127.0.0.1:9000/user/paimon/warehouse',
    }),
  })
  @ColumnDecorator()
  @SyncField()
  @IngestionField()
  @I18n('meta.Sources.Paimon.Warehouse')
  warehouse: string;

  @FieldDecorator({
    type: 'radio',
    rules: [{ required: true }],
    initialValue: false,
    props: values => ({
      disabled: values?.status === 101,
      options: [
        {
          label: i18n.t('basic.Yes'),
          value: true,
        },
        {
          label: i18n.t('basic.No'),
          value: false,
        },
      ],
    }),
    tooltip: i18n.t('meta.Sources.Paimon.ReadStreamingSkipCompactionHelp'),
  })
  @ColumnDecorator()
  @SyncField()
  @IngestionField()
  @I18n('meta.Sources.Paimon.ReadStreamingSkipCompaction')
  readStreamingSkipCompaction: boolean;

  @FieldDecorator({
    type: 'input',
    rules: [{ required: true }],
    props: values => ({
      disabled: [110, 130].includes(values?.status),
      placeholder: '20221213211100',
    }),
    tooltip: i18n.t('meta.Sources.Paimon.ReadStartCommitHelp'),
  })
  @ColumnDecorator()
  @SyncField()
  @IngestionField()
  @I18n('meta.Sources.Paimon.ReadStartCommit')
  readStartCommit: string;

  @FieldDecorator({
    type: EditableTable,
    rules: [{ required: false }],
    initialValue: [],
    tooltip: i18n.t('meta.Sources.Paimon.ExtListHelper'),
    props: values => ({
      size: 'small',
      columns: [
        {
          title: 'Key',
          dataIndex: 'keyName',
          props: {
            disabled: [110, 130].includes(values?.status),
          },
        },
        {
          title: 'Value',
          dataIndex: 'keyValue',
          props: {
            disabled: [110, 130].includes(values?.status),
          },
        },
      ],
    }),
  })
  @ColumnDecorator()
  @SyncField()
  @IngestionField()
  @I18n('meta.Sources.Paimon.ExtList')
  extList: string;
}
