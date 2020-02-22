package com.lc.delay.frame.common;

/**
 * @author liuchong
 * @version InvokeType.java, v 0.1 2020年02月21日 19:22
 */
public enum InvokeType {

    /** 任务消息 */
    TASK(1, "任务消息"),
    /** 回调消息 */
    CALLBACK(2, "回调消息"),
    ;

    /** 类型 */
    private int type;
    /** 描述 */
    private String desc;

    private InvokeType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 获取对应调用类型
     *
     * @param type
     * @return
     */
    public static InvokeType getInvokeType(int type) {
        for(InvokeType t : InvokeType.values()) {
            if(t.type == type) {
                return t;
            }
        }

        return null;
    }
}
