package net.xdclass.xdclassredis;

import net.xdclass.xdclassredis.model.UserDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class XdclassRedisApplicationTests {

	@Autowired
	private RedisTemplate redisTemplate;

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
}
