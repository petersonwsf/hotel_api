package com.hotel.hotel.modules.files.service;

import com.hotel.hotel.infra.exceptions.MyCustomStorageException;
import com.hotel.hotel.infra.exceptions.ResourceNotFoundException;
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

    public void uploadFile(MultipartFile fileData, String objectName, Room room, User user) {
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

    public void deleteById(Long id) {
        log.info("Start process to delete file with id: {}", id);
        File file = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File does not exists"));
        repository.deleteById(id);
        log.info("File with ID: {} successfully deleted", id);
    }

    public FileResponse findById(Long id) {
        System.out.println(id);
        File file = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File does not exists"));
        String url = getFileUrl(file.getMinioKey());
        return new FileResponse(file, url);
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
