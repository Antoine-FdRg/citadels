package com.seinksansdoozebank.fr.view;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * A custom handler to get the last log record
 */
public class TestHandler extends Handler {
    private LogRecord lastLogRecord;
    private final List<LogRecord> logRecords = new ArrayList<>();

    /**
     * Publish a <tt>LogRecord</tt>.
     *
     * @param record description of the log event. A null record is
     *               silently ignored and is not published
     */
    @Override
    public void publish(LogRecord record) {
        String formattedMessage = record.getMessage();
        if (record.getParameters() != null) {
            formattedMessage = MessageFormat.format(formattedMessage, record.getParameters());
        }
        lastLogRecord = new LogRecord(record.getLevel(), formattedMessage);
        logRecords.add(lastLogRecord);
    }

    /**
     * Flush any buffered output.
     */
    @Override
    public void flush() {
    }

    /**
     * Close the <tt>Handler</tt> and free all associated resources.
     * @throws SecurityException
     */
    @Override
    public void close() throws SecurityException {
    }

    /**
     * Get the last log record
     *
     * @return the last log record
     */
    public LogRecord getLastLogRecord() {
        return lastLogRecord;
    }

    /**
     * Assert that the last log record is the expected one
     *
     * @return the last log record
     */
    public List<LogRecord> getLogRecords() {
        return logRecords;
    }

    public void cleanLogRecords() {
        logRecords.clear();
    }

    public String getAllLogRecordsAsString() {
        StringBuilder sb = new StringBuilder();
        for (LogRecord record : logRecords) {
            sb.append(record.getMessage()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * Remove the ANSI codes from the message
     * @param message the message to strip
     * @return the message without ANSI codes
     */
    public static String stripAnsiCodes(String message) {
        return message.replaceAll("\u001B\\[[;\\d]*m", "");
    }
}