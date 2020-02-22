package com.lc.delay.frame.delayserver.dispatch;

/**
 * 任务调度分发器
 *
 * @author liuchong
 * @version InvokeDispatcher.java, v 0.1 2020年02月21日 21:19
 */
public interface InvokeDispatcher<T> {
    // 调用分发器，参数没想好，先范型

    /**
     * 调用派发
     * @param param
     */
    void dispatch(T param);

}
