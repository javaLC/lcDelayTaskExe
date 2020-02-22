package com.lc.delay.frame.delayserver.task;

import java.util.List;

/**
 * 任务存储管理器（简单定义任务数据基本的插，删，改，查）
 * <pre>
 *     数据存储一般会进行读写接口分离，针对题设免去这些
 * </pre>
 * @author liuchong
 * @version TaskStorageManger.java, v 0.1 2020年02月22日 11:21
 */
public interface TaskStorageManger {

    /***
     * 添加任务
     * @param task
     */
    void addTask(TaskModel task);

    /**
     * 删除任务
     * @param taskId
     */
    void delTask(String taskId);

    /**
     * 查询任务（针对题设定义个查询）
     *
     * @return
     */
    List<TaskModel> queryRedisTask(String queueName, int delayTime);


}
