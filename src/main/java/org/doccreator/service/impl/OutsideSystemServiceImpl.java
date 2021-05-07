package org.doccreator.service.impl;

import org.doccreator.component.entity.OutsideSystemDTO;
import org.doccreator.repository.OutsideSystemRepository;
import org.doccreator.service.OutsideSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutsideSystemServiceImpl implements OutsideSystemService {
    private OutsideSystemRepository outsideSystemRepository;

    @Autowired
    public OutsideSystemServiceImpl(OutsideSystemRepository outsideSystemRepository){
        this.outsideSystemRepository = outsideSystemRepository;
    }

    @Override
    public List<OutsideSystemDTO> findAll() {
        return outsideSystemRepository.findAll();
    }

    @Override
    public OutsideSystemDTO save(OutsideSystemDTO systemDTO) {
        return outsideSystemRepository.save(systemDTO);
    }
}
