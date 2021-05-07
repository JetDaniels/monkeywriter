package org.doccreator.controller;

import org.doccreator.component.entity.DocumentDTO;
import org.doccreator.service.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class DocumentController {
    private final DocumentsService documentsService;

    @Autowired
    public DocumentController(DocumentsService documentsService){
        this.documentsService = documentsService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/documents",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DocumentDTO>> getDocuments(){
        return ResponseEntity.ok(documentsService.findAll());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/addDocument",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DocumentDTO>> addDocument(@RequestBody DocumentDTO document){
        documentsService.save(document);
        return ResponseEntity.ok(documentsService.findAll());
    }
}
