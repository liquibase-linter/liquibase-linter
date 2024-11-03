package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeLogRule;
import liquibase.changelog.DatabaseChangeLog;

@SuppressWarnings("rawtypes")
@AutoService({ChangeLogRule.class})
public class ChangeLogFileNameRule extends AbstractLintRule implements ChangeLogRule {
    private static final String NAME = "changelog-file-name";
    private static final String MESSAGE = "ChangeLog filename '%s' must follow pattern '%s'";

    public ChangeLogFileNameRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(DatabaseChangeLog changeLog) {
        return checkMandatoryPattern(changeLog.getPhysicalFilePath(), changeLog);
    }

    @Override
    public String getMessage(DatabaseChangeLog changeLog) {
        return formatMessage(changeLog.getPhysicalFilePath(), getConfig().getPatternString());
    }
}
