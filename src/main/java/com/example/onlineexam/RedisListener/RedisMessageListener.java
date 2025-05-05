//
//package com.example.onlineexam.RedisListener;
//
//import com.example.onlineexam.RedisMessageReceive.RedisReceiver;
//import com.example.onlineexam.exception.RedisCode;
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.listener.PatternTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//public class RedisMessageListener {
//
//
//    /**
//     * 关注消息订阅处理器,并指定处理方法
//     *
//     * @param receiver
//     * @return
//     */
//    @Bean
//    MessageListenerAdapter sendallListenerAdapter(RedisReceiver receiver) {
//        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "sendall");
//        return messageListenerAdapter;
//    }
//    @Bean
//    MessageListenerAdapter sendListenerAdapter(RedisReceiver receiver) {
//        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "send");
//        return messageListenerAdapter;
//    }
//    /**
//     * redis消息监听器容器
//     *
//     *            发送单人订阅处理器
//     * @param sendallListenerAdapter
//     *            群发订阅处理器
//     * @param sendListenerAdapter
//     * @return
//     */
//    @Bean
//    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
//                                            MessageListenerAdapter sendallListenerAdapter,
//                                            MessageListenerAdapter sendListenerAdapter
//                                            ) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        //发送所有消息
//        container.addMessageListener(sendallListenerAdapter, new PatternTopic(RedisCode.TOPIC_SENDALL));
//        //发送单人消息
//        container.addMessageListener(sendListenerAdapter, new PatternTopic(RedisCode.TOPIC_SEND));
//
//        return container;
//    }
//    /**redis 读取内容的template */
//    @Bean
//    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
//        return new StringRedisTemplate(connectionFactory);
//    }
//    @Bean
//    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        serializer.serialize(om);
//        //jackson2JsonRedisSerializer.setObjectMapper(om);
//        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//
//        template.setDefaultSerializer(serializer);
//
//        template.setConnectionFactory(connectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//        template.afterPropertiesSet();
//        return template;
//
//    }
//
//
//}
