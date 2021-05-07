package org.doccreator.service.impl;

import org.doccreator.component.entity.CDRDocumentDTO;
import org.doccreator.repository.CDRDocumentsRepository;
import org.doccreator.service.CDRDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CDRDocumentsServiceImpl implements CDRDocumentsService {
    private final CDRDocumentsRepository cdrDocumentsRepository;

    @Autowired
    public CDRDocumentsServiceImpl(CDRDocumentsRepository cdrDocumentsRepository){
        this.cdrDocumentsRepository = cdrDocumentsRepository;
    }

    @Override
    public CDRDocumentDTO save(CDRDocumentDTO cdrDocument) {
        return cdrDocumentsRepository.save(cdrDocument);
    }

    @Override
    public List<CDRDocumentDTO> findById_CdrId(Integer id_cdrId) {
        return cdrDocumentsRepository.findById_CdrId(id_cdrId);
    }
}
