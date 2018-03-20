package ncc_automat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

class LogFormater extends Formatter {

    private static final DateFormat DEFAULT_DATA_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(16);
        builder.append(DEFAULT_DATA_FORMAT.format(new Date(record.getMillis()))).append(" - ");
        builder.append("[").append(record.getLevel()).append("] - ");
        builder.append(formatMessage(record));

        if (record.getThrown() != null) {
            builder.append(record.getThrown().toString());
            builder.append("\r\n");
            StackTraceElement[] el = record.getThrown().getStackTrace();
            for (StackTraceElement el1 : el) {
                builder.append("\t").append(el1.toString()).append("\r\n");
            }
        }
        builder.append("\r\n");
        return builder.toString();
    }

    @Override
    public synchronized String formatMessage(LogRecord record) {
        if (record.getLevel() != Level.WARNING) {
            return super.formatMessage(record).replaceAll("\r\n", " ").replaceAll("\r", " ").replaceAll("\n", " ").trim();
        } else {
            return super.formatMessage(record) + "\r\n";
        }
    }
}