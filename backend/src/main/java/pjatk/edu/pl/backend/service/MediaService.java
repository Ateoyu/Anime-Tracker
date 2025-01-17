package pjatk.edu.pl.backend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjatk.edu.pl.backend.client.MediaClient;
import pjatk.edu.pl.backend.service.mapper.EntityMapper;
import pjatk.edu.pl.data.dto.MediaDto;
import pjatk.edu.pl.data.dto.MediaFilterDto;
import pjatk.edu.pl.data.exception.MediaClientException;
import pjatk.edu.pl.data.exception.MediaNotFoundException;
import pjatk.edu.pl.data.exception.MediaObjectIncomplete;
import pjatk.edu.pl.data.model.Date;
import pjatk.edu.pl.data.model.Media;
import pjatk.edu.pl.data.repository.MediaRepository;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class MediaService {
    private final MediaClient mediaClient;
    private final MediaRepository mediaRepository;
    private final EntityMapper<Media, MediaDto> mediaMapper;

    @Transactional
    @Cacheable(value = "media")
    public Media getMediaById(Integer anilistId) {
        if (anilistId == null) {
            throw new MediaObjectIncomplete("Media ID cannot be null");
        }

        return mediaRepository.findByAnilistId(anilistId)
                .orElseGet(() -> {
                    log.info("Media with AniList ID: {} - not found in local database, proceeding to save media",anilistId);
                    MediaDto mediaDto = mediaClient.viewMedia(anilistId);
                    if (mediaDto == null) {
                        throw new MediaClientException("Failed to fetch media with ID: " + anilistId + " from external API");
                    }

                    Media media = mediaMapper.toEntity(mediaDto);
                    if (media == null) {
                        throw new MediaObjectIncomplete("Failed to map media data for ID: " + anilistId);
                    }

                    return mediaRepository.save(media);
                });
    }

    @Transactional
    @Cacheable(value = "mediaByDateRange")
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

    @Cacheable(value = "mediaPaginated")
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

    public Page<Media> getFilteredMedia(MediaFilterDto filters) {
        log.debug("Applying filters: {}", filters);
        
        // If only genre filter is active, use custom query
        if (!"All".equals(filters.getGenreFilter()) && 
            "All".equals(filters.getYearFilter()) && 
            "All".equals(filters.getEpisodeFilter())) {
            return mediaRepository.findByGenreName(
                filters.getGenreFilter(), 
                PageRequest.of(filters.getPage(), 50)
            );
        }
        
        // Otherwise, use QueryByExample for other filters
        Media probe = new Media();
        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues();
        
        // Handle Year filter
        if (!"All".equals(filters.getYearFilter())) {
            try {
                Date startDate = new Date();
                startDate.setYear(Integer.parseInt(filters.getYearFilter()));
                probe.setStartDate(startDate);
            } catch (NumberFormatException e) {
                log.error("Invalid year format: {}", filters.getYearFilter());
            }
        }
        
        // Handle Episode filter
        if (!"All".equals(filters.getEpisodeFilter())) {
            try {
                probe.setEpisodes(Integer.parseInt(filters.getEpisodeFilter()));
            } catch (NumberFormatException e) {
                log.error("Invalid episode format: {}", filters.getEpisodeFilter());
            }
        }
        
        Example<Media> example = Example.of(probe, matcher);
        Page<Media> filteredMedia = mediaRepository.findAll(example, PageRequest.of(filters.getPage(), 50));
        
        // If genre filter is active, filter the results in memory
        if (!"All".equals(filters.getGenreFilter())) {
            List<Media> genreFiltered = filteredMedia.getContent().stream()
                .filter(media -> media.getGenres().stream()
                    .anyMatch(genre -> genre.getName().equals(filters.getGenreFilter())))
                .collect(Collectors.toList());
            
            return new PageImpl<>(
                genreFiltered, 
                PageRequest.of(filters.getPage(), 50),
                genreFiltered.size()
            );
        }
        
        return filteredMedia;
    }
}
