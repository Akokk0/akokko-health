package com.akokko;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JedisTest {
    @Test
    public void test1() {
        Jedis jedis = new Jedis("106.14.73.110", 6379);
        try {
            jedis.ping();
        } catch (
                JedisConnectionException e) {
            System.err.println("连接超时");
        }
    }
}
