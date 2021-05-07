package org.doccreator.service;

import org.doccreator.component.entity.CDRDocumentDTO;

import java.util.List;

public interface CDRDocumentsService {
    CDRDocumentDTO save(CDRDocumentDTO cdrDocument);
    List<CDRDocumentDTO> findById_CdrId(Integer id_cdrId);
}
