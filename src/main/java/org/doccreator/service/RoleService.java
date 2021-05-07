package org.doccreator.service;

import org.doccreator.component.entity.RoleDTO;

import java.util.List;


public interface RoleService {
    RoleDTO findOne(String role);
    List<RoleDTO> findAll();
    RoleDTO save(RoleDTO roleDTO);
}
