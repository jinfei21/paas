package com.yjfei.paas.filter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@ComponentScan
@Configuration
@EnableWebMvc
public class WebMVCConfig extends WebMvcConfigurerAdapter{
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/static/js/**").addResourceLocations("classpath:/static/static/js/");
	    registry.addResourceHandler("/static/css/**").addResourceLocations("classpath:/static/static/css/");
	    registry.addResourceHandler("/static/css/theme-green/**").addResourceLocations("classpath:/static/static/css/theme-green/");
	    registry.addResourceHandler("/static/css/theme-green/fonts/**").addResourceLocations("classpath:/static/static/css/theme-green/fonts/");
	    registry.addResourceHandler("/static/img/**").addResourceLocations("classpath:/static/static/img/");
	    registry.addResourceHandler("/static/fonts/**").addResourceLocations("classpath:/static/static/fonts/");
	    registry.addResourceHandler("/index.html").addResourceLocations("classpath:/static/index.html");
	    
//	    registry.addResourceHandler("/js/**").addResourceLocations("classpath:/public/js/");
//	    registry.addResourceHandler("/css/**").addResourceLocations("classpath:/public/css/");
//	    registry.addResourceHandler("/css/theme-green/**").addResourceLocations("classpath:/public/css/theme-green/");
//	    registry.addResourceHandler("/css/theme-green/fonts/**").addResourceLocations("classpath:/public/css/theme-green/fonts/");
//	    registry.addResourceHandler("/img/**").addResourceLocations("classpath:/public/img/");
//	    registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/public/fonts/");
	}

}
