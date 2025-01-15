package pjatk.edu.pl.backend.unitTest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pjatk.edu.pl.backend.client.MediaClient;
import pjatk.edu.pl.backend.service.MediaService;
import pjatk.edu.pl.backend.service.mapper.MediaMapper;
import pjatk.edu.pl.data.repository.MediaRepository;

@ExtendWith(MockitoExtension.class)
public class MediaServiceUnitTest {

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private MediaClient mediaClient;

    @Mock
    private MediaMapper mediaMapper;

    @InjectMocks
    private MediaService mediaService;



    void getMediaByIdTest() {
        Integer mediaId = 21827;
    }
}
