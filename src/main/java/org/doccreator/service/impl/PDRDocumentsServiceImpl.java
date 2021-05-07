package org.doccreator.service.impl;

import org.doccreator.component.entity.PDRDocumentDTO;
import org.doccreator.repository.PDRDocumentsRepository;
import org.doccreator.service.PDRDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PDRDocumentsServiceImpl implements PDRDocumentsService {
    private PDRDocumentsRepository pdrDocumentsRepository;

    @Autowired
    public PDRDocumentsServiceImpl(PDRDocumentsRepository pdrDocumentsRepository){
        this.pdrDocumentsRepository = pdrDocumentsRepository;
    }

    @Override
    public PDRDocumentDTO save(PDRDocumentDTO pdrDocument) {
        return pdrDocumentsRepository.save(pdrDocument);
    }

    @Override
    public List<PDRDocumentDTO> findById_PdrId(Integer id_pdrId) {
        return pdrDocumentsRepository.findById_PdrId(id_pdrId);
    }

    @Override
    public List<PDRDocumentDTO> findById_DocumentName(String documentName) {
        return pdrDocumentsRepository.findById_DocumentName(documentName);
    }
}
