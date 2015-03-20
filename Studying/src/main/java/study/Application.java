package study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    /**
     * 加密器
     * @return
     */
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
    	return new BCryptPasswordEncoder();
    }
    
    /*
     * 编码问题
     */
    @Bean
    CharacterEncodingFilter characterEncodingFilter() {
    	CharacterEncodingFilter filter = new CharacterEncodingFilter();
    	filter.setEncoding("utf-8");
    	return filter;
    }
    
//    @Bean
//    MultipartResolver multipartResolver() 
//    {
//    	CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//    	multipartResolver.setMaxUploadSize(-1);
//    	return multipartResolver;
//    	
//    }
}
