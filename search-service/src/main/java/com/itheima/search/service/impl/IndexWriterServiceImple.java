package com.itheima.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.gossip.pojo.News;
import com.itheima.search.service.IndexWriterService;
import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class IndexWriterServiceImple implements  IndexWriterService {
    @Autowired // 从容器中获取
    // 如果在注入的时候, 不知道该加那个注解了, 问自己一个问题: 这个对象的实例是一个服务呢, 还是spring容器中对象
    private SolrServer solrServer;

    @Override
    public void saveBeans(List<News> newsList)  throws  Exception{
        solrServer.addBeans(newsList);  // 不要使用addBean这个方法了.....
        solrServer.commit();
    }
}
