package com.itheima.gossip.controller;

import com.itheima.gossip.service.TopKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class TopKeyController {
    @Autowired
    private TopKeyService topKeyService;
    @RequestMapping("/top")
    @ResponseBody
    public List<Map<String,Object>> findByTopKey(Integer num){
        if(num==null){
            num=5;
        }
        //调用service层,查询数据
        List<Map<String, Object>> mapList = topKeyService.findByTopKey(num);
        return mapList;
    }
}
