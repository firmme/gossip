package com.itheima.gossip.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.gossip.pojo.News;
import com.itheima.gossip.pojo.ResultBean;
import com.itheima.gossip.service.IndexSearchProtalService;
import com.itheima.search.service.IndexSearcherService;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class IndexSearchProtalServiceImpl implements IndexSearchProtalService{
   @Reference(timeout = 50000)
   private IndexSearcherService indexSearcherService;
    @Override
    public ResultBean findByPage(ResultBean resultBean) throws Exception {
        System.out.println(indexSearcherService);
        resultBean = indexSearcherService.findByPage(resultBean);
        //时间统一
        List<News> newslist = resultBean.getPageBean().getNewsList();
        System.out.println(newslist);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(News news : newslist){
        String oldTime = news.getTime();
            Date date1 = sdf.parse(oldTime);
            long date2 = date1.getTime();
            date1.setTime(date2-(1000*3600*8));
            String newTime = sdf.format(date1);
        }
        return resultBean;
    }
}
