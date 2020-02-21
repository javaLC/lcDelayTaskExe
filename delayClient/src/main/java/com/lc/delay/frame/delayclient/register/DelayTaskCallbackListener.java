package com.lc.delay.frame.delayclient.register;

/**
 * 延迟任务回调
 *
 * @author liuchong
 * @version DelayTaskCallbackListener.java, v 0.1 2020年02月21日 21:44
 */
public interface DelayTaskCallbackListener<P, R> {

    /**
     * 任务执行回调
     *
     * @param param
     * @param result
     */
    void callback(P param, R result);

}
