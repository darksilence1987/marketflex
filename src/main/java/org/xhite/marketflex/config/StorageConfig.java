package org.xhite.marketflex.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xhite.marketflex.exception.StorageException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StorageConfig {
    @Value("${app.upload.dir:${user.home}/marketflex/uploads}")
    private String uploadDir;
    
    @Bean
    public Path uploadPath() {
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new StorageException("Could not initialize storage", e);
            }
        }
        return path;
    }
}