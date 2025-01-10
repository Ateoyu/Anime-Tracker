package pjatk.edu.pl.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GraphQlClientConfig {

    @Bean
    public HttpGraphQlClient httpGraphQlClient() {
        //todo: add a cache to the client

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();
        
        WebClient webClient = WebClient.builder()
                .baseUrl("https://graphql.anilist.co")
                .exchangeStrategies(strategies)
                .build();

        return HttpGraphQlClient.builder(webClient).build();
    }
}
