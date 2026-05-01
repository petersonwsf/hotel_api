package com.hotel.hotel.modules.files.model;

import com.hotel.hotel.modules.room.model.Room;
import com.hotel.hotel.modules.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Entity(name = "File")
@Table(name = "hotel_files")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "minio_key")
    private String minioKey;
    @Column(name = "original_name")
    private String originalName;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "file_size")
    private Long fileSize;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "hotel_id")
    private Long hotelId;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public File(String minioKey, MultipartFile file, Long hotelId, Room room, User user) {
        this.minioKey = minioKey;
        this.originalName = file.getOriginalFilename();
        this.contentType = file.getContentType();
        this.fileSize = file.getSize();
        this.hotelId = hotelId;
        this.room = room;
        this.user = user;
    }
}
