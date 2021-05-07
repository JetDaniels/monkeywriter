package org.doccreator.service;

import org.doccreator.component.entity.PutDocumentsRequestDTO;

import java.util.List;

public interface PutDocumentsRequestService {
    PutDocumentsRequestDTO save(PutDocumentsRequestDTO request);
    PutDocumentsRequestDTO findById(Integer id);
    List<PutDocumentsRequestDTO> findByCdrId(Integer cdrId);
}
