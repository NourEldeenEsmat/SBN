package com.nouresmat.book.file;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileUtils {
    public static byte[] getFileFromLocation(String bookCoverUrl) {
        if (StringUtils.isBlank(bookCoverUrl)) {
            return null;
        }

        try {
            Path path = new File(bookCoverUrl).toPath();
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("no file found in the path {}", bookCoverUrl);
        }
        return null;
    }
}
