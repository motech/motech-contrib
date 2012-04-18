package org.ei.commcare.listener;

import org.ei.commcare.api.domain.CommcareFormInstance;
import org.ei.commcare.listener.event.CommCareFormEvent;
import org.ei.commcare.api.service.CommCareFormImportService;
import org.motechproject.gateway.OutboundEventGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class CommCareListener {
    private final CommCareFormImportService formImportService;
    private final OutboundEventGateway outboundEventGateway;
    private static Logger logger = LoggerFactory.getLogger(CommCareListener.class.toString());
    private static final ReentrantLock lock = new ReentrantLock();

    @Autowired
    public CommCareListener(CommCareFormImportService formImportService, OutboundEventGateway outboundEventGateway) {
        this.formImportService = formImportService;
        this.outboundEventGateway = outboundEventGateway;
    }

    public void fetchFromServer() throws Exception {
        lock.lock();
        try {
            logger.info("Fetching from CommCareHQ.");
            List<CommcareFormInstance> commcareFormInstances = this.formImportService.fetchForms();

            for (CommcareFormInstance formInstance : commcareFormInstances) {
                Map<String, String> fieldsWeCareAbout = formInstance.content();
                outboundEventGateway.sendEventMessage(new CommCareFormEvent(formInstance, fieldsWeCareAbout).toMotechEvent());
            }
            logger.info("Done fetching from CommCareHQ.");
            } finally {
            lock.unlock();
        }
    }
}
