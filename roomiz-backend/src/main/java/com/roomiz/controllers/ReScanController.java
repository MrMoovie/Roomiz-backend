package com.roomiz.controllers;

import com.roomiz.responses.BasicResponse;
import com.roomiz.service.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
public class ReScanController {
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }

    @RequestMapping("/scan")
    public BasicResponse scan(){
        //---scan api ---//
        //---process api---//
        return null;
    }


}
