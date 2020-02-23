package com.lc.delay.frame.delayserver.util;

import com.lc.delay.frame.common.msg.InvokeMsg;
import com.lc.delay.frame.common.util.DateUtils;
import com.lc.delay.frame.delayserver.task.TaskModel;

import java.util.Date;
import java.util.UUID;

/**
 * @author liuchong
 * @version TaskUtil.java, v 0.1 2020年02月23日 12:09
 */
public class TaskUtil {

    /**
     *
     * @param msg
     * @return
     */
    public static TaskModel toTaskModel(InvokeMsg msg) {
        TaskModel task = new TaskModel();

        task.setDelayScore(msg.getDelay());
        task.setTaskClass(msg.getParamClass());
        task.setTaskId(msg.getMsgId() == null ? UUID.randomUUID().toString() : msg.getMsgId());
        task.setTaskName(msg.getRegisterQueue());
        task.setTaskParam(msg.getParamJson());
        task.setType(msg.getType());

        task.setReceiveTime(
            msg.getNotifyTime() == null ? DateUtils.formatDate(new Date(), DateUtils.yyyy_MM_dd_HH_mm_ss_SSS)
                : msg.getNotifyTime());

        return task;
    }

    /**
     *
     * @param task
     * @return
     */
    public static InvokeMsg toInvokeMsg(TaskModel task) {
        InvokeMsg msg = new InvokeMsg();
        msg.setType(task.getType());

        // 负数会自动设置为0
        msg.setDelay((int)task.getDelayScore());
        msg.setParamClass(task.getTaskClass());
        msg.setParamJson(task.getTaskParam());
        msg.setRegisterQueue(task.getTaskName());
        msg.setNotifyTime(task.getReceiveTime());
        msg.setMsgId(task.getTaskId());

        return msg;
    }
}
