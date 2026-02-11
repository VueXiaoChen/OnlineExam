package com.example.onlineexam.config;

import com.example.onlineexam.service.CommentService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;

@Component
public class RedisCleaner {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(RedisCleaner.class);
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void clearRedisOnShutdown() {
        LOG.info("🔄 应用正在关闭，清空 Redis 数据...");
        try {
            // 清空当前数据库（默认 db0）
            redisTemplate.getConnectionFactory().getConnection().flushDb();
            // 如果需要清空所有数据库，使用 flushAll()
            // redisTemplate.getConnectionFactory().getConnection().flushAll();
            LOG.info("✅ Redis 数据已清空");
        } catch (Exception e) {
            LOG.info("❌ Redis 清空失败：" + e.getMessage());
        }
    }
}