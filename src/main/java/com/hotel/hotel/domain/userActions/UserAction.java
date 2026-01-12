package com.hotel.hotel.domain.userActions;

import java.time.LocalDateTime;

import com.hotel.hotel.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "user_actions")
@Entity(name = "UserAction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserAction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "action_timestamp", nullable = false)
    private LocalDateTime actionTime;
    @Enumerated(EnumType.STRING)
    private Action action;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserAction(Action action, User user) {
        this.actionTime = LocalDateTime.now();
        this.action = action;
        this.user = user;
    }
}
