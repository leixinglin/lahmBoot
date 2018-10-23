package com.lhh.lahm.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

import java.time.Duration;

import static java.util.Collections.singletonMap;

@Configuration
// 必须加，使配置生效
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        //  设置自动key的生成规则，配置spring boot的注解，进行方法级别的缓存
        // 使用：进行分割，可以很多显示出层级关系
        // 这里其实就是new了一个KeyGenerator对象，只是这是lambda表达式的写法，我感觉很好用，大家感兴趣可以去了解下
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(":");
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(":" + String.valueOf(obj));
            }
            String rsToUse = String.valueOf(sb);
            return rsToUse;
        };
    }
    @Bean
    @Override
    public CacheManager cacheManager() {
        // 初始化缓存管理器，在这里我们可以缓存的整体过期时间什么的，我这里默认没有配置
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(jedisConnectionFactory);
        return builder.build();
    }


  /*  @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

         默认配置， 默认超时时间为30s
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration
                .ofSeconds(30L)).disableCachingNullValues();

         配置test的超时时间为120s
        RedisCacheManager cacheManager = RedisCacheManager.builder(RedisCacheWriter.lockingRedisCacheWriter
                (connectionFactory)).cacheDefaults(defaultCacheConfig).withInitialCacheConfigurations(singletonMap
                ("findCache", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(45L))
                        .disableCachingNullValues())).transactionAware().build();

        return cacheManager;
    }*/

    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory ) {
        //设置序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer); // key序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer); // value序列化
        redisTemplate.setHashKeySerializer(stringSerializer); // Hash key序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer); // Hash value序列化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Override
    @Bean
    public CacheErrorHandler errorHandler() {
        // 异常处理，当Redis发生异常时，打印日志，但是程序正常走
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key)    {
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
            }
        };
        return cacheErrorHandler;
    }

    /**
     * 此内部类就是把yml的配置数据，进行读取，创建JedisConnectionFactory和JedisPool，以供外部类初始化缓存管理器使用
     * 不了解的同学可以去看@ConfigurationProperties和@Value的作用
     *
     */
    @ConfigurationProperties
    class DataJedisProperties{
        @Value("${spring.redis.host}")
        private  String host;
        @Value("${spring.redis.database}")
        private  int database;
        @Value("${spring.redis.password}")
        private  String password;
        @Value("${spring.redis.port}")
        private  int port;
        @Value("${spring.redis.timeout}")
        private  int timeout;
        @Value("${spring.redis.jedis.pool.max-idle}")
        private int maxIdle;
        @Value("${spring.redis.jedis.pool.max-wait}")
        private long maxWaitMillis;

        @Value("${spring.redis.jedis.pool.max-active}")
        private int maxActive;

       /*老版本
        @Bean
       public JedisConnectionFactory jedisConnectionFactory() {
            JedisConnectionFactory factory = new JedisConnectionFactory();
            factory.setHostName(host);
            factory.setPort(port);
            factory.setTimeout(timeout);
            factory.setPassword(password);
            System.out.println("注入");
            System.out.println(factory);
            return factory;
        }*/
        /**
         * jedis连接工厂
         * @param jedisPoolConfig
         * @return
         */
        @Bean
        public JedisConnectionFactory JedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            //设置redis服务器的host或者ip地址
            redisStandaloneConfiguration.setHostName(host);
            redisStandaloneConfiguration.setPort(port);
            redisStandaloneConfiguration.setDatabase(database);
            redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
            //获得默认的连接池构造
            //这里需要注意的是，edisConnectionFactoryJ对于Standalone模式的没有（RedisStandaloneConfiguration，JedisPoolConfig）的构造函数，对此
            //我们用JedisClientConfiguration接口的builder方法实例化一个构造器，还得类型转换
            JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcf = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
            //修改我们的连接池配置
            jpcf.poolConfig(jedisPoolConfig);
            //通过构造器来构造jedis客户端配置
            JedisClientConfiguration jedisClientConfiguration = jpcf.build();

            return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
        }

        /**
         * 连接池配置信息
         */

        @Bean
        public JedisPoolConfig jedisPoolConfig(){
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            //最大连接数
            jedisPoolConfig.setMaxTotal(maxActive);
            //最小空闲连接数
            jedisPoolConfig.setMinIdle(maxIdle);
            //当池内没有可用连接时，最大等待时间
            jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
            //其他属性可以自行添加

            return jedisPoolConfig;
        }


        @Bean
        public JedisPool redisPoolFactory(JedisPoolConfig jedisPoolConfig) {
           /* JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxIdle(maxIdle);
            jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);*/

            JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
            return jedisPool;
        }
    }

}




