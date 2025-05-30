package com.example.wiremock.service.util;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;

import java.io.File;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileUtilTest {

    @InjectMocks
    private FileUtil fileUtil;

    @Mock
    private Logger log;

    private File mappingsDir;
    private File filesDir;

    @BeforeEach
    void setUp() {
        mappingsDir = mock(File.class);
        filesDir = mock(File.class);
    }

    @Test
    void createDirIfNotPresent_shouldCreateBothDirs() {
        when(mappingsDir.exists()).thenReturn(false);
        when(mappingsDir.mkdirs()).thenReturn(true);
        when(filesDir.exists()).thenReturn(false);
        when(filesDir.mkdirs()).thenReturn(true);

        fileUtil.createDirIfNotPresent(mappingsDir, filesDir);

        verify(mappingsDir).mkdirs();
        verify(filesDir).mkdirs();
    }

    @Test
    void createDirIfNotPresent_shouldNotCreateIfDirsExist() {
        when(mappingsDir.exists()).thenReturn(true);
        when(filesDir.exists()).thenReturn(true);

        fileUtil.createDirIfNotPresent(mappingsDir, filesDir);

        verify(mappingsDir, never()).mkdirs();
        verify(filesDir, never()).mkdirs();
    }
}