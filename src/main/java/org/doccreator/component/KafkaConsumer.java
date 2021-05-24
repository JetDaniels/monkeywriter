package org.doccreator.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.doccreator.CreateDocumentsRequest;
import org.doccreator.component.entity.CDRDocumentDTO;
import org.doccreator.component.entity.CreateDocumentsRequestDTO;
import org.doccreator.component.entity.embedded.CDRDocumentId;
import org.doccreator.service.CDRDocumentsService;
import org.doccreator.service.CreateDocumentsRequestService;
import org.doccreator.service.CreateDocumentsRequestsStepsService;
import org.doccreator.service.DocumentsService;
import org.doccreator.util.DTOGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class KafkaConsumer {
    private final CreateDocumentsRequestService createDocumentsRequestService;
    private final CreateDocumentsRequestsStepsService createDocumentsRequestsStepsService;
    private final DocumentsService documentService;
    private final CDRDocumentsService cdrDocumentsService;
    private Operator operator;

    @Autowired
    public KafkaConsumer(CreateDocumentsRequestService createDocumentsRequestService,
                         CreateDocumentsRequestsStepsService createDocumentsRequestsStepsService,
                         DocumentsService documentService, CDRDocumentsService cdrDocumentsService,
                         Operator operator){
        this.createDocumentsRequestService = createDocumentsRequestService;
        this.createDocumentsRequestsStepsService = createDocumentsRequestsStepsService;
        this.documentService = documentService;
        this.cdrDocumentsService = cdrDocumentsService;
        this.operator = operator;
    }

    @KafkaListener(topics = "test_in", group = "test-consumer-group")
    public void listen(String message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CreateDocumentsRequest request = mapper.readValue(message, CreateDocumentsRequest.class);
        CreateDocumentsRequestDTO requestDTO = createDocumentsRequestService.save(DTOGenerator.getCDRDTO(request));
        createDocumentsRequestsStepsService.save(DTOGenerator.getCDRStepDTO(requestDTO, 0, "Request received", "Request received"));
        for(String document: request.getDocument()){
            CDRDocumentDTO cdrDocumentDTO = new CDRDocumentDTO();
            CDRDocumentId documentId = new CDRDocumentId();
            documentId.setCdrId(requestDTO.getId());
            documentId.setDocumentName(document);
            cdrDocumentDTO.setId(documentId);
            cdrDocumentsService.save(cdrDocumentDTO);
        }
        System.out.println(String.format("Received message from kafka: %s", request.getId()));
        operator.CreateAndPutDocuments(requestDTO);
    }
}
