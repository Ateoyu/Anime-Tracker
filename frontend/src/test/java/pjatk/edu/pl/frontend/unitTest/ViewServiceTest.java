package pjatk.edu.pl.frontend.unitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import pjatk.edu.pl.data.model.CustomMediaList;
import pjatk.edu.pl.frontend.service.ViewService;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewServiceTest {

    @Mock
    private RestClient restClient;

    @InjectMocks
    private ViewService viewService;

    private CustomMediaList testList;

    @BeforeEach
    void setUp() {
        testList = new CustomMediaList();
        testList.setId(1);
        testList.setTitle("Test List");
        testList.setDescription("Test Description");
        testList.setMediaList(new HashSet<>());
    }

    @Test
    void createMediaList_Success() {

        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Mock chain of RestClient calls
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/customMediaList/create")).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(CustomMediaList.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(ResponseEntity.ok().build());

        assertDoesNotThrow(() -> viewService.createMediaList(testList));

        verify(restClient).post();
        verify(requestBodySpec).body(testList);
    }

    @Test
    void createMediaList_WhenApiCallFails_ThrowsException() {

        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/customMediaList/create")).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(CustomMediaList.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenThrow(new RuntimeException("API Error"));

        assertThrows(RuntimeException.class, () -> viewService.createMediaList(testList));
    }

    @Test
    void getAllMediaLists_Success() {
        List<CustomMediaList> expectedLists = Collections.singletonList(testList);
        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Mock chain of RestClient calls
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/customMediaList/getAll")).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(expectedLists);

        List<CustomMediaList> result = viewService.getAllMediaLists();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testList, result.getFirst());
    }

    @Test
    void getMediaList_Success() {
        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Mock chain of RestClient calls
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/customMediaList/getMediaList/1")).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(CustomMediaList.class)).thenReturn(ResponseEntity.ok(testList));

        CustomMediaList result = viewService.getMediaList(1);

        assertNotNull(result);
        assertEquals(testList.getId(), result.getId());
        assertEquals(testList.getTitle(), result.getTitle());
    }

    @Test
    void addMediaToList_Success() {
        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Mock chain of RestClient calls
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/customMediaList/addTo/1?mediaId=2")).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(ResponseEntity.ok().build());

        assertDoesNotThrow(() -> viewService.addMediaToList(1, 2));

        verify(restClient).post();
        verify(requestBodyUriSpec).uri("/customMediaList/addTo/1?mediaId=2");
    }

    @Test
    void deleteMediaFromMediaList_Success() {
        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Mock chain of RestClient calls
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/customMediaList/deleteFrom/1?mediaId=2")).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(ResponseEntity.ok().build());

        assertDoesNotThrow(() -> viewService.deleteMediaFromMediaList(1, 2));

        verify(restClient).post();
        verify(requestBodyUriSpec).uri("/customMediaList/deleteFrom/1?mediaId=2");
    }

    @Test
    void deleteMediaList_Success() {
        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Mock chain of RestClient calls
        when(restClient.delete()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/customMediaList/delete/1")).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(ResponseEntity.ok().build());

        assertDoesNotThrow(() -> viewService.deleteMediaList(1));

        verify(restClient).delete();
        verify(requestHeadersUriSpec).uri("/customMediaList/delete/1");
    }

    @Test
    void deleteMediaList_WhenApiCallFails_ThrowsException() {
        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);

        // Mock chain of RestClient calls that results in an exception
        when(restClient.delete()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/customMediaList/delete/1")).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenThrow(new RuntimeException("API Error"));

        assertThrows(RuntimeException.class, () -> viewService.deleteMediaList(1));
    }
}