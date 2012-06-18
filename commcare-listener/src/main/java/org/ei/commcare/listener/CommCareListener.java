package org.ei.commcare.listener;

import org.ei.commcare.api.domain.CommCareFormInstance;
import org.ei.commcare.api.service.CommCareModuleImportService;
import org.ei.commcare.listener.event.CommCareFormEvent;
import org.motechproject.scheduler.gateway.OutboundEventGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class CommCareListener {
    private final OutboundEventGateway outboundEventGateway;
    private CommCareModuleImportService moduleImportService;
    private static Logger logger = LoggerFactory.getLogger(CommCareListener.class.toString());
    private static final ReentrantLock lock = new ReentrantLock();

    @Autowired
    public CommCareListener(OutboundEventGateway outboundEventGateway, CommCareModuleImportService moduleImportService) {
        this.outboundEventGateway = outboundEventGateway;
        this.moduleImportService = moduleImportService;
    }

    public void fetchFromServer() throws Exception {
        if (!lock.tryLock()) {
            logger.warn("Not fetching from CommCareHQ. It is already in progress.");
            return;
        }
        try {
            logger.info("Fetching from CommCareHQ.");
            List<List<CommCareFormInstance>> formsForAllModules = moduleImportService.fetchFormsForAllModules();
            for (List<CommCareFormInstance> formsForOneModule : formsForAllModules) {
                outboundEventGateway.sendEventMessage(new CommCareFormEvent(formsForOneModule).toMotechEvent());
            }
            logger.info("Done fetching from CommCareHQ.");
            } finally {
            lock.unlock();
        }
    }

}
