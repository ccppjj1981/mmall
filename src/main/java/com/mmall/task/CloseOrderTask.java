package com.mmall.task;

import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: macbook
 * Date: 19/4/15
 * Time: 下午11:42
 * Description: No Description
 */
@Component
@Slf4j
public class CloseOrderTask {
    @Autowired
    private IOrderService iOrderService;
    @Scheduled(cron="0 */1 * * * ?")
    public void closeOrderTaskV1(){
        log.info("closeOrderTaskV1 start");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.other.task.time.hour","1"));
        log.info("hour:{}",hour);
        //iOrderService.closeOrder(hour);
        log.info("closeOrderTaskV1 end");
    }

}