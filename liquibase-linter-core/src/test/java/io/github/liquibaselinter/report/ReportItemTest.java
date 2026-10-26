package io.github.liquibaselinter.report;

import static org.assertj.core.api.Assertions.assertThat;

import liquibase.changelog.DatabaseChangeLog;
import org.junit.jupiter.api.Test;

class ReportItemTest {

    @Test
    void shouldUseChangeLogFilePathWithoutChangeSet() {
        ReportItem item = ReportItem.error(new DatabaseChangeLog("changelog.xml"), null, "rule", "message");

        assertThat(item.filePath()).isEqualTo("changelog.xml");
        assertThat(item.changeSetId()).isNull();
    }

    @Test
    void shouldHaveNoFilePathWithoutChangeLogNorChangeSet() {
        ReportItem item = ReportItem.error(null, null, "rule", "message");

        assertThat(item.filePath()).isNull();
        assertThat(item.changeSetId()).isNull();
    }
}
