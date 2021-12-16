package net.xdclass.xdclassredis.controller;

import net.xdclass.xdclassredis.model.VideoCardDO;
import net.xdclass.xdclassredis.service.VideoCardService;
import net.xdclass.xdclassredis.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 小滴课堂,愿景：让技术不再难学
 *
 * @Description
 * @Author 二当家小D
 * @Remark 有问题直接联系我，源码-笔记-技术交流群
 * @Version 1.0
 **/

@RequestMapping("/api/v1/card")
@RestController
public class VideoCardController {

    @Autowired
    private VideoCardService videoCardService;

    /**
     * 有缓存
     * @return
     */
    @GetMapping("list_cache")
    public JsonData listCardCache(){
        return JsonData.buildSuccess(videoCardService.list(true));
    }


    /**
     * 无缓存
     * @return
     */
    @GetMapping("list_nocache")
    public JsonData listCardNoCache(){

        List<VideoCardDO> list = videoCardService.list(false);
        return JsonData.buildSuccess(list);

    }
}