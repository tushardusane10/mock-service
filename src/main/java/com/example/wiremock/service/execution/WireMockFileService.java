package com.example.wiremock.service.execution;

import com.example.wiremock.service.util.FileUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "wiremock", name = "enabled", havingValue = "true", matchIfMissing = false)
public class WireMockFileService {

    @Getter
    @Value("${wiremock.server.file-root}")
    private String filesRoot;

    private final FileUtil fileUtil;

    public WireMockFileService(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public void storeFiles(MultipartFile mappingFile, MultipartFile responseFile) throws IOException {
        File mappingsDir = new File( filesRoot, "mappings");
        File filesDir = new File(filesRoot, "__files");

        fileUtil.createDirIfNotPresent(mappingsDir, filesDir);

        File mappingDest = new File(mappingsDir, mappingFile.getOriginalFilename());
        File responseDest = new File(filesDir, responseFile.getOriginalFilename());

        mappingFile.transferTo(mappingDest);
        responseFile.transferTo(responseDest);
    }


}
