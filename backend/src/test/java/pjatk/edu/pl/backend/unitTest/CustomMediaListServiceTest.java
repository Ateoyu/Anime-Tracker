package pjatk.edu.pl.backend.unitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjatk.edu.pl.backend.service.CustomMediaListService;
import pjatk.edu.pl.backend.service.MediaService;
import pjatk.edu.pl.data.exception.CustomMediaListIncompleteException;
import pjatk.edu.pl.data.model.CustomMediaList;
import pjatk.edu.pl.data.model.Media;
import pjatk.edu.pl.data.model.Title;
import pjatk.edu.pl.data.repository.CustomMediaListRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomMediaListServiceTest {

    @Mock
    private CustomMediaListRepository mediaListRepository;

    @Mock
    private MediaService mediaService;

    @InjectMocks
    private CustomMediaListService customMediaListService;

    private CustomMediaList testList;
    private Media testMedia;

    @BeforeEach
    void setUp() {
        testList = new CustomMediaList();
        testList.setId(1);
        testList.setTitle("My Favorite Anime");
        testList.setDescription("A collection of my favorite anime");
        testList.setMediaList(new HashSet<>());

        testMedia = Media.builder()
                .id(1)
                .anilistId(1)
                .title(new Title("Test Anime", "テストアニメ", "Test Anime"))
                .episodes(12)
                .build();
    }

    @Test
    void createMediaList_Success() {
        when(mediaListRepository.save(any(CustomMediaList.class))).thenReturn(testList);

        assertDoesNotThrow(() -> customMediaListService.createMediaList(testList));
        verify(mediaListRepository).save(testList);
    }

    @Test
    void createMediaList_WithEmptyTitle_ThrowsException() {
        testList.setTitle("");

        assertThrows(CustomMediaListIncompleteException.class, () -> customMediaListService.createMediaList(testList));
        verify(mediaListRepository, never()).save(any());
    }

    @Test
    void createMediaList_WithNullTitle_ThrowsException() {
        testList.setTitle(null);

        assertThrows(CustomMediaListIncompleteException.class, () -> customMediaListService.createMediaList(testList));
        verify(mediaListRepository, never()).save(any());
    }

    @Test
    void createMediaList_WithEmptyDescription_ThrowsException() {
        testList.setDescription("");

        assertThrows(CustomMediaListIncompleteException.class, () -> customMediaListService.createMediaList(testList));
        verify(mediaListRepository, never()).save(any());
    }

    @Test
    void createMediaList_WithNullDescription_ThrowsException() {
        testList.setDescription(null);

        assertThrows(CustomMediaListIncompleteException.class, () -> customMediaListService.createMediaList(testList));
        verify(mediaListRepository, never()).save(any());
    }

    @Test
    void getAllMediaLists_Success() {
        List<CustomMediaList> expectedLists = Collections.singletonList(testList);
        when(mediaListRepository.findAll()).thenReturn(expectedLists);

        List<CustomMediaList> result = customMediaListService.getAllMediaLists();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testList, result.getFirst());
        verify(mediaListRepository).findAll();
    }

    @Test
    void getMediaList_WhenExists_ReturnsMediaList() {
        when(mediaListRepository.findById(1)).thenReturn(Optional.of(testList));

        CustomMediaList result = customMediaListService.getMediaList(1);

        assertNotNull(result);
        assertEquals(testList.getId(), result.getId());
        assertEquals(testList.getTitle(), result.getTitle());
        verify(mediaListRepository).findById(1);
    }

    @Test
    void getMediaList_WhenDoesNotExist_ReturnsNull() {
        when(mediaListRepository.findById(1)).thenReturn(Optional.empty());

        CustomMediaList result = customMediaListService.getMediaList(1);

        assertNull(result);
        verify(mediaListRepository).findById(1);
    }

    @Test
    void addMediaToList_Success() {
        when(mediaListRepository.findById(1)).thenReturn(Optional.of(testList));
        when(mediaService.getMediaById(1)).thenReturn(testMedia);

        customMediaListService.addMediaToList(1, 1);

        verify(mediaListRepository).save(testList);
        assertTrue(testList.getMediaList().contains(testMedia));
    }

    @Test
    void addMediaToList_WhenListNotFound_DoesNothing() {
        when(mediaListRepository.findById(1)).thenReturn(Optional.empty());

        customMediaListService.addMediaToList(1, 1);

        verify(mediaService, never()).getMediaById(anyInt());
        verify(mediaListRepository, never()).save(any());
    }

    @Test
    void addMediaToList_WhenMediaNotFound_DoesNothing() {
        when(mediaListRepository.findById(1)).thenReturn(Optional.of(testList));
        when(mediaService.getMediaById(1)).thenReturn(null);

        customMediaListService.addMediaToList(1, 1);

        verify(mediaListRepository, never()).save(any());
        assertTrue(testList.getMediaList().isEmpty());
    }

    @Test
    void addMediaToList_WhenMediaListIsNull_CreatesNewSet() {
        testList.setMediaList(null);
        when(mediaListRepository.findById(1)).thenReturn(Optional.of(testList));
        when(mediaService.getMediaById(1)).thenReturn(testMedia);

        customMediaListService.addMediaToList(1, 1);

        verify(mediaListRepository).save(testList);
        assertNotNull(testList.getMediaList());
        assertTrue(testList.getMediaList().contains(testMedia));
    }

    @Test
    void deleteMediaFromList_Success() {
        testList.getMediaList().add(testMedia);
        when(mediaListRepository.findById(1)).thenReturn(Optional.of(testList));
        when(mediaService.getMediaById(1)).thenReturn(testMedia);

        customMediaListService.deleteMediaFromList(1, 1);

        verify(mediaListRepository).save(testList);
        assertFalse(testList.getMediaList().contains(testMedia));
    }

    @Test
    void deleteMediaFromList_WhenListNotFound_DoesNothing() {
        when(mediaListRepository.findById(1)).thenReturn(Optional.empty());

        customMediaListService.deleteMediaFromList(1, 1);

        verify(mediaService, never()).getMediaById(anyInt());
        verify(mediaListRepository, never()).save(any());
    }

    @Test
    void deleteMediaList_Success() {
        customMediaListService.deleteMediaList(1);

        verify(mediaListRepository).deleteById(1);
    }
}
