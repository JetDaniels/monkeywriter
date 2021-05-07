package org.doccreator.controller;

import org.doccreator.component.entity.ConnectionDTO;
import org.doccreator.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class ConnectionController {
    private final ConnectionService connectionService;

    @Autowired
    public ConnectionController(ConnectionService connectionService){
        this.connectionService = connectionService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/connections",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ConnectionDTO>> getConnections(){
        return ResponseEntity.ok(connectionService.findAll());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/addConnection",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ConnectionDTO>> addConnection(@RequestBody ConnectionDTO connection){
        connectionService.save(connection);
        return ResponseEntity.ok(connectionService.findAll());
    }
}
