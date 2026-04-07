package com.hotel.hotel.modules.roomTypes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hotel.hotel.infra.exceptions.ResourceNotFoundException;
import com.hotel.hotel.modules.roomTypes.dtos.RoomTypeSaveDTO;
import com.hotel.hotel.modules.roomTypes.model.RoomType;
import com.hotel.hotel.modules.roomTypes.repository.RoomTypeRepository;

@Service
public class RoomTypeService {
    
    @Autowired
    private RoomTypeRepository repository;

    public RoomType create(RoomTypeSaveDTO data) {
        RoomType roomType = new RoomType(data);
        var newRoomType = repository.save(roomType);
        return newRoomType;
    }

    public Page<RoomType> list(Pageable pagination) {
        return repository.findAll(pagination);
    }

    public RoomType getDetails(Long id) {
        var roomType = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Room type with id " + id + " does not exists"));
                    
        return roomType;
    }

    public void deleteById(Long id) {
        var roomType = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Room type with id " + id + " does not exists"));
        
        repository.delete(roomType);
    }

    public RoomType edit(RoomTypeSaveDTO data, Long id) {
        var roomType = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Room type with id " + id + " does not exists"));
        roomType.edit(data);
        return roomType;
    }
}
