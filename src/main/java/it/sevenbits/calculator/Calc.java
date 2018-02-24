package it.sevenbits.calculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Reverse Polish notation calculator.
 * <a href='https://en.wikipedia.org/wiki/Reverse_Polish_notation'>wiki</a>
 */
public class Calc {
    private Map<String, BiFunction<Float, Float, Float>> operations;
    private Tokenizer tokenizer;

    public Calc(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        operations = new HashMap<>();
        operations.put("+", (left, right) -> left + right);
        operations.put("*", (left, right) -> left * right);
        operations.put("-", (left, right) -> right - left);
        operations.put("/", (left, right) -> right / left);
    }

    public float eval(String expression) {
        List<String> tokens = tokenizer.tokenize(expression);
        if (tokens == null || tokens.isEmpty()) return 0f;
        Stack numbers = new ArrayStack();
        for (String token : tokens) {
            if (isNumber(token)) {
                numbers.push(Float.parseFloat(token));
            } else {
                numbers.push(operations.get(token).apply(
                        numbers.pop(),
                        numbers.pop()
                ));
            }
        }

        return numbers.pop();
    }

    private boolean isNumber(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}