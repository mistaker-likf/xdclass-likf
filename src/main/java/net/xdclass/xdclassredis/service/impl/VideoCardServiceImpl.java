package net.xdclass.xdclassredis.service.impl;

import net.xdclass.xdclassredis.dao.VideoCardDao;
import net.xdclass.xdclassredis.model.VideoCardDO;
import net.xdclass.xdclassredis.service.VideoCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class VideoCardServiceImpl implements VideoCardService {

    @Autowired
    private VideoCardDao videoCardDao;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 缓存key
     */
    private static final String VIDEO_CARD_CACHE_KEY = "video:card:key";

    public List<VideoCardDO> list(boolean isCache){

        if(isCache){
            Object cacheObj = redisTemplate.opsForValue().get(VIDEO_CARD_CACHE_KEY);

            if(cacheObj != null){

                List<VideoCardDO> list= (List<VideoCardDO>) cacheObj;
                return list;

            } else {

                List<VideoCardDO> list = videoCardDao.list();
                redisTemplate.opsForValue().set(VIDEO_CARD_CACHE_KEY,list,10, TimeUnit.MINUTES);
                return list;

            }
        }else{
            return videoCardDao.list();
        }

    }
}
