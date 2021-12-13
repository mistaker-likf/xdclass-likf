package net.xdclass.xdclassredis.controller;


import com.google.code.kaptcha.Producer;
import net.xdclass.xdclassredis.util.CommonUtil;
import net.xdclass.xdclassredis.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * 小滴课堂,愿景：让技术不再难学
 *
 * @Description
 * @Author 二当家小D
 * @Remark 有问题直接联系我，源码-笔记-技术交流群
 * @Version 1.0
 **/

@RestController
@RequestMapping("api/v1/captcha")
public class CaptchaController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private Producer captchaProducer;


    /**
     * 生成验证码 存入redis缓存 (运营商同时发送短信)
     * @param request
     * @param response
     */
    @GetMapping("get_captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response){
        String captchaText = captchaProducer.createText();
        String key = getCaptchaKey(request);
        redisTemplate.opsForValue().set(key, captchaText, 10, TimeUnit.MINUTES);
        BufferedImage bufferedImage = captchaProducer.createImage(captchaText);
        ServletOutputStream outputStream = null;

        try{
            outputStream = response.getOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 发送验证码
     * @param to
     * @param captcha
     * @param request
     * @return
     */
    @GetMapping("send_code")
    public JsonData sendCode(@RequestParam(value = "to", required = true) String to,
                             @RequestParam(value = "captcha", required = true) String captcha,
                             HttpServletRequest request){
        String key = getCaptchaKey(request);
        String cacheCaptcha = redisTemplate.opsForValue().get(key);

        if(captcha!=null && cacheCaptcha!=null && cacheCaptcha.equalsIgnoreCase(captcha)){

            redisTemplate.delete(key);//为什么要delete 可以不删除 因为有过期时间 而且会匹配手机号和ip及浏览器信息。
            return JsonData.buildSuccess();
        }else{
            return JsonData.buildError("验证码错误");
        }

    }

    private String getCaptchaKey(HttpServletRequest request){
        String ip = CommonUtil.getIpAddr(request);
        String userAgent = request.getHeader("user-Agent");
        return "user-service:captcha" + CommonUtil.MD5(ip+userAgent);
    }

}
