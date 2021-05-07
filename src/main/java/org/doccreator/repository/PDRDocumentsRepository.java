package org.doccreator.repository;

import org.doccreator.component.entity.PDRDocumentDTO;
import org.doccreator.component.entity.embedded.PDRDocumentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDRDocumentsRepository extends JpaRepository<PDRDocumentDTO, PDRDocumentId> {
    List<PDRDocumentDTO> findById_PdrId(Integer id_pdrId);
    List<PDRDocumentDTO> findById_DocumentName(String documentName);
}
