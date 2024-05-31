package com.bengkel.booking.utilities;

import java.util.Scanner;
import java.util.function.Consumer;

public class UtilityMenu extends Utility {
    private Scanner scanner;
    private UtilityInput input;
    private String chosenMenu;

    public String displayBorder = "==========================================";

    public UtilityMenu() {
        this.scanner = new Scanner(System.in);
        this.input = new UtilityInput();
    }

    public Boolean confirmation(String dialog) {
        return confirmationCustom(dialog + " (y/n)", "y", "n");
    }

    public Boolean confirmationCustom(String dialog, String yes, String no) {
        String input;
        do {
            System.out.print(dialog + " : ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase(yes)) {
                return true;
            } else if (input.equalsIgnoreCase(no)) {
                return false;
            }
        } while (true);
    }

    public void enterToContinue() {
        System.out.println("Press ENTER to continue");
        scanner.nextLine();
    }

    public void resetDisplay() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void createMenu(Consumer<String> function, String menuName, String menuSubName, String[] menuArray,
            int navigationNumber, String navigationName) {
        do {
            resetDisplay();
            if (menuSubName != null)
                printTitleCustom(menuSubName, 1, 0);
            printTitleCustom(menuName, 0, 1);
            for (int i = 0; i < menuArray.length; i++) {
                System.out.println((i + 1) + ". " + menuArray[i]);
            }
            if (navigationName != null) {
                System.out.println(navigationNumber + ". " + navigationName);
            }
            System.out.println();
            this.chosenMenu = input.validate("Pilih menu", "Menu yang dipilih tidak tersedia",
                    s -> isNumberWithRange(s, 1, menuArray.length)
                            || (isNumber(s) && Integer.valueOf(s) == navigationNumber));
            function.accept(this.chosenMenu);
        } while (Integer.valueOf(this.chosenMenu) != navigationNumber);

    }

    public void createMenuLegacy(Consumer<String> function, String menuName, String[] menuArray,
            int navigationNumber, String navigationName) {
        do {
            resetDisplay();
            System.out.println(displayBorder);
            System.out.println(menuName);
            System.out.println(displayBorder);
            for (int i = 0; i < menuArray.length; i++) {
                System.out.println((i + 1) + ". " + menuArray[i]);
            }
            if (navigationName != null) {
                System.out.println(navigationNumber + ". " + navigationName);
            }
            System.out.println(displayBorder);
            this.chosenMenu = input.validate("Pilih menu", "Menu yang dipilih tidak tersedia",
                    s -> isNumberWithRange(s, 1, menuArray.length)
                            || (isNumber(s) && Integer.valueOf(s) == navigationNumber));
            function.accept(this.chosenMenu);
        } while (Integer.valueOf(this.chosenMenu) != navigationNumber);

    }

    public String print() {
        return chosenMenu;
    }

}
