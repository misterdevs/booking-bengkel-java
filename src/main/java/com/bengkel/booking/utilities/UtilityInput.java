package com.bengkel.booking.utilities;

import java.util.Scanner;
import java.util.function.Predicate;

public class UtilityInput extends Utility {
    private final Scanner scanner;

    public UtilityInput() {
        this.scanner = new Scanner(System.in);
    }

    public UtilityInput close() {
        this.scanner.close();
        System.exit(0);
        return this;
    }

    public String validateRegex(String inputName, String errorMessage, String regex) {
        return validate(inputName, errorMessage, input -> input.matches(regex));
    }

    public String validate(String inputName, String errorMessage, Predicate<String> condition) {
        return validateCustom(inputName, s -> {
            if (condition.test(s)) {
                return true;
            } else {
                System.out.println(errorMessage);
                return false;
            }
        });
    }

    public String validateCustom(String inputName, Predicate<String> function) {
        String input;
        while (true) {
            System.out.print(inputName + " : ");
            input = scanner.nextLine();

            if (function.test(input))
                break;
        }
        return input;
    }

}
