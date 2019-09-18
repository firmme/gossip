package com.itheima.gossip.controller;

import com.itheima.gossip.pojo.PageBean;
import com.itheima.gossip.pojo.ResultBean;
import com.itheima.gossip.service.IndexSearchProtalService;
import com.itheima.gossip.service.IndexWriterProtalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexSearcherProtalController {

    @Autowired
    private IndexSearchProtalService indexSearchProtalService;

    @ResponseBody
    @RequestMapping("/s")
    public ResultBean findByPage(ResultBean resultBean) {
        try {
            //1. 验证前端参数是否传递正确
            if (resultBean == null) {
                //返回首页
                return new ResultBean("参数传递非法....请重新传递", false);
            }
            String keywords = resultBean.getKeywords();

            if (keywords == null || "".equals(keywords)) {
                //返回首页
                return new ResultBean("参数传递非法....请重新传递", false);
            }

            PageBean pageBean = resultBean.getPageBean();
            if (pageBean == null) {
                pageBean = new PageBean();
                resultBean.setPageBean(pageBean);
            }

            //2. 调用service查询数据:
            resultBean = indexSearchProtalService.findByPage(resultBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultBean;
    }


}
