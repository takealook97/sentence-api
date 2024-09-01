package site.udtk.sentenceapi.config;

import static java.time.Duration.*;
import static site.udtk.sentenceapi.config.constant.RateLimitConstant.*;
import static site.udtk.sentenceapi.exception.ErrorCode.*;
import static site.udtk.sentenceapi.util.ClientIpUtil.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.udtk.sentenceapi.config.properties.RateLimitingProperties;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitFilter implements Filter {
	private final ProxyManager<String> proxyManager;
	private final RateLimitingProperties rateLimitingProperties;
	private static final Set<String> ALLOWED_IPS = new HashSet<>();

	@PostConstruct
	public void init() {
		ALLOWED_IPS.addAll(rateLimitingProperties.getAllowedIps());
		log.info("Allowed IPs: {}", ALLOWED_IPS);
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
		throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
		String clientIp = getClientIp(httpRequest);
		if (ALLOWED_IPS.contains(clientIp)) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}
		Supplier<BucketConfiguration> bucketConfigurationSupplier = getConfigSupplier();
		Bucket bucket = proxyManager.builder().build(clientIp, bucketConfigurationSupplier);
		ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(REQUEST_COST_IN_TOKENS.getValue());

		if (probe.isConsumed()) {
			// log.info("success!\trequest ip : {}\turi : {}\tremainingToken : {}",
			// 	clientIp, httpRequest.getRequestURI(), probe.getRemainingTokens());
			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			log.info("too many request!\trequest ip : {}\turi : {}\tremainingToken : {}",
				clientIp, httpRequest.getRequestURI(), probe.getRemainingTokens());
			HttpServletResponse httpServletResponse = makeRateLimitResponse(servletResponse, probe);
		}
	}

	public Supplier<BucketConfiguration> getConfigSupplier() {
		return () -> BucketConfiguration.builder()
			.addLimit(limit -> limit.capacity(BUCKET_CAPACITY.getValue())
				.refillIntervally(REFILL_AMOUNT.getValue(), ofMinutes(REFILL_DURATION_MINUTE.getValue())))
			.build();
	}

	private HttpServletResponse makeRateLimitResponse(ServletResponse servletResponse, ConsumptionProbe probe) throws
		IOException {

		HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;
		httpResponse.setContentType("text/plain");
		httpResponse.setHeader("X-Rate-Limit-Retry-After-Seconds",
			"" + TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill()));
		httpResponse.setStatus(SC_TOO_MANY_REQUESTS.getHttpStatus().value());
		httpResponse.getWriter().append(SC_TOO_MANY_REQUESTS.getErrorMsg());

		return httpResponse;
	}
}
