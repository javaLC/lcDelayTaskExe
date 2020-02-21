package com.lc.delay.frame.delayclient.register;

/**
 * 延迟任务执行器
 *
 * @author liuchong
 * @version DelayTaskExecutor.java, v 0.1 2020年02月21日 21:45
 */
public interface DelayTaskExecutor<T> {


    /**
     *
     * @param param
     */
    void doTask(T param);


}
