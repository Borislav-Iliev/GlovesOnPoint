package bg.softuni.kickboxing.config;

import bg.softuni.kickboxing.interceptor.LoggInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final LoggInterceptor loggInterceptor;

    public WebConfiguration(LoggInterceptor loggInterceptor) {
        this.loggInterceptor = loggInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.loggInterceptor)
                .excludePathPatterns("/css/bootstrap.min.css",
                        "/images/home-page-background.jpg",
                        "/css/style.css");
    }
}
