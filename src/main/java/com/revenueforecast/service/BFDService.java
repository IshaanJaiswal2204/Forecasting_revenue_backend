package com.revenueforecast.service;

import com.revenueforecast.model.BFD;
import com.revenueforecast.repository.BFDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BFDService {

    @Autowired
    private BFDRepository bfdRepository;

    public List<BFD> getAllBfdData() {
        return bfdRepository.findAll();
    }
}
