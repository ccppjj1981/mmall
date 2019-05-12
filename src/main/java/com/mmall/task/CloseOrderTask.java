package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.common.RedisShardedPool;
import com.mmall.common.RedissonManager;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private RedissonManager redissonManager;
    @PreDestroy
    public void delLock(){
         RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }

    //@Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV1() {
        log.info("closeOrderTaskV1 start");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.other.task.time.hour", "1"));
        log.info("hour:{}", hour);
        //iOrderService.closeOrder(hour);
        log.info("closeOrderTaskV1 end");
    }
    //@Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV2() {
        log.info("closeOrderTaskV2 start");
        Long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,
                String.valueOf(System.currentTimeMillis()+lockTimeout));
        if(setnxResult !=null && setnxResult.intValue() == 1){
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else {
            log.info("没有获得分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("closeOrderTaskV2 start");
    }
    //@Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV3() {
        log.info("closeOrderTaskV3 start");
        Long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,
                String.valueOf(System.currentTimeMillis()+lockTimeout));
        if(setnxResult !=null && setnxResult.intValue() == 1){
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else {
            //未获取到锁，继续判断时间戳，是否可以重置并获取到锁
            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            if(lockValueStr !=null && System.currentTimeMillis() > Long.parseLong(lockValueStr)){
                String getSetResult = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,
                        String.valueOf(System.currentTimeMillis()+lockTimeout));
                //当key没有旧值，及key不存在，返回nil,-->获取锁
                if(getSetResult != null || (getSetResult != null && StringUtils.equals(getSetResult,lockValueStr))){
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }else{
                    log.info("没有获得分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            }

        }
        log.info("closeOrderTaskV3 end");
    }
    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV4() {
        RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        boolean getLock = false;
        try {
            if(getLock = lock.tryLock(2,5, TimeUnit.SECONDS)){
                log.info("Redisson获取分布式锁:{},ThreadName:{}",
                        Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
                int hour = Integer.parseInt(PropertiesUtil.getProperty("close.other.task.time.hour", "1"));
                //iOrderService.closeOrder(hour);

            }else{
                log.info("Redisson没有获取分布式锁:{},ThreadName:{}",
                        Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
           log.error("Redission分布式锁获取异常",e);
        }finally {
            if(!getLock){
                return;
            }
            lock.unlock();
            log.info("Redission分布式锁释放");
        }
    }
    private void closeOrder(String lockName){
        RedisShardedPoolUtil.setExpire(lockName,50);//防止死锁
        log.info("获取{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,
                Thread.currentThread());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.other.task.time.hour", "1"));
        log.info("hour:{}", hour);
        //iOrderService.closeOrder(hour);
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,
                Thread.currentThread());
    }
}