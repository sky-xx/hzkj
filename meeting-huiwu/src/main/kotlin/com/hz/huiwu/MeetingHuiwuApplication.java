package com.hz.huiwu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages ={"com.hz"} )
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaRepositories(basePackages = {"com.hz"})
@EntityScan(basePackages = {"com.hz"})
public class MeetingHuiwuApplication    extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MeetingHuiwuApplication.class);
    }


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MeetingHuiwuApplication.class);
    }
}
