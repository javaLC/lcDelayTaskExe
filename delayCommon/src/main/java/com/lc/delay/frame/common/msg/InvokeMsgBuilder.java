package com.lc.delay.frame.common.msg;

import com.alibaba.fastjson.JSON;
import com.lc.delay.frame.common.InvokeType;

import java.io.Serializable;

/**
 * 消息构建者
 *
 * @author liuchong
 * @version InvokeMsgBuilder.java, v 0.1 2020年02月21日 18:57
 */
public class InvokeMsgBuilder {

    private InvokeMsg msg = new InvokeMsg();

    private InvokeMsgBuilder() {
    }

    /**
     * 构建任务类型的消息
     * @param queue 注册到队列
     * @return
     */
    public static InvokeMsgBuilder buidlTaskMsg(String queue) {
        InvokeMsgBuilder builder = new InvokeMsgBuilder();
        builder.msg.setRegisterQueue(queue);
        builder.msg.setType(InvokeType.TASK.getType());
        return builder;
    }

    /**
     * 构建回调类型消息
     * @param queue 注册到队列
     * @return
     */
    public static InvokeMsgBuilder buidlCallbackMsg(String queue) {
        InvokeMsgBuilder builder = new InvokeMsgBuilder();
        builder.msg.setRegisterQueue(queue);
        builder.msg.setType(InvokeType.CALLBACK.getType());
        return builder;
    }

    /**
     * 构建请求参数
     * @return
     */
    public <T extends Serializable> InvokeMsgBuilder buildReqParam(T param) {
        if (param != null) {
            msg.setParamClass(param.getClass().getName());
            msg.setParamJson(JSON.toJSONString(param));
        }

        return this;
    }

    /**
     * 构建延迟
     * @param delaySecond
     * @return
     */
    public InvokeMsgBuilder buildDelayTime(int delaySecond) {
        // 如果延迟时间小于等于0，说明是实时任务执行，不需要延迟
        if (delaySecond > 0) {
            msg.setDelay(delaySecond * 1000);
        }
        return this;
    }

    /**
     *
     * @return
     */
    public InvokeMsg toTaskMsg() {
        return msg;
    }

}
