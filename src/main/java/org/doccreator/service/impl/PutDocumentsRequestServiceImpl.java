package org.doccreator.service.impl;

import org.doccreator.component.entity.PutDocumentsRequestDTO;
import org.doccreator.repository.PutDocumentsRequestRepository;
import org.doccreator.service.PutDocumentsRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PutDocumentsRequestServiceImpl implements PutDocumentsRequestService {
    private final PutDocumentsRequestRepository putDocumentsRequestRepository;

    @Autowired
    public PutDocumentsRequestServiceImpl(PutDocumentsRequestRepository putDocumentsRequestRepository){
        this.putDocumentsRequestRepository = putDocumentsRequestRepository;
    }

    @Override
    public PutDocumentsRequestDTO save(PutDocumentsRequestDTO request) {
        return putDocumentsRequestRepository.save(request);
    }

    @Override
    public PutDocumentsRequestDTO findById(Integer id) {
        return putDocumentsRequestRepository.findById(id);
    }

    @Override
    public List<PutDocumentsRequestDTO> findByCdrId(Integer cdrId) {
        return putDocumentsRequestRepository.findByCdrId(cdrId);
    }
}
