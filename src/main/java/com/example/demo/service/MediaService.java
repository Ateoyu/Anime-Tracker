package com.example.demo.service;

import com.example.demo.client.MediaClient;
import com.example.demo.dto.MediaDto;
import com.example.demo.model.Media;
import com.example.demo.repository.MediaRepository;
import com.example.demo.service.mapper.MediaMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return mediaRepository.findByAnilistId(anilistId)
                .orElseGet(() -> {
                    MediaDto mediaDto = mediaClient.viewMedia(anilistId);
                    log.info("Media with AniList ID: {} - not found in local database, proceeding to save media", anilistId);
                    return mediaRepository.save(mediaMapper.toEntity(mediaDto));
                });
    }

    @Transactional
    public List<Media> getMediaByDateRange(Integer fromDate, Integer toDate) {
        List<Media> databaseMediaList = mediaRepository.findByDateRange(fromDate, toDate);
        List<MediaDto> apiMediaList = mediaClient.getAnimeByDateRange(fromDate, toDate);
        for (MediaDto mediaDto : apiMediaList) {
            Media media = mediaMapper.toEntity(mediaDto);
            if (!databaseMediaList.contains(media)) {
                log.info("Media with AniList ID: {} - not found in local database, proceeding to save media.", media.getAnilistId());
                databaseMediaList.add(mediaRepository.save(media));
            }
            log.info("Media with AniList ID: {} - already in database.", media.getAnilistId());
        }

        return databaseMediaList;
    }

    public Page<Media> getMediaByPage(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return mediaRepository.findAll(pageRequest);
    }

    public List<Integer> getAllAnimeReleaseYear() {
        return mediaRepository.getAllMediaYears();
    }

    public List<Integer> getAllAnimeEpisodes() {
        return mediaRepository.getAllMediaEpisodes();
    }

}
