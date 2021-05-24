package org.doccreator.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.doccreator.CreateDocumentsRequest;
import org.doccreator.CreateDocumentsResponse;
import org.doccreator.component.KafkaConsumer;
import org.doccreator.component.KafkaProducer;
import org.doccreator.component.Operator;
import org.doccreator.component.entity.CDRDocumentDTO;
import org.doccreator.component.entity.CreateDocumentsRequestDTO;
import org.doccreator.component.entity.embedded.CDRDocumentId;
import org.doccreator.service.CDRDocumentsService;
import org.doccreator.service.CreateDocumentsRequestService;
import org.doccreator.service.CreateDocumentsRequestsStepsService;
import org.doccreator.service.DocumentsService;
import org.doccreator.util.DTOGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Endpoint
public class CreateDocumentsRequestEndpoint {
    private static final String NAMESPACE_URI = "http://doccreator.org";
    private final CreateDocumentsRequestService createDocumentsRequestService;
    private final CreateDocumentsRequestsStepsService createDocumentsRequestsStepsService;
    private final DocumentsService documentService;
    private final CDRDocumentsService cdrDocumentsService;
    private KafkaProducer kafkaProducer;

    @Autowired
    CreateDocumentsRequestEndpoint(CreateDocumentsRequestService createDocumentsRequestService,
                                   CreateDocumentsRequestsStepsService createDocumentsRequestsStepsService,
                                   DocumentsService documentService, CDRDocumentsService cdrDocumentsService,
                                   KafkaProducer kafkaProducer){
        this.createDocumentsRequestService = createDocumentsRequestService;
        this.createDocumentsRequestsStepsService = createDocumentsRequestsStepsService;
        this.documentService = documentService;
        this.cdrDocumentsService = cdrDocumentsService;
        this.kafkaProducer = kafkaProducer;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createDocumentsRequest")
    @ResponsePayload
    public CreateDocumentsResponse createDocuments(@RequestPayload CreateDocumentsRequest request) throws Exception{
        CreateDocumentsResponse response = new CreateDocumentsResponse();
        response.setID(request.getId());
        response.setCode("200");
        response.setMessage("SUCCESS");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        kafkaProducer.sendMessage(ow.writeValueAsString(request));
        return response;
    }

}
