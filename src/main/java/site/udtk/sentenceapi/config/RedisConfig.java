package site.udtk.sentenceapi.config;

import static site.udtk.sentenceapi.config.constant.RateLimitConstant.*;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import site.udtk.sentenceapi.dto.SentenceDto;

@Configuration
public class RedisConfig {
	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.port}")
	private int port;

	@Value("${spring.data.redis.password}")
	private String password;

	private LettuceConnectionFactory createLettuceConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPassword(password);
		redisStandaloneConfiguration.setPort(port);

		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
		lettuceConnectionFactory.afterPropertiesSet();

		return lettuceConnectionFactory;
	}

	@Bean
	public RedisTemplate<Long, Object> redisTemplate() {
		RedisTemplate<Long, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(createLettuceConnectionFactory());
		redisTemplate.setKeySerializer(new GenericToStringSerializer<>(Long.class));
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(SentenceDto.class));
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	@Bean
	public ProxyManager<String> lettuceBasedProxyManager() {
		RedisClient redisClient = redisClient();
		StatefulRedisConnection<String, byte[]> redisConnection = redisClient
			.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

		return LettuceBasedProxyManager.builderFor(redisConnection)
			.withExpirationStrategy(
				ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(
					Duration.ofHours(EXPIRATION_OF_HOUR.getValue())))
			.build();
	}

	private RedisClient redisClient() {
		return RedisClient.create(RedisURI.builder()
			.withHost(host)
			.withPort(port)
			.withPassword(password)
			.build());
	}
}
