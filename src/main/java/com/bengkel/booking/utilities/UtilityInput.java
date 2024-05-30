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
        String input;
        while (true) {
            System.out.print(inputName + " : ");
            input = scanner.nextLine();

            if (condition.test(input)) {
                break;
            } else {
                System.out.println(errorMessage);
            }
        }
        return input;
    }
}
