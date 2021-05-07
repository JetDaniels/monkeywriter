package org.doccreator.util;

import org.doccreator.CreateDocumentsRequest;
import org.doccreator.component.entity.CreateDocumentsRequestDTO;
import org.doccreator.component.entity.CreateDocumentsRequestsStepsDTO;
import org.doccreator.component.entity.PDRDocumentDTO;
import org.doccreator.component.entity.PutDocumentsRequestDTO;
import org.doccreator.component.entity.embedded.CreateDocumentsRequestsStepsId;
import org.doccreator.component.entity.embedded.PDRDocumentId;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DTOGenerator {

    public static PDRDocumentDTO getPDRDocumentDTO(PutDocumentsRequestDTO pdRequest, String documentName, String fileLink){
        PDRDocumentDTO pdrDoc = new PDRDocumentDTO();
        PDRDocumentId pdrDocId = new PDRDocumentId();
        pdrDocId.setDocumentName(documentName);
        pdrDocId.setPdrId(pdRequest.getId());
        pdrDoc.setId(pdrDocId);
        pdrDoc.setDocumentSource(fileLink);
        return pdrDoc;
    }

    public static PutDocumentsRequestDTO getPDRDTO(CreateDocumentsRequestDTO request){
        PutDocumentsRequestDTO pdr = new PutDocumentsRequestDTO();
        pdr.setXmlData(" ");
        pdr.setCdrId(request.getId());
        pdr.setTimeBegin(Timestamp.valueOf(LocalDateTime.now()));
        return pdr;
    }

    public static CreateDocumentsRequestDTO getCDRDTO(CreateDocumentsRequest request){
        CreateDocumentsRequestDTO requestDTO = new CreateDocumentsRequestDTO();
        requestDTO.setRequestId(request.getId());
        requestDTO.setOutsideSystem(request.getSystem());
        requestDTO.setConnection(request.getConnection());
        requestDTO.setDocsCount(request.getDocument().size());
        requestDTO.setXmlData(request.getXmlData());
        requestDTO.setTimeBegin(Timestamp.valueOf(LocalDateTime.now()));
        return requestDTO;
    }

    public static CreateDocumentsRequestsStepsDTO getCDRStepDTO(
            CreateDocumentsRequestDTO requestDTO, Integer step, String stage, String message){
        CreateDocumentsRequestsStepsDTO stepDTO = new CreateDocumentsRequestsStepsDTO();
        CreateDocumentsRequestsStepsId id = new CreateDocumentsRequestsStepsId();
        id.setCdrId(requestDTO.getId());
        id.setStep(step);
        stepDTO.setId(id);
        stepDTO.setStage(stage);
        stepDTO.setMessage(message);
        stepDTO.setTimeBegin(Timestamp.valueOf(LocalDateTime.now()));
        return stepDTO;
    }
}
