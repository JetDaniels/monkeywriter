package org.doccreator.service.impl;

import org.doccreator.component.entity.UserDTO;
import org.doccreator.repository.UserRepository;
import org.doccreator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO findOne(String username) {
        return userRepository.findOne(username);
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDTO findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        return userRepository.save(userDTO);
    }
}
