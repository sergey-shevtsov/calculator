package com.example.android.calculator;

public class CalculatorData {
    private String result = "";
    private String preResult = "";
    private String argumentOne = "";
    private String argumentTwo = "";
    private char operand;
    private String previousArgumentTwo = "";
    private char previousOperand;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPreResult() {
        return preResult;
    }

    public void setPreResult(String preResult) {
        this.preResult = preResult;
    }

    public String getArgumentOne() {
        return argumentOne;
    }

    public void setArgumentOne(String argumentOne) {
        this.argumentOne = argumentOne;
    }

    public String getArgumentTwo() {
        return argumentTwo;
    }

    public void setArgumentTwo(String argumentTwo) {
        this.argumentTwo = argumentTwo;
    }

    public char getOperand() {
        return operand;
    }

    public void setOperand(char operand) {
        this.operand = operand;
    }

    public String getPreviousArgumentTwo() {
        return previousArgumentTwo;
    }

    public void setPreviousArgumentTwo(String previousArgumentTwo) {
        this.previousArgumentTwo = previousArgumentTwo;
    }

    public char getPreviousOperand() {
        return previousOperand;
    }

    public void setPreviousOperand(char previousOperand) {
        this.previousOperand = previousOperand;
    }
}
