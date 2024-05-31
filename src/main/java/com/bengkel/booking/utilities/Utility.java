package com.bengkel.booking.utilities;

import java.text.DecimalFormat;
import java.util.function.Consumer;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_AbsoluteEven;
import de.vandermeer.asciithemes.TA_GridThemes;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

public class Utility {
    public static final String REGEX_WORD = "^[-a-zA-Z ]+$";
    public static final String REGEX_NUMBER = "^[0-9]+$";
    public static final int TABLE_WIDTH = 120;

    public Utility() {
    }

    public void printTitle(String title) {
        printTitleCustom(title, 1, 1);
    }

    public void printTitleCustom(String title, int paddingTop, int paddingBottom) {
        createTable(table -> {
            {
                table.addRow(title);
                table.setTextAlignment(TextAlignment.CENTER);
                table.setPaddingTop(paddingTop);
                table.setPaddingBottom(paddingBottom);
                table.getRenderer().setCWC(new CWC_AbsoluteEven());
                table.getContext().setGridTheme(TA_GridThemes.NONE);
                System.out.println(table.render(TABLE_WIDTH));
            }
        });
    }

    public void printTable(Object[] headers, Consumer<AsciiTable> function) {
        createTable(table -> {
            table.addRule();
            table.addRow(headers);
            table.addRule();
            function.accept(table);
            table.setTextAlignment(TextAlignment.CENTER);
            table.getRenderer().setCWC(new CWC_AbsoluteEven());
            System.out.println(table.render(TABLE_WIDTH));
        });
    }

    public AsciiTable createTable(Consumer<AsciiTable> function) {
        AsciiTable table = new AsciiTable();
        function.accept(table);
        return table;
    }

    public String createIdPattern(String pattern, int number) {
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);
    }

    public String currencyFormatter(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
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
