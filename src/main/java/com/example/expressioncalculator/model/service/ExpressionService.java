package com.example.expressioncalculator.model.service;

import com.example.expressioncalculator.model.entitie.Expression;

import java.util.List;

public interface ExpressionService {

    @SuppressWarnings("UnusedReturnValue")
    Expression save(Expression expression);
    void delete(int id);
    Expression getById(int id);
    List<Expression> getByResult(double result, String sign);
    List<Expression> getAll();

}
