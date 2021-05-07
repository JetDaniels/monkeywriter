package org.doccreator.controller;

import org.doccreator.component.entity.OutsideSystemDTO;
import org.doccreator.service.OutsideSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class OutsideSystemController {
    private final OutsideSystemService outsideSystemService;

    @Autowired
    public OutsideSystemController(OutsideSystemService outsideSystemService){
        this.outsideSystemService = outsideSystemService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/systems",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OutsideSystemDTO>> getSystems(){
        return ResponseEntity.ok(outsideSystemService.findAll());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/addSystem",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OutsideSystemDTO>> addSystem(@RequestBody OutsideSystemDTO system){
        outsideSystemService.save(system);
        return ResponseEntity.ok(outsideSystemService.findAll());
    }
}
