package com.hotel.hotel.modules.roomTypes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.hotel.modules.roomTypes.model.RoomType;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    Optional<RoomType> findById(Long id);
}
