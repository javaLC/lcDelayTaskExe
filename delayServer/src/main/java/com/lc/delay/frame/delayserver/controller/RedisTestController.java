package com.lc.delay.frame.delayserver.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import javax.annotation.Resource;

/**
 * @author liuchong
 * @version RedisTestController.java, v 0.1 2020年02月23日 09:52
 */
@RestController
public class RedisTestController {

    @Resource
    StringRedisTemplate template;

    @RequestMapping(value = "/redis/add", method = { RequestMethod.GET })
    public String add(String queue, String value, double score) {
        if (queue != null && queue.length() > 0 && value != null && value.length() > 0 && score != 0) {
            template.opsForZSet().add(queue, value, score);
            return "添加成功";
        }

        return "参数非法";
    }

    @RequestMapping(value = "/redis/query", method = { RequestMethod.GET })
    public String query(String queue, double min, double max) {
        Set<ZSetOperations.TypedTuple<String>> ss = template.opsForZSet().rangeByScoreWithScores(queue, min, max);
        Set<String> res = new HashSet<>();
        if (ss != null) {
            ss.forEach(s -> {
                res.add("{score:" + s.getScore() + ", value:" + s.getValue() + "}");
            });
        }

        return JSON.toJSONString(res);
    }

    @RequestMapping(value = "/redis/query2", method = { RequestMethod.GET })
    public String query2(String queue, double min, double max) {
        Set<String> res = template.opsForZSet().rangeByScore(queue, min, max);

        return JSON.toJSONString(res);
    }


    @RequestMapping(value = "/redis/del", method = { RequestMethod.GET })
    public String del(String queue, double min, double max) {

        template.opsForZSet().removeRangeByScore(queue, min, max);

        return "删除成功";
    }

}
