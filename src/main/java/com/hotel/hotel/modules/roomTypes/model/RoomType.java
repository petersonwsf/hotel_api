package com.hotel.hotel.modules.roomTypes.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hotel.hotel.modules.room.model.Room;
import com.hotel.hotel.modules.roomTypes.dtos.RoomTypeSaveDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "RoomType")
@Table(name = "room_type")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RoomType {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer capacity;
    private BigDecimal basePrice;
    private String bedConfig;
    private String amenities;

    @OneToMany(mappedBy = "roomType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Room> rooms;

    @Enumerated(EnumType.STRING)
    private Category category;

    public RoomType(RoomTypeSaveDTO data) {
        this.name = data.name();
        this.capacity = data.capacity();
        this.basePrice = data.basePrice();
        this.bedConfig = data.bedConfig();
        this.amenities = data.amenities();
        this.category = data.category();
    }

    public void edit(RoomTypeSaveDTO data) {
        if (data.name() != null) {
            this.name = data.name();
        }
        if (data.capacity() != null) {
            this.capacity = data.capacity();
        }
        if (data.basePrice() != null) {
            this.basePrice = data.basePrice();
        }
        if (data.bedConfig() != null) {
            this.bedConfig = data.bedConfig();
        }
        if (data.amenities() != null) {
            this.amenities = data.amenities();
        }
        if (data.category() != null) {
            this.category = data.category();
        }
    }
}
