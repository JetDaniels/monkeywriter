package org.doccreator.repository;

import org.doccreator.component.entity.PutDocumentsRequestDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PutDocumentsRequestRepository extends JpaRepository<PutDocumentsRequestDTO, Integer> {
    PutDocumentsRequestDTO findById(Integer id);
    List<PutDocumentsRequestDTO> findByCdrId(Integer cdrId);
}
