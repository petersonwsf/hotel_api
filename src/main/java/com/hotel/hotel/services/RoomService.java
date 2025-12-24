package com.hotel.hotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hotel.hotel.domain.room.Room;
import com.hotel.hotel.domain.room.RoomEditDTO;
import com.hotel.hotel.domain.room.RoomRepository;
import com.hotel.hotel.domain.room.RoomSaveDTO;
import com.hotel.hotel.domain.roomTypes.RoomTypeRepository;
import com.hotel.hotel.infra.exceptions.ResourceAlreadyExists;
import com.hotel.hotel.infra.exceptions.ResourceNotFoundException;

@Service
public class RoomService {

    @Autowired
    private RoomRepository repository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;
    
    public Room create(RoomSaveDTO data) {
        
        var roomCodeAlreadyExists = repository.findByCode(data.code());

        if (roomCodeAlreadyExists.isPresent()) {
            throw new ResourceAlreadyExists("Room code already exists");
        }

        var roomType = roomTypeRepository.findById(data.roomType())
            .orElseThrow(() -> new ResourceNotFoundException("Room type not found"));
        
        var room = new Room(data, roomType);

        var newRoom = repository.save(room);
        
        return newRoom;
    }

    public Page<Room> list(Pageable pagination) {
        return repository.findAll(pagination);
    }

    public Room getDetails(Long id) {
        var room = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        
        return room;
    }

    public Room edit(RoomEditDTO data, Long id) {
        var room = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        if (data.roomType() != null) {
            var roomType = roomTypeRepository.findById(data.roomType())
                .orElseThrow(() -> new ResourceNotFoundException("Room type not found"));
            room.assignRoomType(roomType);
        }

        room.edit(data);

        return room;
    }

    public void delete(Long id) {
        var room = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        
        repository.delete(room);
    }
}
