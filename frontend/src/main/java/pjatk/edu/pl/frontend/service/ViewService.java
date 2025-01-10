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
        try {
            restClient.post()
                    .uri("/customMediaList/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mediaList)
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception e) {
            log.error("Failed to create new media list: {}", e.getMessage());
        }
    }

    public List<CustomMediaList> getAllMediaLists() {
        return restClient.get()
                .uri("/customMediaList/getAll")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<CustomMediaList>>() {})
                .getBody();
    }

    public CustomMediaList getMediaList(int id) {
        return restClient.get()
                .uri("/customMediaList/getMediaList/" + id)
                .retrieve()
                .toEntity(CustomMediaList.class)
                .getBody();
    }
}
