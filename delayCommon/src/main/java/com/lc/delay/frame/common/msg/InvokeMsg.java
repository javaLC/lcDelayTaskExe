package com.lc.delay.frame.common.msg;

import java.io.Serializable;

/**
 * 统一消息定义。 <br>
 * 不建议直接使用new InvokeMsg
 * {@link InvokeMsgBuilder}
 *
 * @author liuchong
 * @version InvokeMsg.java, v 0.1 2020年02月21日 18:48
 */
public class InvokeMsg implements Serializable {

    /**
     * 消息类型。这里分为：1-任务消息，2-回调消息{@link com.lc.delay.frame.common.InvokeType}
     * 其实从调度来说：不管是真正的任务，还是任务完后回调，本质上都可以看作一次调度
     */
    private int type;
    /** 调用注册队列 */
    private String registerQueue;
    /** 调度时入参json格式 */
    private String paramJson;
    /** 参数对应类型 */
    private String paramClass;
    /** 任务延迟时间（毫秒） */
    private int delay;
    /** 消息发送时时间 */
    private String notifyTime;
    /** 为了打印查看效果方便。这由服务器生成，与消rocketmq无关 */
    private String msgId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRegisterQueue() {
        return registerQueue;
    }

    public void setRegisterQueue(String registerQueue) {
        this.registerQueue = registerQueue;
    }

    public String getParamJson() {
        return paramJson;
    }

    public void setParamJson(String paramJson) {
        this.paramJson = paramJson;
    }

    public String getParamClass() {
        return paramClass;
    }

    public void setParamClass(String paramClass) {
        this.paramClass = paramClass;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        if(delay > 0) {
            this.delay = delay;
        }
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
