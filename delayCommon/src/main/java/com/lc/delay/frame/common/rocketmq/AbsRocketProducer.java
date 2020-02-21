package com.lc.delay.frame.common.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSON;
import com.lc.delay.frame.common.msg.InvokeMsg;

/**
 * mq消息生产
 *
 * @author liuchong
 * @version AbsRocketProducer.java, v 0.1 2020年02月21日 19:43
 */
public abstract class AbsRocketProducer implements InitializingBean, DisposableBean {

    private Logger log = LoggerFactory.getLogger(AbsRocketProducer.class);

    private RocketMqConfig config;
    private DefaultMQProducer producer;

    public AbsRocketProducer(RocketMqConfig config) {
        this.config = config;
    }

    /**
     * 发送mq消息
     *
     * @param invokeMsg
     * @param delay
     */
    public void sendInvokeMsg(InvokeMsg invokeMsg, int delay) {
        String body = JSON.toJSONString(invokeMsg);
        /*
          延迟等级时间关系： 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
         */

        try {
            Message msg = new Message(config.getTopic(), invokeMsg.getRegisterQueue(),
                body.getBytes(RemotingHelper.DEFAULT_CHARSET));
            msg.setDelayTimeLevel(delay);
            SendResult sendResult = producer.send(msg);
            System.out.println(JSON.toJSONString(sendResult));
        } catch (Exception e) {
            log.error("发送调用消息失败msg：" + JSON.toJSONString(invokeMsg), e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        producer = new DefaultMQProducer(config.getGroup());
        producer.setNamesrvAddr(config.getHost());
        producer.start();
    }

    @Override
    public void destroy() throws Exception {
        producer.shutdown();
    }
}
