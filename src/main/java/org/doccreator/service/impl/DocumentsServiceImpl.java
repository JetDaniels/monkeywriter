package org.doccreator.service.impl;

import org.doccreator.component.entity.DocumentDTO;
import org.doccreator.repository.DocumentsRepository;
import org.doccreator.service.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentsServiceImpl implements DocumentsService {
    private final DocumentsRepository documentsRepository;

    @Autowired
    public DocumentsServiceImpl(DocumentsRepository documentsRepository){
        this.documentsRepository = documentsRepository;
    }

    @Override
    public DocumentDTO findOne(String documentName) {
        return documentsRepository.findOne(documentName);
    }

    @Override
    public List<DocumentDTO> findAll() {
        return documentsRepository.findAll();
    }

    @Override
    public DocumentDTO save(DocumentDTO documentDTO) {
        return documentsRepository.save(documentDTO);
    }
}
