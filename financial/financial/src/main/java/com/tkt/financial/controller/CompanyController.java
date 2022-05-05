package com.tkt.financial.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkt.financial.model.Company;
import com.tkt.financial.model.Result;
import com.tkt.financial.service.CompanyService;
import com.tkt.financial.service.ResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/company")
public class CompanyController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

    private final CompanyService companyService;
    private final ResultService resultService;

    public CompanyController(CompanyService companyService, ResultService resultService) {
        this.companyService = companyService;
        this.resultService = resultService;
    }

    @GetMapping("/load")
    public ResponseEntity<String> loadDataFromPath(@RequestParam String path) {
        File file = new File(path);
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Company> companies = mapper.readValue(file, new TypeReference<List<Company>>() {
            });
            if (CollectionUtils.isEmpty(companies)) {
                return new ResponseEntity<>("EMPTY_FILE", HttpStatus.BAD_REQUEST);
            }
            this.companyService.create(companies);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Company>> getAll(@RequestParam(required = false) String name, @RequestParam(required = false) String sector, @RequestParam(required = false) Integer siren, @RequestParam(required = false) String sortedAttribute, @RequestParam(required = false) Boolean desc) {
        List<Company> companies = this.companyService.getAll();
        companies = this.companyService.manageFilters(companies, name, sector, siren);
        try {
            companies = this.companyService.manageSort(companies, sortedAttribute, desc);
        } catch (Exception ignored) {
        }
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        try {
            this.companyService.deleteById(id);
        } catch (Exception e) {
            LOGGER.error("Failed to delete company with id {} and message {}", id, e.getMessage(), e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Company> delete(@RequestBody Company company) {
        try {
            return new ResponseEntity<>(this.companyService.create(company), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Failed to create company with data {} and message {}", company.toString(), e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/compare")
    public ResponseEntity<Result> delete(@PathVariable Integer id, @RequestParam Integer year1, @RequestParam Integer year2) {
        try {
            Company company = this.companyService.getById(id);
            if (company == null) {
                LOGGER.error("company not found with id {}", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(this.companyService.getResultDiff(company, year1, year2), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Failed to compare company results with message {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/edit/result/add")
    public ResponseEntity<Result> delete(@PathVariable Integer id, @RequestBody Result result) {
        try {
            Company company = this.companyService.getById(id);
            if (company == null) {
                LOGGER.error("company not found with id {}", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            result.setCompany(company);
            return new ResponseEntity<>(this.resultService.create(result), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Failed to create result with data {} and message {}", result.toString(), e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
