package beotkkotthon.Newsletter_BE.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class  SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addServersItem(new Server().url("/"));
    }

    private Info apiInfo() {
        return new Info()
                .title("가정통신문 API")
                .description("가정통신문 API 명세서")
                .version("1.0.0");
    }
}