package org.doccreator.endpoint.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.doccreator.CreateDocumentsRequest;
import org.doccreator.CreateDocumentsResponse;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;


@Component
@Aspect
public class CreateDocumentsRequestEndpointLoggingAspect {
    private Logger LOGGER = Logger.getLogger(CreateDocumentsRequestEndpointLoggingAspect.class.getName());

    @Before(value = "execution(* org.doccreator.endpoint.CreateDocumentsRequestEndpoint.createDocuments(*)) && args(request)")
    public void logBeforeCreateDocumentMethod(JoinPoint jp, CreateDocumentsRequest request){
        LOGGER.info("createDocuments request: " + request.getXmlData());
    }

    @AfterReturning(
            pointcut = "execution(* org.doccreator.endpoint.CreateDocumentsRequestEndpoint.createDocuments(*))",
            returning = "response")
    public void logAfterReturningCreateDocumentMethod(JoinPoint jp, CreateDocumentsResponse response){
        LOGGER.info("createDocuments return: " + response.getMessage());
    }
}
