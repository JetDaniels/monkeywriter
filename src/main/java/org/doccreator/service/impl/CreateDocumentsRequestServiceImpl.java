package org.doccreator.service.impl;

import org.doccreator.component.entity.CreateDocumentsRequestDTO;
import org.doccreator.component.entity.CreateDocumentsRequestLabelDTO;
import org.doccreator.repository.CreateDocumentsRequestRepository;
import org.doccreator.repository.CreateDocumentsRequestsLabelRepository;
import org.doccreator.service.CreateDocumentsRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CreateDocumentsRequestServiceImpl implements CreateDocumentsRequestService {

    private final CreateDocumentsRequestRepository createDocumentsRequestRepository;
    private final CreateDocumentsRequestsLabelRepository createDocumentsRequestsLabelRepository;

    @Autowired
    CreateDocumentsRequestServiceImpl(CreateDocumentsRequestRepository createDocumentsRequestRepository,
                                      CreateDocumentsRequestsLabelRepository createDocumentsRequestsLabelRepository){
        this.createDocumentsRequestRepository = createDocumentsRequestRepository;
        this.createDocumentsRequestsLabelRepository = createDocumentsRequestsLabelRepository;
    }

    @Override
    public CreateDocumentsRequestDTO save(CreateDocumentsRequestDTO createDocumentsRequest) {
        return createDocumentsRequestRepository.save(createDocumentsRequest);
    }

    @Override
    public Page<CreateDocumentsRequestDTO> findAll(Pageable pageable) {
        return createDocumentsRequestRepository.findAll(pageable);
    }

    @Override
    public Page<CreateDocumentsRequestLabelDTO> findAllLabels(Pageable pageable) {
        return createDocumentsRequestsLabelRepository.findAll(pageable);
    }

    @Override
    public Long getCountAll() {
        return createDocumentsRequestRepository.getCountAll();
    }

    @Override
    public CreateDocumentsRequestDTO findById(Integer id) {
        return createDocumentsRequestRepository.findById(id);
    }
}
