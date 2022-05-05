package com.tkt.financial.service;

import com.tkt.financial.model.Company;
import com.tkt.financial.model.Result;
import com.tkt.financial.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository repository;
    private final ResultService resultService;

    public CompanyServiceImpl(CompanyRepository repository, ResultService resultService) {
        this.repository = repository;
        this.resultService = resultService;
    }

    public List<Company> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Company getById(Integer id) {
        return this.repository.getById(id);
    }

    @Override
    public Company create(Company company) {
        Company savedCompany = this.repository.save(company);
        company.getResults().forEach(r -> r.setCompany(savedCompany));
        savedCompany.setResults(this.resultService.create(company.getResults()));
        return savedCompany;
    }

    @Override
    public List<Company> create(List<Company> companies) {
        List<Company> result = new ArrayList<>();
        Company tmpC;
        for (Company company : companies) {
            tmpC = this.repository.save(company);
            Company finalTmpC = tmpC;
            company.getResults().forEach(r -> r.setCompany(finalTmpC));
            tmpC.setResults(this.resultService.create(company.getResults()));
            result.add(tmpC);
        }
        return result;
    }

    @Override
    public void deleteById(Integer id) {
        this.repository.deleteById(id);
    }

    @Override
    public List<Company> manageFilters(List<Company> companies, String name, String sector, Integer siren) {
        if (name != null) {
            companies = companies.stream().filter(c -> c.getName().equals(name)).collect(Collectors.toList());
        }
        if (sector != null) {
            companies = companies.stream().filter(c -> c.getSector().equals(sector)).collect(Collectors.toList());
        }
        if (siren != null) {
            companies = companies.stream().filter(c -> c.getSiren() == siren).collect(Collectors.toList());
        }
        return companies;
    }

    @Override
    public List<Company> manageSort(List<Company> companies, String sortedAttribute, Boolean desc) {
        if (sortedAttribute != null) {
            companies = companies.stream().sorted(new Comparator<Company>() {
                @Override
                public int compare(Company o1, Company o2) {
                    return Company.compareTo(o1, o2, sortedAttribute, desc);
                }
            }).collect(Collectors.toList());
        }
        return companies;
    }

    @Override
    public Result getResultDiff(Company company, Integer year1, Integer year2) {
        Optional<Result> resultYear1 = company.getResults().stream().filter(r -> r.getYear() == year1).findFirst();
        Optional<Result> resultYear2 = company.getResults().stream().filter(r -> r.getYear() == year2).findFirst();
        if (resultYear1.isEmpty() && resultYear2.isEmpty()) {
            LOGGER.error("No result found for theses years and company {}, {} ,{}", year1, year2, company.getId());
            return new Result();
        }
        if(resultYear1.isEmpty()){
            return resultYear2.get();
        }
        if(resultYear2.isEmpty()){
            return resultYear1.get();
        }
        return this.resultService.compareResults(resultYear1.get(), resultYear2.get());
    }
}
