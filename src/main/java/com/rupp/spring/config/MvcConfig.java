package com.rupp.spring.config;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc //mvc:annotation-driven
@Configuration
@ComponentScan(basePackages = {"com.rupp.spring.controller", "com.rupp.spring.service", "com.rupp.spring.dao"})
@PropertySource(name = "application", value = { "classpath:/application.properties" }) //import properties file
@EnableSwagger2
public class MvcConfig extends WebMvcConfigurerAdapter {
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        /**convert message json and date format*/
        SkipNullObjectMapper skipNullMapper = new SkipNullObjectMapper();
        skipNullMapper.init();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(skipNullMapper);
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        skipNullMapper.setDateFormat(formatter);
        
        converters.add(converter);
    }
    
    /**create database source bean*/
    @Bean
    public DataSource dataSource() {
        final String propsFile = "db.properties";
        final Properties props = new Properties();
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResource(propsFile).openStream());
            return BasicDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }
        return null;
    }
    
    //<mvc:resources location="classpath:/META-INF/resources/" mapping="swagger-ui.html"></mvc:resources>
    //<mvc:resources location="classpath:/META-INF/resources/webjars/" mapping="/webjars/**"></mvc:resources>
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
          .addResourceLocations("classpath:/META-INF/resources/");
     
        registry.addResourceHandler("/webjars/**")
          .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    
//    @Override
//    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//    }
    
    //<bean class="springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration" id="swagger2Config"></bean>
//    @Bean("swagger2Config")
//    public Swagger2DocumentationConfiguration getSwagger2DocumentationConfiguration() {
//        return new Swagger2DocumentationConfiguration();
//    }
    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)
           .apiInfo(getApiInfoForVersion("1"))
          .select()                                  
          .apis(RequestHandlerSelectors.any())
          .paths(PathSelectors.any())
          .build()
         // .pathMapping("api")
          //.securitySchemes(Arrays.asList(apiKey())
                  ;
    }
    
//    private ApiKey apiKey() {
//        return new ApiKey("mykey", "api_key", "header");
//    }
    
    private ApiInfo getApiInfoForVersion(String version) {
        Contact defaultContact = new Contact("Company", "https://github.com/sophea/docrest-swagger-spring-rest-api", "");
        return new ApiInfo("Version " + version, "Api Documentation. Each REST-API Request must start with /api. ", version, "urn:tos",
            defaultContact, "Restricted usage", "https://github.com/sophea/docrest-swagger-spring-rest-api");
    }
}
