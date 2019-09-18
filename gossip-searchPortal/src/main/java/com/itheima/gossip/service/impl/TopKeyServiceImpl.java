package com.itheima.gossip.service.impl;

import com.itheima.gossip.service.TopKeyService;
import com.itheima.gossip.utils.JedisUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.*;
@Service
public class TopKeyServiceImpl implements TopKeyService {
    @Override
    public List<Map<String, Object>> findByTopKey(Integer num) {
        Jedis jedis = JedisUtils.getJedis();
        //执行查询,从大到小的前num个数据
        Set<Tuple> tuples = jedis.zrevrangeWithScores("bigData:gossip;topkey", 0, (num - 1));
        //封装数据
        List<Map<String,Object>>mapList=new ArrayList<>();
        for(Tuple tuple:tuples){
            String topKey = tuple.getElement();
            double score = tuple.getScore();
            Map<String,Object> map = new HashMap<>();
            map.put("topKey",topKey);
            map.put("score",score);
            mapList.add(map);
        }
        jedis.close();
        return mapList;
    }
}
