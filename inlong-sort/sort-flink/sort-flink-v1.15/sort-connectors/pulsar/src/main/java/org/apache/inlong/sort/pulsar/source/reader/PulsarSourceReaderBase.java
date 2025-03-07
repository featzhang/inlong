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

package org.apache.inlong.sort.pulsar.source.reader;

import org.apache.inlong.sort.base.util.OpenTelemetryLogger;

import org.apache.flink.api.connector.source.SourceReaderContext;
import org.apache.flink.connector.base.source.reader.RecordsWithSplitIds;
import org.apache.flink.connector.base.source.reader.SourceReaderBase;
import org.apache.flink.connector.base.source.reader.synchronization.FutureCompletingBlockingQueue;
import org.apache.flink.connector.pulsar.source.config.SourceConfiguration;
import org.apache.flink.connector.pulsar.source.reader.emitter.PulsarRecordEmitter;
import org.apache.flink.connector.pulsar.source.reader.fetcher.PulsarFetcherManagerBase;
import org.apache.flink.connector.pulsar.source.reader.message.PulsarMessage;
import org.apache.flink.connector.pulsar.source.split.PulsarPartitionSplit;
import org.apache.flink.connector.pulsar.source.split.PulsarPartitionSplitState;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.api.PulsarClient;

/**
 * The common pulsar source reader for both ordered & unordered message consuming.
 *
 * @param <OUT> The output message type for flink.
 * copy from {@link org.apache.flink.connector.pulsar.source.reader.source.PulsarUnorderedSourceReader}
 * not modified
 */
abstract class PulsarSourceReaderBase<OUT>
        extends
            SourceReaderBase<PulsarMessage<OUT>, OUT, PulsarPartitionSplit, PulsarPartitionSplitState> {

    protected final SourceConfiguration sourceConfiguration;
    protected final PulsarClient pulsarClient;
    protected final PulsarAdmin pulsarAdmin;
    private OpenTelemetryLogger openTelemetryLogger;
    protected final boolean enableLogReport;

    protected PulsarSourceReaderBase(
            FutureCompletingBlockingQueue<RecordsWithSplitIds<PulsarMessage<OUT>>> elementsQueue,
            PulsarFetcherManagerBase<OUT> splitFetcherManager,
            SourceReaderContext context,
            SourceConfiguration sourceConfiguration,
            PulsarClient pulsarClient,
            PulsarAdmin pulsarAdmin,
            boolean enableLogReport) {
        super(
                elementsQueue,
                splitFetcherManager,
                new PulsarRecordEmitter<>(),
                sourceConfiguration,
                context);

        this.sourceConfiguration = sourceConfiguration;
        this.pulsarClient = pulsarClient;
        this.pulsarAdmin = pulsarAdmin;
        this.enableLogReport = enableLogReport;
        if (enableLogReport) {
            this.openTelemetryLogger = new OpenTelemetryLogger.Builder()
                    .setLogLevel(org.apache.logging.log4j.Level.ERROR)
                    .setServiceName(this.getClass().getSimpleName())
                    .setLocalHostIp(this.context.getLocalHostName()).build();
        }
    }

    @Override
    protected PulsarPartitionSplitState initializedState(PulsarPartitionSplit split) {
        return new PulsarPartitionSplitState(split);
    }

    @Override
    protected PulsarPartitionSplit toSplitType(
            String splitId, PulsarPartitionSplitState splitState) {
        return splitState.toPulsarPartitionSplit();
    }

    @Override
    public void start() {
        if (enableLogReport) {
            this.openTelemetryLogger.install();
        }
        super.start();
    }

    @Override
    public void close() throws Exception {
        // Close the all the consumers first.
        super.close();

        // Close shared pulsar resources.
        pulsarClient.shutdown();
        pulsarAdmin.close();
        if (enableLogReport) {
            openTelemetryLogger.uninstall();
        }
    }
}
