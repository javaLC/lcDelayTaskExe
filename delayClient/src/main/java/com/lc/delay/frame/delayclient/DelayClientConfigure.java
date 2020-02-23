package com.lc.delay.frame.delayclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lc.delay.frame.common.rocketmq.RocketMqConfig;

/**
 * @author liuchong
 * @version DelayClientConfigure.java, v 0.1 2020年02月23日 13:19
 */
@Configuration
public class DelayClientConfigure {

    // 客户端必要配置的。对于这些配置可以使用springboot的起步依赖进行，比如可以参考redis的：RedisAutoConfiguration

    /**
     * 配置。通过接收mq的方式通知执行job
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "delay.task.rocket.consume")
    public RocketMqConfig taskExecutorConfig() {

        return new RocketMqConfig();
    }

    /**
     * 调度通信实现（rocketmq的方式）。简单来说就是任务mq消费者
     * @param config
     * @return
     */
    /*@Bean
    @Lazy(false)
    public RocketMqInvokeDispatcher taskExecutor(@Qualifier("taskExecutorConfig") RocketMqConfig config) {
        return new RocketMqInvokeDispatcher(config);
    }
*/
    /**
     * 配置。任务提交是通过mq方式通知的
     * @return
     */
    @Bean
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
    @Bean
    public DelayClient taskSubmiter(@Qualifier("taskSubmitConfig") RocketMqConfig config) {
        return new DelayClient(config);
    }
}
