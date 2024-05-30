package com.bengkel.booking.utilities;

import java.text.DecimalFormat;

public class Utility {
    public static final String REGEX_WORD = "^[-a-zA-Z ]+$";
    public static final String REGEX_NUMBER = "^[0-9]+$";

    public Utility() {
    }

    public String createIdPattern(String pattern, int number) {
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);
    }

    public String rupiahFormatter(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("'Rp. '#,###");
        return decimalFormat.format(number);
    }

    public boolean isNumber(String input) {
        return input.matches(REGEX_NUMBER);
    }

    public boolean isWord(String input) {
        return input.matches(REGEX_WORD);
    }

    public boolean isNumberWithRange(String input, int min, int max) {
        if (isNumber(input)) {
            int number = Integer.parseInt(input);
            return number >= min && number <= max;
        }
        return false;
    }
}
