package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.common.RedisShardedPool;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

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

    //@Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV1() {
        log.info("closeOrderTaskV1 start");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.other.task.time.hour", "1"));
        log.info("hour:{}", hour);
        //iOrderService.closeOrder(hour);
        log.info("closeOrderTaskV1 end");
    }

    public void closeOrderTaskV2() {
        log.info("closeOrderTaskV2 start");
        Long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,
                String.valueOf(System.currentTimeMillis()+lockTimeout));
        if(setnxResult !=null && setnxResult.intValue() == 1){

        }else {
            log.info("没有获得分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("closeOrderTaskV2 start");
    }
    private void closeOrder(String lockName){
        RedisShardedPoolUtil.setExpire(lockName,50);//防止死锁
        log.info("获取{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,
                Thread.currentThread());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.other.task.time.hour", "1"));
        log.info("hour:{}", hour);
        iOrderService.closeOrder(hour);
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,
                Thread.currentThread());
    }
}