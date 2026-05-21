CREATE TABLE audit_logs (
    id            VARCHAR(36)  NOT NULL,
    action        VARCHAR(50)  NOT NULL,
    resource_type VARCHAR(100),
    resource_id   VARCHAR(100),
    actor         VARCHAR(100),
    actor_ip      VARCHAR(45),
    user_agent    VARCHAR(255),
    payload_before TEXT,
    payload_after  TEXT,
    extra_data     TEXT,
    outcome        VARCHAR(20)  NOT NULL,
    error_message  VARCHAR(500),
    created_at     TIMESTAMPTZ  NOT NULL,
    trace_id       VARCHAR(64),

    CONSTRAINT pk_audit_logs PRIMARY KEY (id),
    CONSTRAINT chk_audit_outcome CHECK (outcome IN ('SUCCESS', 'FAILURE'))
);

CREATE INDEX idx_audit_actor     ON audit_logs (actor);
CREATE INDEX idx_audit_resource  ON audit_logs (resource_type, resource_id);
CREATE INDEX idx_audit_timestamp ON audit_logs (created_at DESC);
CREATE INDEX idx_audit_action    ON audit_logs (action);
CREATE INDEX idx_audit_trace     ON audit_logs (trace_id);