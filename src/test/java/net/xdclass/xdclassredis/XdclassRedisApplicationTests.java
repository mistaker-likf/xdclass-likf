package net.xdclass.xdclassredis;

import net.xdclass.xdclassredis.model.UserDO;
import net.xdclass.xdclassredis.model.VideoDO;
import net.xdclass.xdclassredis.vo.UserPointVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class XdclassRedisApplicationTests {

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Test
	void testStringSet() {
		ValueOperations valueOperations = redisTemplate.opsForValue();
		valueOperations.set("name", "xdclass.net");
	}


	@Test
	void testStringGet() {
		ValueOperations valueOperations = redisTemplate.opsForValue();
		System.out.println((String) valueOperations.get("name"));
	}

	@Test
	public void testSeria(){

		UserDO userDO = new UserDO();
		userDO.setId(1);
		userDO.setName("xd");
		userDO.setPwd("224336");
		redisTemplate.opsForValue().set("user-service:user:2", userDO);

	}

	@Test
	public void testDLock(){

		boolean flag = stringRedisTemplate.opsForValue().setIfAbsent("coupon:1001","success",20, TimeUnit.SECONDS);
		                                                 //setIfAbsent 对应 SETNT命令 (原子性操作)
		System.out.println("加锁是否成功:" +flag);

	}

	@Test
	public void saveRank(){

		String DAILY_RANK_KEY = "video:rank:daily";

		VideoDO video1 = new VideoDO(1, "springcloud微服务视频", "xdclass.net", 43);
		VideoDO video2 = new VideoDO(2, "Pass工业级项目实战", "xdclass.net", 1099);
		VideoDO video3 = new VideoDO(3, "面试专题视频", "xdclass.net", 59);
		VideoDO video4 = new VideoDO(4, "spring源码实战", "xdclass.net", 49);
		VideoDO video5 = new VideoDO(5, "Nginx网关+LVS+KeepAlive实战", "xdclass.net", 89);

		redisTemplate.opsForList().leftPushAll(DAILY_RANK_KEY, video5, video4, video3, video2, video1);
	}

	@Test
	public void replaceRank(){
		String DAILY_RANK_KEY = "video:rank:daily";
		VideoDO video = new VideoDO(9,"小滴课堂面试专题第一季", "xdclass.net", 233);
		redisTemplate.opsForList().set(DAILY_RANK_KEY, 1, video);
	}

	/**
	 * 用户画像去重
	 */
	@Test
	public void userProfile(){

		BoundSetOperations operations = redisTemplate.boundSetOps("user:tags:1");
		operations.add("car", "student", "rich", "dog", "guangdong", "rich");

	}

	@Test
	public void testSocial(){

		BoundSetOperations operationsLW = redisTemplate.boundSetOps("user:lw");
		operationsLW.add("A", "B", "C", "D", "E");
		System.out.println("老王的粉丝："+ operationsLW.members());

		BoundSetOperations operationsXD = redisTemplate.boundSetOps("user:xd");
		operationsXD.add("A", "B", "F", "G", "H", "I", "J", "W");
		System.out.println("老王的粉丝："+ operationsXD.members());

		//差集
		Set lwSet = operationsLW.diff("user:xd");
		System.out.println("老王专属粉丝："+ lwSet);
		Set xdSet = operationsXD.diff("user:lw");
		System.out.println("小D专属粉丝："+ xdSet);

		//交集
		Set interSet = operationsLW.intersect("user:xd");
		System.out.println("两人的共同粉丝：" + interSet);

		//并集
		Set unionSet = operationsLW.union("user:xd");
		System.out.println("两人的所有好友：" + unionSet);

	}

	@Test
	void testData() {

		UserPointVO p1 = new UserPointVO("A", "324");
		UserPointVO p2 = new UserPointVO("B", "242");
		UserPointVO p3 = new UserPointVO("C", "542345");
		UserPointVO p4 = new UserPointVO("D", "235");
		UserPointVO p5 = new UserPointVO("E", "1245");
		UserPointVO p6 = new UserPointVO("F", "234543");
		UserPointVO p7 = new UserPointVO("G", "3236");

		BoundZSetOperations<String, UserPointVO> operations = redisTemplate.boundZSetOps("point:rank:real");

		operations.removeRange(0, -1);
		operations.add(p1, 324);
		operations.add(p2, 542);
		operations.add(p3, 52);
		operations.add(p4, 434);
		operations.add(p5, 23);
		operations.add(p6, 64);
		operations.add(p7, 765);

	}
}

