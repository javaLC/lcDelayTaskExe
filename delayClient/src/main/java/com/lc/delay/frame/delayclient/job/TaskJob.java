package com.lc.delay.frame.delayclient.job;

import com.lc.delay.frame.common.InvokeType;

/**
 * 规范task定义
 *
 * @author liuchong
 * @version TaskJob.java, v 0.1 2020年02月22日 09:54
 */
public abstract class TaskJob<T> extends InvokeJob<T> {

    public TaskJob() {
        super(InvokeType.TASK);
    }

    @Override
    public String invoke(T param) {
        return doTask(param);
    }

    /**
     * 执行任务
     *
     * @param param
     * @return
     */
    public abstract String doTask(T param);
}
