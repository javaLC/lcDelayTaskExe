package com.lc.delay.frame.delayclient.register;

import com.lc.delay.frame.common.msg.InvokeMsg;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 * 给客户端使用的
 *
 * @author liuchong
 * @version DelayClient.java, v 0.1 2020年02月21日 21:42
 */
@Component
public class DelayClient implements ApplicationContextAware {


    private static ApplicationContext context;


    /**
     * 添加延迟任务
     *
     * @param task
     * @param callback
     * @param delayTime
     */
    public void addDelayTask(DelayTaskExecutor<InvokeMsg> task, DelayTaskCallbackListener callback, int delayTime) {

        RegisterManager.registerTaskExecutor(null, task);
        RegisterManager.registerTaskCallbackListener(null, callback);

    }


    /**
     * 添加延迟任务
     *
     * @param task
     * @param delayTime
     */
    public void addDelayTask(DelayTaskExecutor<InvokeMsg> task, int delayTime) {
        addDelayTask(task, null, delayTime);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DelayClient.context = applicationContext;
    }
}
