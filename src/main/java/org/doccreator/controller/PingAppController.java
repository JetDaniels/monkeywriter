package org.doccreator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingAppController {

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseEntity<Boolean> ping(){
        return ResponseEntity.ok(Boolean.TRUE);
    }

}
