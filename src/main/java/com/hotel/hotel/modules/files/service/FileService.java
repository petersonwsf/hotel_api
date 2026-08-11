package com.hotel.hotel.modules.files.service;

import com.hotel.hotel.infra.exceptions.MyCustomStorageException;
import com.hotel.hotel.infra.exceptions.ResourceNotFoundException;
import com.hotel.hotel.modules.audit.Auditable;
import com.hotel.hotel.modules.files.dto.FileResponse;
import com.hotel.hotel.modules.files.model.File;
import com.hotel.hotel.modules.files.repository.FileRepository;
import com.hotel.hotel.modules.room.model.Room;
import com.hotel.hotel.modules.user.model.User;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FileService {

    @Autowired
    private FileRepository repository;

    @Autowired
    private MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucketName;

    @Auditable(action = "FILE_UPDATE", resourceType = "FILE")
    public FileResponse uploadFile(MultipartFile fileData, String objectName, Room room, User user) {
        try {
            log.info("Start process to upload file: {}", fileData.getOriginalFilename());
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(fileData.getInputStream(), fileData.getSize(), -1)
                            .contentType(fileData.getContentType())
                            .build()
            );

            File file = new File(objectName, fileData, null, room, user);
            repository.save(file);
            log.info("File {} successfully created", fileData.getOriginalFilename());
            return new FileResponse(file, getFileUrl(objectName));
        } catch (ErrorResponseException e) {
            throw new MyCustomStorageException(e.getMessage());
        } catch (IOException e) {
            throw new MyCustomStorageException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during upload", e);
        }
    }

    public List<FileResponse> listImagesByRoom(Long id) {
        log.info("Start process to list images of room with id: {}", id);
        List<File> listImages = repository.findByRoomId(id);
        List<FileResponse> images = listImages.stream().map(file -> {
           String url = getFileUrl(file.getMinioKey());
           return new FileResponse(file, url);
        }).toList();
        log.info("Returning list of images from room with ID: {}", id);
        return images;
    }

    public void deleteFromMinio(String objectName) {
        try {
            log.info("Start process to delete object from MinIO: {}", objectName);
            
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            
            log.info("Object {} successfully deleted from MinIO", objectName);
        } catch (ErrorResponseException e) {
            log.error("MinIO error trying to delete object: {}", objectName, e);
            throw new MyCustomStorageException(e.getMessage());
        } catch (IOException e) {
            log.error("I/O error trying to delete object from MinIO: {}", objectName, e);
            throw new MyCustomStorageException(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error deleting object from MinIO: {}", objectName, e);
            throw new RuntimeException("Unexpected error during MinIO deletion", e);
        }
    }

    @Auditable(action = "FILE_DELETE", resourceType = "FILE")
    public void deleteById(Long id) {
        log.info("Start process to delete file with id: {}", id);
        File file = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File does not exists"));
        repository.delete(file);
        log.info("File with ID: {} successfully deleted", id);
    }

    public FileResponse findById(Long id) {
        System.out.println(id);
        File file = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File does not exists"));
        String url = getFileUrl(file.getMinioKey());
        return new FileResponse(file, url);
    }

    public FileResponse findByMinioKey(String minioKey) {
        Optional<File> file = repository.findByMinioKey(minioKey);
        if (file.isPresent()) {
            String url = getFileUrl(file.get().getMinioKey());
            return new FileResponse(file.get(), url);
        }
        return null;
    }

    private String getFileUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(2, TimeUnit.HOURS)
                            .build()
            );
        } catch (ErrorResponseException e) {
            throw new MyCustomStorageException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during upload", e);
        }
    }

}
