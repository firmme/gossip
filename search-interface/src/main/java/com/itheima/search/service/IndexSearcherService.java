package com.itheima.search.service;

import com.itheima.gossip.pojo.ResultBean;

public interface IndexSearcherService {
    public ResultBean findByPage(ResultBean resultBean) throws Exception;

}
