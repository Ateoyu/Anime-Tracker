package com.example.demo.service;

import com.example.demo.client.AnimeClient;
import com.example.demo.dto.MediaDto;
import com.example.demo.model.Media;
import com.example.demo.repository.AnimeRepository;
import com.example.demo.service.mapper.MediaMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class MediaService {
    private final AnimeClient animeClient;
    private final AnimeRepository animeRepository;
    private final MediaMapper mediaMapper;


    @Transactional
    public Media getMediaById(Integer anilistId) {
        return animeRepository.findByAnilistId(anilistId)
                .orElseGet(() -> {
                    MediaDto mediaDto = animeClient.viewMedia(anilistId);
                    return animeRepository.save(mediaMapper.toEntity(mediaDto));
                });
    }

    @Transactional
    public List<Media> getMediaByDateRange(Integer fromDate, Integer toDate) {
//        List<Media> mediaList = animeRepository.findByDateRange(fromDate, toDate);


        List<MediaDto> apiMediaList = animeClient.getAnimeByDateRange(fromDate, toDate);
        for (MediaDto mediaDto : apiMediaList) {
            animeRepository.save(mediaMapper.toEntity(mediaDto));
        }

        return Collections.emptyList();
    }
}
