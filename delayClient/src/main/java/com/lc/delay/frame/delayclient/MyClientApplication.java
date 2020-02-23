package com.lc.delay.frame.delayclient;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lc.delay.frame.common.InvokeType;
import com.lc.delay.frame.common.msg.InvokeMsg;
import com.lc.delay.frame.common.msg.InvokeMsgBuilder;
import com.lc.delay.frame.common.util.DateUtils;
import com.lc.delay.frame.delayclient.invoke.InvokeManager;
import com.lc.delay.frame.delayclient.invoke.RocketMqInvokeDispatcher;
import com.lc.delay.frame.delayclient.job.CallBackJob;
import com.lc.delay.frame.delayclient.job.TaskJob;

/**
 * @author liuchong
 * @version MyClientApplication.java, v 0.1 2020年02月21日 14:26
 */
@SpringBootApplication(scanBasePackages = {"com.lc.delay.frame"})
@Configuration
@RestController
public class MyClientApplication {
    private Logger log = LoggerFactory.getLogger(MyClientApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MyClientApplication.class, args);
    }

    @Autowired
    DelayClient delayClient;

    @Autowired
    RocketMqInvokeDispatcher RocketMqInvokeDispatcher;

    @RequestMapping("/")
    public String host() {
        return "delayClient run ok";
    }

    @RequestMapping("/submitTask")
    public String submitTask(String taskName, Integer delay) {
        if(taskName == null) {
            return "任务名不能为空";
        }
        log.info("正在添加任务：" + taskName);

        // 参数之类的就默认
        InvokeMsg msg = InvokeMsgBuilder.buidlTaskMsg(taskName).buildDelayTime(delay == null ? 0 : delay)
            .buildReqParam(new TestParam("参数1", "参数2")).toTaskMsg();

        delayClient.submitTask(msg, new TaskJob<InvokeMsg>() {
            @Override
            public String doTask(InvokeMsg param) {
                // 通用执行逻辑。
                log.info(JSON.toJSONString(DateUtils.formatDate(new Date(), DateUtils.yyyy_MM_dd_HH_mm_ss_SSS)
                                           + "受到任务调度通知执行：" + JSON.toJSONString(param)));
                return null;
            }
        }, new CallBackJob<InvokeMsg>() {

            @Override
            public String callback(InvokeMsg param) {
                // 回调
                log.info(JSON.toJSONString(DateUtils.formatDate(new Date(), DateUtils.yyyy_MM_dd_HH_mm_ss_SSS)
                                           + "任务执行完后回调：" + JSON.toJSONString(param)));
                return null;
            }
        });

        return "添加成功";
    }

    @RequestMapping("/taskList")
    public String getAllTaskQueue() {
        return JSON.toJSONString(InvokeManager.fetchJobNames(InvokeType.TASK));
    }



    public static class TestParam implements Serializable {
        private String p1;
        private String p2;

        public TestParam() {
        }

        public TestParam(String p1, String p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        public String getP1() {
            return p1;
        }

        public void setP1(String p1) {
            this.p1 = p1;
        }

        public String getP2() {
            return p2;
        }

        public void setP2(String p2) {
            this.p2 = p2;
        }
    }

}
