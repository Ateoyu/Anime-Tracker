package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GraphQlClientConfig {

    @Bean
    public HttpGraphQlClient httpGraphQlClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://graphql.anilist.co")
                .build();

        return HttpGraphQlClient.builder(webClient).build();
    }
}
