package com.lc.delay.frame.delayclient.exe;

import com.alibaba.fastjson.JSON;
import com.lc.delay.frame.common.InvokeType;
import com.lc.delay.frame.common.msg.InvokeMsg;
import com.lc.delay.frame.delayclient.register.DelayTaskCallbackListener;
import com.lc.delay.frame.delayclient.register.DelayTaskExecutor;
import com.lc.delay.frame.delayclient.register.RegisterManager;

/**
 * 调用job
 *
 * @author liuchong
 * @version InvokeJob.java, v 0.1 2020年02月21日 21:33
 */
public interface InvokeJob<T> {

    String SUCCESS = "SUCCESS";

    /**
     * 执行
     * @param param
     * @return
     */
    String execute(T param);





    /**
     * 暂时先放这里吧 <br/>
     * mq或者redis方式进行任务调度通信
     *
     * @param param
     * @return
     */
    default String defaultInvoke(InvokeMsg param) {
        InvokeType type = InvokeType.getInvokeType(param.getType());
        if(type == null) {
            return "未知的调用类型：" + JSON.toJSONString(param);
        }

        switch (type) {
            case TASK:
                DelayTaskExecutor exe = RegisterManager.fetchDelayTaskExecutor(param.getRegisterQueue());
                if(exe != null) {
                    exe.doTask(param);
                }
                break;

            case CALLBACK:
                // 通过mq方式的回调，就不存调用结果了吧
                DelayTaskCallbackListener listener = RegisterManager.fetchDelayTaskCallbackListener(param.getRegisterQueue());
                if(listener != null) {
                    listener.callback(param, null);
                }

                break;
        }
        return SUCCESS;
    }

}
