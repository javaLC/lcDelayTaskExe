package com.lc.delay.frame.delayserver.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lc.delay.frame.delayserver.task.TaskModel;

import javax.annotation.Resource;

/**
 * 在此不对redis进行设计行的封装了，直接提供个操作的util
 *
 * @author liuchong
 * @version RedisUtil.java, v 0.1 2020年02月22日 11:38
 */
@Component
public class RedisUtil {

    @Value("${delay.task.queueNames}")
    String taskNameQueue;

    @Resource
    StringRedisTemplate template;

    /**
     * 获取数据
     * @param taskName
     * @return
     */
    public List<TaskModel> fetchTask(String taskName, int delaySeconds) {
        final long maxScore = System.currentTimeMillis() + delaySeconds * 1000;
        // ZREMRANGEBYSCORE
        Set<String> tasks = template.opsForZSet().rangeByScore(taskName, 0, maxScore);
        List<TaskModel> res = new ArrayList<>();
        if (tasks != null && !tasks.isEmpty()) {
            tasks.forEach(task -> {
                res.add(JSONObject.parseObject(task, TaskModel.class));
            });

            // 删除。暂忽略删除时，刚好新增任务满足该条件
            template.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    // 为什么不返回删除的数据
                    redisConnection.zRemRange(null, 0, maxScore);
                    return null;
                }
            });
        }

        return res;
    }

    /**
     * 添加任务
     * @param task
     */
    public void addTask(TaskModel task) {

        double score = task.getDelayScore();

        template.opsForZSet().add(task.getTaskName(), JSONObject.toJSONString(task), score);

        addTaskQueue(task.getTaskName());
    }

    /**
     *
     * @param taskName
     */
    public void addTaskQueue(String taskName) {
        // 可以使用zset进行任务统计。本题设中，就不这么做了。
        template.opsForSet().add(taskNameQueue, taskName);
    }

    /**
     * 获取所有任务
     * @return
     */
    public Set<String> fetchAllTaskName() {
        Set<String> set = template.opsForSet().members(taskNameQueue);

        return set == null ? new HashSet<>() : set;
    }

}
