package com.example.demo.client;

import com.example.demo.dto.MediaDto;
import com.example.demo.dto.PageDto;
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
    public MediaDto viewMedia(Integer animeId) {
        //language=GraphQL
        String query = """
                query animeByMediaId($mediaId: Int) {
                    Media(type: ANIME, id: $mediaId, isAdult: false, status_in: [FINISHED, RELEASING]) {
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
                        averageScore
                        coverImage {
                            large
                        }
                        bannerImage
                        description
                        characters(sort: ROLE) {
                            nodes {
                                id
                                name {
                                    full
                                    native
                                }
                                gender
                                age
                                dateOfBirth {
                                    year
                                    month
                                    day
                                }
                                image {
                                    large
                                }
                            }
                        }
                    }
                }
                """;

        MediaDto mediaDto = graphQlClient.document(query)
                .variable("mediaId", animeId)
                .retrieve("Media")
                .toEntity(MediaDto.class).block();

        return mediaDto;
    }

    //todo: figure out why all startDate and endDate is always null, even if it shouldn't be (it works for animeByAverageScore)
    public List<MediaDto> getAnimeByDateRange(Integer from, Integer to) {
        List<MediaDto> allMediaDto = new ArrayList<>();
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
                            media(type: ANIME,
                                startDate_greater: $startDateGreater,
                                startDate_lesser: $startDateLesser,
                                status_in: [FINISHED, RELEASING]
                                sort: START_DATE,
                                isAdult: false) {
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
                                averageScore
                                coverImage {
                                    large
                                }
                                bannerImage
                                description
                                characters(sort: ROLE) {
                                    nodes {
                                        id
                                        name {
                                            full
                                            native
                                        }
                                        gender
                                        age
                                        dateOfBirth {
                                            year
                                            month
                                            day
                                        }
                                        image {
                                            large
                                        }
                                    }
                                }
                            }
                        }
                    }
                    """;

            PageDto pageDtoResponse = graphQlClient.document(query)
                    .variable("startDateGreater", from)
                    .variable("startDateLesser", to)
                    .variable("page", currentPage)
                    .retrieve("Page")
                    .toEntity(PageDto.class).block();

            if (pageDtoResponse != null && pageDtoResponse.mediaList() != null) {
                totalProcessedMedia += pageDtoResponse.mediaList().size();
                allMediaDto.addAll(pageDtoResponse.mediaList());
                hasNextPage = pageDtoResponse.pageInfo().hasNextPage();

                log.info("Processed page {} with {} entries. Total processed media: {}", currentPage, pageDtoResponse.mediaList().size(), totalProcessedMedia);

                currentPage++;
            } else {
                log.warn("No mediaDto found for page {}. Stopping pagination", currentPage);
                hasNextPage = false;
            }
        }

        log.info("Completed fetching anime. Total processed media: {}", allMediaDto.size());
        return allMediaDto;
    }

    public List<MediaDto> animeByAverageScoreGreaterThan(Integer score) {
        List<MediaDto> allMediaDto = new ArrayList<>();
        boolean hasNextPage = true;
        int currentPage = 1;
        int totalProcessedMedia = 0;

        while (hasNextPage) {
            log.info("Fetching page {} of anime", currentPage);

            //language=GraphQL
            String query = """
                    query animeByAverageScoreGreaterThan($page: Int, $averageScoreGreater: Int) {
                        Page(page: $page, perPage: 50) {
                            pageInfo {
                                currentPage
                                hasNextPage
                            }
                            media(type: ANIME,
                                averageScore_greater: $averageScoreGreater,
                                sort: SCORE,
                                status_in: [FINISHED, RELEASING]
                            ) {
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
                                averageScore
                                characters(sort: ROLE) {
                                    nodes {
                                        id
                                        name {
                                            full
                                            native
                                        }
                                        gender
                                        age
                                        dateOfBirth {
                                            year
                                            month
                                            day
                                        }
                                        image {
                                            large
                                        }
                                    }
                                }
                            }
                        }
                    }
                    """;

            PageDto pageDtoResponse = graphQlClient.document(query)
                    .variable("page", currentPage)
                    .variable("averageScoreGreater", score)
                    .retrieve("Page")
                    .toEntity(PageDto.class).block();

            if (pageDtoResponse != null && pageDtoResponse.mediaList() != null) {
                totalProcessedMedia += pageDtoResponse.mediaList().size();
                allMediaDto.addAll(pageDtoResponse.mediaList());
                hasNextPage = pageDtoResponse.pageInfo().hasNextPage();

                log.info("Processed page {} with {} entries. Total processed media: {}", currentPage, pageDtoResponse.mediaList().size(), totalProcessedMedia);

                currentPage++;
            } else {
                log.warn("No mediaDto found for page {}. Stopping pagination", currentPage);
                hasNextPage = false;
            }
        }

        log.info("Completed fetching anime. Total processed media: {}", allMediaDto.size());
        return allMediaDto;
    }

    //todo: get the characters that feature in each anime
}
