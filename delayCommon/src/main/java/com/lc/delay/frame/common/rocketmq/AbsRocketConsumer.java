package com.lc.delay.frame.common.rocketmq;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSONObject;
import com.lc.delay.frame.common.msg.InvokeMsg;

/**
 * mq消息消费
 *
 * @author liuchong
 * @version AbsRocketConsumer.java, v 0.1 2020年02月21日 20:38
 */
public abstract class AbsRocketConsumer implements InitializingBean, DisposableBean {

    private Logger log = LoggerFactory.getLogger(AbsRocketConsumer.class);

    private RocketMqConfig config;
    private DefaultMQPushConsumer consumer;

    public AbsRocketConsumer(RocketMqConfig config) {
        this.config = config;
    }

    /**
     * 消费处理(暂时不做消费成功与否的返回)
     * @param msg
     */
    protected abstract void consume(InvokeMsg msg);

    @Override
    public void destroy() throws Exception {
        consumer.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        consumer = new DefaultMQPushConsumer(config.getGroup());
        consumer.setNamesrvAddr(config.getHost());
        consumer.subscribe(config.getTopic(), "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for(MessageExt ext : msgs) {
                    try {
                        InvokeMsg msg = JSONObject.parseObject(
                            new String(msgs.get(0).getBody(), RemotingHelper.DEFAULT_CHARSET), InvokeMsg.class);
                        consume(msg);
                    } catch (Exception e) {
                        log.error("消费异常msgId：" + ext.getMsgId(), e);
                    }
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
    }
}
