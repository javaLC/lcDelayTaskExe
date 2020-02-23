package com.lc.delay.frame.delayclient;

import com.lc.delay.frame.common.msg.InvokeMsg;
import com.lc.delay.frame.common.rocketmq.AbsRocketProducer;
import com.lc.delay.frame.common.rocketmq.RocketMqConfig;
import com.lc.delay.frame.delayclient.invoke.InvokeManager;
import com.lc.delay.frame.delayclient.job.CallBackJob;
import com.lc.delay.frame.delayclient.job.TaskJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 *
 * 给客户端使用的
 *
 * @author liuchong
 * @version DelayClient.java, v 0.1 2020年02月21日 21:42
 */
public class DelayClient extends AbsRocketProducer {

    private Logger log = LoggerFactory.getLogger(DelayClient.class);

    public DelayClient(RocketMqConfig config) {
        super(config);
    }

    /**
     * 提交任务（如果taskMsg提交的给的任务队列名，对应多个任务/调用实现，将以第一次注册的为准）
     * <pre>
     *     比如：
     *     第一次提交给任务：A队列中，任务实现是A1
     *     第二次提交给任务：A队列中，任务实现是A2。
     *     则第二次提交的任务参数会被A1执行。
     * </pre>
     *
     * @param taskMsg
     * @param task
     * @param callback
     */
    public void submitTask(InvokeMsg taskMsg, TaskJob<InvokeMsg> task, CallBackJob<InvokeMsg> callback) {
        if(taskMsg == null) {
            throw new IllegalArgumentException("提交的任务数据不能为空");
        }

        // 提交的是任务，强制更新
        taskMsg.setType(task.getJobType().getType());
        // 为了观察任务客户端和服务端的关系，在这里就指定
        taskMsg.setMsgId(UUID.randomUUID().toString());

        // 注册任务
        InvokeManager.registerInvokeJob(task, taskMsg.getRegisterQueue());
        // 注册回调
        InvokeManager.registerInvokeJob(callback, taskMsg.getRegisterQueue());

        // 提交任务发送
        if(sendInvokeMsg(taskMsg)) {
            log.info("客户端请求提交任务，任务ID：" + taskMsg.getMsgId());
        }
    }


}
