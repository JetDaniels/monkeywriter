package org.doccreator.controller;

import org.doccreator.component.entity.CreateDocumentsRequestDTO;
import org.doccreator.component.entity.CreateDocumentsRequestLabelDTO;
import org.doccreator.component.entity.CreateDocumentsRequestsStepsDTO;
import org.doccreator.service.CreateDocumentsRequestService;
import org.doccreator.service.CreateDocumentsRequestsStepsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class CreateDocumentsRequestsController {

    private final CreateDocumentsRequestService createDocumentsRequestService;
    private final CreateDocumentsRequestsStepsService createDocumentsRequestsStepsService;

    @Autowired
    public CreateDocumentsRequestsController(CreateDocumentsRequestService createDocumentsRequestService,
                                             CreateDocumentsRequestsStepsService createDocumentsRequestsStepsService){
        this.createDocumentsRequestService = createDocumentsRequestService;
        this.createDocumentsRequestsStepsService = createDocumentsRequestsStepsService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/requests",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CreateDocumentsRequestLabelDTO>> getCDRLabels(
            @RequestParam(value = "_page", required = false) Integer pageNum){
        PageRequest pageRequest = new PageRequest(pageNum, 100, Sort.Direction.DESC, "timeBegin");
        Page<CreateDocumentsRequestLabelDTO> page = createDocumentsRequestService.findAllLabels(pageRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(createDocumentsRequestService.getCountAll()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/request",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateDocumentsRequestDTO> getCDR(
            @RequestParam(value = "_id", required = false) Integer id){
        return ResponseEntity.ok(createDocumentsRequestService.findById(id));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/steps",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CreateDocumentsRequestsStepsDTO>> getSteps(
            @RequestParam(name = "_cdrId", required = false) Integer cdrId){
        List<CreateDocumentsRequestsStepsDTO> steps = createDocumentsRequestsStepsService.findById_CdrId(cdrId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(steps.size()));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(steps, headers, HttpStatus.OK);
    }
}
