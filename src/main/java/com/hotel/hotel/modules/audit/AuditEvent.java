package com.hotel.hotel.modules.audit;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity(name = "Audit")
@Table(name = "audit_logs")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false, length = 50)
    private String action;
    @Column(name = "resource_type", length = 50)
    private String resourceType;
    @Column(name = "resource_id", length = 50)
    private String resourceId;
    @Column(length = 100)
    private String actor;
    @Column(name = "actor_ip", length = 45)
    private String actorIp;
    @Column(name = "user_agent", length = 255)
    private String userAgent;
    @Column(name = "payload_before", columnDefinition = "TEXT")
    private String payloadBefore;
    @Column(name = "payload_after", columnDefinition = "TEXT")
    private String payloadAfter;
    @Column(name = "extra_data", columnDefinition = "TEXT")
    private String extraData;
    @Enumerated(EnumType.STRING)
    private AuditOutcome outcome;
    @Column(name = "error_message", length = 500)
    private String errorMessage;
    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
    @Column(name = "trace_id", length = 64)
    private String traceId;

}
