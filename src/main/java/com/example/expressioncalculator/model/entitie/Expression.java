package com.example.expressioncalculator.model.entitie;

import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "expressions")
public class Expression {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @javax.validation.constraints.Pattern(regexp = "^(-?((?<![.,\\d])[(]-?)*(?<![)])\\d+([.,]\\d+)?[)]*[+/*]?)*(?<![+/*.,])$")
    @NotEmpty()
    @Getter
    @Setter
    private String value;

    @Getter
    private int amountOfNumbers;

    @Getter
    private double result;

    /**
     * Returns an error if the calculation fails
     */
    public String calculateExpression() {

        this.amountOfNumbers = 0;
        this.result = 0d;

        if (StringUtils.countOccurrencesOf(value, "(") != StringUtils.countOccurrencesOf(value, ")"))
            return "Amount of opening brackets does not equal amount of closing brackets.";

        if (!Pattern.matches("^(-?((?<![.,\\d])[(]-?)*(?<![)])\\d+([.,]\\d+)?[)]*[+/*]?)*(?<![+/*.,])$", value))
            return "Value must match the pattern ^(-?((?<![.,\\d])[(]-?)*(?<![)])\\d+([.,]\\d+)?[)]*[+/*]?)*(?<![+/*.,])$";

        Pattern pattern = Pattern.compile("/[(-]*0+([.,]0+)?[)]*(?![.\\d])");
        Matcher matcher = pattern.matcher(value);
        if (matcher.find())
            return "You can't divide by zero!";

        setAmountOfNumbers();
        setResult();

        return "";
    }

    private void setAmountOfNumbers() {
        Pattern pattern = Pattern.compile("\\d+([.,]\\d+)?");
        Matcher matcher = pattern.matcher(value);
        amountOfNumbers = (int) matcher.results().count();
    }

    private void setResult() {
        String tempValue = value.replaceAll(",", ".").replaceAll("[)][(]", ")*(");
        double tempResult = calculateExpressionInBrackets(tempValue);
        double scale = Math.pow(10, 3);
        result = Math.round(tempResult * scale) / scale;
    }

    private double calculateExpressionInBrackets(String expression) {

        while (expression.contains("(")) {
            int startPos = -1;
            int endPos = -1;
            int amountOfOpenBrackets = 0;
            int amountOfClosedBrackets = 0;
            double tempResult = 0d;
            char[] charArray = expression.toCharArray();

            for (int i = 0; i < charArray.length; i++) {
                if (charArray[i] == '(') {
                    amountOfOpenBrackets++;
                    if (startPos == -1)
                        startPos = i;
                }

                if (charArray[i] == ')') {
                    amountOfClosedBrackets++;
                    endPos = i;
                }

                if (amountOfOpenBrackets > 0 && amountOfOpenBrackets == amountOfClosedBrackets) {
                    tempResult = calculateExpressionInBrackets(expression.substring(startPos + 1, endPos));
                    break;
                }
            }

            String startSubString = startPos > 0 ? expression.substring(0, startPos) : "";
            expression = startSubString + tempResult + expression.substring(endPos + 1);
        }

        return calculateSimpleExpression(expression);
    }

    private double calculateSimpleExpression(String expression) {

        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?[/*]-?\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            double tempResult = multiplyDivide(matcher.group());
            expression = expression.replaceFirst(matcher.group().replaceAll("[*]", "[*]"), String.valueOf(tempResult));
            matcher = pattern.matcher(expression);
        }

        Pattern pattern1 = Pattern.compile("(--)|([+]-)|([+][+])");
        Matcher matcher1 = pattern1.matcher(expression);
        while (matcher1.find()) {
            expression = expression
                    .replaceAll("--", "+")
                    .replaceAll("[+]-", "-")
                    .replaceAll("[+][+]", "+");
            matcher1 = pattern1.matcher(expression);
        }

        Pattern pattern2 = Pattern.compile("-?\\d+(\\.\\d+)?[+-]\\d+(\\.\\d+)?");
        Matcher matcher2 = pattern2.matcher(expression);
        while (matcher2.find()) {
            double tempResult = addSubtract(matcher2.group());
            expression = expression.replaceAll(matcher2.group().replaceAll("[+]", "[+]"), String.valueOf(tempResult));
            matcher2 = pattern2.matcher(expression);
        }

        return Double.parseDouble(expression);
    }

    private double multiplyDivide(String expression) {

        String[] numbers = expression.split("[/*]");

        double number1 = Double.parseDouble(numbers[0]);
        double number2 = Double.parseDouble(numbers[1]);

        return expression.contains("*") ? number1 * number2 : number1 / number2;
    }

    private double addSubtract(String expression) {

        String[] numbers = expression.split("(?<=\\d)[+-]");

        double number1 = Double.parseDouble(numbers[0]);
        double number2 = Double.parseDouble(numbers[1]);

        return expression.contains("+") ? number1 + number2 : number1 - number2;
    }
}
