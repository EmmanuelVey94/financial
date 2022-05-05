package com.tkt.financial.service;

import com.tkt.financial.model.Result;
import com.tkt.financial.repository.ResultRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {

    private final ResultRepository repository;

    public ResultServiceImpl(ResultRepository repository) {
        this.repository = repository;
    }

    @Override
    public Result create(Result result) {
        return this.repository.save(result);
    }

    @Override
    public void deleteById(Integer id) {
        this.repository.deleteById(id);
    }

    @Override
    public List<Result> create(List<Result> results) {
        List<Result> returnResult = new ArrayList<>();
        for (Result result : results) {
            returnResult.add(this.create(result));
        }
        return returnResult;
    }

    @Override
    public Result compareResults(Result result, Result result1) {
        Result finalResult = new Result();
        finalResult.setCompany(result.getCompany());
        finalResult.setCa(result1.getCa() - result.getCa());
        finalResult.setEbitda(result1.getEbitda() - result.getEbitda());
        finalResult.setLoss(result1.getLoss() - result.getLoss());
        finalResult.setMargin(result1.getMargin() - result.getMargin());
        return finalResult;

    }
}
