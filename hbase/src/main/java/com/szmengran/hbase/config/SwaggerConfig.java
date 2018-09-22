package com.szmengran.hbase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Package com.szmengran.hbase.config
 * @Description: TODO
 * @date 2018年9月21日 上午11:36:37
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String VERSION = "0.0.1";

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("HBase操作API")
                .description("This is to show api description")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .termsOfServiceUrl("")
                .version(VERSION)
                .contact(new Contact("","", "android_li@sina.cn"))
                .build();
    }

    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(SwaggerConfig.class.getPackage().getName()))
                .build()
                .apiInfo(apiInfo());
    }
}
