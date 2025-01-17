/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rocketmq.store;

import org.apache.rocketmq.common.protocol.heartbeat.SubscriptionData;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * 消息过滤器默认实现。
 */
public class DefaultMessageFilter implements MessageFilter {

    private SubscriptionData subscriptionData;

    public DefaultMessageFilter(final SubscriptionData subscriptionData) {
        this.subscriptionData = subscriptionData;
    }

    @Override
    public boolean isMatchedByConsumeQueue(Long tagsCode, ConsumeQueueExt.CqExtUnit cqExtUnit) {
        // 消息tagsCode 空 或者 订阅数据 空
        if (null == tagsCode || null == subscriptionData) {
            return true;
        }

        // classFilter
        if (subscriptionData.isClassFilterMode()) {
            return true;
        }

        //  subscriptionData.getSubString().equals(SubscriptionData.SUB_ALL) 订阅表达式 全匹配
        //  subscriptionData.getCodeSet().contains(tagsCode.intValue()) 订阅数据code数组 是否包含 消息tagsCode
        return subscriptionData.getSubString().equals(SubscriptionData.SUB_ALL)
            || subscriptionData.getCodeSet().contains(tagsCode.intValue());
    }

    @Override
    public boolean isMatchedByCommitLog(ByteBuffer msgBuffer, Map<String, String> properties) {
        return true;
    }
}
