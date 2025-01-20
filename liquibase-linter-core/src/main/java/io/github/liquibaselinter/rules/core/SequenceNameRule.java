package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import io.github.liquibaselinter.rules.LintRuleChecker;
import io.github.liquibaselinter.rules.LintRuleViolationGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import liquibase.change.Change;
import liquibase.change.core.CreateSequenceChange;
import liquibase.change.core.RenameSequenceChange;

@AutoService(ChangeRule.class)
public class SequenceNameRule implements ChangeRule {

    private static final String NAME = "sequence-name";
    private static final String DEFAULT_MESSAGE = "Sequence name '%s' does not follow pattern '%s'";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
        LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
        LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
        return getSequencesName(change)
            .stream()
            .filter(sequenceName -> ruleChecker.checkMandatoryPattern(sequenceName, change))
            .map(sequenceName -> violations.withFormattedMessage(sequenceName, ruleConfig.effectivePatternFor(change)))
            .collect(Collectors.toList());
    }

    private Collection<String> getSequencesName(Change change) {
        if (change instanceof CreateSequenceChange) {
            return Collections.singleton(((CreateSequenceChange) change).getSequenceName());
        }
        if (change instanceof RenameSequenceChange) {
            return Collections.singleton(((RenameSequenceChange) change).getNewSequenceName());
        }
        return Collections.emptyList();
    }
}
