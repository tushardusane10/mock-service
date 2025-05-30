package com.example.wiremock.service.controller;

import com.example.wiremock.service.execution.WireMockFileService;
import com.example.wiremock.service.execution.WiremockServiceExecution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/wiremock")
@ConditionalOnProperty(prefix = "wiremock", name = "enabled", havingValue = "true", matchIfMissing = false)
public class WireMockFileUploadController {

    private final WireMockFileService wireMockFileService;
    private final WiremockServiceExecution wiremockServiceExecution;

    public WireMockFileUploadController(WireMockFileService wireMockFileService, WiremockServiceExecution wiremockServiceExecution) {
        this.wireMockFileService = wireMockFileService;
        this.wiremockServiceExecution = wiremockServiceExecution;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFiles(
            @RequestParam("mappingFile") MultipartFile mappingFile,
            @RequestParam("responseFile") MultipartFile responseFile) throws IOException {

        if (mappingFile.getOriginalFilename() == null || responseFile.getOriginalFilename() == null) {
            return ResponseEntity.badRequest().body("Both files must have valid names.");
        }

        wireMockFileService.storeFiles(mappingFile, responseFile);
        wiremockServiceExecution.restartServer();
        return ResponseEntity.ok("Files uploaded successfully.");
    }



    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(
            @RequestParam("fileName") String fileName,
            @RequestParam(value = "type", defaultValue = "mappings") String type) {

        String dir = type.equals("__files") ? "__files" : "mappings";
        File targetFile = new File(wireMockFileService.getFilesRoot(), dir + File.separator + fileName);

        if (!targetFile.exists()) {
            log.info("File not found: {}", targetFile.getAbsolutePath());
            return ResponseEntity.notFound().build();
        }
        if (targetFile.delete()) {
            log.info("File deleted successfully: {}", targetFile.getAbsolutePath());
            wiremockServiceExecution.restartServer();
            return ResponseEntity.ok("File deleted successfully.");
        } else {
            log.info("Failed to delete file: {}", targetFile.getAbsolutePath());
            return ResponseEntity.status(500).body("Failed to delete file.");
        }
    }


}
