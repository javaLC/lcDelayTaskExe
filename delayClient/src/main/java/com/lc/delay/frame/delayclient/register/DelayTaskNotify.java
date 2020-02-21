package com.lc.delay.frame.delayclient.register;

import org.springframework.stereotype.Component;

import com.lc.delay.frame.common.rocketmq.AbsRocketProducer;

/**
 * 延迟任务通知
 *
 * @author liuchong
 * @version DelayTaskNotify.java, v 0.1 2020年02月21日 23:20
 */
@Component
public class DelayTaskNotify extends AbsRocketProducer {


    public DelayTaskNotify() {
        super(null);
    }
}
