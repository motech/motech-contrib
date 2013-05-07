package org.motechproject.calllog.repository;


import org.motechproject.calllog.domain.CallLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenericCallLogRepository extends JpaRepository<CallLog, Long> {

}
