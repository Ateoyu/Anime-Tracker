package pjatk.edu.pl.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.edu.pl.data.model.CustomMediaList;
import pjatk.edu.pl.data.repository.CustomMediaListRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomMediaListService {
    private final CustomMediaListRepository mediaListRepository;

    public void createMediaList(CustomMediaList mediaList) {
        mediaListRepository.save(mediaList);
    }

    public List<CustomMediaList> getAllMediaLists() {
        return mediaListRepository.findAll();
    }

    public CustomMediaList getMediaList(int id) {
        Optional<CustomMediaList> mediaListInDb = mediaListRepository.findById(id);
        if (mediaListInDb.isPresent()) {
            return mediaListInDb.get();
        }

        log.error("getMediaList: No media list found with id {}", id);
        return null;
    }
}
