package de.predic8.weblogger;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static net.logstash.logback.marker.Markers.appendEntries;

public class LogJsonHandlerInterceptor extends HandlerInterceptorAdapter {

	private final Logger log = LoggerFactory.getLogger(LogJsonHandlerInterceptor.class);

	private Map<String,RequestContext> context = new HashMap<>();
	private final MeterRegistry registry;

	LogJsonHandlerInterceptor(MeterRegistry registry) {
		this.registry = registry;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {

		RequestContext ctx = new RequestContext();

		Metrics.counter("request.counter", "uri", request.getServletPath(), "method", request.getMethod()).increment();

		ctx.timer = Timer.start(registry);

		context.put(Thread.currentThread().getName(), ctx);

		Map<String, Object> entries = new HashMap<>();
		entries.put("method", request.getMethod());
		entries.put("path", request.getServletPath());

		System.out.println("entries = " + entries);
		log.info("{}", entries);

		log.info(appendEntries(entries), "{}");

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) {

		RequestContext ctx = context.get(Thread.currentThread().getName());

		ctx.timer.stop(registry.timer("latency", "statuscode", ""+response.getStatus()));

		Map<String, Object> entries = new HashMap<>();
		entries.put("status_code", response.getStatus());

		System.out.println("entriesPost = " + entries);
		log.info("{}", entries);


		log.info(appendEntries(entries), "{}");
	}

	class RequestContext {
		Timer.Sample timer;
	}
}