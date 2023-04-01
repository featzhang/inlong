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

import React, { useState, useMemo, useRef, useEffect } from 'react';
import { Tabs, Button, Card, message, Steps, Space } from 'antd';
import { useTranslation } from 'react-i18next';
import { parse } from 'qs';
import { PageContainer, FooterToolbar } from '@/ui/components/PageContainer';
import { useParams, useRequest, useSet, useHistory, useLocation } from '@/ui/hooks';
import request from '@/core/utils/request';
import Info from './Info';

const Comp: React.FC = () => {
  const { t } = useTranslation();
  const history = useHistory();
  const location = useLocation();

  const qs = parse(location.search.slice(1));

  const [current, setCurrent] = useState(+qs.step || 0);
  const [, { add: addOpened, has: hasOpened }] = useSet([current]);
  const [confirmLoading, setConfirmLoading] = useState(false);
  const childRef = useRef(null);

  useEffect(() => {
    if (!hasOpened(current)) addOpened(current);
  }, [current, addOpened, hasOpened]);

  const { data } = useRequest(`/sort/get`, {});

  const isReadonly = useMemo(() => [11, 20, 22].includes(data?.status), [data]);

  const list = useMemo(
    () => [
      {
        label: t('pages.SortDetail'),
        value: 'sortDetail',
        content: Info,
      },
    ],
    [t],
  );

  const Div = Tabs;

  return (
    <PageContainer
      breadcrumb={[
        {
          name: t('pages.SortDetail'),
        },
      ]}
      useDefaultContainer={true}
    >
      <Div>
        {list.map(({ content: Content, ...item }, index) => {
          // Lazy load the content of the step, and at the same time make the loaded useCache content not destroy
          const child = (
            <Content readonly={isReadonly} ref={index === current ? childRef : null}></Content>
          );

          return (
            <Tabs.TabPane tab={item.label} key={item.value}>
              {child}
            </Tabs.TabPane>
          );
        })}
      </Div>
    </PageContainer>
  );
};

export default Comp;
