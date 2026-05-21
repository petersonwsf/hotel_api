package com.hotel.hotel.modules.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Aspect
@Component
@Slf4j
public class AuditAspect {

    @Autowired
    private AuditService auditService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint pjp, Auditable auditable) throws Throwable {
        String traceId = MDC.get("traceId");
        Object result = null;
        String errorMsg = null;
        AuditOutcome outcome = AuditOutcome.SUCCESS;

        try {
            result = pjp.proceed(); // Executa o método original
            return result;
        } catch (Exception ex) {
            outcome = AuditOutcome.FAILURE;
            errorMsg = ex.getMessage();
            throw ex; // Re-lança — auditoria nunca engole exceções
        } finally {
            // Roda sempre, independente de sucesso ou falha
            buildAndSend(pjp, auditable, result, outcome, errorMsg, traceId);
        }
    }

    private void buildAndSend(ProceedingJoinPoint pjp, Auditable auditable,
                              Object result, AuditOutcome outcome,
                              String errorMsg, String traceId) {
        try {
            AuditEvent.AuditEventBuilder builder = AuditEvent.builder()
                    .action(auditable.action())
                    .resourceType(auditable.resourceType())
                    .outcome(outcome)
                    .errorMessage(errorMsg)
                    .traceId(traceId)
                    .actor(getCurrentUser())
                    .actorIp(getRequestIp())
                    .userAgent(getRequestUserAgent());

            if (auditable.captureArgs() && pjp.getArgs().length > 0) {
                builder.payloadAfter(toJson(sanitize(pjp.getArgs()[0])));
            }
            if (auditable.captureResult() && result != null) {
                builder.extraData(toJson(result));
            }

            auditService.record(builder.build()); // Assíncrono!

        } catch (Exception e) {
            // Nunca deixa falha de auditoria propagar
            log.error("Falha ao registrar auditoria", e);
        }
    }

    private String getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .orElse("anonymous");
    }

    private String getRequestIp() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(a -> a instanceof ServletRequestAttributes)
                .map(a -> ((ServletRequestAttributes) a).getRequest().getRemoteAddr())
                .orElse("unknown");
    }

    private String getRequestUserAgent() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(a -> a instanceof ServletRequestAttributes)
                .map(a -> ((ServletRequestAttributes) a).getRequest().getHeader("User-Agent"))
                .orElse("unknown");
    }

    private Object sanitize(Object obj) {
        // Remove campos sensíveis via reflection ou Jackson
        // Exemplo com Map:
        if (obj instanceof Map<?, ?> map) {
            var copy = new HashMap<>(map);
            copy.keySet().removeIf(k ->
                    Set.of("password", "token", "secret", "cvv")
                            .contains(k.toString().toLowerCase()));
            return copy;
        }
        return obj;
    }

    private String toJson(Object obj) {
        try { return objectMapper.writeValueAsString(obj); }
        catch (Exception e) { return obj.toString(); }
    }
}
