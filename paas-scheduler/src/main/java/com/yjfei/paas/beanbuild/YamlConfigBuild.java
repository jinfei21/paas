package com.yjfei.paas.beanbuild;

import java.io.InputStream;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yjfei.paas.configuration.PaasConfiguration;
import com.yjfei.paas.yaml.YamlConfigurationFactory;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConditionalOnProperty(name = "paas.config", havingValue = "yaml")
@ConfigurationProperties(prefix = "paas.config.yaml")
public class YamlConfigBuild {

    @Setter
    @Getter
    private String configFile = "paas_config.yml";

    @Bean
    public PaasConfiguration buildConfig() throws Exception {

    	try(InputStream in = YamlConfigBuild.class.getClassLoader().getResourceAsStream(configFile)){
    		PaasConfiguration config = new YamlConfigurationFactory<PaasConfiguration>(PaasConfiguration.class,"dw").build(in);
    		return config;
    	}
    }
}