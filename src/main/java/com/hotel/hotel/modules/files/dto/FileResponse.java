package com.hotel.hotel.modules.files.dto;

import com.hotel.hotel.modules.files.model.File;

import java.time.LocalDateTime;

public record FileResponse(Long id, String url, String originalName, Long fileSize, String contentType, LocalDateTime createdAt) {
    public FileResponse(File file, String url) {
        this(file.getId(), url, file.getOriginalName(), file.getFileSize(), file.getContentType(), file.getCreatedAt());
    }
}
