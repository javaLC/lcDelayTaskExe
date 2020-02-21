package com.lc.delay.frame.common.rocketmq;

/**
 *
 * @author liuchong
 * @version RocketMqConfig.java, v 0.1 2020年02月21日 23:25
 */
public class RocketMqConfig {

    private String topic;
    private String host;
    private String group;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
