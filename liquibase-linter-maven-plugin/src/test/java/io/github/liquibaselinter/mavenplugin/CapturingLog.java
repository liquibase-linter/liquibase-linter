package io.github.liquibaselinter.mavenplugin;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.maven.plugin.logging.Log;

public class CapturingLog implements Log {

    private final StringBuffer logs = new StringBuffer();

    private void appendContent(CharSequence content, String logLevel) {
        logs.append("[").append(logLevel).append("] ").append(content).append(System.lineSeparator());
    }

    public String capturedLogs() {
        return logs.toString();
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public void debug(CharSequence content) {
        appendContent(content, "DEBUG");
    }

    @Override
    public void debug(CharSequence content, Throwable error) {
        throw new NotImplementedException();
    }

    @Override
    public void debug(Throwable error) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public void info(CharSequence content) {
        appendContent(content, "INFO");
    }

    @Override
    public void info(CharSequence content, Throwable error) {
        throw new NotImplementedException();
    }

    @Override
    public void info(Throwable error) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void warn(CharSequence content) {
        appendContent(content, "WARN");
    }

    @Override
    public void warn(CharSequence content, Throwable error) {
        throw new NotImplementedException();
    }

    @Override
    public void warn(Throwable error) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void error(CharSequence content) {
        appendContent(content, "ERROR");
    }

    @Override
    public void error(CharSequence content, Throwable error) {
        throw new NotImplementedException();
    }

    @Override
    public void error(Throwable error) {
        throw new NotImplementedException();
    }
}
