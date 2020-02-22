package com.lc.delay.frame.delayclient.job;

import com.lc.delay.frame.common.InvokeType;

/**
 * 不管是回调还是普通任务执行，本质上都是调用xxx。暂且称之为job
 *
 * @author liuchong
 * @version InvokeJob.java, v 0.1 2020年02月22日 09:53
 */
public abstract class InvokeJob<T> {

    /** 任务类型  */
    private InvokeType jobType;

    public InvokeJob(InvokeType type) {
        if(type == null) {
            throw new IllegalArgumentException("job类型不能为空");
        }
        this.jobType = type;
    }

    /**
     * 返回job类型类型
     * @return
     */
    public InvokeType getJobType() {
        return jobType;
    }

    /**
     * 调用
     * @param param
     * @return
     */
    public abstract String invoke(T param);

}
