package net.xdclass.xdclassredis.controller;

import net.xdclass.xdclassredis.model.VideoDO;
import net.xdclass.xdclassredis.util.JsonData;
import net.xdclass.xdclassredis.vo.UserPointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 小滴课堂,愿景：让技术不再难学
 *
 * @Description
 * @Author 二当家小D
 * @Remark 有问题直接联系我，源码-笔记-技术交流群
 * @Version 1.0
 **/

@RestController
@RequestMapping("api/v1/rank")
public class RankController {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String DAILY_RANK_KEY = "video:rank:daily";

    @RequestMapping("daily_rank")
    public JsonData videoDailyRank(){ //保存Rank的功能在测试启动类中实现

        List<VideoDO> list = redisTemplate.opsForList().range(DAILY_RANK_KEY, 0, -1);

        return JsonData.buildSuccess(list);
    }

    /**
     * 返回全部 从大到小
     * @return
     */
    @RequestMapping("real_rank1")
    public JsonData realRank1(){

        BoundZSetOperations<String, UserPointVO> operations = redisTemplate.boundZSetOps("point:rank:real");
        Set<UserPointVO> set = operations.reverseRange(0, -1);
        return JsonData.buildSuccess(set);

    }

    /**
     * 返回全部 从小到大
     * @return
     */
    @RequestMapping("real_rank2")
    public JsonData realRank2(){

        BoundZSetOperations<String, UserPointVO> operations = redisTemplate.boundZSetOps("point:rank:real");
        Set<UserPointVO> set = operations.range(0, -1);
        return JsonData.buildSuccess(set);

    }

    /**
     * 返回前三 从大到小
     * @return
     */
    @RequestMapping("real_rank3")
    public JsonData realRank3(){

        BoundZSetOperations<String, UserPointVO> operations = redisTemplate.boundZSetOps("point:rank:real");
        Set<UserPointVO> set = operations.reverseRange(0, 2); //注意这里返回元素 start和end都包含 0, 1, 2
        return JsonData.buildSuccess(set);

    }

    /**
     * 查看排名
     * @return
     */
    @RequestMapping("find_myrank")
    public JsonData realMyRank(String name, String phone){

        BoundZSetOperations<String, UserPointVO> operations = redisTemplate.boundZSetOps("point:rank:real");
        UserPointVO userPointVO = new UserPointVO(name, phone);
        long rank = operations.reverseRank(userPointVO);
        return JsonData.buildSuccess(++rank);

    }

    /**
     * 加积分
     * @return
     */
    @RequestMapping("uprank")
    public JsonData updateMyRank(String name, String phone, int point){

        BoundZSetOperations<String, UserPointVO> operations = redisTemplate.boundZSetOps("point:rank:real");
        UserPointVO userPointVO = new UserPointVO(name, phone);
        operations.incrementScore(userPointVO, point);
        long rank = operations.reverseRank(userPointVO);
        return JsonData.buildSuccess(++rank);

    }

    /**
     * 查看某个人的积分
     * @return
     */
    @RequestMapping("mypoint")
    public JsonData realMyPoint(String name, String phone){

        BoundZSetOperations<String, UserPointVO> operations = redisTemplate.boundZSetOps("point:rank:real");
        UserPointVO userPointVO = new UserPointVO(name, phone);

        double point = operations.score(userPointVO);
        return JsonData.buildSuccess(point);

    }

}
