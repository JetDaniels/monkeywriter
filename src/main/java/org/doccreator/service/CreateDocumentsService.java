package org.doccreator.service;

import org.doccreator.component.entity.CreateDocumentsRequestDTO;
import org.doccreator.component.entity.DocumentDTO;

import java.util.List;

public interface CreateDocumentsService {
    void createDocuments(CreateDocumentsRequestDTO request) throws Exception;
    String createDocument(CreateDocumentsRequestDTO request, DocumentDTO document) throws Exception;
    void setBufferDir(String bufferDir);
    String getBufferDir();
}
