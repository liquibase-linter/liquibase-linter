package io.github.liquibaselinter.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import liquibase.changelog.ChangeSet;

/**
 * References a single changeset by its full Liquibase identity &mdash; {@code changeLogFile}, {@code id}
 * and {@code author} &mdash; the same triple that uniquely identifies a changeset in Liquibase (see the
 * {@code changeSetExecuted} precondition and {@code allow-duplicated-changeset-identifiers}). All three
 * attributes are mandatory.
 *
 * <p>Used by the {@code enable-after-changeset} configuration (project level and rule level) to mark the
 * point in history <em>after</em> which linting applies. The referenced changeset itself is not linted;
 * every changeset parsed after it is.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ChangeSetIdentifier {

    private final String changeLogFile;
    private final String id;
    private final String author;

    @JsonCreator
    public ChangeSetIdentifier(
        @JsonProperty("changeLogFile") String changeLogFile,
        @JsonProperty("id") String id,
        @JsonProperty("author") String author
    ) {
        if (isBlank(changeLogFile) || isBlank(id) || isBlank(author)) {
            throw new IllegalArgumentException("'enable-after-changeset' requires 'changeLogFile', 'id' and 'author'");
        }
        this.changeLogFile = changeLogFile;
        this.id = id;
        this.author = author;
    }

    public String getChangeLogFile() {
        return changeLogFile;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    /**
     * @return {@code true} when the given changeset is the one referenced here, matching on the full
     * identity. The changelog file is compared against the changeset's logical file path, consistently
     * with {@code changeSetExecuted}.
     */
    public boolean matches(ChangeSet changeSet) {
        return (
            Objects.equals(id, changeSet.getId()) &&
            Objects.equals(author, changeSet.getAuthor()) &&
            Objects.equals(changeLogFile, changeSet.getFilePath())
        );
    }

    @Override
    public String toString() {
        return changeLogFile + "::" + id + "::" + author;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
