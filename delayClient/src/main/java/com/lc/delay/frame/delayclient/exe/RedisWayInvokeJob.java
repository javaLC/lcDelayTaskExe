package com.lc.delay.frame.delayclient.exe;

import java.util.concurrent.*;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lc.delay.frame.common.msg.InvokeMsg;
import com.lc.delay.frame.delayclient.register.RegisterManager;

/**
 * redis方式执行job/任务（本次题设内容）
 *
 * @author liuchong
 * @version RedisWayInvokeJob.java, v 0.1 2020年02月21日 21:32
 */
@Component
public class RedisWayInvokeJob implements InvokeJob<InvokeMsg>, InitializingBean, DisposableBean {

    /**  */
    private volatile boolean continueFetcher;
    /** 任务获取 */
    private ExecutorService invokeFetcher  = Executors.newFixedThreadPool(1,
        r -> new Thread(r, "RedisWayInvokeJobFetcher-Thread"));
    /** 任务执行 */
    private ExecutorService invokeExecutor = new ThreadPoolExecutor(4, 16, 30L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(), r -> new Thread(r, "RedisWayInvokeJobExecutor-Thread"));

    @Override
    public String execute(InvokeMsg param) {
        return defaultInvoke(param);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        continueFetcher = true;

        invokeFetcher.submit(() -> {
            while (continueFetcher) {

                // 获取所有任务队列名称
                RegisterManager.fetchAllTask().forEach(task -> {

                    // 根据task获取redis队列中的数据，先判断是否有，如果有则获取，否则continue

                    // 获取到任务数据后，for循环执行
                    String taskMsg = null;

                    invokeExecutor.submit(() -> execute(JSONObject.parseObject(taskMsg, InvokeMsg.class)));
                });
            }
        });
    }

    @Override
    public void destroy() throws Exception {
        continueFetcher = false;
        invokeFetcher.shutdownNow();
        invokeExecutor.shutdown();
    }

}
