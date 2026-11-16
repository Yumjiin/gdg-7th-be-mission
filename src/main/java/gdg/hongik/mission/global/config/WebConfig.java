package gdg.hongik.mission.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS 전역 설정.
 *
 * <p>3주차까지는 컨트롤러마다 {@code @CrossOrigin}을 붙였다. 컨트롤러가 늘어날 때마다 같은 코드를 반복해야 하고,
 * 없는 주소(404)처럼 컨트롤러를 거치지 않는 응답에는 적용되지 않는다. 설정 한 곳에서 모든 경로에 적용한다. (DRY)
 *
 * <p>실습용이라 모든 출처를 허용한다. 실제 서비스에서는 {@code allowedOrigins}에 프론트엔드 주소만 넣는다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS");
    }
}
