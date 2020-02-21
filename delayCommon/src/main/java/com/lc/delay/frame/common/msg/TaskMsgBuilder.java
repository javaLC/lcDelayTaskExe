package com.lc.delay.frame.common.msg;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * 消息构建者
 *
 * @author liuchong
 * @version TaskMsgBuilder.java, v 0.1 2020年02月21日 18:57
 */
public class TaskMsgBuilder<T extends Serializable> {

    private InvokeMsg msg = new InvokeMsg();

    private TaskMsgBuilder() {}

    /**
     * 构建任务类型的消息
     * @param queue 注册到队列
     * @return
     */
    public static TaskMsgBuilder buidlTaskMsg(String queue) {
        TaskMsgBuilder builder = new TaskMsgBuilder();
        builder.msg.setRegisterQueue(queue);
        builder.msg.setType(1);
        return builder;
    }

    /**
     * 构建回调类型消息
     * @param queue 注册到队列
     * @return
     */
    public static TaskMsgBuilder buidlCallbackMsg(String queue) {
        TaskMsgBuilder builder = new TaskMsgBuilder();
        builder.msg.setRegisterQueue(queue);
        builder.msg.setType(2);
        return builder;
    }

    /**
     * 构建请求参数
     * @return
     */
    public TaskMsgBuilder buildReqParam(T param) {
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
