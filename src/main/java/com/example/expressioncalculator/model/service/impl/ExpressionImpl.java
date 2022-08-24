package com.example.expressioncalculator.model.service.impl;

import com.example.expressioncalculator.model.entitie.Expression;
import com.example.expressioncalculator.model.repository.ExpressionRepo;
import com.example.expressioncalculator.model.service.ExpressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExpressionImpl implements ExpressionService {
    private ExpressionRepo expressionRepo;

    @Autowired
    public void setExpressionService(ExpressionRepo expressionRepo) {
        this.expressionRepo = expressionRepo;
    }

    @Override
    public Expression save(Expression expression) {
        return expressionRepo.save(expression);
    }

    @Override
    public void delete(int id) {
        expressionRepo.deleteById(id);
    }

    @Override
    public Expression getById(int id) {
        Optional<Expression> optional = expressionRepo.findById(id);
        return optional.isEmpty() ? null : optional.get();
    }

    @Override
    public List<Expression> getByResult(double result, String sign) {
        return switch (sign) {
            case "=" -> expressionRepo.findByResultEqual(result);
            case ">" -> expressionRepo.findByResultGreater(result);
            case "<" -> expressionRepo.findByResultLess(result);
            default -> new ArrayList<>();
        };
    }

    @Override
    public List<Expression> getAll() {
        return expressionRepo.findAll();
    }
}
