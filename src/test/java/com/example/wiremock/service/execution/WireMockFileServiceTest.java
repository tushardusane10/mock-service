package com.example.wiremock.service.execution;

import com.example.wiremock.service.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WireMockFileServiceTest {

    @Mock
    private FileUtil fileUtil;

    @Mock
    private MultipartFile mappingFile;

    @Mock
    private MultipartFile stubFile;

    @InjectMocks
    private WireMockFileService wireMockFileService;

    @BeforeEach
    void setUp() {
        // Set the filesRoot field using reflection since @Value is not processed in unit tests
        wireMockFileService = new WireMockFileService(fileUtil);
        // Set the filesRoot field directly for testing
        try {
            var field = WireMockFileService.class.getDeclaredField("filesRoot");
            field.setAccessible(true);
            field.set(wireMockFileService, "test-root");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void storeFiles_shouldStoreFilesSuccessfully() throws IOException {
        when(mappingFile.getOriginalFilename()).thenReturn("mapping.json");
        when(stubFile.getOriginalFilename()).thenReturn("stub.json");

        wireMockFileService.storeFiles(mappingFile, stubFile);

        verify(fileUtil).createDirIfNotPresent(any(File.class), any(File.class));
        verify(mappingFile).transferTo(any(File.class));
        verify(stubFile).transferTo(any(File.class));
    }

    @Test
    void storeFiles_shouldThrowIOException_whenTransferFails() throws IOException {
        when(mappingFile.getOriginalFilename()).thenReturn("mapping.json");
        when(stubFile.getOriginalFilename()).thenReturn("stub.json");
        doThrow(new IOException("fail")).when(mappingFile).transferTo(any(File.class));

        try {
            wireMockFileService.storeFiles(mappingFile, stubFile);
        } catch (IOException e) {
            // expected
        }

        verify(fileUtil).createDirIfNotPresent(any(File.class), any(File.class));
        verify(mappingFile).transferTo(any(File.class));
        verify(stubFile, never()).transferTo(any(File.class));
    }
}
