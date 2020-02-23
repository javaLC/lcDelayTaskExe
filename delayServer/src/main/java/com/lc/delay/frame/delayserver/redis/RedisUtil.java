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
import org.springframework.data.redis.core.ZSetOperations;
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
     * 获取并移除任务数据
     * @param taskName
     * @param futureSeconds 最近xxx秒需要执行的任务
     * @return
     */
    public List<TaskModel> consumeTask(String taskName, int futureSeconds) {
        final long maxScore = System.currentTimeMillis() + futureSeconds * 1000;

        Set<String> tasks = template.opsForZSet().rangeByScore(taskName, 0.0, maxScore);
        List<TaskModel> res = new ArrayList<>();

        if (tasks != null && !tasks.isEmpty()) {
            tasks.forEach(task -> res.add(JSONObject.parseObject(task, TaskModel.class)));

            // 删除。暂忽略删除时，刚好新增任务满足该条件
            template.opsForZSet().removeRangeByScore(taskName, 0.0, maxScore);
        }

        return res;
    }

    /**
     * 添加任务
     * @param task
     */
    public void addTask(TaskModel task) {
        // 在当前时间上加上延迟时间
        double score = System.currentTimeMillis() + task.getDelayScore();

        // 本次是根据任务名（或者说任务类型）而创建队列。其实也可以只有用一个队列就行了。只需要在这里将队列名固定即可
        template.opsForZSet().add(task.getTaskName(), JSONObject.toJSONString(task), score);

        addTaskQueue(task.getTaskName());
    }

    /**
     *
     * @param taskName
     */
    public void addTaskQueue(String taskName) {
        // 可以使用zset进行任务统计。本题设中，只记录所有任务名，不做统计。
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
