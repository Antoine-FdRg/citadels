package com.seinksansdoozebank.fr.view.logger;

import java.text.MessageFormat;
import java.util.logging.*;

public class CustomFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        Object[] params = record.getParameters();
        if (params != null && params.length > 0) {
            return MessageFormat.format(record.getMessage(), params) + "\n";
        } else {
            return record.getMessage() + "\n";
        }
    }
}
