package com.lc.delay.frame.delayserver.dispatch;

import com.lc.delay.frame.common.msg.InvokeMsg;
import com.lc.delay.frame.common.rocketmq.AbsRocketConsumer;
import org.springframework.stereotype.Component;

/**
 * redis形式的消息进行调度
 *
 * @author liuchong
 * @version RedisWayInvokeDispatcher.java, v 0.1 2020年02月21日 23:08
 */
@Component
public class RedisWayInvokeDispatcher extends AbsRocketConsumer implements InvokeDispatcher<InvokeMsg> {

    @Override
    protected void consume(InvokeMsg msg) {
        dispatch(msg);
    }

    @Override
    public void dispatch(InvokeMsg param) {
        // 将接收到的任务消息放入到redis队列中

        // rpush  param.getRegisterQueue()  JSON.toJSONString(param);
    }

}
