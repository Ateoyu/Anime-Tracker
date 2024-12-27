package com.example.demo.client;

import com.example.demo.dto.Media;
import com.example.demo.dto.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class AnimeClient {
    private final HttpGraphQlClient graphQlClient;

    //todo: ask if I can somehow specify these queries within Resources directory, and call them in here to make the code a lil more organised and navigable.
    public Media viewMedia(Integer animeId) {
        //language=GraphQL
        String query = """
                query Query($mediaId: Int) {
                    Media(type: ANIME, id: $mediaId) {
                        title {
                            english
                            romaji
                            native
                        }
                        episodes
                        startDate {
                            year
                            month
                            day
                        }
                        endDate {
                            year
                            month
                            day
                        }
                        genres
                    }
                }""";

        Media media = graphQlClient.document(query)
                .variable("mediaId", animeId)
                .retrieve("Media")
                .toEntity(Media.class).block();

        return media;
    }

    //todo: figure out why all startDate and endDate is always null, even if it shouldn't be
    public List<Media> getAnimeByDateRange(Integer from, Integer to) {
        List<Media> allMedia = new ArrayList<>();
        boolean hasNextPage = true;
        int currentPage = 1;
        int totalProcessedMedia = 0;

        log.info("Starting to fetch anime between dates {} and {}", from, to);

        while (hasNextPage) {
            log.info("Fetching page {} of anime", currentPage);

            //language=GraphQL
            String query = """
                    query AnimeByDateRange($page: Int, $startDateGreater: FuzzyDateInt, $startDateLesser: FuzzyDateInt) {
                        Page(page: $page, perPage: 50) {
                            pageInfo {
                                currentPage
                                hasNextPage
                            }
                            media(type: ANIME, startDate_greater: $startDateGreater, startDate_lesser: $startDateLesser, sort: START_DATE) {
                                id
                                title {
                                    english
                                    romaji
                                    native
                                }
                                episodes
                                startDate {
                                    year
                                    month
                                    day
                                }
                                endDate {
                                    year
                                    month
                                    day
                                }
                                genres
                            }
                        }
                    }""";

            Page pageResponse = graphQlClient.document(query)
                    .variable("from", from)
                    .variable("to", to)
                    .variable("page", currentPage)
                    .retrieve("Page")
                    .toEntity(Page.class).block();

            if (pageResponse != null && pageResponse.mediaList() != null) {
                totalProcessedMedia += pageResponse.mediaList().size();
                allMedia.addAll(pageResponse.mediaList());
                hasNextPage = pageResponse.pageInfo().hasNextPage();

                log.info("Processed page {} with {} entries. Total processed media: {}", currentPage, pageResponse.mediaList().size(), totalProcessedMedia);

                currentPage++;
            } else {
                log.warn("No media found for page {}. Stopping pagination", currentPage);
                hasNextPage = false;
            }
        }

        log.info("Completed fetching anime. Total processed media: {}", allMedia.size());
        return allMedia;
    }
}
