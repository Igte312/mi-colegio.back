package com.micolegio.controllers;

import com.micolegio.domain.SchoolSupply;
import com.micolegio.domain.service.schoolSupply.ISchoolSupplyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/school-supply")
@AllArgsConstructor
public class SchoolSupplyController {

    private final ISchoolSupplyService schoolSupplyService;

    @GetMapping("/active")
    public List<SchoolSupply> getGeneralActiveSupplies() {
        return schoolSupplyService.getGeneralActiveSupplies();
    }
}
