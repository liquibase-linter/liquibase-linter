package io.github.liquibaselinter.config;

import org.apache.commons.lang3.StringUtils;

/**
 * Validates the three mutually exclusive "enable after" options, shared by {@link Config} (project level)
 * and {@link RuleConfig} (rule level):
 *
 * <ul>
 *   <li>{@code enable-after} — legacy alias of {@code enable-after-changelog}, kept for backward
 *       compatibility and scheduled for removal in 1.0.</li>
 *   <li>{@code enable-after-changelog} — a changelog file; linting starts at the following changelog.</li>
 *   <li>{@code enable-after-changeset} — a {@link ChangeSetIdentifier}; linting starts at the following
 *       changeset.</li>
 * </ul>
 */
final class EnableAfterValidator {

    private EnableAfterValidator() {}

    /**
     * @throws IllegalArgumentException when more than one of the three options is set.
     */
    static void requireAtMostOne(
        String scope,
        String enableAfter,
        String enableAfterChangelog,
        ChangeSetIdentifier enableAfterChangeset
    ) {
        int count = 0;
        if (StringUtils.isNotEmpty(enableAfter)) {
            count++;
        }
        if (StringUtils.isNotEmpty(enableAfterChangelog)) {
            count++;
        }
        if (enableAfterChangeset != null) {
            count++;
        }
        if (count > 1) {
            throw new IllegalArgumentException(
                "Only one of 'enable-after', 'enable-after-changelog' or 'enable-after-changeset' can be set in the " +
                    scope
            );
        }
    }
}
