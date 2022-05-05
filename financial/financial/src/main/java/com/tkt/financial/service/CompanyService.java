package com.tkt.financial.service;

import com.tkt.financial.model.Company;
import com.tkt.financial.model.Result;

import java.util.List;

public interface CompanyService {
    List<Company> getAll();

    Company getById(Integer id);

    Company create(Company company);

    List<Company> create(List<Company> companies);

    void deleteById(Integer id);

    List<Company> manageFilters(List<Company> companies, String name, String sector, Integer siren);

    List<Company> manageSort(List<Company> companies, String sortedAttribute, Boolean desc);

    Result getResultDiff(Company company, Integer year1, Integer year2);
}
