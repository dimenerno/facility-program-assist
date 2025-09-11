package com.facilityassist.controller;

import com.facilityassist.model.Unit;
import com.facilityassist.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitRepository unitRepository;

    @GetMapping
    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }
}
