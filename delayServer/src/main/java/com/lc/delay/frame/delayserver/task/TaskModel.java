package com.lc.delay.frame.delayserver.task;

import java.io.Serializable;

/**
 * 任务模型
 *
 * @author liuchong
 * @version TaskModel.java, v 0.1 2020年02月22日 11:23
 */
public class TaskModel implements Serializable {


    /** job类型（type）{@link com.lc.delay.frame.common.InvokeType} */
    private int type;
    /** 任务名称 */
    private String taskName;
    /** 任务唯一id */
    private String taskId;
    /** 任务参数 */
    private String taskParam;
    /** 参数类型 */
    private String taskClass;

    /** 延迟执行分数：存入时时间 + 延迟时间。（毫秒） */
    private long delayScore;

    /*
    对于任务模型而言，这里不做过多设计，先简单点，针对此次题设。
     */

    /*
     delay，只是调度策略的一种，还可以定义更多的策略，比如：定点执行，频次执行等
     策略可以抽离出单独一个模型。在此略
     */

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskParam() {
        return taskParam;
    }

    public void setTaskParam(String taskParam) {
        this.taskParam = taskParam;
    }

    public String getTaskClass() {
        return taskClass;
    }

    public void setTaskClass(String taskClass) {
        this.taskClass = taskClass;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public long getDelayScore() {
        return delayScore;
    }

    public void setDelayScore(long delayScore) {
        this.delayScore = delayScore;
    }
}
