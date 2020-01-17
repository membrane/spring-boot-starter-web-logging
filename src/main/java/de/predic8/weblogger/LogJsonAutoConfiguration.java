package de.predic8.weblogger;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LogJsonAutoConfiguration {

	private final MeterRegistry registry;

	public LogJsonAutoConfiguration(MeterRegistry registry) {
		this.registry = registry;
	}

	@Bean
	public LogJsonHandlerInterceptor logJsonHandlerInterceptor() {
		return new LogJsonHandlerInterceptor( registry);
	}

	@Configuration
	static class WebConfig implements WebMvcConfigurer {

		private final LogJsonHandlerInterceptor logJsonHandlerInterceptor;

		WebConfig(LogJsonHandlerInterceptor logJsonHandlerInterceptor) {
			this.logJsonHandlerInterceptor = logJsonHandlerInterceptor;
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(logJsonHandlerInterceptor);
		}
	}
}