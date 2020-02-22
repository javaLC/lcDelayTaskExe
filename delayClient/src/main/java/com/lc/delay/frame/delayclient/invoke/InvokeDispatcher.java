package com.lc.delay.frame.delayclient.invoke;

/**
 * 调用分发器
 * <pre>
 *     一般而言，调用分发器是和调度通信关联的
 * </pre>
 *
 * @author liuchong
 * @version InvokeDispatcher.java, v 0.1 2020年02月22日 10:11
 */
public interface InvokeDispatcher<T> {
    /** 成功 */
    String SUCCESS = "SUCCESS";

    /**
     * 调用分发
     * @param param
     * @return
     */
    String invokeDispatcher(T param);

}
