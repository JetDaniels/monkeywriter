package org.doccreator.service.impl;

import org.doccreator.component.entity.CreateDocumentsRequestDTO;
import org.doccreator.component.entity.PDRDocumentDTO;
import org.doccreator.component.entity.PutDocumentsRequestDTO;
import org.doccreator.service.*;
import org.doccreator.util.DTOGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Service
public class PutDocumentsLocalServiceImpl implements PutDocumentsService {
    private final ConnectionService connectionService;
    private final CreateDocumentsRequestsStepsService createDocumentsRequestsStepsService;
    private final PutDocumentsRequestService putDocumentsRequestService;
    private final PDRDocumentsService pdrDocumentsService;

    @Autowired
    public PutDocumentsLocalServiceImpl(ConnectionService connectionService,
                                        CreateDocumentsRequestsStepsService createDocumentsRequestsStepsService,
                                        PutDocumentsRequestService putDocumentsRequestService, PDRDocumentsService pdrDocumentsService){
        this.connectionService = connectionService;
        this.createDocumentsRequestsStepsService = createDocumentsRequestsStepsService;
        this.putDocumentsRequestService = putDocumentsRequestService;
        this.pdrDocumentsService = pdrDocumentsService;
    }

    @Override
    public void putDocuments(CreateDocumentsRequestDTO request, String source) throws Exception {
        createDocumentsRequestsStepsService.save(DTOGenerator.getCDRStepDTO(request, 4, "Put documents",
                "Start put documents"));
        List<PutDocumentsRequestDTO> pdrRequest = putDocumentsRequestService.findByCdrId(request.getId());
        List<PDRDocumentDTO> documents = pdrDocumentsService.findById_PdrId(pdrRequest.get(0).getId());

        for (PDRDocumentDTO document: documents) {
            File file = new File(document.getDocumentSource());
            putDocument(file, connectionService.findOne(request.getConnection()).getUrl());
        }

        createDocumentsRequestsStepsService.save(DTOGenerator.getCDRStepDTO(request, 5, "Put documents",
                "Put document success "));
    }

    private void putDocument(File file, String connection) throws Exception{
        File dest = new File(String.format("%s//%s", connection, file.getName()));
        Files.copy(file.toPath(), dest.toPath());
        Files.delete(file.toPath());
    }
}
