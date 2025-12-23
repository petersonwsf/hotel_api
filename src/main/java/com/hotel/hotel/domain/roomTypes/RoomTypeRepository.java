package com.hotel.hotel.domain.roomTypes;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    Optional<RoomType> findById(Long id);
}
