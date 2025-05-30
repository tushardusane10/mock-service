package com.example.wiremock.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class FileUtil {

    public void createDirIfNotPresent(File mappingsDir, File filesDir) {
        if (!mappingsDir.exists()) {
            boolean isCreated = mappingsDir.mkdirs();
            log.info("Created mappings dir: {}", isCreated);
        }
        if (!filesDir.exists()) {
            boolean isCreated = filesDir.mkdirs();
            log.info("Created files dir: {}", isCreated);
        }
    }
}
