package com.example.expressioncalculator.controller;

import com.example.expressioncalculator.model.entitie.Expression;
import com.example.expressioncalculator.model.service.ExpressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SameReturnValue")
@Controller
public class MainController {

    private ExpressionService expressionService;

    @Autowired
    public void setExpressionService(ExpressionService expressionService) {
        this.expressionService = expressionService;
    }

    @GetMapping(value = "/")
    public String expressionsGet(Model model) {
        model.addAttribute("expressions", expressionService.getAll());
        return "expressions";
    }

    @GetMapping(value = "search")
    public String expressionsSearchGet(@RequestParam String sign, @RequestParam double result, Model model) {
        model.addAttribute("expressions", expressionService.getByResult(result, sign));
        model.addAttribute("sign", sign);
        model.addAttribute("result", result);
        return "expressions";
    }

    @GetMapping(value = "create")
    public String expressionCreateGet() {
        return "edit";
    }

    @PostMapping(value = "create")
    public String expressionEditPost(@Valid Expression expression, BindingResult result, Model model) {

        if (result.hasErrors()) {

            List<String> stringErrors = new ArrayList<>();

            for (ObjectError objectError : result.getAllErrors()) {
                stringErrors.add(objectError.getCodes()[0] + " " + objectError.getDefaultMessage());
            }
            model.addAttribute("errorList", stringErrors);
            return "edit";
        }

        String error = expression.calculateExpression();

        if (!error.isEmpty()) {
            model.addAttribute("errorList", List.of(error));
            return "edit";
        }

        expressionService.save(expression);

        return "redirect:/edit/" + expression.getId();
    }

    @GetMapping(value = "edit/{id}")
    public String expressionEditGet(@PathVariable int id, Model model) {
        Expression foundExpression = expressionService.getById(id);
        if (foundExpression == null) {
            return "redirect:/create";
        } else {
            model.addAttribute("expression", foundExpression);
            return "edit";
        }
    }

    @PostMapping(value = "edit/{id}")
    public String expressionEditPost(@PathVariable int id, @RequestParam String value, Model model) {

        Expression foundExpression = expressionService.getById(id);
        if (foundExpression == null)
            return "redirect:/create";

        foundExpression.setValue(value);
        String error = foundExpression.calculateExpression();

        if (!error.isEmpty()) {
            model.addAttribute("errorList", List.of(error));
            return expressionEditGet(foundExpression.getId(), model);
        }

        expressionService.save(foundExpression);

        return expressionEditGet(foundExpression.getId(), model);
    }

    @GetMapping(value = "delete/{id}")
    public String expressionDeleteGet(@PathVariable int id) {
        expressionService.delete(id);
        return "redirect:/";
    }
}
