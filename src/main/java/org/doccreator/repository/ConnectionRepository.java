package org.doccreator.repository;

import org.doccreator.component.entity.ConnectionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends JpaRepository<ConnectionDTO, String> {
}
