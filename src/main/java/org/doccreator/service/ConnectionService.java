package org.doccreator.service;

import org.doccreator.component.entity.ConnectionDTO;

import java.util.List;

public interface ConnectionService {
    ConnectionDTO findOne(String connection_name);
    List<ConnectionDTO> findAll();
    ConnectionDTO save(ConnectionDTO connectionDTO);
}
