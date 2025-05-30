package com.example.wiremock.service.controller;

import com.example.wiremock.service.execution.WireMockFileService;
import com.example.wiremock.service.execution.WiremockServiceExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WireMockFileUploadControllerTest {

    @Mock
    private WireMockFileService wireMockFileService;

    @Mock
    private WiremockServiceExecution wiremockServiceExecution;

    @Mock
    private MultipartFile mappingFile;

    @Mock
    private MultipartFile stubFile;

    @InjectMocks
    private WireMockFileUploadController controller;

    @BeforeEach
    void setUp() {
        controller = new WireMockFileUploadController(wireMockFileService, wiremockServiceExecution);
    }

    @Test
    void uploadFiles_shouldReturnOk_whenFilesAreValid() throws IOException {
        when(mappingFile.getOriginalFilename()).thenReturn("mapping.json");
        when(stubFile.getOriginalFilename()).thenReturn("stub.json");

        ResponseEntity<String> response = controller.uploadFiles(mappingFile, stubFile);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Files uploaded successfully.", response.getBody());
        verify(wireMockFileService).storeFiles(mappingFile, stubFile);
        verify(wiremockServiceExecution).restartServer();
    }

    @Test
    void uploadFiles_shouldReturnBadRequest_whenFileNamesAreNull() throws IOException {

        ResponseEntity<String> response = controller.uploadFiles(mappingFile, stubFile);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Both files must have valid names.", response.getBody());
        verify(wireMockFileService, never()).storeFiles(any(), any());
        verify(wiremockServiceExecution, never()).restartServer();
    }

    @Test
    void deleteFile_shouldReturnNotFound_whenFileDoesNotExist() {
        String fileName = "notfound.json";
        String type = "mappings";
        File root = new File("root");
        when(wireMockFileService.getFilesRoot()).thenReturn(root.getPath());

        ResponseEntity<String> response = controller.deleteFile(fileName, type);

        assertEquals(404, response.getStatusCodeValue());
    }

//    @Test
//    void deleteFile_shouldReturnOk_whenFileDeleted() throws IOException {
//        String fileName = "found.json";
//        String type = "mappings";
//        String rootPath = "root";
//        File file = mock(File.class);
//
//        when(wireMockFileService.getFilesRoot()).thenReturn(rootPath);
//        when(file.exists()).thenReturn(true);
//        when(file.delete()).thenReturn(true);
//
//        // Simulate new File(root, ...) returns our mock file
//        try (var mocked = mockStatic(File.class)) {
//
//            Mockito.mockConstruction(File.class);
//            ResponseEntity<String> response = controller.deleteFile(fileName, type);
//
//            assertEquals(200, response.getStatusCodeValue());
//            assertEquals("File deleted successfully.", response.getBody());
//            verify(wiremockServiceExecution).restartServer();
//        }
//    }
}