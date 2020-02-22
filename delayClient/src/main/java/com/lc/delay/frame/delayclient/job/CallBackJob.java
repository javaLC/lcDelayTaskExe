package com.lc.delay.frame.delayclient.job;

import com.lc.delay.frame.common.InvokeType;

/**
 * 规范回调定义
 *
 * @author liuchong
 * @version CallBackJob.java, v 0.1 2020年02月22日 09:54
 */
public abstract class CallBackJob<T> extends InvokeJob<T> {

    public CallBackJob() {
        super(InvokeType.CALLBACK);
    }

    @Override
    public String invoke(T param) {
        return callback(param);
    }

    /**
     * 执行回调
     *
     * @param param
     * @return
     */
    public abstract String callback(T param);
}
