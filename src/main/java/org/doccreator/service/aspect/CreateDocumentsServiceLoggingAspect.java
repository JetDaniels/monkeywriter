package org.doccreator.service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.doccreator.CreateDocumentsRequest;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@Aspect
public class CreateDocumentsServiceLoggingAspect {
    private Logger LOGGER = Logger.getLogger(CreateDocumentsServiceLoggingAspect.class.getName());


    @Before(value = "execution(* org.doccreator.service.impl.CreateDocumentsXMLServiceImpl.createDocument(*)) && args(request, template)", argNames = "jp,request,template")
    public void logBeforeCreateDocumentMethod(JoinPoint jp, CreateDocumentsRequest request, String template){
        LOGGER.info("createDocument: " + String.format("%s_%s.docx", request.getId(), template));
    }

    @After(value = "execution(* org.doccreator.service.impl.CreateDocumentsXMLServiceImpl.createDocument(*)) && args(request, template)", argNames = "jp,request,template")
    public void logAfterCreateDocumentMethod(JoinPoint jp, CreateDocumentsRequest request, String template){
        LOGGER.info("createDocument success: " + String.format("%s_%s.docx", request.getId(), template));

    }

}
