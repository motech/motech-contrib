package org.ei.commcare.listener;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.ei.commcare.api.domain.CommCareFormInstance;
import org.ei.commcare.listener.event.CommCareFormEvent;
import org.motechproject.dao.MotechJsonReader;
import org.motechproject.model.MotechEvent;
import org.motechproject.server.event.annotations.MotechListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.ei.commcare.listener.event.CommCareFormEvent.FORM_INSTANCES_PARAMETER;

@Component
public class CommCareFormSubmissionRouter {
    private Object routeEventsHere;
    private static Logger logger = LoggerFactory.getLogger(CommCareFormSubmissionRouter.class.toString());
    private final AuditorRegistrar auditor;

    @Autowired
    public CommCareFormSubmissionRouter(AuditorRegistrar auditor) {
        this.auditor = auditor;
    }

    public void registerForDispatch(Object dispatchToMethodsInThisObject) {
        this.routeEventsHere = dispatchToMethodsInThisObject;
    }

    @MotechListener(subjects = {CommCareFormEvent.EVENT_SUBJECT})
    public void handle(MotechEvent event) throws Exception {
        if (routeEventsHere == null) {
            return;
        }

        List<CommCareFormInstance> instances = (List<CommCareFormInstance>) event.getParameters().get(FORM_INSTANCES_PARAMETER);
        Gson gson = new Gson();
        FormDispatchFailedException exception = new FormDispatchFailedException();

        for (CommCareFormInstance instance : instances) {
            String methodName = instance.formName();
            String parameterJson = gson.toJson(instance.fields());
            String formId = instance.formId();

            try {
                dispatch(formId, methodName, parameterJson);
            } catch (InvocationTargetException e) {
                exception.add(new RuntimeException("Failed during dispatch. Info: Form ID: " + formId + ", Method: " + methodName
                        + ", Parameter JSON: " + parameterJson, e.getTargetException()));
            }
        }

        if (exception.hasExceptions()) {
            throw exception;
        }
    }

    public void dispatch(String formId, String methodName, String parameterJson) throws Exception {
        Method method = findMethodWhichAcceptsOneParameter(methodName);
        if (method == null) {
            logger.warn("Cannot dispatch: Unable to find method: " + methodName + " in " + routeEventsHere.getClass());
            return;
        }

        Object parameter = getParameterFromData(method, parameterJson);
        if (parameter == null) {
            logger.warn("Cannot dispatch: Unable to convert JSON: " + parameterJson + " to object, to call method " + method);
            return;
        }

        auditor.auditFormSubmission(formId, methodName, parameterJson);
        logger.debug("Dispatching " + parameter + " to method: " + method + " in object: " + routeEventsHere);

        method.invoke(routeEventsHere, parameter);
    }

    private Method findMethodWhichAcceptsOneParameter(String methodName) {
        Method[] methods = routeEventsHere.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.getParameterTypes().length == 1) {
                return method;
            }
        }

        return null;
    }

    private Object getParameterFromData(Method method, String jsonData) {
        try {
            return new MotechJsonReader().readFromString(jsonData, method.getParameterTypes()[0]);
        } catch (JsonParseException e) {
            return null;
        }
    }
}
