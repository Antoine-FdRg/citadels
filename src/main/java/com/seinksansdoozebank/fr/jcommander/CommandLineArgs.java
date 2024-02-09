package com.seinksansdoozebank.fr.jcommander;

import com.beust.jcommander.Parameter;
import com.seinksansdoozebank.fr.jcommander.validator.QuickValueValidator;

// Class to hold command-line parameters
public class CommandLineArgs {
    @Parameter(names = "--2thousands", description = "Enable 2thousands option")
    private boolean is2Thousands;

    @Parameter(names = "--demo", description = "Enable demo option")
    private boolean isDemo;

    @Parameter(names = "--csv", description = "Enable CSV option")
    private boolean isCsv;

    @Parameter(names = "--quick", description = "Enable quick option", arity = 1, validateWith = QuickValueValidator.class)
    private String quickValue;

    // Getter methods if needed
    public boolean is2Thousands() {
        return is2Thousands;
    }

    public boolean isDemo() {
        return isDemo;
    }

    public boolean isCsv() {
        return isCsv;
    }

    public String getQuickValue() {
        return quickValue;
    }
}