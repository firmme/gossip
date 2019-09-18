package com.itheima.gossip.mapper;

import com.itheima.gossip.pojo.News;

import java.util.List;

public interface NewsMapper {

    //1. 根据上一次的最大id, 获取分页数据
    //  select * from news where id >lastMaxId limit 0,10
    public List<News>  findByLastMaxId(String lastMaxId);

    //2. 根据上一次的最大id, 获取本次最大id
    //	select max(id) from (select * from news where id >lastMaxId limit 0,10) temp;
    public String findToNextMaxId(String lastMaxId);

}
