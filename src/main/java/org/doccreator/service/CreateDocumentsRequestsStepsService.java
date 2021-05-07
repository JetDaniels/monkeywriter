package org.doccreator.service;

import org.doccreator.component.entity.CreateDocumentsRequestsStepsDTO;

import java.util.List;

public interface CreateDocumentsRequestsStepsService {
    List<CreateDocumentsRequestsStepsDTO> findById_CdrId(Integer id_cdrId);
    CreateDocumentsRequestsStepsDTO save(CreateDocumentsRequestsStepsDTO cdrs);
}
