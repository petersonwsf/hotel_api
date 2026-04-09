package com.hotel.hotel.modules.roomTypes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hotel.hotel.infra.exceptions.ResourceNotFoundException;
import com.hotel.hotel.modules.roomTypes.dtos.RoomTypeSaveDTO;
import com.hotel.hotel.modules.roomTypes.model.RoomType;
import com.hotel.hotel.modules.roomTypes.repository.RoomTypeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoomTypeService {
    
    @Autowired
    private RoomTypeRepository repository;

    public RoomType create(RoomTypeSaveDTO data) {
        log.info("Starting process to create room type");
        RoomType roomType = new RoomType(data);
        var newRoomType = repository.save(roomType);
        log.info("Room type successfully created in database");
        return newRoomType;
    }

    public Page<RoomType> list(Pageable pagination) {
        log.info("Returning a room type list");
        return repository.findAll(pagination);
    }

    public RoomType getDetails(Long id) {
        log.info("Starting process to retrieve room type details with ID: {}", id);
        var roomType = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Room type with id " + id + " does not exists"));
        log.info("Returning room type details with ID: {}", id);
        return roomType;
    }

    public void deleteById(Long id) {
        log.info("Starting process to edit room type with ID: {}", id);
        var roomType = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Room type with id " + id + " does not exists"));
        repository.delete(roomType);
        log.info("Room type with ID: {} successfully deleted ", id);
    }

    public RoomType edit(RoomTypeSaveDTO data, Long id) {
        log.info("Starting process to edit room type with ID: {}", id);
        var roomType = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Room type with id " + id + " does not exists"));
        roomType.edit(data);
        log.info("Room type with ID: {} successfully edited", id);
        return roomType;
    }
}
