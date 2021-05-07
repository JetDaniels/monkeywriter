package org.doccreator.service.impl;

import org.doccreator.component.entity.ConnectionDTO;
import org.doccreator.repository.ConnectionRepository;
import org.doccreator.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    private final ConnectionRepository connectionRepository;

    @Autowired
    public ConnectionServiceImpl(ConnectionRepository connectionRepository){
        this.connectionRepository = connectionRepository;
    }

    @Override
    public ConnectionDTO findOne(String connection_name) {
        return connectionRepository.findOne(connection_name);
    }

    @Override
    public List<ConnectionDTO> findAll() {
        return connectionRepository.findAll();
    }

    @Override
    public ConnectionDTO save(ConnectionDTO connectionDTO) {
        return connectionRepository.save(connectionDTO);
    }
}
