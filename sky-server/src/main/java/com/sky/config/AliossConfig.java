package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AliossConfig {
    @Bean
    public AliOssUtil create(AliOssProperties aliOssProperties){
        return new AliOssUtil(aliOssProperties
                .getEndpoint(), aliOssProperties
                .getAccessKeyId(), aliOssProperties
                .getAccessKeySecret(), aliOssProperties
                .getBucketName());
    }
}
