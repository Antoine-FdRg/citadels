package com.seinksansdoozebank.fr;

import com.beust.jcommander.Parameter;

// Class to hold command-line parameters
class CommandLineArgs {
    @Parameter(names = "--2thousands", description = "Enable 2thousands option")
    private boolean is2Thousands;

    @Parameter(names = "--demo", description = "Enable demo option")
    private boolean isDemo;

    @Parameter(names = "--csv", description = "Enable CSV option")
    private boolean isCsv;

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
}