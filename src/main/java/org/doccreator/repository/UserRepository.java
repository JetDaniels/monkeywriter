package org.doccreator.repository;

import org.doccreator.component.entity.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDTO, String> {
    UserDTO findByLoginAndPassword(String login, String password);
    UserDTO findByLogin(String login);
}
