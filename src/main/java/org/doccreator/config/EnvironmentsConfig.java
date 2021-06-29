package org.doccreator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvironmentsConfig {
    @Value("${word.parser.operatingRoom}")
    private String operatingRoom;
    @Value("${word.parser.templateRoom}")
    private String templateRoom;

    @Bean
    public String operatingRoom() {
        return this.operatingRoom;
    }

    @Bean
    public String templateRoom() {
        return this.templateRoom;
    }
}
