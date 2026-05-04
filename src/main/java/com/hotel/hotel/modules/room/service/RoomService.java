package com.hotel.hotel.modules.room.service;

import com.hotel.hotel.modules.files.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hotel.hotel.infra.exceptions.ResourceAlreadyExists;
import com.hotel.hotel.infra.exceptions.ResourceNotFoundException;
import com.hotel.hotel.modules.room.dtos.RoomEditDTO;
import com.hotel.hotel.modules.room.dtos.RoomFilters;
import com.hotel.hotel.modules.room.dtos.RoomSaveDTO;
import com.hotel.hotel.modules.room.model.Room;
import com.hotel.hotel.modules.room.model.StatusRoom;
import com.hotel.hotel.modules.room.repository.RoomRepository;
import com.hotel.hotel.modules.room.repository.specs.RoomSpecification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class RoomService {

    @Autowired
    private RoomRepository repository;
    @Autowired
    private FileService fileService;
    
    public Room create(RoomSaveDTO data, List<MultipartFile> files) {
        log.info("Starting process to create room");
        var roomCodeAlreadyExists = repository.findByCode(data.code());

        if (roomCodeAlreadyExists.isPresent()) {
            throw new ResourceAlreadyExists("Room code already exists");
        }

        var room = new Room(data);

        var newRoom = repository.save(room);
        log.info("Room successfully created in the database");

        for (MultipartFile file : files) {
            String minioKey = UUID.randomUUID().toString();
            fileService.uploadFile(file, minioKey, newRoom, null);
        }

        return newRoom;
    }

    public Page<Room> list(RoomFilters filters, Pageable pagination) {
        log.info("Starting listing rooms");
        Specification<Room> filter = (root, query, criteriaBuilder) -> null;

        filter = filter.and(RoomSpecification.codeEqual(filters.code()))
                .and(RoomSpecification.roomAvailableOn(filters.checkInDate(), filters.checkOutDate()))
                .and(RoomSpecification.activeEqual(filters.active()))
                .and(RoomSpecification.floorEqual(filters.floor()))
                .and(RoomSpecification.statusEqual(filters.status()))
                .and(RoomSpecification.priceBetween(filters.minPrice(), filters.maxPrice()))
                .and(RoomSpecification.capacityGreaterThan(filters.capacity()))
                .and(RoomSpecification.categoryEquals(filters.category()));
        log.info("Returning rooms list");
        return repository.findAll(filter, pagination);
    }

    public Room getDetails(Long id) {
        log.info("Starting process to retrieve details of the room with ID: {}", id);
        var room = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        log.info("Returning details of the room with ID: {}", id);
        return room;
    }

    public Room edit(RoomEditDTO data, Long id) {
        log.info("Starting process to edit room with ID: {}", id);
        var room = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        room.edit(data);
        log.info("Room with ID: {} successfully edited", id);
        return room;
    }

    public void finishCleaning(Long id) {
        log.info("Starting process to clean room with ID: {}", id);
        var room = getDetails(id);
        room.changeStatus(StatusRoom.AVAILABLE);
        log.info("Room with ID: {} successfully cleaned", id);
    }

    public void delete(Long id) {
        log.info("Starting process to delete room with ID: {}", id);
        var room = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        repository.delete(room);
        log.info("Room with ID: {} successfully deleted", id);
    }
}
