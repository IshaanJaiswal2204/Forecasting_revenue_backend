package com.revenueforecast.controller;

import com.revenueforecast.model.BFD;
import com.revenueforecast.service.BFDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bfd")
public class BFDController {

    @Autowired
    private BFDService bfdService;

    @GetMapping
    public ResponseEntity<List<BFD>> getBfdData() {
        return ResponseEntity.ok(bfdService.getAllBfdData());
    }
}
