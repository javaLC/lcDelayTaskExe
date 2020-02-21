package com.lc.delay.frame.delayclient.exe;

import com.lc.delay.frame.common.msg.InvokeMsg;
import com.lc.delay.frame.common.rocketmq.AbsRocketConsumer;

/**
 * 通过mq方式执行job/任务
 *
 * @author liuchong
 * @version RocketMqWayInvokeJob.java, v 0.1 2020年02月21日 21:37
 */
public class RocketMqWayInvokeJob extends AbsRocketConsumer implements InvokeJob<InvokeMsg> {


    @Override
    protected void consume(InvokeMsg msg) {
        execute(msg);
    }

    @Override
    public String execute(InvokeMsg param) {
        // 以mq通知的方式通知（调度）执行任务（调用任务都是统一的）

        return defaultInvoke(param);
    }
}
