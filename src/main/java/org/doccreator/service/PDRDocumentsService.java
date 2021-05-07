package org.doccreator.service;

import org.doccreator.component.entity.PDRDocumentDTO;

import java.util.List;

public interface PDRDocumentsService {
    PDRDocumentDTO save(PDRDocumentDTO pdrDocument);
    List<PDRDocumentDTO> findById_PdrId(Integer id_pdrId);
    List<PDRDocumentDTO> findById_DocumentName(String documentName);
}
