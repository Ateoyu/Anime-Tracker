package com.example.demo.service;

import com.example.demo.client.MediaClient;
import com.example.demo.dto.MediaDto;
import com.example.demo.exception.MediaClientException;
import com.example.demo.exception.MediaNotFoundException;
import com.example.demo.exception.MediaObjectIncomplete;
import com.example.demo.model.Media;
import com.example.demo.repository.MediaRepository;
import com.example.demo.service.mapper.MediaMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.InputMismatchException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class MediaService {
    private final MediaClient mediaClient;
    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;

    @Transactional
    public Media getMediaById(Integer anilistId) {
        if (anilistId == null) {
            throw new MediaObjectIncomplete("Media ID cannot be null");
        }

        return mediaRepository.findByAnilistId(anilistId)
                .orElseGet(() -> {
                    log.info("Media with AniList ID: {} - not found in local database, proceeding to save media",
                            anilistId);
                    MediaDto mediaDto = mediaClient.viewMedia(anilistId);
                    if (mediaDto == null) {
                        throw new MediaClientException(
                                "Failed to fetch media with ID: " + anilistId + " from external API");
                    }

                    Media media = mediaMapper.toEntity(mediaDto);
                    if (media == null) {
                        throw new MediaObjectIncomplete("Failed to map media data for ID: " + anilistId);
                    }


                    return mediaRepository.save(media);
                });
    }

    @Transactional
    public List<Media> getMediaByDateRange(Integer fromDate, Integer toDate) {
//        List<Media> databaseMediaList = mediaRepository.findByDateRange(fromDate, toDate);
//        List<MediaDto> apiMediaList = mediaClient.getAnimeByDateRange(fromDate, toDate);
//        for (MediaDto mediaDto : apiMediaList) {
//            Media media = mediaMapper.toEntity(mediaDto);
//            if (!databaseMediaList.contains(media)) {
//                log.info("Media with AniList ID: {} - not found in local database, proceeding to save media.",
//                        media.getAnilistId());
//                databaseMediaList.add(mediaRepository.save(media));
//            }
//            log.info("Media with AniList ID: {} - already in database.", media.getAnilistId());
//        }
//
//        return databaseMediaList;

        if (fromDate == null || toDate == null) throw new IllegalArgumentException("From and To dates cannot be null");
        if (fromDate > toDate) throw new InputMismatchException("From Date cannot be greater than To Date");

        List<Media> databaseMediaList = mediaRepository.findByDateRange(fromDate, toDate);
        List<MediaDto> apiMediaList;
        try {
            apiMediaList = mediaClient.getAnimeByDateRange(fromDate, toDate);
        } catch (MediaClientException e) {
            throw new MediaClientException("Failed to fetch media from external API: " + e.getMessage());
        }

        for (MediaDto mediaDto : apiMediaList) {
            Media media = mediaMapper.toEntity(mediaDto);
            if (!databaseMediaList.contains(media)) {
                log.info("Media with AniList ID: {} - not found in local database, proceeding to save media.", media.getAnilistId());
                databaseMediaList.add(mediaRepository.save(media));
            } else {
                log.debug("Media with AniList ID: {} - already exists in local databse", media.getAnilistId());
            }
        }

        if (databaseMediaList.isEmpty()) {
            throw new MediaNotFoundException("No media found for date range " + fromDate + " to " + toDate);
        }
        return databaseMediaList;
    }

    public Page<Media> getMediaByPage(Integer page, Integer size) {
        if (page == null || size == null) throw new IllegalArgumentException("Page size cannot be null");
        if (page < 0) throw new IllegalArgumentException("Page index cannot be negative");
        if (size <= 0 || size > 50) throw new IllegalArgumentException("Page size cannot be less than or equal to 0 nor greater than 50");

        Page<Media> mediaPage = mediaRepository.findAll(PageRequest.of(page, size));
        if (mediaPage.isEmpty()) {
            throw new MediaNotFoundException("No media found for page " + page);
        }
        return mediaPage;
    }

    public List<Media> getAllMedia() {
        List<Media> mediaList = mediaRepository.findAll();
        if (mediaList.isEmpty()) {
            throw new MediaNotFoundException("No media found in local database.");
        }

        return mediaList;
    }

    public List<Integer> getAllAnimeReleaseYear() {
        List<Integer> mediaList = mediaRepository.getAllMediaYears();
        if (mediaList.isEmpty()) {
            throw new MediaNotFoundException("No anime release years found in local database");
        }
        return mediaList;
    }

    public List<Integer> getAllAnimeEpisodes() {
        List<Integer> episodes = mediaRepository.getAllMediaEpisodes();
        if (episodes.isEmpty()) {
            throw new MediaNotFoundException("No anime episodes found in database");
        }
        return episodes;
    }
}
