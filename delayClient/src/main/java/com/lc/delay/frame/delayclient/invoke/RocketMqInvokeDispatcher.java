package com.lc.delay.frame.delayclient.invoke;

import com.alibaba.fastjson.JSON;
import com.lc.delay.frame.common.InvokeType;
import com.lc.delay.frame.common.msg.InvokeMsg;
import com.lc.delay.frame.common.rocketmq.AbsRocketConsumer;
import com.lc.delay.frame.common.rocketmq.RocketMqConfig;
import com.lc.delay.frame.delayclient.job.InvokeJob;

/**
 * 建立在rocketmq通信方式的job分发
 *
 * @author liuchong
 * @version RocketMqInvokeDispatcher.java, v 0.1 2020年02月22日 10:12
 */
public class RocketMqInvokeDispatcher extends AbsRocketConsumer implements InvokeDispatcher<InvokeMsg> {

    public RocketMqInvokeDispatcher(RocketMqConfig config) {
        super(config);
    }

    @Override
    protected void consume(InvokeMsg msg) {
        invokeDispatcher(msg);
    }

    @Override
    public String invokeDispatcher(InvokeMsg param) {
        InvokeType type = InvokeType.getInvokeType(param.getType());
        if(type == null) {
            return "未知的调用类型：" + JSON.toJSONString(param);
        }
        InvokeJob invoke = InvokeManager.fetchInvokeJob(type, param.getRegisterQueue());

        if(invoke != null) {
            invoke.invoke(param);
        }

        return SUCCESS;
    }


}
