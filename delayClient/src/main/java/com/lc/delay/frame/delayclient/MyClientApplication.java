package com.lc.delay.frame.delayclient;

import com.alibaba.fastjson.JSON;
import com.lc.delay.frame.common.InvokeType;
import com.lc.delay.frame.common.msg.InvokeMsg;
import com.lc.delay.frame.common.msg.InvokeMsgBuilder;
import com.lc.delay.frame.common.rocketmq.RocketMqConfig;
import com.lc.delay.frame.delayclient.invoke.InvokeManager;
import com.lc.delay.frame.delayclient.invoke.RocketMqInvokeDispatcher;
import com.lc.delay.frame.delayclient.job.CallBackJob;
import com.lc.delay.frame.delayclient.job.TaskJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * @author liuchong
 * @version MyClientApplication.java, v 0.1 2020年02月21日 14:26
 */
@SpringBootApplication
@Configuration
@RestController
public class MyClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyClientApplication.class, args);
    }

    @Autowired
    DelayClient delayClient;

    @RequestMapping("/")
    public String host() {
        return "delayClient run ok";
    }

    @RequestMapping("/submitTask")
    public String submitTask(String taskName, Integer delay) {

        InvokeMsg msg = InvokeMsgBuilder.buidlTaskMsg(taskName).buildDelayTime(delay == null ? 0 : delay)
            .buildReqParam(new TestParam("参数1", "参数2")).toTaskMsg();

        delayClient.submitTask(msg, new TaskJob<InvokeMsg>() {
            @Override
            public String doTask(InvokeMsg param) {
                // 通用执行逻辑。
                System.out.println(JSON.toJSONString(System.currentTimeMillis() + "任务执行：" + JSON.toJSONString(param)));
                return null;
            }
        }, new CallBackJob<InvokeMsg>() {

            @Override
            public String callback(InvokeMsg param) {
            	// 回调
                System.out.println(JSON.toJSONString(System.currentTimeMillis() + "任务执行完后回调：" + JSON.toJSONString(param)));
                return null;
            }
        });

        return "添加成功";
    }

    @RequestMapping("/taskList")
    public String getAllTaskQueue() {
        return JSON.toJSONString(InvokeManager.fetchJobNames(InvokeType.TASK));
    }

    // 客户端必要配置的。对于这些配置可以使用springboot的起步依赖进行，比如可以参考redis的：RedisAutoConfiguration

    /**
     * 配置。通过接收mq的方式通知执行job
     *
     * @return
     */
    @Bean("taskExecutorConfig")
    @ConfigurationProperties(prefix = "delay.task.rocket.consume")
    public RocketMqConfig taskExecutorConfig() {

        return new RocketMqConfig();
    }

    /**
     * 调度通信实现（rocketmq的方式）。简单来说就是任务mq消费者
     * @param config
     * @return
     */
    @Bean("taskExecutor")
    public RocketMqInvokeDispatcher rocketMqInvokeDispatcher(@Qualifier("taskExecutorConfig") RocketMqConfig config) {
        return new RocketMqInvokeDispatcher(config);
    }

    /**
     * 配置。任务提交是通过mq方式通知的
     * @return
     */
    @Bean("taskSubmitConfig")
    @ConfigurationProperties("delay.task.rocket.producer")
    public RocketMqConfig taskSubmitConfig() {

        return new RocketMqConfig();
    }

    /**
     * 供客户端直接注入使用的任务提交工具。简单来说就是任务mq生产者
     *
     * @param config
     * @return
     */
    @Bean("taskSubmiter")
    public DelayClient taskSubmit(@Qualifier("taskSubmitConfig") RocketMqConfig config) {
        return new DelayClient(config);
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
