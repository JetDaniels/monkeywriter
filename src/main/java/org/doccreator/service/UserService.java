package org.doccreator.service;

import org.doccreator.component.entity.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface UserService {
    UserDTO findOne(String username);
    List<UserDTO> findAll();
    UserDTO findByLogin(String login);
    UserDTO save(UserDTO userDTO);
}
