package org.doccreator.endpoint;

import org.doccreator.CreateDocumentsRequest;
import org.doccreator.CreateDocumentsResponse;
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
    private Operator operator;

    @Autowired
    CreateDocumentsRequestEndpoint(CreateDocumentsRequestService createDocumentsRequestService,
                                   CreateDocumentsRequestsStepsService createDocumentsRequestsStepsService,
                                   DocumentsService documentService, CDRDocumentsService cdrDocumentsService,
                                   Operator operator){
        this.createDocumentsRequestService = createDocumentsRequestService;
        this.createDocumentsRequestsStepsService = createDocumentsRequestsStepsService;
        this.documentService = documentService;
        this.cdrDocumentsService = cdrDocumentsService;
        this.operator = operator;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createDocumentsRequest")
    @ResponsePayload
    public CreateDocumentsResponse createDocuments(@RequestPayload CreateDocumentsRequest request) throws Exception{
        CreateDocumentsResponse response = new CreateDocumentsResponse();
        response.setID(request.getId());
        response.setCode("200");
        response.setMessage("SUCCESS");

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

        operator.CreateAndPutDocuments(requestDTO);
        return response;
    }

}
