package com.hotel.hotel.domain.reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hotel.hotel.domain.client.Client;
import com.hotel.hotel.domain.room.Room;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Reservation")
@Table(name = "reservations")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;    
    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;
    @Column(name = "daily_rate", nullable = false)
    private BigDecimal dailyRate;
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;
    @Column(name = "discount_amount", nullable = false)
    private BigDecimal discountAmount;
    @Column(name = "service_fee", nullable = false)
    private BigDecimal serviceFee;
    @Column(name = "status_reservation", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "source_reservation", nullable = false)
    @Enumerated(EnumType.STRING)
    private Source source;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public Reservation(ReservationSaveDTO data) {
        this.checkInDate = data.checkInDate();
        this.checkOutDate = data.checkOutDate();
        this.dailyRate = data.dailyRate();
        this.discountAmount = data.discountAmount();
        this.serviceFee = data.serviceFee();
        this.status = data.status();
        this.source = data.source();
    }

    public void edit(ReservationEditDTO data) {
        if (data.checkInDate() != null) {
            this.checkInDate = data.checkInDate();
        }
        if (data.checkOutDate() != null) {
            this.checkOutDate = data.checkOutDate();
        }
        if (data.discountAmount() != null) {
            this.discountAmount = data.discountAmount();
        }
        if (data.serviceFee() != null) {
            this.serviceFee = data.serviceFee();
        }
        if (data.status() != null) {
            this.status = data.status();
        }
    }

    public void assignClient(Client client) {
        this.client = client;
    }

    public void assignRoom(Room room) {
        this.room = room;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void changeRoom(Room room) {
        this.room = room;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
