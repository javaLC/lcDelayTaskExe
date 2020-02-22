package com.lc.delay.frame.delayclient.invoke;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

import com.lc.delay.frame.common.InvokeType;
import com.lc.delay.frame.delayclient.job.InvokeJob;

/**
 * invoke管理器
 *
 * @author liuchong
 * @version InvokeManager.java, v 0.1 2020年02月21日 21:55
 */
public class InvokeManager {
    /** job注册（如果外部一直增加，内存可能出现不足） */
    private static Map<InvokeType, Map<String, InvokeJob>> jobMap = new HashMap<>();

    static {
        for (InvokeType type : InvokeType.values()) {
            jobMap.put(type, new ConcurrentHashMap<>());
        }
    }

    /**
     * 注册通用job
     * @param jobName
     * @param job
     */
    public static void registerInvokeJob(InvokeJob job, String jobName) {
        if (job == null || StringUtils.isEmpty(jobName)) {
            throw new IllegalArgumentException("非法job，jobName=" + jobName + ", job=" + job);
        }

        if(!isExist(job.getJobType(), jobName)) {
            Map<String, InvokeJob> tp = jobMap.get(job.getJobType());
            // 保证对应任务参数只会被第一次定义的任务执行
            synchronized (tp) {
                if(tp.get(jobName) == null) {
                    tp.put(jobName, job);
                }
            }
        }

    }

    /**
     * 获取job
     * @param jobType
     * @param jobName
     * @return
     */
    public static InvokeJob fetchInvokeJob(InvokeType jobType, String jobName) {
        if(jobType != null) {
            return jobMap.get(jobType).get(jobName);
        }

        return null;
    }

    /**
     * 获取所有任务
     *
     * @return
     */
    public static Set<String> fetchJobNames(InvokeType jobType) {
        if(jobType != null) {
            return new HashSet<>(jobMap.get(jobType).keySet());
        }

        return new HashSet<>();
    }

    /**
     * 是否存在某个任务
     *
     * @param jobType
     * @param jobName
     * @return
     */
    public static boolean isExist(InvokeType jobType, String jobName) {
        if(jobType == null || jobName == null) {
            return false;
        }

        return jobMap.get(jobType).get(jobName) != null;
    }

}
