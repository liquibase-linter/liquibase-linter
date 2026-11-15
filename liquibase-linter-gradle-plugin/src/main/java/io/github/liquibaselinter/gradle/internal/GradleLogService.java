package io.github.liquibaselinter.gradle.internal;

import java.util.logging.Level;
import liquibase.logging.core.AbstractLogService;
import liquibase.logging.core.AbstractLogger;

/**
 * Routes the Liquibase logs to the Gradle logging.
 */
class GradleLogService extends AbstractLogService {

    private final GradleLogger logger;

    GradleLogService(org.gradle.api.logging.Logger gradleLogger) {
        this.logger = new GradleLogger(gradleLogger);
    }

    @Override
    public int getPriority() {
        return PRIORITY_NOT_APPLICABLE;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public liquibase.logging.Logger getLog(Class clazz) {
        return logger;
    }

    private static final class GradleLogger extends AbstractLogger {

        private final org.gradle.api.logging.Logger gradleLogger;

        GradleLogger(org.gradle.api.logging.Logger gradleLogger) {
            this.gradleLogger = gradleLogger;
        }

        @Override
        public void log(Level level, String message, Throwable e) {
            if (level == Level.OFF) {
                return;
            }
            if (level.equals(Level.SEVERE)) {
                gradleLogger.error(message, e);
            } else if (level.equals(Level.WARNING)) {
                gradleLogger.warn(message, e);
            } else if (level.equals(Level.INFO)) {
                gradleLogger.info(message, e);
            } else {
                gradleLogger.debug(message, e);
            }
        }
    }
}
