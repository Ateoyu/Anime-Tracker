package pjatk.edu.pl.backend.unitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import pjatk.edu.pl.backend.controller.CustomMediaListController;
import pjatk.edu.pl.backend.service.CustomMediaListService;
import pjatk.edu.pl.data.model.CustomMediaList;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomMediaListControllerTest {

    @Mock
    private CustomMediaListService mediaListService;

    @InjectMocks
    private CustomMediaListController controller;

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
        doNothing().when(mediaListService).createMediaList(testList);

        var response = controller.createMediaList(testList);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(mediaListService).createMediaList(testList);
    }

    @Test
    void getMediaList_Success() {
        when(mediaListService.getMediaList(1)).thenReturn(testList);

        var response = controller.getMediaList(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testList, response.getBody());
        verify(mediaListService).getMediaList(1);
    }

    @Test
    void getAllMediaList_Success() {
        List<CustomMediaList> expectedLists = Collections.singletonList(testList);
        when(mediaListService.getAllMediaLists()).thenReturn(expectedLists);

        var response = controller.getAllMediaList();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedLists, response.getBody());
        verify(mediaListService).getAllMediaLists();
    }

    @Test
    void addToMediaList_Success() {
        doNothing().when(mediaListService).addMediaToList(1, 2);

        var response = controller.addToMediaList(1, 2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(mediaListService).addMediaToList(1, 2);
    }

    @Test
    void deleteFromMediaList_Success() {
        doNothing().when(mediaListService).deleteMediaFromList(1, 2);

        var response = controller.deleteFromMediaList(1, 2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(mediaListService).deleteMediaFromList(1, 2);
    }

    @Test
    void deleteList_Success() {
        doNothing().when(mediaListService).deleteMediaList(1);

        var response = controller.deleteList(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(mediaListService).deleteMediaList(1);
    }
}