package com.seinksansdoozebank.fr.view.logger;

import java.text.MessageFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomFormatter extends Formatter {
    @Override
    public String format(LogRecord logRecord) {
        Object[] params = logRecord.getParameters();
        if (params != null && params.length > 0) {
            return MessageFormat.format(logRecord.getMessage(), params) + "\n";
        } else {
            return logRecord.getMessage() + "\n";
        }
    }
}
