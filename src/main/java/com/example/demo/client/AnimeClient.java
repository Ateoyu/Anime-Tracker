package com.example.demo.client;

import com.example.demo.dto.Media;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class AnimeClient {
    private final HttpGraphQlClient graphQlClient;

    public Media viewMedia(Integer animeId) {
        //language=GraphQL
        String query = """
                query Query($mediaId: Int) {
                  Media(id: $mediaId) {
                    title {
                      english
                      romaji
                      native
                    }
                    genres
                    episodes
                    }
                  }
                """;

        Media media = graphQlClient.document(query)
                .variable("mediaId", animeId)
                .retrieve("Media")
                .toEntity(Media.class).block();

        return media;
    }
}
