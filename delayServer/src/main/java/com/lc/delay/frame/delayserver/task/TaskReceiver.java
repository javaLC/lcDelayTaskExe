package com.lc.delay.frame.delayserver.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lc.delay.frame.common.InvokeType;
import com.lc.delay.frame.common.msg.InvokeMsg;
import com.lc.delay.frame.common.rocketmq.AbsRocketConsumer;
import com.lc.delay.frame.common.rocketmq.RocketMqConfig;
import com.lc.delay.frame.common.util.DateUtils;
import com.lc.delay.frame.delayserver.dispatch.RocketMqInvokeDispatcher;
import com.lc.delay.frame.delayserver.redis.RedisUtil;
import com.lc.delay.frame.delayserver.util.TaskUtil;

/**
 * 任务接收(简单实现，不做设计了)
 *
 * @author liuchong
 * @version TaskReceiver.java, v 0.1 2020年02月22日 13:55
 */
public class TaskReceiver extends AbsRocketConsumer {

    private Logger log = LoggerFactory.getLogger(TaskReceiver.class);

    @Autowired
    TaskStorageManger redisStorageManager;
    @Autowired
    RedisUtil redisUtil;

    public TaskReceiver(RocketMqConfig config) {
        super(config);
    }

    @Override
    protected void consume(InvokeMsg msg) {
        TaskModel task = TaskUtil.toTaskModel(msg);
        task.setReceiveTime(DateUtils.formatDate(new Date(), DateUtils.yyyy_MM_dd_HH_mm_ss_SSS));

        // 优化，如果是回调型的消息，则直接进行回调，无需存储
        if(msg.getType() == InvokeType.CALLBACK.getType()) {
            // 回调类型的直接通知，不延迟
            task.setDelayScore(0);
            rocketMqInvokeDispatcher.dispatch(task);
            return;
        }


        log.info("接收到任务请求：" + task.toString());

        redisStorageManager.addTask(task);
    }

    @Autowired
    RocketMqInvokeDispatcher rocketMqInvokeDispatcher;

}
