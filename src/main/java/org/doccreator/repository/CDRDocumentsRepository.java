package org.doccreator.repository;

import org.doccreator.component.entity.CDRDocumentDTO;
import org.doccreator.component.entity.embedded.CDRDocumentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CDRDocumentsRepository extends JpaRepository<CDRDocumentDTO, CDRDocumentId> {
        List<CDRDocumentDTO> findById_CdrId(Integer id_cdrId);
}
