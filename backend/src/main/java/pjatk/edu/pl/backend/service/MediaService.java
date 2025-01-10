package pjatk.edu.pl.backend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjatk.edu.pl.backend.client.MediaClient;
import pjatk.edu.pl.backend.service.mapper.MediaMapper;
import pjatk.edu.pl.data.dto.MediaDto;
import pjatk.edu.pl.data.exception.MediaClientException;
import pjatk.edu.pl.data.exception.MediaNotFoundException;
import pjatk.edu.pl.data.exception.MediaObjectIncomplete;
import pjatk.edu.pl.data.model.Media;
import pjatk.edu.pl.data.repository.MediaRepository;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

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
        if (fromDate == null || toDate == null) throw new IllegalArgumentException("From and To dates cannot be null");
        if (fromDate > toDate) throw new InputMismatchException("From Date cannot be greater than To Date");

        List<MediaDto> apiMediaList;

        try {
            apiMediaList = mediaClient.getAnimeByDateRange(fromDate, toDate);
        } catch (MediaClientException e) {
            throw new MediaClientException("Failed to fetch media from external API: " + e.getMessage());
        }

        for (MediaDto mediaDto : apiMediaList) {
            Optional<Media> mediaInDb = mediaRepository.findByAnilistId(mediaDto.id());
            if (mediaInDb.isEmpty()) {
                log.info("Media with AniList ID: {} - not found in local database, proceeding to save media.", mediaDto.id());
                mediaRepository.save(mediaMapper.toEntity(mediaDto));
            } else {
                log.debug("Media with AniList ID: {} - already exists in local database", mediaDto.id());
            }
        }

        List<Media> mediaList = mediaRepository.findByDateRange(fromDate, toDate);

        if (mediaList.isEmpty()) {
            throw new MediaNotFoundException("No media found for date range " + fromDate + " to " + toDate);
        }
        return mediaList;
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
