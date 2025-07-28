package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeLogRule;
import io.github.liquibaselinter.rules.LintRuleChecker;
import io.github.liquibaselinter.rules.LintRuleViolationGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import liquibase.changelog.DatabaseChangeLog;

@AutoService(ChangeLogRule.class)
public class ChangeLogFileNameRule implements ChangeLogRule {

    private static final String NAME = "changelog-file-name";
    private static final String MESSAGE = "ChangeLog filename '%s' must follow pattern '%s'";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(DatabaseChangeLog changeLog, RuleConfig ruleConfig) {
        if (isInvalid(changeLog, ruleConfig)) {
            LintRuleViolationGenerator violations = new LintRuleViolationGenerator(MESSAGE, ruleConfig);
            return Collections.singleton(
                violations.withFormattedMessage(changeLog.getPhysicalFilePath(), ruleConfig.getPatternString())
            );
        }
        return Collections.emptyList();
    }

    private boolean isInvalid(DatabaseChangeLog changeLog, RuleConfig ruleConfig) {
        LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
        return ruleChecker.checkMandatoryPattern(changeLog.getPhysicalFilePath(), changeLog);
    }
}
