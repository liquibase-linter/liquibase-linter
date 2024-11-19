package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.core.CreateSequenceChange;
import liquibase.change.core.RenameSequenceChange;

@SuppressWarnings("rawtypes")
@AutoService(ChangeRule.class)
public class SequenceNameRule extends AbstractLintRule implements ChangeRule<Change> {
    private static final String NAME = "sequence-name";
    private static final String MESSAGE = "Sequence name '%s' does not follow pattern '%s'";

    public SequenceNameRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean supports(Change change) {
        return change instanceof CreateSequenceChange || change instanceof RenameSequenceChange;
    }

    @Override
    public boolean invalid(Change change) {
        return checkMandatoryPattern(getSequenceName(change), change);
    }

    @Override
    public String getMessage(Change change) {
        return formatMessage(getSequenceName(change), getConfig().getPatternString());
    }

    private String getSequenceName(Change change) {
        if (change instanceof CreateSequenceChange) {
            return ((CreateSequenceChange) change).getSequenceName();
        } else {
            return ((RenameSequenceChange) change).getNewSequenceName();
        }
    }
}
