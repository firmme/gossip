package com.itheima.search.service;

import com.itheima.gossip.pojo.News;

import java.util.List;

public interface IndexWriterService {

    public void saveBeans(List<News> newsList) throws  Exception;
}
