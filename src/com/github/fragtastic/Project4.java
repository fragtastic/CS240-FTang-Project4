package com.github.fragtastic;

import java.util.EmptyStackException;
import java.util.Stack;

public class Project4 {

    public Project4() {
        System.out.println("Good examples");
        ppfix("a+b+c");
        ppfix("a+b*c");
        ppfix("(a+b)*(c-d)");
        ppfix("a*(b+c)/d");

        System.out.println("\nBroken examples");
        ppfix("(a+b)*(c-d))");
        ppfix("((a+b)*(c-d)");
    }

    public void ppfix(String input) {
        postfix(input);
        prefix(input);
    }

    public void prefix(String input) {
        System.out.print("Prefix: ");
        Stack<String> operand = new Stack<>();
        Stack<Character> operator = new Stack<>();
        String leftOperand = "";
        String rightOperand = "";
        char op = (char)0;
        for (char c : input.toCharArray()) {
            if (isLetter(c)) {
                operand.push(String.valueOf(c));
            } else if (c == '(') {
                operator.push(c);
            } else if (c == ')') {
                try {
                    while (operator.peek() != '(') {
                        op = operator.pop();
                        rightOperand = operand.pop();
                        leftOperand = operand.pop();
                        operand.push(op + leftOperand + rightOperand);
                    }
                    operator.pop();
                } catch (EmptyStackException e) {
                    System.out.println("Equation \"" + input + "\" is unbalanced parenthesis.");
                    return;
                }
            } else if (!operator.isEmpty() && greaterPrecedence(c, operator)) {
                operator.push(c);
            } else if (!operator.isEmpty() && (lowerPrecedence(c, operator) || samePrecedence(c, operator))) {
                operator.push(c);
                op = operator.pop();
                rightOperand = operand.pop();
                leftOperand = operand.pop();
                operand.push(op + leftOperand + rightOperand);

            } else {
                operator.push(c);
            }
        }
        if (operator.contains('(')) {
            System.out.println("Equation \"" + input + "\" is unbalanced parenthesis.");
            return;
        }
        while (!operator.isEmpty()) {
            String o = String.valueOf(operator.pop());
            String r = operand.pop();
            String l = operand.pop();
            o += l;
            o += r;
            operand.push(o);
        }
        for (String opr : operand) {
            System.out.print(opr);
        }
        System.out.println("");
    }

    public boolean greaterPrecedence(char c, Stack<Character> stack) {
        return (c == '*' || c == '/' || c == '%') && (stack.peek() == '+' || stack.peek() == '-');
    }

    public boolean lowerPrecedence(char c, Stack<Character> stack) {
        return (c == '+' || c == '-') && (stack.peek() == '*' || stack.peek() == '/' || stack.peek() == '%');
    }

    public boolean samePrecedence(char c, Stack<Character> stack) {
        return c == stack.peek()
                || (((c == '*' || c == '%') && stack.peek() == '/')
                || ((c == '/' || c == '%') && stack.peek() == '*')
                || ((c == '/' || c == '*') && stack.peek() == '%')
                || (c == '+' && stack.peek() == '-')
                || (c == '-' && stack.peek() == '+'));
    }

    public void postfix(String input) {
        System.out.print("Postfix: ");
        Stack<Character> stack = new Stack<>();
        for (char c : input.toCharArray()) {
            if (isLetter(c)) {
                System.out.print(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    System.out.print(stack.pop());
                }
                if (stack.isEmpty()) {
                    System.out.println("\nEquation \"" + input + "\" is unbalanced parenthesis.");
                    return;
                }
                stack.pop();
            } else {
                while (true) {
                    if (!stack.isEmpty() && greaterPrecedence(c, stack)) {
                        stack.push(c);
                        break;
                    } else if (!stack.isEmpty() && lowerPrecedence(c, stack)) {
                        System.out.print(stack.pop());
                    } else if (!stack.isEmpty() && samePrecedence(c, stack)) {
                        System.out.print(stack.pop());
                    } else {
                        stack.push(c);
                        break;
                    }
                }
            }
        }
        if (stack.contains('(')) {
            System.out.println("\nEquation \"" + input + "\" is unbalanced parenthesis.");
            return;
        }
        while (!stack.isEmpty()) {
            System.out.print(stack.pop());
        }
        System.out.println("");
    }

    public boolean isLetter(char c) {
        return c >= 97 && c <= 122;
    }

    public static void main(String[] args) {
        Project4 p4 = new Project4();
    }
}
