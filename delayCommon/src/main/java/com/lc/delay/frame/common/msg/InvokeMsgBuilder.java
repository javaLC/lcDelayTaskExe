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
public class InvokeMsgBuilder<T extends Serializable> {

    private InvokeMsg msg = new InvokeMsg();

    private InvokeMsgBuilder() {}

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
    public InvokeMsgBuilder buildReqParam(T param) {
        if (param != null) {
            msg.setParamClass(param.getClass().getName());
            msg.setParamJson(JSON.toJSONString(param));
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
