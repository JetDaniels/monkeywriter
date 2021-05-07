package org.doccreator.repository;

import org.doccreator.component.entity.OutsideSystemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutsideSystemRepository extends JpaRepository<OutsideSystemDTO, String> {
    List<OutsideSystemDTO> findAll();
}
