package org.doccreator.repository;

import org.doccreator.component.entity.CreateDocumentsRequestDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateDocumentsRequestRepository extends JpaRepository<CreateDocumentsRequestDTO, Integer> {
    CreateDocumentsRequestDTO findById(Integer id);

    @Query("select count(c.id) from CreateDocumentsRequestDTO c")
    Long getCountAll();
}
