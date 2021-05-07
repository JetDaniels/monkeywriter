package org.doccreator.repository;

import org.doccreator.component.entity.CreateDocumentsRequestsStepsDTO;
import org.doccreator.component.entity.embedded.CreateDocumentsRequestsStepsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreateDocumentsRequestsStepsRepository extends JpaRepository<CreateDocumentsRequestsStepsDTO, CreateDocumentsRequestsStepsId> {
    List<CreateDocumentsRequestsStepsDTO> findById_CdrId(Integer id_cdrId);

}
