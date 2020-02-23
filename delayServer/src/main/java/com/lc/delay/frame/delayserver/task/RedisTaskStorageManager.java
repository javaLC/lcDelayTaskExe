package com.lc.delay.frame.delayserver.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lc.delay.frame.delayserver.redis.RedisUtil;

/**
 * redis存储任务
 *
 * @author liuchong
 * @version RedisTaskStorageManager.java, v 0.1 2020年02月22日 11:37
 */
@Service
public class RedisTaskStorageManager implements TaskStorageManger {

    @Autowired
    private RedisUtil redis;

    @Override
    public void addTask(TaskModel task) {
        redis.addTask(task);
    }

    @Override
    public void delTask(String taskId) {
        //
    }

}
