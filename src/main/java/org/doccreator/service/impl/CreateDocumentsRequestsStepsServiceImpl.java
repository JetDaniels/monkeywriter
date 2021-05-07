package org.doccreator.service.impl;

import org.doccreator.component.entity.CreateDocumentsRequestsStepsDTO;
import org.doccreator.repository.CreateDocumentsRequestsStepsRepository;
import org.doccreator.service.CreateDocumentsRequestsStepsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateDocumentsRequestsStepsServiceImpl implements CreateDocumentsRequestsStepsService {
    CreateDocumentsRequestsStepsRepository createDocumentsRequestsStepsRepository;

    @Autowired
    CreateDocumentsRequestsStepsServiceImpl(CreateDocumentsRequestsStepsRepository createDocumentsRequestsStepsRepository){
        this.createDocumentsRequestsStepsRepository = createDocumentsRequestsStepsRepository;
    }

    @Override
    public List<CreateDocumentsRequestsStepsDTO> findById_CdrId(Integer id_cdrId) {
        return createDocumentsRequestsStepsRepository.findById_CdrId(id_cdrId);
    }

    @Override
    public CreateDocumentsRequestsStepsDTO save(CreateDocumentsRequestsStepsDTO cdrs) {
        return createDocumentsRequestsStepsRepository.save(cdrs);
    }
}
