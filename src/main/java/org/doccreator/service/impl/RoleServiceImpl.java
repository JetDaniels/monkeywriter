package org.doccreator.service.impl;

import org.doccreator.component.entity.RoleDTO;
import org.doccreator.repository.RoleRepository;
import org.doccreator.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleDTO findOne(String role) {
        return roleRepository.findOne(role);
    }

    @Override
    public List<RoleDTO> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public RoleDTO save(RoleDTO roleDTO) {
        return roleRepository.save(roleDTO);
    }

}
