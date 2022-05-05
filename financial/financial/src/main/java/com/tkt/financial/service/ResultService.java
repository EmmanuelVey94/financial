package com.tkt.financial.service;

import com.tkt.financial.model.Result;

import java.util.List;

public interface ResultService {

    Result create(Result result);

    void deleteById(Integer id);

    List<Result> create(List<Result> result);

    Result compareResults(Result result, Result result1);
}
