package com.itheima.gossip.timing;

import com.itheima.gossip.service.IndexWriterProtalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

// 定时执行的类
//@Component
public class Timing {

    @Autowired
    private IndexWriterProtalService indexWriterProtalService;
    // 只要能够让timing这个方法定时的执行, 那么service的方法也就会定时的执行  SpringTask
    // cron 表达式:  规定什么时候执行任务   cronTab 中表达式  至少应该有6位, 最多可以有7位
    @Scheduled(cron = "0/30 * * * * ?")
    public void timing(){

        try {
       //     System.out.println(new Date().toLocaleString());

            indexWriterProtalService.saveBeans();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
