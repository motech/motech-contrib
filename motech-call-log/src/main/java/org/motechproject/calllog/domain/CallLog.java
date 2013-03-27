package org.motechproject.calllog.domain;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.motechproject.calllog.repository.HstoreUserType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Map;

@Data
@Entity
@Table(name = "call_log", uniqueConstraints = {@UniqueConstraint(columnNames = {"call_log_id"})})
@TypeDef(name = "hstore", typeClass = HstoreUserType.class)
public class CallLog {

    @Id
    @Column(name = "call_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "call_id")
    private String callId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "start_date_time")
    private Timestamp startDateTime;

    @Column(name = "end_date_time")
    private Timestamp endDateTime;

    @Column(name = "disposition")
    private String disposition;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "call_type")
    private String callType;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "attempt_date_time")
    private Timestamp attemptTime;

    @Column(name = "attempt_number")
    private String attempt;

    @Type(type = "hstore")
    @Column(columnDefinition = "hstore", name = "call_events")
    private Map<String, String> callEvents;

    @Type(type = "hstore")
    @Column(columnDefinition = "hstore", name = "custom_data")
    private Map<String, String> customData;
}
