package com.yjfei.paas.beanbuild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;
import com.yjfei.paas.filter.CORSFilter;

import lombok.extern.slf4j.Slf4j;

@Configuration  
@Slf4j
public class DataSourceBuild {

    //数据源监控
   @Bean
   public ServletRegistrationBean servletRegistrationBean() {
       ServletRegistrationBean registration = new ServletRegistrationBean();
       registration.setServlet(new StatViewServlet());
       registration.setName("druid");
       registration.setUrlMappings(Lists.newArrayList("/druid/*"));
       //自定义添加初始化参数
       Map<String, String> intParams = new HashMap<>();
       intParams.put("loginUsername","druid");
       intParams.put("loginPassword","druid");
       registration.setName("DruidWebStatFilter");
       registration.setInitParameters(intParams);
       return registration;
   }
   
   @Bean  
   public FilterRegistrationBean  filterRegistrationBean() {  
       FilterRegistrationBean registrationBean = new FilterRegistrationBean();  
       CORSFilter corsFilter = new CORSFilter();  
       registrationBean.setFilter(corsFilter);  
       List<String> urlPatterns = new ArrayList<String>();  
       urlPatterns.add("/*");  
       registrationBean.setUrlPatterns(urlPatterns);  
       return registrationBean;  
   } 
   
   @Bean 
   public RestTemplate restTemplate(RestTemplateBuilder builder) {
       return builder.build();
   }
}
