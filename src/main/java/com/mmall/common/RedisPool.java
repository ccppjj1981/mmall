package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created with IntelliJ IDEA.
 * User: macbook
 * Date: 18/11/11
 * Time: 下午7:16
 * Description: No Description
 */
public class RedisPool {
    private  static JedisPool pool;
    private static  Integer maxTotal= Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20")); //最大连接数
    private static  Integer maxIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));
    private static  Integer minIdle= Integer.valueOf(PropertiesUtil.getProperty("redis.min.idle","10"));
    private static  Boolean testOnBorrow=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true"));//是否验证
    private static boolean testOnReturn=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));
    private static String redisIp=PropertiesUtil.getProperty("redis.ip");
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));
    private static void initPool(){
        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMaxTotal(maxTotal);
        conf.setMaxIdle(maxIdle);
        conf.setTestOnBorrow(testOnBorrow);
        conf.setTestOnReturn(testOnReturn);
        pool = new JedisPool(conf,redisIp,redisPort,1000*2);
    }
    static {
        initPool();
    }
    public static Jedis getJedis(){
        return pool.getResource();
    }
    public static void returnResource(Jedis jedis){
        if(jedis != null){
            pool.returnResource(jedis);
        }
    }
    public static void returnBrokenResource(Jedis jedis){
        if(jedis != null){
            pool.returnBrokenResource(jedis);
        }
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("chenpeijian2","chenpeijian");
        pool.returnResource(jedis);
        pool.destroy();

    }
}