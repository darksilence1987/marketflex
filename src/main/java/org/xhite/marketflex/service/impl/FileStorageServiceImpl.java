package org.xhite.marketflex.service.impl;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xhite.marketflex.exception.StorageException;
import org.xhite.marketflex.service.FileStorageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {
    private final Path uploadPath;
    private final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");
    
    public FileStorageServiceImpl(Path uploadPath) {
        this.uploadPath = uploadPath;
        log.info("FileStorageService initialized with path: {}", uploadPath);
    }
    
    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file");
            }
            
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = getFileExtension(originalFilename);
            
            if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                throw new StorageException("Cannot store file with extension " + extension + ". Allowed extensions are: " + String.join(", ", ALLOWED_EXTENSIONS));
            }
            
            String uniqueFilename = UUID.randomUUID().toString() + "." + extension;
            Path destinationFile = uploadPath.resolve(uniqueFilename);
            
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                log.info("Stored file: {} at {}", originalFilename, destinationFile);
                return uniqueFilename;
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }
    
    private String getFileExtension(String filename) {
        return Optional.ofNullable(filename)
            .filter(f -> f.contains("."))
            .map(f -> f.substring(filename.lastIndexOf(".") + 1))
            .orElse("");
    }
}
