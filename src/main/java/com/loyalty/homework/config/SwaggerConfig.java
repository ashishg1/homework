package com.loyalty.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This swagger config class is used by swagger ui to generate swagger docs automatically on the url /swagger-ui.html
 */
@SuppressWarnings("unused")
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * The beans to provide scanning information for the Swagger UI
     *
     * @return Docket
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.loyalty.homework.controllers"))
                .paths(PathSelectors.any())
                .build().apiInfo(apiEndPointsInfo());
    }

    /**
     * The API info that will be used by swagger ui to show the description
     *
     * @return ApiInfo
     */
    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Homework Rest API")
                .description("This Rest API provides a way to create new posts and reply to them")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0-SNAPSHOT")
                .build();
    }

}