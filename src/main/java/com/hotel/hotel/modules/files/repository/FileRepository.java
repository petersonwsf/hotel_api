package com.hotel.hotel.modules.files.repository;

import com.hotel.hotel.modules.files.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByRoomId(Long id);
    Optional<File> findByMinioKey(String minioKey);
}
