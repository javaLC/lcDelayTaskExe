package com.lc.delay.frame.delayclient.register;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册管理器
 *
 * @author liuchong
 * @version RegisterManager.java, v 0.1 2020年02月21日 21:55
 */
public class RegisterManager {

    /**
     * 延迟任务执行
     */
    private static Map<String, DelayTaskExecutor> delayTaskExecutorMap = new ConcurrentHashMap<>();
    /**
     * 延迟任务回调
     */
    private static Map<String, DelayTaskCallbackListener> delayTaskCallbackMap = new ConcurrentHashMap<>();

    /**
     * 注册任务执行器
     * 
     * @param taskName
     * @param executor
     */
    public static void registerTaskExecutor(String taskName, DelayTaskExecutor executor) {
        if(executor != null) {
            delayTaskExecutorMap.put(taskName, executor);
        }
    }

    /**
     * 注册任务回调器
     * 
     * @param taskName
     * @param listener
     */
    public static void registerTaskCallbackListener(String taskName, DelayTaskCallbackListener listener) {
        if(listener != null) {
            delayTaskCallbackMap.put(taskName, listener);
        }
    }

    /**
     * 获取任务执行期
     *
     * @param taskName
     * @return
     */
    public static DelayTaskExecutor fetchDelayTaskExecutor(String taskName) {
        return delayTaskExecutorMap.get(taskName);
    }

    /**
     * 获取任务执行完后的回调器
     *
     * @param taskName
     * @return
     */
    public static DelayTaskCallbackListener fetchDelayTaskCallbackListener(String taskName) {
        return delayTaskCallbackMap.get(taskName);
    }

    /**
     * 获取所有任务
     *
     * @return
     */
    public static Set<String> fetchAllTask() {
        // 正常操作中，任务执行器的数据是包含回调器的
        return new HashSet<>(delayTaskExecutorMap.keySet());
    }

    /**
     * 是否存在某个任务
     *
     * @param taskName
     * @return
     */
    public static boolean isExistDelayTask(String taskName) {
        return delayTaskExecutorMap.get(taskName) != null;
    }



}
