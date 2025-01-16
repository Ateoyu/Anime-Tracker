package pjatk.edu.pl.frontend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pjatk.edu.pl.data.model.CustomMediaList;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewService {
    private final RestClient restClient;

    public void createMediaList(CustomMediaList mediaList) {
        log.info("Attempting to create new media list with title: {}", mediaList.getTitle());
        try {
            restClient.post()
                    .uri("/customMediaList/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mediaList)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Successfully created media list: {}", mediaList.getTitle());
        } catch (Exception e) {
            log.error("Failed to create new media list: {}. Error: {}", mediaList.getTitle(), e.getMessage());
            throw e;
        }
    }

    public List<CustomMediaList> getAllMediaLists() {
        log.info("Fetching all media lists from backend");
        try {
            List<CustomMediaList> mediaLists = restClient.get()
                    .uri("/customMediaList/getAll")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            log.info("Retrieved {} media lists", mediaLists != null ? mediaLists.size() : 0);
            return mediaLists;
        } catch (Exception e) {
            log.error("Failed to fetch media lists. Error: {}", e.getMessage());
            throw e;
        }
    }

    public CustomMediaList getMediaList(int id) {
        log.info("Fetching media list with id: {}", id);
        try {
            CustomMediaList mediaList = restClient.get()
                    .uri("/customMediaList/getMediaList/" + id)
                    .retrieve()
                    .toEntity(CustomMediaList.class)
                    .getBody();
            log.info("Retrieved media list with id: {}", id);
            return mediaList;
        } catch (Exception e) {
            log.error("Failed to fetch media list with id: {}. Error: {}", id, e.getMessage());
            throw e;
        }
}

    public void addMediaToList(int listId, int mediaId) {
        log.info("Attempting to add media to list with id: {}", listId);
        try {
            restClient.post()
                    .uri("/customMediaList/addTo/" + listId + "?mediaId=" + mediaId)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Successfully added media to list with id: {}", listId);
        } catch (Exception e) {
            log.error("Failed to add media to list: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteMediaFromMediaList(int mediaListId, int mediaId) {
        log.info("Attempting to delete media from list with id: {}", mediaListId);
        try {
            restClient.post()
                    .uri("/customMediaList/deleteFrom/" + mediaListId + "?mediaId=" + mediaId)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Successfully deleted media from list with id: {}", mediaListId);
        } catch (Exception e) {
            log.error("Failed to delete media from list: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteMediaList(int id) {
        log.info("Attempting to delete media list with id: {}", id);
        try {
            restClient.delete()
                    .uri("/customMediaList/delete/" + id)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Successfully deleted media list with id: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete media list: {}", e.getMessage());
            throw e;
        }
    }
}
