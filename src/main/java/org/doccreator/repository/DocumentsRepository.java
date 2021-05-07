package org.doccreator.repository;

import org.doccreator.component.entity.DocumentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsRepository extends JpaRepository<DocumentDTO, String> {
}
