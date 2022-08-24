package com.example.expressioncalculator.model.entitie;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("UnusedAssignment")
class ExpressionTest {

    @Test
    void calculateExpression() {
        Expression expression = new Expression();

        expression.setValue("5");
        String error = expression.calculateExpression();
        Assertions.assertEquals(1, expression.getAmountOfNumbers());
        Assertions.assertEquals(5d, expression.getResult());

        expression.setValue("(0.9+5/1.0-(5)))");
        error = expression.calculateExpression();
        Assertions.assertEquals(0, expression.getAmountOfNumbers());
        Assertions.assertEquals(0d, expression.getResult());
        Assertions.assertEquals("Amount of opening brackets does not equal amount of closing brackets.", error);

        expression.setValue("(0.9+5/1.0-(5))/0.0");
        error = expression.calculateExpression();
        Assertions.assertEquals(0, expression.getAmountOfNumbers());
        Assertions.assertEquals(0d, expression.getResult());
        Assertions.assertEquals("You can't divide by zero!", error);

        expression.setValue("0.6/00.000");
        error = expression.calculateExpression();
        Assertions.assertEquals("You can't divide by zero!", error);

        expression.setValue("0.6/00.0020000");
        error = expression.calculateExpression();
        Assertions.assertEquals(2, expression.getAmountOfNumbers());
        Assertions.assertEquals(300d, expression.getResult());

        expression.setValue("((0.9+5)/2,1-5*25/40*-6)*(2)-642-(-5*-2)");
        error = expression.calculateExpression();
        Assertions.assertEquals(11, expression.getAmountOfNumbers());
        Assertions.assertEquals(-608.881, expression.getResult());

        expression.setValue("-90+87.25/-22-22");
        error = expression.calculateExpression();
        Assertions.assertEquals(4, expression.getAmountOfNumbers());
        Assertions.assertEquals(-115.966, expression.getResult());

        expression.setValue("-90+(-87,25/-22)+-45,0");
        error = expression.calculateExpression();
        Assertions.assertEquals(4, expression.getAmountOfNumbers());
        Assertions.assertEquals(-131.034, expression.getResult());

        expression.setValue("((0.9+5)/2.1-5*25/40*-6)");
        error = expression.calculateExpression();
        Assertions.assertEquals(7, expression.getAmountOfNumbers());
        Assertions.assertEquals(21.56, expression.getResult());

        expression.setValue("5/(-((-(4+-56+-6*(-22.36))))(-7*6,0))");
        error = expression.calculateExpression();
        Assertions.assertEquals(7, expression.getAmountOfNumbers());
        Assertions.assertEquals(-0.001, expression.getResult());
    }
}