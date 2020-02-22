package com.lc.delay.frame.delayserver.dispatch;

import java.util.List;
import java.util.concurrent.*;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.lc.delay.frame.common.msg.InvokeMsg;
import com.lc.delay.frame.common.rocketmq.AbsRocketProducer;
import com.lc.delay.frame.common.rocketmq.RocketMqConfig;
import com.lc.delay.frame.delayserver.redis.RedisUtil;
import com.lc.delay.frame.delayserver.task.TaskModel;

/**
 * rockmq形式的mq消息进行调度(将调度和通信放一起，不拆了)
 *
 * @author liuchong
 * @version RocketMqInvokeDispatcher.java, v 0.1 2020年02月21日 21:13
 */
public class RocketMqInvokeDispatcher extends AbsRocketProducer
                                      implements InvokeDispatcher<InvokeMsg>, InitializingBean, DisposableBean {

    @Autowired
    RedisUtil redisUtil;

    public RocketMqInvokeDispatcher(RocketMqConfig config) {
        super(config);
    }

    @Override
    public void dispatch(InvokeMsg param) {
        if (param.getDelay() > 0) {
            invokeExecutor.scheduleWithFixedDelay(() -> sendInvokeMsg(param), 0, param.getDelay(),
                TimeUnit.MILLISECONDS);
        } else {
            sendInvokeMsg(param);
        }
    }

    // 任务捞取延迟就暂时放这里吧

    private volatile boolean continueFetcher;
    /** 任务获取 */
    private ExecutorService invokeFetcher  = Executors.newSingleThreadExecutor(r -> new Thread(r, "RedisWayInvokeJobFetcher-Thread"));
    /** 任务执行 */
    private ScheduledExecutorService invokeExecutor = Executors.newScheduledThreadPool(16, r -> new Thread(r, "RedisWayInvokeJobExecutor-Thread"));

    @Override
    public void afterPropertiesSet() throws Exception {
        continueFetcher = true;

        invokeFetcher.submit(() -> {

            int scope = 1;
            while (continueFetcher) {

                // 获取所有任务队列名称。这里单线程进行任务数据获取。如果有很多不同任务，则会影响整体时间执行精度
                redisUtil.fetchAllTaskName().forEach(taskName -> {

                    List<TaskModel> tasks = redisUtil.fetchTask(taskName, scope);

                    for(TaskModel t : tasks) {
                        InvokeMsg msg = new InvokeMsg();
                        msg.setType(t.getType());
                        long delayMill = t.getDelayScore() - System.currentTimeMillis();

                        // 负数会自动设置为0
                        msg.setDelay((int)delayMill);
                        msg.setParamClass(t.getTaskClass());
                        msg.setParamJson(t.getTaskParam());
                        msg.setRegisterQueue(t.getTaskName());

                        dispatch(msg);
                    }
                });
                try {
                    // 上面每次拉去最近1S内的。
                    Thread.sleep(scope * 1000);
                } catch (Exception e) {

                }
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
