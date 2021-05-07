package org.doccreator.service;

import org.doccreator.component.entity.DocumentDTO;

import java.util.List;

public interface DocumentsService {
    DocumentDTO findOne(String documentName);
    List<DocumentDTO> findAll();
    DocumentDTO save(DocumentDTO documentDTO);
}
