package org.doccreator.repository;

import org.doccreator.component.entity.RoleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleDTO, String> {
}
