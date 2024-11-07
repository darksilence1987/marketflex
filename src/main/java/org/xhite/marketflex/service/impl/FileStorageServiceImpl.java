package org.xhite.marketflex.service.impl;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xhite.marketflex.exception.StorageException;
import org.xhite.marketflex.service.FileStorageService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {
    private final Path uploadPath;
    
    public FileStorageServiceImpl(Path uploadPath) {
        this.uploadPath = uploadPath;
    }
    
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file");
            }
            
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            // Generate unique filename
            String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
            
            Path destinationFile = uploadPath.resolve(uniqueFilename)
                .normalize().toAbsolutePath();
                
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            
            return uniqueFilename;
            
        } catch (IOException e) {
            throw new StorageException("Failed to store file", e);
        }
    }
}
