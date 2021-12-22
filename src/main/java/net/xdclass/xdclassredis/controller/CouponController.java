package net.xdclass.xdclassredis.controller;

import net.xdclass.xdclassredis.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/coupon")
public class CouponController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("add")
    public JsonData saveCoupon(@RequestParam(value = "coupon_id", required=true) int couponId) {

        //用于防止其它线程误删
        String uuid = UUID.randomUUID().toString();

        String lockKey = "lock:coupon:" + couponId;

        lock(uuid, lockKey);

        return JsonData.buildSuccess();
    }

    private void lock(String uuid, String lockKey) {

        String script = "if redis.call('get',KEYS[1]) == " +
                "ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

        Boolean nativaLock = redisTemplate.opsForValue().setIfAbsent(lockKey, uuid, Duration.ofSeconds(30));
        System.out.println(uuid + "加锁状态:" + nativaLock);

        if(nativaLock) {
            //加锁成功
            try{
                //TODO 相关业务逻辑
                TimeUnit.MILLISECONDS.sleep(20000);
            }catch(InterruptedException e){
            }finally{
                Long result = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList(lockKey), uuid);
                System.out.println("解锁状态:" + result);
            }
        }else{
            //自旋操作
            try{
                System.out.println("加锁失败 睡眠2秒 进行自旋");
                TimeUnit.MILLISECONDS.sleep(2000);
            }catch(InterruptedException e){
            }
            //睡眠一会在尝试获取锁
            lock(uuid, lockKey);
        }
    }
}
