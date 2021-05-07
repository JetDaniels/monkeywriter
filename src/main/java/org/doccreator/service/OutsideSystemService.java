package org.doccreator.service;

import org.doccreator.component.entity.OutsideSystemDTO;

import java.util.List;

public interface OutsideSystemService {
    List<OutsideSystemDTO> findAll();
    OutsideSystemDTO save(OutsideSystemDTO systemDTO);
}
