package org.doccreator.service;

import org.doccreator.CreateDocumentsRequest;
import org.doccreator.component.entity.CreateDocumentsRequestDTO;

public interface PutDocumentsService{
    void putDocuments(CreateDocumentsRequestDTO request, String source) throws Exception;
}
