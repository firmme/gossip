package com.itheima.gossip.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.gossip.mapper.NewsMapper;
import com.itheima.gossip.pojo.News;
import com.itheima.gossip.service.IndexWriterProtalService;
import com.itheima.gossip.utils.JedisUtils;
import com.itheima.search.service.IndexWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Service
public class IndexWriterProtalServiceImpl  implements IndexWriterProtalService{
    @Autowired
    private NewsMapper newsMapper;
    @Reference(timeout = 5000)
    private IndexWriterService indexWriterService;
    // 索引写入的方法
    @Override
    public void saveBeans() throws Exception {
        // 1) 从redis中获取上一次的最大id, 如果没有设置为 0
        Jedis jedis = JedisUtils.getJedis();
        String lastMaxId = jedis.get("bigData:search:lastMaxId");
        jedis.close();
        if(lastMaxId == null || "".equals(lastMaxId)){
            lastMaxId = "0";
        }
        // 6 循环获取
        while (true) {
            //2) 调用mapper 查询数据 , 如果返回值长度为0 , 认为没有数据了, 当前id为最大id值
            List<News> newsList = newsMapper.findByLastMaxId(lastMaxId);
            if (newsList == null || newsList.size() == 0) {
                System.out.println("当前没有任何的数据: "+ lastMaxId);
                // 跳出循环
                break;
            }
          //  System.out.println(newsList.size());
            //3. 其他的业务操作 : 进行日期转换, 保证数据到达solr服务之前数据的格式都是正确的
            // 2019-05-13 09:43:25
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 2019-05-13'T'09:43:25'Z'
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            for (News news : newsList) {
                String oldTime = news.getTime();

                Date oldDate = format1.parse(oldTime);
                String newTime = format2.format(oldDate);

                news.setTime(newTime);
            }
            //4. 调用服务,写入索引
            indexWriterService.saveBeans(newsList);

            //5. 获取本次数据中的最大id值
            lastMaxId = newsMapper.findToNextMaxId(lastMaxId);

        }

        // 将最大id存储到redis中
        jedis = JedisUtils.getJedis();
        jedis.set("bigData:search:lastMaxId",lastMaxId);
        jedis.close();

    }
}
