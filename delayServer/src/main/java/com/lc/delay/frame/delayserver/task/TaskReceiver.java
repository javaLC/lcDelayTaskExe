package com.lc.delay.frame.delayserver.task;

import java.util.UUID;

import com.lc.delay.frame.common.InvokeType;
import org.springframework.beans.factory.annotation.Autowired;

import com.lc.delay.frame.common.msg.InvokeMsg;
import com.lc.delay.frame.common.rocketmq.AbsRocketConsumer;
import com.lc.delay.frame.common.rocketmq.RocketMqConfig;
import com.lc.delay.frame.delayserver.redis.RedisUtil;

/**
 * 任务接收(简单实现，不做设计了)
 *
 * @author liuchong
 * @version TaskReceiver.java, v 0.1 2020年02月22日 13:55
 */
public class TaskReceiver extends AbsRocketConsumer {

    @Autowired
    TaskStorageManger redisStorageManager;
    @Autowired
    RedisUtil redisUtil;

    public TaskReceiver(RocketMqConfig config) {
        super(config);
    }

    @Override
    protected void consume(InvokeMsg msg) {
        // 优化，如果是回调型的消息，则直接进行回调，无需存储
        if(msg.getType() == InvokeType.CALLBACK.getType()) {
            return;
        }

        TaskModel task = new TaskModel();

        task.setDelayScore(System.currentTimeMillis() + msg.getDelay() * 1000);
        task.setTaskClass(msg.getParamClass());
        task.setTaskId(UUID.randomUUID().toString());
        task.setTaskName(msg.getRegisterQueue());
        task.setTaskParam(msg.getParamJson());
        task.setType(msg.getType());

        redisStorageManager.addTask(task);
    }
}
