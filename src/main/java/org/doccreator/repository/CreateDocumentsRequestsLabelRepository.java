package org.doccreator.repository;

import org.doccreator.component.entity.CreateDocumentsRequestLabelDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateDocumentsRequestsLabelRepository extends JpaRepository<CreateDocumentsRequestLabelDTO, Integer> {
}
