package com.example.android.calculator;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorLogic implements Parcelable {
    private Context context;
    private String ERROR;
    private String ZERO = "0";
    private String EMPTY = "";
    private char CHAR_EMPTY = (char) 0;
    private String POINT = ".";
    private  String PERCENT = "%";
    private int MAX_LENGTH = 9;
    private final CalculatorData calculatorData = new CalculatorData();
    private boolean deleteOnNextInput;

    public CalculatorLogic(Context context) {
        this.context = context;
        this.ERROR = context.getString(R.string.error_message);
    }

    protected CalculatorLogic(Parcel in) {
        ERROR = in.readString();
        ZERO = in.readString();
        EMPTY = in.readString();
        CHAR_EMPTY = (char) in.readInt();
        POINT = in.readString();
        PERCENT = in.readString();
        MAX_LENGTH = in.readInt();
        deleteOnNextInput = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ERROR);
        dest.writeString(ZERO);
        dest.writeString(EMPTY);
        dest.writeInt((int) CHAR_EMPTY);
        dest.writeString(POINT);
        dest.writeString(PERCENT);
        dest.writeInt(MAX_LENGTH);
        dest.writeByte((byte) (deleteOnNextInput ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CalculatorLogic> CREATOR = new Creator<CalculatorLogic>() {
        @Override
        public CalculatorLogic createFromParcel(Parcel in) {
            return new CalculatorLogic(in);
        }

        @Override
        public CalculatorLogic[] newArray(int size) {
            return new CalculatorLogic[size];
        }
    };

    public String getResult() {
        return calculatorData.getResult();
    }

    public String getPreResult() {
        return calculatorData.getPreResult();
    }

    public void numKeyProcessing(String value) {
        resetPrevious();
        if (value.length() > 1) {
            for (int i = 0; i < ((CharSequence) value).length(); i++) {
                numKeyProcessing(String.valueOf(((CharSequence) value).charAt(i)));
            }
            return;
        }

        if (calculatorData.getOperand() == CHAR_EMPTY) {
            if (calculatorData.getArgumentOne().endsWith(PERCENT))
                return;
            if (deleteOnNextInput) {
                calculatorData.setArgumentOne(EMPTY);
                deleteOnNextInput = false;
            }
            if (value.equals(POINT)) {
                if (isFloat(calculatorData.getArgumentOne())) return;
                if (calculatorData.getArgumentOne().equals(EMPTY)) {
                    calculatorData.setArgumentOne(ZERO);
                }
            } else if (isMaxLength(calculatorData.getArgumentOne())) {
                return;
            } else if (calculatorData.getArgumentOne().equals(ZERO)) {
                calculatorData.setArgumentOne(EMPTY);
            }
            calculatorData.setArgumentOne(calculatorData.getArgumentOne() + value);
        } else {
            if (calculatorData.getArgumentTwo().endsWith(PERCENT))
                return;
            if (value.equals(POINT)) {
                if (isFloat(calculatorData.getArgumentTwo())) return;
                if (calculatorData.getArgumentTwo().equals(EMPTY)) {
                    calculatorData.setArgumentTwo(ZERO);
                }
            } else if (isMaxLength(calculatorData.getArgumentTwo())) {
                return;
            } else if (calculatorData.getArgumentTwo().equals(ZERO)) {
                calculatorData.setArgumentTwo(EMPTY);
            }
            calculatorData.setArgumentTwo(calculatorData.getArgumentTwo() + value);
            createPreResult();
        }
        createResult();
    }

    private boolean isFloat(String argument) {
        return argument.contains(POINT);
    }

    private boolean isMaxLength(String argument) {
        int length;
        if (isFloat(argument)) {
            int pointPos = argument.indexOf(POINT);
            String argumentInt = argument.substring(0, pointPos - 1);
            length = argumentInt.length();
        } else {
            length = argument.length();
        }
        return length == MAX_LENGTH;
    }

    public void backspaceKeyProcessing() {
        if (calculatorData.getResult().equals(ERROR)) return;
        resetPrevious();
        if (calculatorData.getArgumentOne().equals(EMPTY)) return;
        if (calculatorData.getOperand() == CHAR_EMPTY) {
            String argument = calculatorData.getArgumentOne();
            calculatorData.setArgumentOne(argument.substring(0, argument.length() - 1));
            createResult();
            createPreResult();
            return;
        }
        if (calculatorData.getArgumentTwo().equals(EMPTY)) {
            calculatorData.setOperand(CHAR_EMPTY);
            createResult();
            return;
        }
        String argument = calculatorData.getArgumentTwo();
        calculatorData.setArgumentTwo(argument.substring(0, argument.length() - 1));
        createResult();
        createPreResult();
    }

    public void resetKeyProcessing() {
        calculatorData.setResult(EMPTY);
        calculatorData.setPreResult(EMPTY);
        calculatorData.setArgumentOne(EMPTY);
        calculatorData.setArgumentTwo(EMPTY);
        calculatorData.setOperand(CHAR_EMPTY);
        resetPrevious();
    }

    private void resetPrevious() {
        calculatorData.setPreviousArgumentTwo(EMPTY);
        calculatorData.setPreviousOperand(CHAR_EMPTY);
    }

    public void operandKeyProcessing(char operand) {
        if (calculatorData.getResult().equals(ERROR)) return;
        resetPrevious();
        if (calculatorData.getOperand() == CHAR_EMPTY) {
            if (calculatorData.getArgumentOne().equals(EMPTY)) {
                calculatorData.setArgumentOne(ZERO);
            }
            calculatorData.setOperand(operand);
            createResult();
        } else {
            if (calculatorData.getArgumentTwo().equals(EMPTY)) {
                calculatorData.setOperand(operand);
                createResult();
                return;
            }
            createResult(operand);
        }
    }

    public void percentKeyProcessing() {
        if (calculatorData.getResult().equals(ERROR)) return;
        resetPrevious();
        if (calculatorData.getArgumentOne().equals(EMPTY)) return;
        if (calculatorData.getOperand() == CHAR_EMPTY) {
            if (calculatorData.getArgumentOne().endsWith(PERCENT)) return;
            calculatorData.setArgumentOne(calculatorData.getArgumentOne() + PERCENT);
            createResult();
            createPreResult();
        }
        if (calculatorData.getArgumentTwo().equals(EMPTY) || calculatorData.getArgumentTwo().endsWith(PERCENT))
            return;
        calculatorData.setArgumentTwo(calculatorData.getArgumentTwo() + PERCENT);
        createResult();
        createPreResult();
    }

    public void equallyKeyProcessing() {
        if (calculatorData.getResult().equals(ERROR)) return;
        if (calculatorData.getOperand() == CHAR_EMPTY) {
            if (calculatorData.getArgumentOne().endsWith(PERCENT)) {
                calculatorData.setResult(calculatorData.getPreResult());
                calculatorData.setArgumentOne(calculatorData.getPreResult());
                calculatorData.setPreResult(EMPTY);
                return;
            }
            if (calculatorData.getPreviousOperand() == CHAR_EMPTY) {
                return;
            }
            previousToCurrent();
            createPreResult();
        } else if (divOnZero()) {
            return;
        } else if (calculatorData.getArgumentTwo().equals(EMPTY)) {
            calculatorData.setArgumentTwo(ZERO);
            createPreResult();
        }
        calculatorData.setArgumentOne(calculatorData.getPreResult());
        currentToPrevious();
        calculatorData.setPreResult(EMPTY);
        createResult();
        deleteOnNextInput = true;
    }

    private void currentToPrevious() {
        calculatorData.setPreviousArgumentTwo(calculatorData.getArgumentTwo());
        calculatorData.setPreviousOperand(calculatorData.getOperand());
        calculatorData.setArgumentTwo(EMPTY);
        calculatorData.setOperand(CHAR_EMPTY);
    }

    private void previousToCurrent() {
        calculatorData.setArgumentTwo(calculatorData.getPreviousArgumentTwo());
        calculatorData.setOperand(calculatorData.getPreviousOperand());
        calculatorData.setPreviousArgumentTwo(EMPTY);
        calculatorData.setPreviousOperand(CHAR_EMPTY);
    }

    private boolean divOnZero() {
        if (calculatorData.getOperand() == '/') {
            String divider = calculatorData.getArgumentTwo();
            if (calculatorData.getArgumentTwo().endsWith(PERCENT)) {
                String percent = divider.substring(0, divider.length() - 1);
                return Double.parseDouble(percent) == 0.0;
            }
            return divider.equals(EMPTY) || Double.parseDouble(divider) == 0.0;
        }
        return false;
    }

    private void createResult() {
        if (calculatorData.getOperand() == CHAR_EMPTY) {
            calculatorData.setResult(calculatorData.getArgumentOne());
        } else {
            String result = calculatorData.getArgumentOne() + calculatorData.getOperand() + calculatorData.getArgumentTwo();
            calculatorData.setResult(result);
        }
    }

    private void createResult(char operand) {
        calculatorData.setResult(calculatorData.getPreResult());
        calculatorData.setArgumentOne(calculatorData.getPreResult());
        calculatorData.setArgumentTwo(EMPTY);
        calculatorData.setPreResult(EMPTY);
        calculatorData.setOperand(operand);
        createResult();
    }

    private void createPreResult() {
        if (calculatorData.getArgumentTwo().equals(EMPTY) || divOnZero()) {
            calculatorData.setPreResult(EMPTY);

            if (calculatorData.getArgumentOne().endsWith(PERCENT)) {
                String argument = calculatorData.getArgumentOne();
                String percent = argument.substring(0, argument.length() - 1);
                double factor = Double.parseDouble(percent) / 100;
                calculatorData.setPreResult(String.valueOf(factor));
            }
        } else {
            double preResult;
            Operation operation;

            double argumentOne;
            if (calculatorData.getArgumentOne().endsWith(PERCENT)) {
                String argument = calculatorData.getArgumentOne();
                String percent = argument.substring(0, argument.length() - 1);
                argumentOne = Double.parseDouble(percent) / 100;
            } else {
                argumentOne = Double.parseDouble(calculatorData.getArgumentOne());
            }

            double argumentTwo;
            if (calculatorData.getArgumentTwo().endsWith(PERCENT)) {
                String argument = calculatorData.getArgumentTwo();
                String percent = argument.substring(0, argument.length() - 1);
                double factor = Double.parseDouble(percent) / 100;
                argumentTwo = argumentOne * factor;
            } else {
                argumentTwo = Double.parseDouble(calculatorData.getArgumentTwo());
            }

            switch (calculatorData.getOperand()) {
                case '+':
                    operation = Operation.SUM;
                    break;
                case '-':
                    operation = Operation.SUBTRACT;
                    break;
                case '*':
                    operation = Operation.MULTIPLY;
                    break;
                case '/':
                    operation = Operation.DIVISION;
                    break;
                default:
                    throw new IllegalArgumentException(ERROR);
            }
            preResult = operation.action(argumentOne, argumentTwo);
            if (Double.isInfinite(preResult)) {
                resetKeyProcessing();
                calculatorData.setPreResult(ERROR);
            } else {
                calculatorData.setPreResult(removeTail(preResult));
            }
        }
    }

    private String removeTail(double number) {
        if (number == (int) number) return String.valueOf((int) number);
        return String.valueOf(number);
    }
}
