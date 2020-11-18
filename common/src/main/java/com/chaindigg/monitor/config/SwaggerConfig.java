package com.chaindigg.monitor.config;//package com.chaindigg.monitor.config;
//
//import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//public class SwaggerConfig {
//  @Bean
//  public Docket createRestApi() {
//    return new Docket(DocumentationType.OAS_30)
//        .apiInfo(apiInfo())
//        .select()
//        .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//        .paths(PathSelectors.any())
//        .build();
//  }
//
//  private ApiInfo apiInfo() {
//    return new ApiInfoBuilder()
//        .title("Monitor api")
//        .description("")
//        .contact(new Contact("chenghao", "", "454947233@qq.com"))
//        .version("0.0.1-SNAPSHOT")
//        .build();
//  }
//}
//
//
