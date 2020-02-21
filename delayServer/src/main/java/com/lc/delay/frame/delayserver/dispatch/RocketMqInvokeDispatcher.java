package com.lc.delay.frame.delayserver.dispatch;

import com.lc.delay.frame.common.msg.InvokeMsg;
import com.lc.delay.frame.common.rocketmq.AbsRocketConsumer;

/**
 * rockmq形式的mq消息进行调度
 *
 * @author liuchong
 * @version RocketMqInvokeDispatcher.java, v 0.1 2020年02月21日 21:13
 */
public class RocketMqInvokeDispatcher extends AbsRocketConsumer implements InvokeDispatcher<InvokeMsg> {


    @Override
    protected void consume(InvokeMsg msg) {
        dispatch(msg);
    }

    @Override
    public void dispatch(InvokeMsg param) {
        // 发送mq消息，给delayClient（对应：RocketMqWayInvokeJob）

        // 这种方式，则与redis没有什么关系了。与题设中需要利用redis不符合。放着即可

    }
}
