package com.hotel.hotel.modules.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditService {

    @Autowired
    private AuditRepository repository;

    // @Async usa o executor configurado em AsyncConfig
    @Async("auditExecutor")
    public void record(AuditEvent event) {
        try {
            repository.save(event);
        } catch (Exception e) {
            log.error("AUDIT_FALLBACK action={} actor={} outcome={}",
                    event.getAction(), event.getActor(), event.getOutcome(), e);
        }
    }

    // Para casos onde você precisa de diff antes/depois
    @Async("auditExecutor")
    public void recordUpdate(String action, String resourceType,
                             String resourceId, Object before, Object after) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            AuditEvent log = AuditEvent.builder()
                    .action(action)
                    .resourceType(resourceType)
                    .resourceId(resourceId)
                    .payloadBefore(mapper.writeValueAsString(before))
                    .payloadAfter(mapper.writeValueAsString(after))
                    .outcome(AuditOutcome.SUCCESS)
                    .build();
            repository.save(log);
        } catch (Exception e) {
            log.error("Falha ao registrar auditoria de update", e);
        }
    }
}
