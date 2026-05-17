package me.reiner.cloudbooks.ui.selenium.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class LoggingConfig {
	
	private LoggingConfig() {}

    public static void configure() {
        String logLevel = ConfigManager.getLogLevel();   // reads from .env
        String logDir   = ConfigManager.getLogDir();

        Level level = Level.toLevel(logLevel, Level.INFO);

        // ── Pass values to Log4j2 as system properties ──
        // These are referenced in log4j2.xml via ${sys:LOG_LEVEL} etc.
        System.setProperty("LOG_LEVEL", level.name());
        System.setProperty("LOG_DIR",   logDir);
        System.setProperty("LOG_CONSOLE_ENABLED", String.valueOf(ConfigManager.isConsoleLogEnabled()));
        System.setProperty("LOG_FILE_ENABLED",    String.valueOf(ConfigManager.isFileLogEnabled()));

        // ── Apply level change to live Log4j2 context ───
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();

        // Update root logger
        LoggerConfig rootConfig = config.getRootLogger();
        rootConfig.setLevel(level);

        LoggerConfig frameworkConfig = config.getLoggerConfig("me.reiner.cloudbooks.ui.selenium");
        if (frameworkConfig != null) {
            frameworkConfig.setLevel(level);
        }

        ctx.updateLoggers(); // apply changes live — no restart needed
        LogManager.getLogger(LoggingConfig.class)
                  .info("Log level set to [{}] from .env", level);
    }
}
