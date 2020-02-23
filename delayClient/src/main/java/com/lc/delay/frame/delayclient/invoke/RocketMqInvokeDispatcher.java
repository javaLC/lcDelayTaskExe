package com.lc.delay.frame.delayclient.invoke;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.lc.delay.frame.common.InvokeType;
import com.lc.delay.frame.common.msg.InvokeMsg;
import com.lc.delay.frame.common.rocketmq.AbsRocketConsumer;
import com.lc.delay.frame.common.rocketmq.RocketMqConfig;
import com.lc.delay.frame.delayclient.job.InvokeJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 建立在rocketmq通信方式的job分发
 *
 * @author liuchong
 * @version RocketMqInvokeDispatcher.java, v 0.1 2020年02月22日 10:12
 */
@Component
public class RocketMqInvokeDispatcher extends AbsRocketConsumer implements InvokeDispatcher<InvokeMsg> {

    @Autowired
    public RocketMqInvokeDispatcher(@Qualifier("taskExecutorConfig")RocketMqConfig config) {
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
            // 也可以将结果给callback
            ifNeedDoCallBack(param);
        }

        return SUCCESS;
    }

    void ifNeedDoCallBack(InvokeMsg msg) {
        // 执行job后回调。项目中支持两种方式：1. 发mq消息（类型是callback）；2. 在这里直接调用。
        // 如果，需要知道任务执行的结果，采用第2种方式。
        // 对于回调，也可以在任务模型中定义：是否需要回调。即：当注册过回调函数，且需要回调则进行回调。否则不回调
        if (msg.getType() == InvokeType.TASK.getType()) {
            InvokeJob invoke = InvokeManager.fetchInvokeJob(InvokeType.CALLBACK, msg.getRegisterQueue());
            if(invoke != null) {
                InvokeMsg target = new InvokeMsg();
                BeanUtils.copyProperties(msg, target);
                target.setType(InvokeType.CALLBACK.getType());
                invoke.invoke(target);
                // 2. 发送mq消息
            }

        }
    }

}
