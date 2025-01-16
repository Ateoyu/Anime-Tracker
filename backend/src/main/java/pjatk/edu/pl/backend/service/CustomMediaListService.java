package pjatk.edu.pl.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.edu.pl.data.exception.CustomMediaListIncompleteException;
import pjatk.edu.pl.data.model.CustomMediaList;
import pjatk.edu.pl.data.model.Media;
import pjatk.edu.pl.data.repository.CustomMediaListRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomMediaListService {
    private final CustomMediaListRepository mediaListRepository;
    private final MediaService mediaService;


    public void createMediaList(CustomMediaList mediaList) {
        if (mediaList.getTitle() == null || mediaList.getTitle().trim().isEmpty()) {
            log.error("Attempted to create media list with empty title");
            throw new CustomMediaListIncompleteException("Media list title cannot be empty");
        }
        
        if (mediaList.getDescription() == null || mediaList.getDescription().trim().isEmpty()) {
            log.error("Attempted to create media list with empty description");
            throw new CustomMediaListIncompleteException("Media list description cannot be empty");
        }
        
        mediaListRepository.save(mediaList);
        log.debug("Successfully created media list with ID: {}", mediaList.getId());
    }

    public List<CustomMediaList> getAllMediaLists() {
        return mediaListRepository.findAll();
    }

    public CustomMediaList getMediaList(int id) {
        return getCustomMediaList(id);
    }

    private CustomMediaList getCustomMediaList(int id) {
        Optional<CustomMediaList> mediaListInDb = mediaListRepository.findById(id);
        if (mediaListInDb.isPresent()) {
            return mediaListInDb.get();
        }

        log.error("getMediaList: No media list found with id {}", id);
        return null;
    }

    public void addMediaToList(int listId, int mediaId) {
        log.info("Attempting to add media with ID {} to list with ID: {}", mediaId,  listId);
        CustomMediaList mediaList = getCustomMediaList(listId);
        if (mediaList == null) {
            log.error("addMediaToList: No media list found with id {}", listId);
            return;
        }

        Media media = mediaService.getMediaById(mediaId);
        if (media == null) {
            log.error("addMediaToList: No media found with id {}", mediaId);
            return;
        }

        if (mediaList.getMediaList() == null) {
            mediaList.setMediaList(new HashSet<>());
        }
        
        mediaList.getMediaList().add(media);
        mediaListRepository.save(mediaList);
        log.info("Successfully added media with ID {} to list with ID {}", mediaId,  listId);
    }

    public void deleteMediaFromList(int id, int mediaId) {
        CustomMediaList mediaList = getCustomMediaList(id);
        if (mediaList != null) {
            mediaList.getMediaList().remove(mediaService.getMediaById(mediaId));
            mediaListRepository.save(mediaList);
            log.info("Successfully deleted media with ID {} from list with ID {}", mediaId, id);
        }
    }

    public void deleteMediaList(int id) {
        mediaListRepository.deleteById(id);
    }
}
