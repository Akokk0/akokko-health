package com.akokko.jobs;

import com.akokko.constant.RedisConstant;
import com.akokko.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 清理图片方法
     */
    public void clearImg() {
        Set<String> clearImgs = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        if (clearImgs != null) {
            for (String clearImg : clearImgs) {
                QiniuUtils.deleteFileFromQiniu(clearImg);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, clearImg);
                System.out.println("已删除" + clearImg);
            }
        }
    }
}
