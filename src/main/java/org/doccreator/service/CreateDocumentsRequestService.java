package org.doccreator.service;

import org.doccreator.component.entity.CreateDocumentsRequestDTO;
import org.doccreator.component.entity.CreateDocumentsRequestLabelDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CreateDocumentsRequestService {
    CreateDocumentsRequestDTO save(CreateDocumentsRequestDTO createDocumentsRequest);
    Page<CreateDocumentsRequestDTO> findAll(Pageable pageable);
    Page<CreateDocumentsRequestLabelDTO> findAllLabels(Pageable pageable);
    Long getCountAll();
    CreateDocumentsRequestDTO findById(Integer id);
}
