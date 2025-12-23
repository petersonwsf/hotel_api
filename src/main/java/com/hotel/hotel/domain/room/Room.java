package com.hotel.hotel.domain.room;

import java.math.BigDecimal;

import com.hotel.hotel.domain.roomTypes.RoomType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "room")
@Entity(name = "Room")
public class Room {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String code;
    private String floor;
    private BigDecimal customPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private Boolean active;

    @Enumerated(EnumType.STRING)
    private StatusRoom status;

    public Room(RoomSaveDTO data, RoomType roomType) {
        this.code = data.code();
        this.floor = data.floor();
        this.customPrice = data.customPrice();
        this.status = data.status();
        this.roomType = roomType;
        this.active = true;
    }

    public void edit(RoomEditDTO data) {
        if (data.customPrice() != null) {
            this.customPrice = data.customPrice();
        }
        if (data.status() != null) {
            this.status = data.status();
        }
    }

    public void assignRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
}
