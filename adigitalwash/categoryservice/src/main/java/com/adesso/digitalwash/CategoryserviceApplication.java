package com.adesso.digitalwash;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableDiscoveryClient
@EnableSwagger2
@EnableScheduling
@SpringBootApplication
public class CategoryserviceApplication {
	
	@Value("${server.allowedOrigin}")
	int allowedOrigin;
	
    public static void main( String[] args ) {
    	System.setProperty("spring.devtools.restart.enabled", "false");
    	SpringApplication.run(CategoryserviceApplication.class, args);
    }
    
//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**").allowedMethods("GET", "PUT", "POST", "DELETE", "OPTIONS")
//						.allowedOrigins("http://localhost:" + allowedOrigin);
//			}
//		};
//	}
    
    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Category Service API Documentation")
            .description("Documentation automatically generated")
            .license("Apache 2.0")
            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
            .termsOfServiceUrl("")
            .version("0.0.1")
            .contact(new Contact("Tamer Karatekin","", "tamer.karatekin@adesso.de"))
            .build();
    }
    
    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("com.adesso.digitalwash.api"))
                    .build()
                .apiInfo(apiInfo());
    }
}
