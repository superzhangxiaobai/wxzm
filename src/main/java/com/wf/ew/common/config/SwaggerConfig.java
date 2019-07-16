package com.wf.ew.common.config;

import org.springframework.beans.factory.annotation.Value;
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
 * Swagger生成api文档
 * Created by wangfan on 2018-02-22 上午 11:29.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	//是否开启swagger，正式环境一般是需要关闭的，可根据springboot的多环境配置进行设置
    @Value(value = "${swagger.enabled}")
    Boolean swaggerEnabled;
	
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //.groupName("测试")
                .enable(swaggerEnabled)// 是否开启
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wf.ew.light.api"))//扫描的路径包
                .paths(PathSelectors.any())// 指定路径处理PathSelectors.any()代表所有的路径
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("无线照明系统 API文档")//标题
                .description("2019-05-14 上线版本")//描述
                .termsOfServiceUrl("http://mindao.com.cn")//（不可见）条款地址，公司内部使用的话不需要配
                .contact("无线照明研发部")//作者信息
                .version("1.6.0")//版本号
                .build();
    }
}
