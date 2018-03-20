package ncc_automat;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class DefaultLoggerFactory {

    private static final String DATE_FORMAT_NOW = "yyyy_MM_dd";

    public static java.util.logging.Logger getDefaultLogger(String katalogPath, java.util.logging.Level level) throws IOException {
        java.util.Date DataDzisiaj = java.util.Calendar.getInstance().getTime();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT_NOW);
        String obecnyDzienCzas = sdf.format(DataDzisiaj);
        String loggerPath = katalogPath + "LOGS_NCC_ADD_" + obecnyDzienCzas + ".txt";

        LogFormater formatter = new LogFormater();
        Logger logger = Logger.getLogger(loggerPath);

        FileHandler fileHandler = new FileHandler(loggerPath);
        fileHandler.setLevel(level);
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(level);
        consoleHandler.setFormatter(formatter);
        logger.addHandler(consoleHandler);

        logger.setLevel(level);
        return logger;
    }
}
